package xyz.taika.cryptohodler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//Create JsonTask class that gets API-data via Internet
class UpdateDataTask extends AsyncTask<String, String, String> {

    private Context context;
    private JSONArray jsonArray = null;
    private ProgressDialog progressDialog = null;
    private AssetList assetList = new AssetList();
    private String changePercentRate;
    private AssetAdapter assetAdapter;
    private Boolean eurFiat;
    //OLD WORKING String changePercentRate = getChangePercentage();


    UpdateDataTask(Context context, AssetList assetList, AssetAdapter assetAdapter, String changePercentRate, Boolean eurFiat) {
        this.context = context;
        this.assetList = assetList;
        this.changePercentRate = changePercentRate;
        this.assetAdapter = assetAdapter;
        this.eurFiat = eurFiat;
    }

    protected void onPreExecute() {
        super.onPreExecute();

        //Set dialog that show Please wait sign to user while getting fresh data via API call
        progressDialog = new ProgressDialog(context);
        //OLD WORKING progressDialog = new ProgressDialog(AssetListActivity.this);

        progressDialog.setMessage("Getting fresh data...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder buffer = new StringBuilder();
            String line;



            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            //try to create JsonArray, so its easy to get specific JsonObject and values from it.
            try {
                jsonArray = new JSONArray(buffer.toString());
                Log.i("UpdateDataTask.java", "JSON array created, length: " + jsonArray.length());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


        //Cycle through jsonArray to update data of already created assets
        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                //Get JSON object from JSON array at position[i]
                JSONObject jsonArrayObject = jsonArray.getJSONObject(i);

                String jsonObjectName = jsonArrayObject.getString("name");
                String jsonObjectSymbol = jsonArrayObject.getString("symbol");

                //Create String to determine to use EUR or USD as a fiat value
                String fiatValueString = "";
                if (eurFiat) {
                    fiatValueString += "price_eur";
                } else {
                    fiatValueString += "price_usd";
                }

                //Cycle through already created assets to update newest data to them
                for (Asset asset : assetList.getAssetList())
                    if (jsonObjectName.equals(asset.getAssetName()) || jsonObjectSymbol.equals(asset.getAssetSymbol())) {
                        Double changePercent;
                        String changePercentString;

                        //Save correct price percent change data to String
                        switch (this.changePercentRate) {
                            case "1H":
                                changePercentString = jsonArrayObject.getString("percent_change_1h");
                                break;
                            case "7D":
                                changePercentString = jsonArrayObject.getString("percent_change_7d");
                                break;
                            default:
                                changePercentString = jsonArrayObject.getString("percent_change_24h");
                                break;
                        }

                        //Check if change percent data is not null and assign right value to it
                        if (changePercentString.equals("null")) {
                            changePercent = 0.0;
                        } else {
                            changePercent = Double.parseDouble(changePercentString);
                        }


                        //Save correct price data to Double
                        Double assetPrice;
                        assetPrice = Double.parseDouble(jsonArrayObject.getString(fiatValueString));

                        //Save updated data to current asset
                        asset.setAssetValue(assetPrice);
                        asset.setChange24h(changePercent);
                        asset.calculateAssetTotalValue();
                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //Sort list
        assetList.sortList();


        //Delete old assetlist -file and save new one with fresh data to internal storage
        context.deleteFile("assetListData");
        saveAssetListToInternalStorage(context);

        //Notify adapter that data has changed and refresh ListView
        assetAdapter.notifyDataSetChanged();

    }

    // Save assetList to internal storage
    private void saveAssetListToInternalStorage(Context context) {
        try {
            String filename = "AssetListData";
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(this.assetList);

            ous.flush();
            ous.close();
            fos.close();
        } catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

}
package xyz.taika.cryptohodler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
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

/**
 * Created by Jukka on 07/11/2017.
 */

//Create JsonTask class that gets API-data via Internet
public class UpdateDataTask extends AsyncTask<String, String, String> {

    private Context context;
    private JSONArray jsonArray = null;
    private ProgressDialog progressDialog = null;
    private AssetList assetList = new AssetList();
    private String changePercent;
    private Boolean eurFiat;
    //OLD WORKING String changePercent = getChangePercentage();


    public UpdateDataTask(Context context, AssetList assetList, String changePercent) {
        this.context = context;
        this.assetList = assetList;
        this.changePercent = changePercent;

        //First testing with default value
        this.eurFiat = false;
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
                Log.i("AssetListActivity!", "JSON array created, length: " + jsonArray.length());

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
                for (Asset asset : assetList.getAssetList()) {

                    if (jsonObjectName.equals(asset.getAssetName()) || jsonObjectSymbol.equals(asset.getAssetSymbol())) {
                        Double changePercent;
                        String changePercentString;

                        //Save correct price percent change data to String
                        if (this.changePercent.equals("1H")) {
                            changePercentString = jsonArrayObject.getString("percent_change_1h");
                        } else if (this.changePercent.equals("7D")) {
                            changePercentString = jsonArrayObject.getString("percent_change_7d");
                        } else {
                            changePercentString = jsonArrayObject.getString("percent_change_24h");
                        }


                        if (changePercentString.equals("null")) {
                            changePercent = 0.0;
                        } else {
                            changePercent = Double.parseDouble(changePercentString);
                        }


                        //Save correct price data to Double
                        Double assetPrice = 0.0;
                        assetPrice = Double.parseDouble(jsonArrayObject.getString(fiatValueString));

                        //Save updated data to current asset
                        asset.setAssetValue(assetPrice);
                        asset.setChange24h(changePercent);
                        asset.calculateAssetTotalValue();
                    }
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

        //TEST to notify that adapter right after returning from this class
        //Notify adapter that data has changed and refresh ListView
        //OLD WORKING assetAdapter.notifyDataSetChanged();

    }

    // Save assetList to internal storage
    public void saveAssetListToInternalStorage(Context context) {
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

/*
    //This method starts UpdateDataTask that tries to get fresh API data from internet
    public void getApiData() {

        //Check if user has updated data already within 10 seconds otherwise show toast
        if (!needDelay) {
            //Execute UpdateDataTask to get fresh API data
            if (this.eurFiat) {
                new AssetListActivity.JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?convert=EUR&limit=0");
            } else {
                new AssetListActivity.JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=0");
            }

            //Set 10sec delay before user can update data again
            needDelay = true;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    needDelay = false;
                }
            }, 10000);
        } else {
            // Show toast
            Toast.makeText(getApplicationContext(), "Fresh asset data can updated once in every 10 seconds", Toast.LENGTH_SHORT).show();
        }

    }
*/


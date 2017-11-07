package xyz.taika.cryptohodler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AssetListActivity extends AppCompatActivity {

    private AssetList assetList;
    private AssetAdapter assetAdapter;
    private boolean needDelay;
    private boolean eurFiat;
    private String changePercent;
    UpdateDataTask updateDataTask;

    @Override
    public void onResume() {
        super.onResume();
        needDelay = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);
        eurFiat = false;

        //Get Intent data from MainActivity
        eurFiat = getIntent().getExtras().getBoolean("eurFiat");
        changePercent = getIntent().getExtras().getString("changePercent");

        //Read file from internal storage
        this.assetList = new AssetList();
        this.assetList = readFromInternalStorage(AssetListActivity.this);

        needDelay = false;

        //TEST Create  all default Asset objects
        assetList.addNewAssetToList("Bitcoin", 0.0, R.mipmap.bitcoin);
        /*assetList.addNewAssetToList("Ethereum", 2.070);
        assetList.addNewAssetToList("Komodo", 15006.50);
        assetList.addNewAssetToList("Byteball", 20.7860);
        assetList.addNewAssetToList("DeepONION", 4005.40);
        assetList.addNewAssetToList("NEO", 1000.001);
        assetList.addNewAssetToList("GAS", 162.54);
        assetList.addNewAssetToList("Bitcore", 5.6);
        assetList.addNewAssetToList("Bitcoin Cash", 5.847);
        assetList.addNewAssetToList("HEAT", 8045.30);
        assetList.addNewAssetToList("NEM", 23000.70);
        assetList.addNewAssetToList("Zcash", 5.90);
        assetList.addNewAssetToList("Stellar", 5400.12);
        assetList.addNewAssetToList("KekCoin", 10000.0);
        assetList.addNewAssetToList("Lisk", 580.0);
        assetList.addNewAssetToList("Monero");
        assetList.addNewAssetToList("Factom", 20.67);*/





        //Create a AssetAdapter and give this (AssetListActivity) as a context
        assetAdapter = new AssetAdapter(this, assetList.getAssetList());

        //Create a ListView object and allocate correct XML-layout to it
        ListView listView = (ListView) findViewById(R.id.asset_list);

        if (listView != null) {
            listView.setAdapter(assetAdapter);
        }


        //Add floating action button with add new asset -functionality
        FloatingActionButton newAssetFAB = (FloatingActionButton) findViewById(R.id.new_asset_fab);
        newAssetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show Add new asset dialog
                showNewAssetDialog();

            }
        });


        // Set a click listener to show custom Dialog that enables a change to change specific list view item when the list item is clicked on
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get and store just clicked word object temporarily.
                Asset assetJustClicked = assetList.getAssetList().get(position);

                //Show dialog
                showEditAssetDialog(assetJustClicked);

            }
        });


        /*Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        performs a swipe-to-refresh gesture. */
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("AssetListActivity", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getApiData();

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );


    }

    public String getChangePercentage() {
        return this.changePercent;
    }


    //Show dialog that gives possibility to edit asset from the list
    public void showEditAssetDialog(Asset assetJustClicked) {

        final Asset clickedAsset = assetJustClicked;

        // Creating alert Dialog with one Button
        final AlertDialog.Builder editAssetDialog = new AlertDialog.Builder(AssetListActivity.this);

        // Setting Dialog Title
        editAssetDialog.setTitle("EDIT ASSET");

        //Show Dialog message
        editAssetDialog.setMessage("Edit your " + clickedAsset.getAssetName() + " asset");

        // Add LinearLayout to show in custom AlertDialog
        LinearLayout layout = new LinearLayout(AssetListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 0, 50, 0);

        // Create EditText View for asset name and add it to LinearLayout
        final EditText assetNameField = new EditText(AssetListActivity.this);
        assetNameField.setHint("Change asset name");

        // Create EditText View for asset quantity and add it to LinearLayout
        final EditText assetQuantityField = new EditText(AssetListActivity.this);
        assetQuantityField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        assetQuantityField.setHint("Change asset quantity");

        //Add view to layout
        layout.addView(assetNameField);
        layout.addView(assetQuantityField);

        // Set LinearLayout to AlertDialog
        editAssetDialog.setView(layout);


        // Setting Positive "Done" Button
        editAssetDialog.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog id DONE button is pressed

                        for (Asset asset : assetList.getAssetList()) {
                            if (clickedAsset.getAssetName().toLowerCase().equals(asset.getAssetName().toLowerCase())) {

                                String assetQuantityString = assetQuantityField.getText().toString();
                                String assetName = assetNameField.getText().toString();

                                if (!assetQuantityString.isEmpty()) {
                                    Double assetQuantityDouble = asset.getAssetQuantity();

                                    //assetQuantity = "0.0";

                                    assetQuantityDouble = Double.valueOf(assetQuantityString);

                                    asset.setAssetQuantity(assetQuantityDouble);
                                }

                                if (!assetName.isEmpty()) {
                                    asset.setAssetName(assetName);
                                }
                            }
                        }

                        assetList.updateTotalValues();

                        //Sort list
                        assetList.sortList();

                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

                        //Delete old saved file and save new one with changed data
                        deleteFile("assetListData");
                        saveAssetListToInternalStorage(AssetListActivity.this);

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Asset data changed", Toast.LENGTH_SHORT).show();


                        // TEST Possibility to start different activity
                        //Intent myIntent1 = new Intent(view.getContext(), Show.class);
                        //startActivityForResult(myIntent1, 0);
                    }
                });

        // Setting Negative "Cancel" Button
        editAssetDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // TEST trying to add delete button to edit asset dialog
        editAssetDialog.setNeutralButton("DELETE ASSET",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        assetList.deleteAssetFromList(clickedAsset.getAssetName());


                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

                        //Delete old assetlist -file and save new one with fresh data
                        deleteFile("assetListData");
                        saveAssetListToInternalStorage(AssetListActivity.this);

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Asset deleted", Toast.LENGTH_SHORT).show();

                        //Dismiss dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        editAssetDialog.show();

    }


    //Show dialog that gives possibility to add new asset to the list
    public void showNewAssetDialog() {

        // Creating alert Dialog with one Button
        AlertDialog.Builder newAssetDialog = new AlertDialog.Builder(AssetListActivity.this);

        //AlertDialog newAssetDialog = new AlertDialog.Builder(MainActivity.this).create();

        // Setting Dialog Title
        newAssetDialog.setTitle("ADD NEW ASSET");

        // Setting Dialog Message
        newAssetDialog.setMessage("Give asset name, symbol and quantity");


        // Add LinearLayout to show in custom AlertDialog
        LinearLayout layout = new LinearLayout(AssetListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 0, 50, 50);

        // Create EditText View and add it to LinearLayout
        final EditText assetNameField = new EditText(AssetListActivity.this);
        assetNameField.setHint("Asset name");
        layout.addView(assetNameField);

        // Create EditText View and add it to LinearLayout
        final EditText assetSymbolField = new EditText(AssetListActivity.this);
        assetSymbolField.setHint("Asset symbol(ie. BTC)");
        assetSymbolField.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        layout.addView(assetSymbolField);

        // Create another EditText View and add it to LinearLayout
        final EditText assetQuantityField = new EditText(AssetListActivity.this);
        assetQuantityField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        assetQuantityField.setHint("Your asset quantity");
        layout.addView(assetQuantityField);

        // Set LinearLayout to AlertDialog
        newAssetDialog.setView(layout);


        // Setting Positive "Done" Button
        newAssetDialog.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

                        String assetName = assetNameField.getText().toString();
                        String assetSymbol = assetSymbolField.getText().toString();
                        String assetQuantity = assetQuantityField.getText().toString();

                        if (assetQuantity.isEmpty()) {
                            assetQuantity = "0.0";
                        }
                        Double quantity = Double.valueOf(assetQuantity);

                        assetList.addNewAssetToList(assetName, assetSymbol, quantity);


                        //Execute UpdateDataTask to get fresh API data
                        getApiData();

                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

                        // Show toast
                        Toast.makeText(getApplicationContext(), "Asset created", Toast.LENGTH_SHORT).show();


                        // TEST Possibility to start different activity
                        //Intent myIntent1 = new Intent(view.getContext(), Show.class);
                        //startActivityForResult(myIntent1, 0);
                    }
                });

        // Setting Negative "Cancel" Button
        newAssetDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        newAssetDialog.show();
    }


    //Create UpdateDataTask class that gets API-data via Inet
    private class UpdateDataTask extends AsyncTask<String, String, String> {

        JSONArray jsonArray = null;
        ProgressDialog progressDialog = null;
        String changePercent = getChangePercentage();

        protected void onPreExecute() {
            super.onPreExecute();

            //Set dialog that show Please wait sign to user while getting fresh data via API
            progressDialog = new ProgressDialog(AssetListActivity.this);
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
                            switch (this.changePercent) {
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
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //Sort list
            assetList.sortList();

            //Delete old assetlist -file and save new one with fresh data to internal storage
            deleteFile("assetListData");
            saveAssetListToInternalStorage(AssetListActivity.this);

            //Notify adapter that data has changed and refresh ListView
            assetAdapter.notifyDataSetChanged();

        }

    }


    //This method starts UpdateDataTask that tries to get fresh API data from internet
    public void getApiData() {

        //Check if user has updated data already within 10 seconds otherwise show toast
        if (!needDelay) {
            //Execute UpdateDataTask to get fresh API data
            if (this.eurFiat) {
                new UpdateDataTask().execute("https://api.coinmarketcap.com/v1/ticker/?convert=EUR&limit=0");
            } else {
                new UpdateDataTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=0");
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

    //Get assetlist data from internal Storage
    public AssetList readFromInternalStorage(Context context) {
        AssetList toReturn = new AssetList();
        FileInputStream fis;
        try {
            fis = context.openFileInput("AssetListData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            toReturn = (AssetList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return toReturn;
    }


}

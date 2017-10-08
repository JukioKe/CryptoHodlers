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

    @Override
    public void onResume() {
        super.onResume();
        needDelay = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        //Read file from internal storage
        this.assetList = new AssetList();
        this.assetList = readFromInternalStorage(AssetListActivity.this);
        needDelay = false;


        //TEST Execute JsonTask to get fresh API data
        //new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=400");


        //TEST Create  all default Asset objects
        assetList.addNewAssetToList("Bitcoin", 6.045, R.mipmap.bitcoin);
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


        //Add floating action button with add new asset -functionality
        FloatingActionButton newAssetFAB = (FloatingActionButton) findViewById(R.id.new_asset_fab);
        newAssetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show Add new asset dialog
                showNewAssetDialog();

            }
        });

        /*Add floating action button with add refresh asset list -functionality
        final FloatingActionButton refreshFAB = (FloatingActionButton) findViewById(R.id.refresh_fab);

        //Set onClick listener to the refresh button
        refreshFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getApiData();

                //Delete old saved file and save new one with changed data
                //deleteFile("assetListData");
                //saveAssetListToInternalStorage(AssetListActivity.this);
            }
        }); */


        //Create a AssetAdapter and give this (AssetListActivity) as a context
        assetAdapter = new AssetAdapter(this, assetList.getAssetList());

        //Create a ListView object and allocate correct XML-layout to it
        ListView listView = (ListView) findViewById(R.id.asset_list);

        if (listView != null) {
            listView.setAdapter(assetAdapter);
        }


        // Set a click listener to show custom Dialog that enables a change to change specific list view item when the list item is clicked on
        //assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get and store just clicked word object temporarily.
                Asset assetJustClicked = assetList.getAssetList().get(position);

                //Show dialog
                showEditAssetDialog(assetJustClicked);

            }
        });

        /* TEST Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
        performs a swipe-to-refresh gesture.
*/
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

        // Create EditText View for asset quantity and add it to LinearLayout
        final EditText assetQuantityField = new EditText(AssetListActivity.this);
        assetQuantityField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        assetQuantityField.setHint("Change your " + clickedAsset.getAssetName() + " quantity");
        layout.addView(assetQuantityField);


        // Set LinearLayout to AlertDialog
        editAssetDialog.setView(layout);


        // Setting Positive "Done" Button
        editAssetDialog.setPositiveButton("DONE",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog id DONE button is pressed
                        String assetQuantity = assetQuantityField.getText().toString();

                        if (assetQuantity.isEmpty()) {
                            assetQuantity = "0.0";
                        }
                        Double quantity = Double.valueOf(assetQuantity);

                        for (Asset asset : assetList.getAssetList()) {
                            if (clickedAsset.getAssetName().toLowerCase().equals(asset.getAssetName().toLowerCase())) {
                                asset.setAssetQuantity(quantity);
                            }
                        }

                        assetList.updateTotalValues();

                        //Delete old saved file and save new one with changed data
                        deleteFile("assetListData");
                        saveAssetListToInternalStorage(AssetListActivity.this);

                        //Notify adapter that data has changed and refresh ListView
                        assetAdapter.notifyDataSetChanged();

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
        assetSymbolField.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
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


                        //Execute JsonTask to get fresh API data
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

    //Create JsonTask class that gets API-data via Inet
    private class JsonTask extends AsyncTask<String, String, String> {

        JSONArray jsonArray = null;
        ProgressDialog progressDialog = null;

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

                    Log.d("Response: ", "> " + line);   //Log whole response line by line

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

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    //Get JSON object from JSON array at position[i]
                    JSONObject jsonObjectI = jsonArray.getJSONObject(i);
                    String jsonObjectName = jsonObjectI.getString("name");
                    String jsonObjectSymbol = jsonObjectI.getString("symbol");

                    for (Asset asset : assetList.getAssetList()) {
                        if (jsonObjectName.equals(asset.getAssetName()) || jsonObjectSymbol.equals(asset.getAssetSymbol())) {
                            Double assetPrice = Double.parseDouble(jsonObjectI.getString("price_usd"));
                            asset.setAssetValue(assetPrice);
                            asset.calculateAssetTotalValue(assetPrice);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //Notify adapter that data has changed and refresh ListView
            assetAdapter.notifyDataSetChanged();

        }

    }


    public void getApiData() {
        if (!needDelay) {
            //Execute JsonTask to get fresh API data
            new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=900");

            //Delete old assetlist -file and save new one with fresh data
            deleteFile("assetListData");
            saveAssetListToInternalStorage(AssetListActivity.this);

            needDelay = true;
        } else {
            // Show toast
            Toast.makeText(getApplicationContext(), "Fresh asset data can updated once in every 10 seconds", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Execute JsonTask to get fresh API data
                    needDelay = false;
                }
            }, 10000);
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

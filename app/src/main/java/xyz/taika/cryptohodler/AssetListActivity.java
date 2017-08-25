package xyz.taika.cryptohodler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.FloatingActionButton;

import static java.lang.Math.round;
import static java.security.AccessController.getContext;

public class AssetListActivity extends AppCompatActivity {

    private AssetList assetList2;
    private ArrayList<Asset> assetList;
    private ListView listView;
    private AssetAdapter assetAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        //TEST ing with custom AssetList Class
        assetList2 = new AssetList();
        Double doubleLuku = 0.09877;
        assetList2.addNewAssetToList("TestiCoini", doubleLuku);

        /* Not sure if toolbar is needed here
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new asset", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // Creating alert Dialog with one Button
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AssetListActivity.this);

                //AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle("ADD NEW ASSET");

                // Setting Dialog Message
                alertDialog.setMessage("Give asset name and quantity");


                // Add LinearLayout to show in custom AlertDialog
                LinearLayout layout = new LinearLayout(AssetListActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Create EditText View and add it to LinearLayout
                final EditText assetNameField = new EditText(AssetListActivity.this);
                assetNameField.setHint("Asset name");
                layout.addView(assetNameField);

                // Create another EditText View and add it to LinearLayout
                final EditText assetQuantityField = new EditText(AssetListActivity.this);
                assetQuantityField.setHint("Quantity");
                layout.addView(assetQuantityField);

                // Set LinearLayout to AlertDialog
                alertDialog.setView(layout);


                // TEST Possibility to set Icon to Dialog
                //alertDialog.setIcon(R.drawable.XXX);

                // Setting Positive "Done" Button
                alertDialog.setPositiveButton("DONE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                Toast.makeText(getApplicationContext(), "Asset created", Toast.LENGTH_SHORT).show();

                                String assetName = assetNameField.getText().toString();
                                String assetQuantity = assetQuantityField.getText().toString();
                                Double quantity = Double.valueOf(assetQuantity);

                                Log.i("AssetListActivity", assetName);
                                Log.i("AssetListActivity", String.valueOf(quantity));

                                // TEST Possibility to start different activity
                                //Intent myIntent1 = new Intent(view.getContext(), Show.class);
                                //startActivityForResult(myIntent1, 0);
                            }
                        });

                // Setting Negative "Cancel" Button
                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // Showing Alert Message
                alertDialog.show();

            }
        });


        // TEST
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TEST Execute JsonTask to get fresh API data
        new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=20");

        //Create new AssetList and all default Asset objects
        assetList = new ArrayList<>();
        assetList.add(new Asset("Bitcoin", 5.0, 3454.08, R.mipmap.bitcoin));
        assetList.add(new Asset("Ethereum", 3.0, 230.30, R.mipmap.bitcoin));
        assetList.add(new Asset("Komodo", 500.0, 1.09, R.mipmap.bitcoin));
        assetList.add(new Asset("Byteball", 15.0, 480.67, R.mipmap.bitcoin));
        assetList.add(new Asset("DeepONION", 1500.0, 12.23, R.mipmap.bitcoin));
        assetList.add(new Asset("Litecoin", 43.12, 4.08, R.mipmap.bitcoin));
        assetList.add(new Asset("NEO", 1500.0, 18.54, R.mipmap.bitcoin));
        assetList.add(new Asset("GAS", 200.0, 8.65, R.mipmap.bitcoin));
        assetList.add(new Asset("BTX", 5.87, 2.67, R.mipmap.bitcoin));
        assetList.add(new Asset("BHC", 5.87, 330.83, R.mipmap.bitcoin));
        assetList.add(new Asset("HEAT", 8045.30, 0.34, R.mipmap.bitcoin));
        assetList.add(new Asset("FIMK", 3124.0, 0.012, R.mipmap.bitcoin));
        assetList.add(new Asset("NEM", 29000.70, 0.29, R.mipmap.bitcoin));
        assetList.add(new Asset("Zcash", 5.90, 214.67, R.mipmap.bitcoin));
        assetList.add(new Asset("Stellar", 5400.12, 0.022, R.mipmap.bitcoin));
        assetList.add(new Asset("KEK", 10000.0, 0.09, R.mipmap.bitcoin));
        assetList.add(new Asset("Monero", 0.1, 0.07, R.mipmap.bitcoin));
        assetList.add(new Asset("LiteShares", 50000.0, 0.02, R.mipmap.bitcoin));
        assetList.add(new Asset("Lisk", 580.0, 12.23, R.mipmap.bitcoin));
        assetList.add(new Asset("Factom", 20.67, 20.98, R.mipmap.bitcoin));


        //TEST test for the file input and output
        /*
        FileInputStream fis;
        try {
            fis = openFileInput("assetList");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Asset> returnList = (ArrayList<Asset>) ois.readObject();
            Log.i("AssetListActivity", "Tulostetaan returnlist: " + returnList);
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } */

        //Create a AssetAdapter and give this (AssetListActivity) as a context
        assetAdapter = new AssetAdapter(this, assetList);

        //Create a ListView object and allocate correct XML-layout to it
        listView = (ListView) findViewById(R.id.asset_list);

        if (listView != null) {
            listView.setAdapter(assetAdapter);
        }

    }

    public void showCustomDialog() {

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        JSONArray jsonArray = null;
        ProgressDialog progressDialog = null;

        protected void onPreExecute() {
            super.onPreExecute();

            //Set dialog that show Please wait sign to user while getting fresh data via API
            progressDialog = new ProgressDialog(AssetListActivity.this);
            progressDialog.setMessage("Please wait");
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

                    //Move fresh data from JSON object to correct Asset in AssetList at position[i]
                    Asset asset = assetList.get(i);
                    String jsonObjectName = jsonObjectI.getString("name");
                    Double assetValue = Double.parseDouble(jsonObjectI.getString("price_usd"));
                    asset.setAssetName(jsonObjectName);
                    asset.setAssetValue(assetValue);

                    //Notify adapter that data has changed and refresh ListView
                    assetAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}

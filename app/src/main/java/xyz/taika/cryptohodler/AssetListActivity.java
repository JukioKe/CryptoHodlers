package xyz.taika.cryptohodler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Math.round;

public class AssetListActivity extends AppCompatActivity {

    private AssetList assetList;
    private AssetAdapter assetAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        //Get AssetList object from MainActivity an initialize it to this.assetList
        // WORKING TEST ArrayList<Asset> assetList2 = (ArrayList<Asset>) getIntent().getSerializableExtra("assetList");
        // Log.i("AssetListActivity", assetList2.toString());

        //TEST WORKING this.assetList = new AssetList();
        // TEST this.assetList.readFromInternalStorage(AssetListActivity.this);


        this.assetList = readFromInternalStorage(AssetListActivity.this);
        Log.i("AssetListActivity", "Logita tallennukset luettu: " + assetList.getAssetList().toString());


        /* Not sure if toolbar is needed here
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); */

        //TEST Execute JsonTask to get fresh API data
        new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=400");

        //TEST Create  all default Asset objects
        assetList.addNewAssetToList("Bitcoin", 6.045, R.mipmap.bitcoin);
        assetList.addNewAssetToList("Ethereum", 2.070);
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
        assetList.addNewAssetToList("KEK", 10000.0);
        assetList.addNewAssetToList("Lisk", 580.0);
        assetList.addNewAssetToList("Monero");
        assetList.addNewAssetToList("Factom", 20.67);

        saveAssetListToInternalStorage(AssetListActivity.this);

        /* WORKING TEST Create new AssetList and all default Asset objects
        assetList = new ArrayList<>();
        assetList.add(new Asset("Bitcoin", 6.045, R.mipmap.bitcoin));
        assetList.add(new Asset("Ethereum", 2.070));
        assetList.add(new Asset("Komodo", 15006.50));
        assetList.add(new Asset("Byteball", 20.7860));
        assetList.add(new Asset("DeepONION", 4005.40));
        assetList.add(new Asset("NEO", 1000.001));
        assetList.add(new Asset("GAS", 162.54));
        assetList.add(new Asset("Bitcore", 5.6));
        assetList.add(new Asset("Bitcoin Cash", 5.847));
        assetList.add(new Asset("HEAT", 8045.30));
        assetList.add(new Asset("NEM", 23000.70));
        assetList.add(new Asset("Zcash", 5.90));
        assetList.add(new Asset("Stellar", 5400.12));
        assetList.add(new Asset("KEK", 10000.0));
        assetList.add(new Asset("Lisk", 580.0));
        assetList.add(new Asset("Monero"));
        assetList.add(new Asset("Factom", 20.67));  */


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* TEST SnackbarTest
                Snackbar.make(view, "Add new asset", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show(); */

                showCustomDialog();

            }
        });

        // TEST
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        //WORKING TEST assetAdapter = new AssetAdapter(this, assetList);

        assetAdapter = new AssetAdapter(this, assetList.getAssetList());

        //Create a ListView object and allocate correct XML-layout to it
        ListView listView = (ListView) findViewById(R.id.asset_list);

        if (listView != null) {
            listView.setAdapter(assetAdapter);
        }

    }

    public void showCustomDialog() {

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

                        String assetName = assetNameField.getText().toString();
                        String assetQuantity = assetQuantityField.getText().toString();
                        Double quantity = Double.valueOf(assetQuantity);

                        assetList.addNewAssetToList(assetName, quantity);

                        Log.i("AssetListActivity", assetName);
                        Log.i("AssetListActivity", String.valueOf(quantity));

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
                    String jsonObjectID = jsonObjectI.getString("id");
                    String jsonObjectName = jsonObjectI.getString("name");

                    for (Asset asset : assetList.getAssetList()) {
                        if (jsonObjectID.equals(asset.getAssetID()) || jsonObjectName.equals(asset.getAssetName())) {
                            Double assetPrice = Double.parseDouble(jsonObjectI.getString("price_usd"));
                            asset.setAssetValue(assetPrice);
                            asset.setTotalValue(assetPrice);
                        }
                    }

                    /* WORKING TEST for (Asset asset : assetList) {
                        if (jsonObjectID.equals(asset.getAssetID()) || jsonObjectName.equals(asset.getAssetName())) {
                            Double assetPrice = Double.parseDouble(jsonObjectI.getString("price_usd"));
                            asset.setAssetValue(assetPrice);
                            asset.setTotalValue(assetPrice);
                        }
                    } */


                    //Move fresh data from JSON object to correct Asset in AssetList at position[i]
                    /*Asset asset = assetList.get(i);
                    String jsonObjectName = jsonObjectI.getString("name");
                    Double assetPrice = Double.parseDouble(jsonObjectI.getString("price_usd"));
                    asset.setAssetName(jsonObjectName);
                    asset.setAssetValue(assetPrice);
                    asset.setTotalValue(assetPrice);*/

                    //Notify adapter that data has changed and refresh ListView
                    //assetAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //Notify adapter that data has changed and refresh ListView
            assetAdapter.notifyDataSetChanged();

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

    /*TEST public ArrayList<Asset> readFromInternalStorage(Context context) {
        ArrayList<Asset> toReturn = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = context.openFileInput("AssetListData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            toReturn = (ArrayList<Asset>) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return toReturn;
    } */

    // Save assetList to internal storage
    public void saveAssetListToInternalStorage(Context context) {
        try {
            String filename = "AssetListData";
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream ous = new ObjectOutputStream(fos);
            ous.writeObject(this.assetList);
            //TEST ous.writeObject(this.assetList.getAssetList());
            ous.flush();
            ous.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

}

package xyz.taika.cryptohodler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AssetListActivity extends AppCompatActivity {

    //TEST Json
    TextView txtJson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_list);

        /* Not sure if toolbar of FAB is needed here

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        */

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TEST Json
        new JsonTask().execute("https://api.coinmarketcap.com/v1/ticker/?limit=10");


        //Create a list for Asset objects
        final ArrayList<Asset> assetList = new ArrayList<>();

        //Create all Asset objects
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


        //Create a AssetAdapter and give this (AssetListActivity) as a context
        AssetAdapter assetAdapter = new AssetAdapter(this, assetList);

        //Create a ListView object and allocate correct XML-layout to it
        ListView listView = (ListView) findViewById(R.id.asset_list);

        if (listView != null) {
            listView.setAdapter(assetAdapter);
        }

    }


    private class JsonTask extends AsyncTask<String, String, String> {

        JSONArray jsonArray = null;
        JSONObject jsonObject = null; //For TESTS
        ProgressDialog progressDialog = null;

        protected void onPreExecute() {
            super.onPreExecute();

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
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                //try to create JsonArray and JsonObject, so its easy to get spesific value from the API call
                try {
                    jsonArray = new JSONArray(buffer.toString());
                    Log.i("MOIKKA!!", "" + jsonArray.length());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /* TEST Json
                try {
                    jsonObject = jsonArray.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                } */


                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
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

            /*TEST Json
            try {
                txtJson.setText("Bitcoinin arvo tällä hetkellä: $" + jsonObject.getString("price_usd"));
            } catch (JSONException e) {
                e.printStackTrace();
            } */

        }
    }

}

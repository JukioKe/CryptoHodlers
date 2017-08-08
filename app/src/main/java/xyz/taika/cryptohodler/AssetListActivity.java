package xyz.taika.cryptohodler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AssetListActivity extends AppCompatActivity {

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
}

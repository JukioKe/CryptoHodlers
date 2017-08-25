package xyz.taika.cryptohodler;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Jukka on 5.8.2017. JEE
 */

public class AssetList {

    private ArrayList<Asset> assetList;

    public AssetList() {
    }

    public ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public void addNewAssetToList(String assetName, Double assetQuantity) {
        Double quantity = assetQuantity;
        NumberFormat formatter = new DecimalFormat("#0.0000");

        Log.i("AssetListClass", String.valueOf(formatter.format(quantity)));
    }
}

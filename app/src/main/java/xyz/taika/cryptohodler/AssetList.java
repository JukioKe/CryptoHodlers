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
        this.assetList = new ArrayList<>();
    }

    public ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public void addNewAssetToList(String assetName, Double assetQuantity) {
        Double quantity = assetQuantity;
        assetList.add(new Asset(assetName, assetQuantity));

    }

    public void deleteAssetFromList(String assetID) {
        for (Asset asset: assetList) {
            if (asset.getAssetID().equals(assetID)) {
                assetList.remove(asset);
            }
        }
    }

}

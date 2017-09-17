package xyz.taika.cryptohodler;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by Jukka on 5.8.2017. JEE
 */

public class AssetList implements Serializable {

    private ArrayList<Asset> assetList;

    public AssetList() {
        this.assetList = new ArrayList<>();
    }

    public AssetList(ArrayList<Asset> assetList) {
        this.assetList = assetList;
    }

    public ArrayList<Asset> getAssetList() {
        return assetList;
    }

    public void addNewAssetToList(String assetName) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName));
        }
    }

    public void addNewAssetToList(String assetName, Double assetQuantity) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName, assetQuantity));
        }
    }

    public void addNewAssetToList(String assetName, Double assetQuantity, int imageResourceID) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName, assetQuantity, imageResourceID));
        }
    }

    public boolean isAlreadyInList(String assetName) {
        for (Asset asset : assetList) {
            if (asset.getAssetName().equals(assetName)) {
                return true;
            }
        }
        return false;
    }

    public void deleteAssetFromList(String assetID) {
        for (Asset asset: assetList) {
            if (asset.getAssetID().equals(assetID)) {
                assetList.remove(asset);
            }
        }
    }

    // Save assetlist to internal storage
    public void saveAssetListToInternalStorage(Context ctx) {
        try {
            String filename = "AssetListData";
            FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream of = new ObjectOutputStream(fos);
            of.writeObject(this.assetList);
            of.flush();
            of.close();
            fos.close();
        }
        catch (Exception e) {
            Log.e("InternalStorage", e.getMessage());
        }
    }

    //Get assetlist data from internal Storage
    public ArrayList<Asset> readFromInternalStorage(Context ctx) {
        ArrayList<Asset> toReturn = new ArrayList<>();
        FileInputStream fis;
        try {
            fis = ctx.openFileInput("AssetListData");
            ObjectInputStream oi = new ObjectInputStream(fis);
            toReturn = (ArrayList<Asset>) oi.readObject();
            oi.close();
        } catch (IOException e) {
            Log.e("InternalStorage", e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

}

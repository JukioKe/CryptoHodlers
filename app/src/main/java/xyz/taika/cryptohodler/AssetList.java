package xyz.taika.cryptohodler;


import java.io.Serializable;
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

    public void updateTotalValues() {
        for (Asset asset : assetList) {
            if (!asset.getAssetValue().isNaN()) {
                asset.setTotalValue(asset.getAssetValue());
            }
        }
    }

    public void deleteAssetFromList(String assetID) {
        for (int i=0; i<assetList.size(); i++) {
            if (assetList.get(i).getAssetID().equals(assetID)) {
                assetList.remove(i);
            }
        }
    }

}

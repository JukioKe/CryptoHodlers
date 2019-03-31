package xyz.taika.cryptohodler;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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


    public void addNewAssetToList(String assetName, Double assetQuantity) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName, assetQuantity));
        }
    }

    public boolean addNewAssetToList(String assetName, String assetSymbol, Double assetQuantity) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName, assetSymbol, assetQuantity));
            return true;
        }
        return false;
    }

    public void addNewAssetToList(String assetName, Double assetQuantity, int imageResourceID) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName, assetQuantity, imageResourceID));
        }
    }

    public void addNewAssetToList(String assetName, String assetSymbol, Double assetQuantity, int imageResourceID) {
        if (!isAlreadyInList(assetName)) {
            assetList.add(new Asset(assetName, assetSymbol, assetQuantity, imageResourceID));
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

    //Updates total values to all assets in list
    void updateTotalValues() {
        for (Asset asset : assetList) {
            System.out.println(asset.toString());
            if (asset.getAssetValue() != null) {
                asset.calculateAssetTotalValue();
            }
        }
    }

    void deleteAssetFromList(String assetName) {
        for (int i=0; i<assetList.size(); i++) {
            if (assetList.get(i).getAssetName().toLowerCase().equals(assetName.toLowerCase())) {
                assetList.remove(i);
            }
        }
    }

    void sortList() {
        Collections.sort(assetList, new Comparator<Asset>() {
            @Override public int compare(Asset p1, Asset p2) {

                try {
                    if (p1.getTotalValue() > p2.getTotalValue()) {
                        return -1;
                    } else if (p1.getTotalValue() < p2.getTotalValue()) {
                        return 1;
                    }
                    return 0;
                } catch (Exception e) {
                    return 1;
                }
            }
        });

    }

}

package xyz.taika.cryptohodler;

import java.io.Serializable;

/**
 * Created by jukka1 on 5.8.2017.
 */

public class Asset implements Serializable {

    private String assetName;
    private String assetID;
    private Double assetValue;
    private Double assetQuantity;
    private Double totalValue;
    private int imageResourceId;

    public Asset(String assetName) {
        this.assetName = assetName;
        this.assetID = assetName.toLowerCase();
        this.assetQuantity = 0.0;
        this.imageResourceId = R.mipmap.crypto_hodlers_icon;
    }

    public Asset(String assetName, Double assetQuantity) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.assetID = assetName.toLowerCase();
        this.imageResourceId = R.mipmap.crypto_hodlers_icon;
    }

    public Asset(String assetName, Double assetQuantity, int imageResourceId) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.imageResourceId = imageResourceId;
        this.assetID = assetName.toLowerCase();
    }


    public String getAssetName() {
        return assetName;
    }

    public Double getAssetValue() {
        return assetValue;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public void setAssetValue(Double assetValue) {
        this.assetValue = assetValue;
    }

    public Double getAssetQuantity() {
        return assetQuantity;
    }

    public void setAssetQuantity(Double assetQuantity) {
        this.assetQuantity = assetQuantity;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setTotalValue(Double price) {
        this.totalValue = this.assetQuantity * price;
    }

    public Double getTotalValue() {
        return totalValue;
    }
}

package xyz.taika.cryptohodler;

/**
 * Created by jukka1 on 5.8.2017.
 */

public class Asset {

    private String assetName;
    private Double assetValue;
    private Double assetQuantity;
    private int imageResourceId;

    public Asset(String assetName) {
        this.assetName = assetName;
    }

    public Asset(String assetName, Double assetQuantity) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
    }

    public Asset(String assetName, Double assetQuantity, int imageResourceId) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.imageResourceId = imageResourceId;
    }

    public Asset(String assetName, Double assetValue, Double assetQuantity) {
        this.assetName = assetName;
        this.assetValue = assetValue;
        this.assetQuantity = assetQuantity;
    }

    public Asset(String assetName, Double assetQuantity, Double assetValue, int imageResourceId) {
        this.assetName = assetName;
        this.assetValue = assetValue;
        this.assetQuantity = assetQuantity;
        this.imageResourceId = imageResourceId;
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
}

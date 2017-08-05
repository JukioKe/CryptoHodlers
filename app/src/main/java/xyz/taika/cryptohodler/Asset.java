package xyz.taika.cryptohodler;

/**
 * Created by jukka1 on 5.8.2017.
 */

public class Asset {

    private String assetName;
    private Double assetValue;

    public Asset(String assetName) {
        this.assetName = assetName;
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
}

package xyz.taika.cryptohodler;

import java.io.Serializable;


/**
 * Created by jukka1 on 5.8.2017.
 */

public class Asset implements Serializable {

    private String assetName;
    private String assetSymbol;
    private Double assetValue;
    private Double assetQuantity;
    private Double totalValue;
    private Double change24h;
    private Double change1h;
    private Double change7d;
    private int imageResourceId;

    public Asset(String assetName) {
        this.assetName = assetName;
        this.assetQuantity = 0.0;
        this.assetValue = 0.0;
        this.change24h = 0.0;
        this.change1h = 0.0;
        this.change7d = 0.0;
        this.imageResourceId = R.mipmap.crypto_hodlers_icon;
        calculateAssetTotalValue();
    }

    public Asset(String assetName, int imageResourceId) {
        this.assetName = assetName;
        this.assetQuantity = 0.0;
        this.assetValue = 0.0;
        this.change24h = 0.0;
        this.change1h = 0.0;
        this.change7d = 0.0;
        this.imageResourceId = imageResourceId;
        calculateAssetTotalValue();
    }

    Asset(String assetName, Double assetQuantity) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.assetValue = 0.0;
        this.change24h = 0.0;
        this.change1h = 0.0;
        this.change7d = 0.0;
        this.imageResourceId = R.mipmap.crypto_hodlers_icon;
        calculateAssetTotalValue();
    }

    Asset(String assetName, String assetSymbol, Double assetQuantity) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.assetSymbol = assetSymbol;
        this.assetValue = 0.0;
        this.change24h = 0.0;
        this.change1h = 0.0;
        this.change7d = 0.0;
        this.imageResourceId = R.mipmap.crypto_hodlers_icon;
        calculateAssetTotalValue();
    }

    Asset(String assetName, Double assetQuantity, int imageResourceId) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.assetValue = 0.0;
        this.change24h = 0.0;
        this.change1h = 0.0;
        this.change7d = 0.0;
        this.imageResourceId = imageResourceId;
        calculateAssetTotalValue();
    }

    public Asset(String assetName, String assetSymbol, Double assetQuantity, int imageResourceId) {
        this.assetName = assetName;
        this.assetQuantity = assetQuantity;
        this.imageResourceId = imageResourceId;
        this.assetValue = 0.0;
        this.change24h = 0.0;
        this.change1h = 0.0;
        this.change7d = 0.0;
        this.assetSymbol = assetSymbol;
        calculateAssetTotalValue();
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

    public void calculateAssetTotalValue() {
        this.totalValue = this.assetQuantity * this.assetValue;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public String getAssetSymbol() {
        return assetSymbol;
    }

    public Double getChange24h() {
        return change24h;
    }

    public void setChange24h(Double change24h) {
        this.change24h = change24h;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "assetName='" + assetName + '\'' +
                ", assetValue=" + assetValue +
                ", assetQuantity=" + assetQuantity +
                ", totalValue=" + totalValue +
                ", imageResourceId=" + imageResourceId +
                '}';
    }


}

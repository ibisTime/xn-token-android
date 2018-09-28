package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 币加宝购买
 * Created by cdkj on 2018/9/28.
 */

public class BiJiaBaoBuyModel implements Parcelable {

    private String coinSymbol;

    private String productCode;

    private String productName;

    private BigDecimal avilAmount;//剩余额度

    private float expectYield;//预期年化收益

    private BigDecimal increAmount;//递增金额

    public BigDecimal getIncreAmount() {
        return increAmount;
    }

    public void setIncreAmount(BigDecimal increAmount) {
        this.increAmount = increAmount;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getAvilAmount() {
        return avilAmount;
    }

    public void setAvilAmount(BigDecimal avilAmount) {
        this.avilAmount = avilAmount;
    }

    public float getExpectYield() {
        return expectYield;
    }

    public void setExpectYield(float expectYield) {
        this.expectYield = expectYield;
    }

    public BiJiaBaoBuyModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coinSymbol);
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
        dest.writeSerializable(this.avilAmount);
        dest.writeFloat(this.expectYield);
        dest.writeSerializable(this.increAmount);
    }

    protected BiJiaBaoBuyModel(Parcel in) {
        this.coinSymbol = in.readString();
        this.productCode = in.readString();
        this.productName = in.readString();
        this.avilAmount = (BigDecimal) in.readSerializable();
        this.expectYield = in.readFloat();
        this.increAmount = (BigDecimal) in.readSerializable();
    }

    public static final Creator<BiJiaBaoBuyModel> CREATOR = new Creator<BiJiaBaoBuyModel>() {
        @Override
        public BiJiaBaoBuyModel createFromParcel(Parcel source) {
            return new BiJiaBaoBuyModel(source);
        }

        @Override
        public BiJiaBaoBuyModel[] newArray(int size) {
            return new BiJiaBaoBuyModel[size];
        }
    };
}

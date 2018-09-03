package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 钱包列表资产
 * Created by cdkj on 2018/7/1.
 */

public class WalletBalanceModel implements Parcelable {

    private String coinImgUrl;
    private String coinSymbol;

    private String localMarketPrice;   //行情
    private String localAmount;//本地选择amount  （cny usd krw）
    private String coinType;
    private String accountNumber;
    private String address;
    private String coinBalance = "0";        //拥有币数量


    private BigDecimal frozenAmount;//冻结金额
    private BigDecimal amount;     //总金额

    private BigDecimal availableAmount;         //可用总额 =总金额-冻结金额

    public String getCoinImgUrl() {
        return coinImgUrl;
    }

    public void setCoinImgUrl(String coinImgUrl) {
        this.coinImgUrl = coinImgUrl;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getLocalMarketPrice() {
        return localMarketPrice;
    }

    public void setLocalMarketPrice(String localMarketPrice) {
        this.localMarketPrice = localMarketPrice;
    }

    public String getLocalAmount() {
        return localAmount;
    }

    public void setLocalAmount(String localAmount) {
        this.localAmount = localAmount;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoinBalance() {
        return coinBalance;
    }

    public void setCoinBalance(String coinBalance) {
        this.coinBalance = coinBalance;
    }

    public BigDecimal getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public WalletBalanceModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coinImgUrl);
        dest.writeString(this.coinSymbol);
        dest.writeString(this.localMarketPrice);
        dest.writeString(this.localAmount);
        dest.writeString(this.coinType);
        dest.writeString(this.accountNumber);
        dest.writeString(this.address);
        dest.writeString(this.coinBalance);
        dest.writeSerializable(this.frozenAmount);
        dest.writeSerializable(this.amount);
        dest.writeSerializable(this.availableAmount);
    }

    protected WalletBalanceModel(Parcel in) {
        this.coinImgUrl = in.readString();
        this.coinSymbol = in.readString();
        this.localMarketPrice = in.readString();
        this.localAmount = in.readString();
        this.coinType = in.readString();
        this.accountNumber = in.readString();
        this.address = in.readString();
        this.coinBalance = in.readString();
        this.frozenAmount = (BigDecimal) in.readSerializable();
        this.amount = (BigDecimal) in.readSerializable();
        this.availableAmount = (BigDecimal) in.readSerializable();
    }

    public static final Creator<WalletBalanceModel> CREATOR = new Creator<WalletBalanceModel>() {
        @Override
        public WalletBalanceModel createFromParcel(Parcel source) {
            return new WalletBalanceModel(source);
        }

        @Override
        public WalletBalanceModel[] newArray(int size) {
            return new WalletBalanceModel[size];
        }
    };
}

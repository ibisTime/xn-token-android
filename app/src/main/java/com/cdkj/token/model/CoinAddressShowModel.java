package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cdkj on 2018/8/31.
 */

public class CoinAddressShowModel implements Parcelable {

    private String address;

    private String coinSymbol;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeString(this.coinSymbol);
    }

    public CoinAddressShowModel() {
    }

    protected CoinAddressShowModel(Parcel in) {
        this.address = in.readString();
        this.coinSymbol = in.readString();
    }

    public static final Parcelable.Creator<CoinAddressShowModel> CREATOR = new Parcelable.Creator<CoinAddressShowModel>() {
        @Override
        public CoinAddressShowModel createFromParcel(Parcel source) {
            return new CoinAddressShowModel(source);
        }

        @Override
        public CoinAddressShowModel[] newArray(int size) {
            return new CoinAddressShowModel[size];
        }
    };
}

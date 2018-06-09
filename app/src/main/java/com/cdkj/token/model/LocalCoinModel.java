package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * 本地币种类型
 * Created by cdkj on 2018/6/8.
 */

public class LocalCoinModel implements Parcelable {


    private String coinEName;//币种英文名称

    private String coinShortName;//币种短名称

    private String coinCName;//币种中文名称

    private String coinType;//币种类型 用于区分是什么币种


    public String getCoinEName() {
        return coinEName;
    }

    public void setCoinEName(String coinEName) {
        this.coinEName = coinEName;
    }

    public String getCoinCName() {
        return coinCName;
    }

    public void setCoinCName(String coinCName) {
        this.coinCName = coinCName;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getCoinShortName() {
        return coinShortName;
    }

    public void setCoinShortName(String coinShortName) {
        this.coinShortName = coinShortName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coinEName);
        dest.writeString(this.coinShortName);
        dest.writeString(this.coinCName);
        dest.writeString(this.coinType);
    }

    public LocalCoinModel() {
    }

    protected LocalCoinModel(Parcel in) {
        this.coinEName = in.readString();
        this.coinShortName = in.readString();
        this.coinCName = in.readString();
        this.coinType = in.readString();
    }

    public static final Parcelable.Creator<LocalCoinModel> CREATOR = new Parcelable.Creator<LocalCoinModel>() {
        @Override
        public LocalCoinModel createFromParcel(Parcel source) {
            return new LocalCoinModel(source);
        }

        @Override
        public LocalCoinModel[] newArray(int size) {
            return new LocalCoinModel[size];
        }
    };
}

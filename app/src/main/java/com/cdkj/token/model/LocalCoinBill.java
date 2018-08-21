package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 本地币种流水
 * Created by cdkj on 2018/6/12.
 */

public class LocalCoinBill implements Parcelable {


    /**
     * txHash : 0x2297d9975eea16e8a1472ff949d766b1535551960f1387dcf7f90eadc46ec1d7
     * height : 660778
     * direction : 1
     * from : 0x8f84573c8bab4d56fddb48cc792424e8816908fb
     * to : 0x896f4ea0dde222bd55464d464898456859046ef4
     * value : 8000000000000000000
     * txFee : 4200000000000000
     * transDatetime : Mar 31, 2018 6:58:13 AM
     */

    private String txHash;
    private long height;
    private String direction;
    private String from;
    private String to;
    private BigDecimal value;
    private BigDecimal txFee;
    private String transDatetime;

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }


    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getTxFee() {
        return txFee;
    }

    public void setTxFee(BigDecimal txFee) {
        this.txFee = txFee;
    }

    public String getTransDatetime() {
        return transDatetime;
    }

    public void setTransDatetime(String transDatetime) {
        this.transDatetime = transDatetime;
    }

    public LocalCoinBill() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.txHash);
        dest.writeLong(this.height);
        dest.writeString(this.direction);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeSerializable(this.value);
        dest.writeSerializable(this.txFee);
        dest.writeString(this.transDatetime);
    }

    protected LocalCoinBill(Parcel in) {
        this.txHash = in.readString();
        this.height = in.readLong();
        this.direction = in.readString();
        this.from = in.readString();
        this.to = in.readString();
        this.value = (BigDecimal) in.readSerializable();
        this.txFee = (BigDecimal) in.readSerializable();
        this.transDatetime = in.readString();
    }

    public static final Creator<LocalCoinBill> CREATOR = new Creator<LocalCoinBill>() {
        @Override
        public LocalCoinBill createFromParcel(Parcel source) {
            return new LocalCoinBill(source);
        }

        @Override
        public LocalCoinBill[] newArray(int size) {
            return new LocalCoinBill[size];
        }
    };
}

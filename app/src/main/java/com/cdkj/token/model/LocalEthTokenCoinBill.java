package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 本地币种流水 (ETH token币种)
 * Created by cdkj on 2018/6/12.
 */

public class LocalEthTokenCoinBill implements Parcelable {


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

    private String blockHash;
    private long blockNumber;
    private String direction;
    private String from;
    private String to;
    private BigDecimal value;
    private BigDecimal txFee;
    private String createDatetime;

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
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

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public LocalEthTokenCoinBill() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.blockHash);
        dest.writeLong(this.blockNumber);
        dest.writeString(this.direction);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeSerializable(this.value);
        dest.writeSerializable(this.txFee);
        dest.writeString(this.createDatetime);
    }

    protected LocalEthTokenCoinBill(Parcel in) {
        this.blockHash = in.readString();
        this.blockNumber = in.readLong();
        this.direction = in.readString();
        this.from = in.readString();
        this.to = in.readString();
        this.value = (BigDecimal) in.readSerializable();
        this.txFee = (BigDecimal) in.readSerializable();
        this.createDatetime = in.readString();
    }

    public static final Creator<LocalEthTokenCoinBill> CREATOR = new Creator<LocalEthTokenCoinBill>() {
        @Override
        public LocalEthTokenCoinBill createFromParcel(Parcel source) {
            return new LocalEthTokenCoinBill(source);
        }

        @Override
        public LocalEthTokenCoinBill[] newArray(int size) {
            return new LocalEthTokenCoinBill[size];
        }
    };
}

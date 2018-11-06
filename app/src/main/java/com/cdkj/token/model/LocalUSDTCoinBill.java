package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by cdkj on 2018/11/1.
 */

public class LocalUSDTCoinBill implements Parcelable {


    /**
     * amount : 200000000000
     * block : 542445
     * blockHash : 0000000000000000001a65066c6e1a582d3e7a529198ca6c3c6697c0ccb32ade
     * blockTime : 1537566805
     * confirmations : 3808
     * divisible : true
     * fee : 9282
     * ismine : false
     * positioninBlock : 750
     * propertyId : 1
     * propertyName : Omni
     * sendingAddress : 1EXoDusjGwvnjZUyKkxZ4UHEf77z6A5S4P
     * txid : 7a6307608d3d229448085cff2ef1ca88abe9f00a9d944d6f591247404ee5bdf4
     * type : Simple Send
     * typeInt : 0
     * vaild : true
     * referenceAddress : 19hf8QEkD3GR7NhUrujWXRg6e4gsHUTysp
     */

    private BigDecimal amount;
    private int block;
    private String blockHash;
    private int blockTime;
    private int confirmations;
    private String divisible;
    private BigDecimal fee;
    private String ismine;
    private int positioninBlock;
    private int propertyId;
    private String propertyName;
    private String sendingAddress;
    private String txid;
    private String type;
    private int typeInt;
    private String vaild;
    private String referenceAddress;

    protected LocalUSDTCoinBill(Parcel in) {
        block = in.readInt();
        blockHash = in.readString();
        blockTime = in.readInt();
        confirmations = in.readInt();
        divisible = in.readString();
        ismine = in.readString();
        positioninBlock = in.readInt();
        propertyId = in.readInt();
        propertyName = in.readString();
        sendingAddress = in.readString();
        txid = in.readString();
        type = in.readString();
        typeInt = in.readInt();
        vaild = in.readString();
        referenceAddress = in.readString();
        fee = (BigDecimal) in.readSerializable();
        amount = (BigDecimal) in.readSerializable();
    }

    public static final Creator<LocalUSDTCoinBill> CREATOR = new Creator<LocalUSDTCoinBill>() {
        @Override
        public LocalUSDTCoinBill createFromParcel(Parcel in) {
            return new LocalUSDTCoinBill(in);
        }

        @Override
        public LocalUSDTCoinBill[] newArray(int size) {
            return new LocalUSDTCoinBill[size];
        }
    };

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(int blockTime) {
        this.blockTime = blockTime;
    }

    public int getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(int confirmations) {
        this.confirmations = confirmations;
    }

    public String getDivisible() {
        return divisible;
    }

    public void setDivisible(String divisible) {
        this.divisible = divisible;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getIsmine() {
        return ismine;
    }

    public void setIsmine(String ismine) {
        this.ismine = ismine;
    }

    public int getPositioninBlock() {
        return positioninBlock;
    }

    public void setPositioninBlock(int positioninBlock) {
        this.positioninBlock = positioninBlock;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSendingAddress() {
        return sendingAddress;
    }

    public void setSendingAddress(String sendingAddress) {
        this.sendingAddress = sendingAddress;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public String getVaild() {
        return vaild;
    }

    public void setVaild(String vaild) {
        this.vaild = vaild;
    }

    public String getReferenceAddress() {
        return referenceAddress;
    }

    public void setReferenceAddress(String referenceAddress) {
        this.referenceAddress = referenceAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(block);
        parcel.writeString(blockHash);
        parcel.writeInt(blockTime);
        parcel.writeInt(confirmations);
        parcel.writeString(divisible);
        parcel.writeString(ismine);
        parcel.writeInt(positioninBlock);
        parcel.writeInt(propertyId);
        parcel.writeString(propertyName);
        parcel.writeString(sendingAddress);
        parcel.writeString(txid);
        parcel.writeString(type);
        parcel.writeInt(typeInt);
        parcel.writeString(vaild);
        parcel.writeString(referenceAddress);
        parcel.writeSerializable(fee);
        parcel.writeSerializable(amount);
    }

    public LocalUSDTCoinBill(){

    }
}
package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by cdkj on 2018/8/16.
 */

public class MyManagementMoney implements Parcelable {


    /**
     * code : CP545435353342
     * name : 币币赢第一期
     * symbol : BTC
     * description : 产品详情描述
     * expectYield : 0.07
     * actualYield : 0.08
     * limitDays : 360
     * minAmount : 100
     * increAmount : 10
     * limitAmount : 500
     * amount : 1000
     * avilAmount : 100
     * saleAmount : 100
     * successAmount : 1000
     * saleNum : 4
     * status : 0
     * createDatetime : 2018-08-09 00:00:00
     * startDatetime : 2018-08-09 00:00:00
     * endDatetime : 2018-08-09 00:00:00
     * incomeDatetime : 2018-08-09 00:00:00
     * arriveDatetime : 2018-08-09 00:00:00
     * repayDatetime : 2018-08-09 00:00:00
     * paymentType : 0
     * approver : admin
     * approveDatetime : 2018-08-09 00:00:00
     * approveNote : 通过
     * updater : admin
     * updateDatetime : 2018-08-09 00:00:00
     * remark : 无
     */

    private String code;
    private String name;
    private String symbol;
    private float expectYield;
    private float actualYield;
    private int limitDays;
    private BigDecimal minAmount;
    private BigDecimal increAmount;
    private BigDecimal limitAmount;
    private BigDecimal amount;
    private BigDecimal investAmount;
    private BigDecimal expectIncome;
    private String saleNum;
    private String status;
    private String createDatetime;
    private String startDatetime;
    private String endDatetime;
    private String incomeDatetime;
    private String arriveDatetime;
    private String repayDatetime;
    private String approver;
    private String approveDatetime;
    private String approveNote;
    private String updater;
    private String updateDatetime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public float getExpectYield() {
        return expectYield;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public BigDecimal getExpectIncome() {
        return expectIncome;
    }

    public void setExpectIncome(BigDecimal expectIncome) {
        this.expectIncome = expectIncome;
    }

    public void setExpectYield(float expectYield) {
        this.expectYield = expectYield;
    }

    public float getActualYield() {
        return actualYield;
    }

    public void setActualYield(float actualYield) {
        this.actualYield = actualYield;
    }

    public int getLimitDays() {
        return limitDays;
    }

    public void setLimitDays(int limitDays) {
        this.limitDays = limitDays;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getIncreAmount() {
        return increAmount;
    }

    public void setIncreAmount(BigDecimal increAmount) {
        this.increAmount = increAmount;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(String saleNum) {
        this.saleNum = saleNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public String getIncomeDatetime() {
        return incomeDatetime;
    }

    public void setIncomeDatetime(String incomeDatetime) {
        this.incomeDatetime = incomeDatetime;
    }

    public String getArriveDatetime() {
        return arriveDatetime;
    }

    public void setArriveDatetime(String arriveDatetime) {
        this.arriveDatetime = arriveDatetime;
    }

    public String getRepayDatetime() {
        return repayDatetime;
    }

    public void setRepayDatetime(String repayDatetime) {
        this.repayDatetime = repayDatetime;
    }


    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(String approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public String getApproveNote() {
        return approveNote;
    }

    public void setApproveNote(String approveNote) {
        this.approveNote = approveNote;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }


    public MyManagementMoney() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.symbol);
        dest.writeFloat(this.expectYield);
        dest.writeFloat(this.actualYield);
        dest.writeInt(this.limitDays);
        dest.writeSerializable(this.minAmount);
        dest.writeSerializable(this.increAmount);
        dest.writeSerializable(this.limitAmount);
        dest.writeSerializable(this.amount);
        dest.writeSerializable(this.investAmount);
        dest.writeSerializable(this.expectIncome);
        dest.writeString(this.saleNum);
        dest.writeString(this.status);
        dest.writeString(this.createDatetime);
        dest.writeString(this.startDatetime);
        dest.writeString(this.endDatetime);
        dest.writeString(this.incomeDatetime);
        dest.writeString(this.arriveDatetime);
        dest.writeString(this.repayDatetime);
        dest.writeString(this.approver);
        dest.writeString(this.approveDatetime);
        dest.writeString(this.approveNote);
        dest.writeString(this.updater);
        dest.writeString(this.updateDatetime);
    }

    protected MyManagementMoney(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.symbol = in.readString();
        this.expectYield = in.readFloat();
        this.actualYield = in.readFloat();
        this.limitDays = in.readInt();
        this.minAmount = (BigDecimal) in.readSerializable();
        this.increAmount = (BigDecimal) in.readSerializable();
        this.limitAmount = (BigDecimal) in.readSerializable();
        this.amount = (BigDecimal) in.readSerializable();
        this.investAmount = (BigDecimal) in.readSerializable();
        this.expectIncome = (BigDecimal) in.readSerializable();
        this.saleNum = in.readString();
        this.status = in.readString();
        this.createDatetime = in.readString();
        this.startDatetime = in.readString();
        this.endDatetime = in.readString();
        this.incomeDatetime = in.readString();
        this.arriveDatetime = in.readString();
        this.repayDatetime = in.readString();
        this.approver = in.readString();
        this.approveDatetime = in.readString();
        this.approveNote = in.readString();
        this.updater = in.readString();
        this.updateDatetime = in.readString();
    }

    public static final Creator<MyManagementMoney> CREATOR = new Creator<MyManagementMoney>() {
        @Override
        public MyManagementMoney createFromParcel(Parcel source) {
            return new MyManagementMoney(source);
        }

        @Override
        public MyManagementMoney[] newArray(int size) {
            return new MyManagementMoney[size];
        }
    };
}

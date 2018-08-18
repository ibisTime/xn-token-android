package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * 我的理财产品
 * Created by cdkj on 2018/8/18.
 */

public class MyManamentMoneyProduct implements Parcelable {


    /**
     * code : IN4353453465354
     * productCode : CP4353453465354
     * userId : U4353453465354
     * investNum : 1
     * investAmount : 100
     * expectIncome : 5
     * income : 0
     * leftPrincipal : 100
     * redeemAmount : 0
     * redeemTimes : 0
     * lastInvestDatetime : 2018-08-08 00:00:00
     * lastRedeemDatetime : 2018-08-08 00:00:00
     * createDatetime : 2018-08-08 00:00:00
     * status : 0
     * productInfo : 参考product对象
     * userInfo : 参考user对象
     */

    private String code;
    private String productCode;
    private String userId;
    private String investNum;
    private BigDecimal investAmount;
    private String expectIncome;
    private String income;
    private String leftPrincipal;
    private String redeemAmount;
    private String redeemTimes;
    private String lastInvestDatetime;
    private String lastRedeemDatetime;
    private String createDatetime;
    private String status;
    private String productInfo;
    private String userInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInvestNum() {
        return investNum;
    }

    public void setInvestNum(String investNum) {
        this.investNum = investNum;
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public String getExpectIncome() {
        return expectIncome;
    }

    public void setExpectIncome(String expectIncome) {
        this.expectIncome = expectIncome;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getLeftPrincipal() {
        return leftPrincipal;
    }

    public void setLeftPrincipal(String leftPrincipal) {
        this.leftPrincipal = leftPrincipal;
    }

    public String getRedeemAmount() {
        return redeemAmount;
    }

    public void setRedeemAmount(String redeemAmount) {
        this.redeemAmount = redeemAmount;
    }

    public String getRedeemTimes() {
        return redeemTimes;
    }

    public void setRedeemTimes(String redeemTimes) {
        this.redeemTimes = redeemTimes;
    }

    public String getLastInvestDatetime() {
        return lastInvestDatetime;
    }

    public void setLastInvestDatetime(String lastInvestDatetime) {
        this.lastInvestDatetime = lastInvestDatetime;
    }

    public String getLastRedeemDatetime() {
        return lastRedeemDatetime;
    }

    public void setLastRedeemDatetime(String lastRedeemDatetime) {
        this.lastRedeemDatetime = lastRedeemDatetime;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(String productInfo) {
        this.productInfo = productInfo;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.productCode);
        dest.writeString(this.userId);
        dest.writeString(this.investNum);
        dest.writeSerializable(this.investAmount);
        dest.writeString(this.expectIncome);
        dest.writeString(this.income);
        dest.writeString(this.leftPrincipal);
        dest.writeString(this.redeemAmount);
        dest.writeString(this.redeemTimes);
        dest.writeString(this.lastInvestDatetime);
        dest.writeString(this.lastRedeemDatetime);
        dest.writeString(this.createDatetime);
        dest.writeString(this.status);
        dest.writeString(this.productInfo);
        dest.writeString(this.userInfo);
    }

    public MyManamentMoneyProduct() {
    }

    protected MyManamentMoneyProduct(Parcel in) {
        this.code = in.readString();
        this.productCode = in.readString();
        this.userId = in.readString();
        this.investNum = in.readString();
        this.investAmount = (BigDecimal) in.readSerializable();
        this.expectIncome = in.readString();
        this.income = in.readString();
        this.leftPrincipal = in.readString();
        this.redeemAmount = in.readString();
        this.redeemTimes = in.readString();
        this.lastInvestDatetime = in.readString();
        this.lastRedeemDatetime = in.readString();
        this.createDatetime = in.readString();
        this.status = in.readString();
        this.productInfo = in.readString();
        this.userInfo = in.readString();
    }

    public static final Parcelable.Creator<MyManamentMoneyProduct> CREATOR = new Parcelable.Creator<MyManamentMoneyProduct>() {
        @Override
        public MyManamentMoneyProduct createFromParcel(Parcel source) {
            return new MyManamentMoneyProduct(source);
        }

        @Override
        public MyManamentMoneyProduct[] newArray(int size) {
            return new MyManamentMoneyProduct[size];
        }
    };
}

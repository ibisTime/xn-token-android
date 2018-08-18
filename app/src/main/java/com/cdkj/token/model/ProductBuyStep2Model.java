package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * 产品购买第二步
 * Created by cdkj on 2018/8/18.
 */

public class ProductBuyStep2Model {

    public String productName;//购买产品

    public String buyAmountString;//购买额度

    public BigDecimal buyAmount;//购买额度

    public String endTime;//本息到期时间

    public String expectInComeAmount;//预计收益

    public String coinSymbol;

    public String getBuyAmountString() {
        return buyAmountString;
    }

    public void setBuyAmountString(String buyAmountString) {
        this.buyAmountString = buyAmountString;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getBuyAmount() {
        return buyAmount;
    }

    public void setBuyAmount(BigDecimal buyAmount) {
        this.buyAmount = buyAmount;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExpectInComeAmount() {
        return expectInComeAmount;
    }

    public void setExpectInComeAmount(String expectInComeAmount) {
        this.expectInComeAmount = expectInComeAmount;
    }
}

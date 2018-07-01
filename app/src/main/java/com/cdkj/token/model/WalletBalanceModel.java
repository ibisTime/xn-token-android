package com.cdkj.token.model;

import java.math.BigInteger;

/**
 * Created by cdkj on 2018/7/1.
 */

public class WalletBalanceModel {

    private String coinImgUrl;
    private String coinName;
    private String amount;
    private String marketPrice;
    private String amountCny;

    public String getCoinImgUrl() {
        return coinImgUrl;
    }

    public void setCoinImgUrl(String coinImgUrl) {
        this.coinImgUrl = coinImgUrl;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getAmountCny() {
        return amountCny;
    }

    public void setAmountCny(String amountCny) {
        this.amountCny = amountCny;
    }
}

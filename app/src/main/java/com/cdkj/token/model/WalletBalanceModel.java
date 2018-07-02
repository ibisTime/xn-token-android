package com.cdkj.token.model;

import java.math.BigInteger;

/**
 * Created by cdkj on 2018/7/1.
 */

public class WalletBalanceModel {

    private String coinImgUrl;
    private String coinName;
    private String amount;
    private String marketPriceCNY;
    private String marketPriceUSD;
    private String amountCny;
    private String amountUSD;

    public String getAmountUSD() {
        return amountUSD;
    }

    public void setAmountUSD(String amountUSD) {
        this.amountUSD = amountUSD;
    }

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

    public String getMarketPriceCNY() {
        return marketPriceCNY;
    }

    public void setMarketPriceCNY(String marketPriceCNY) {
        this.marketPriceCNY = marketPriceCNY;
    }

    public String getMarketPriceUSD() {
        return marketPriceUSD;
    }

    public void setMarketPriceUSD(String marketPriceUSD) {
        this.marketPriceUSD = marketPriceUSD;
    }

    public String getAmountCny() {
        return amountCny;
    }

    public void setAmountCny(String amountCny) {
        this.amountCny = amountCny;
    }
}

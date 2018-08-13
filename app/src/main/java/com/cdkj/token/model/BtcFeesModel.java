package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * btc转账矿工费
 * Created by cdkj on 2018/8/13.
 */

public class BtcFeesModel {

    /**
     * fastestFeeMin : 12
     * fastestFeeMax : 24
     * halfHourFeeMin : 12
     * halfHourFeeMax : 24
     * hourFeeMin : 8
     * hourFeeMax : 16
     */

    private BigDecimal fastestFeeMin;
    private BigDecimal fastestFeeMax;
    private BigDecimal halfHourFeeMin;
    private BigDecimal halfHourFeeMax;
    private BigDecimal hourFeeMin;
    private BigDecimal hourFeeMax;

    public BigDecimal getFastestFeeMin() {
        return fastestFeeMin;
    }

    public void setFastestFeeMin(BigDecimal fastestFeeMin) {
        this.fastestFeeMin = fastestFeeMin;
    }

    public BigDecimal getFastestFeeMax() {
        return fastestFeeMax;
    }

    public void setFastestFeeMax(BigDecimal fastestFeeMax) {
        this.fastestFeeMax = fastestFeeMax;
    }

    public BigDecimal getHalfHourFeeMin() {
        return halfHourFeeMin;
    }

    public void setHalfHourFeeMin(BigDecimal halfHourFeeMin) {
        this.halfHourFeeMin = halfHourFeeMin;
    }

    public BigDecimal getHalfHourFeeMax() {
        return halfHourFeeMax;
    }

    public void setHalfHourFeeMax(BigDecimal halfHourFeeMax) {
        this.halfHourFeeMax = halfHourFeeMax;
    }

    public BigDecimal getHourFeeMin() {
        return hourFeeMin;
    }

    public void setHourFeeMin(BigDecimal hourFeeMin) {
        this.hourFeeMin = hourFeeMin;
    }

    public BigDecimal getHourFeeMax() {
        return hourFeeMax;
    }

    public void setHourFeeMax(BigDecimal hourFeeMax) {
        this.hourFeeMax = hourFeeMax;
    }
}

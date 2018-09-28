package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * 用户投资数据
 * Created by cdkj on 2018/9/28.
 */

public class InvestmentAmountModel {

    private BigDecimal totalInvest;//投资总额
    private BigDecimal totalIncome;//总收益

    public BigDecimal getTotalInvest() {
        return totalInvest;
    }

    public void setTotalInvest(BigDecimal totalInvest) {
        this.totalInvest = totalInvest;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
}

package com.cdkj.token.model;


import java.math.BigDecimal;
import java.util.List;

public class BjbMyIncome {

    /**
     * userId : U1234567890
     * incomeYesterday : 10.0000
     * incomeTotal : 10.0000
     * incomePop : 1.0000
     * incomeRatioPop : 0.1
     * incomeInvite : 9.0000
     * incomeRatioInvite : 0.9
     * top5 : [{"rank":"1","mobile":"+86 15268501481","incomeTotal":"10.0000"}]
     */

    private String userId;
    private BigDecimal incomeYesterday;
    private BigDecimal incomeTotal;
    private BigDecimal incomePop;
    private String incomeRatioPop;
    private BigDecimal incomeInvite;
    private String incomeRatioInvite;
    private List<Top5> top5;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getIncomeYesterday() {
        return incomeYesterday;
    }

    public void setIncomeYesterday(BigDecimal incomeYesterday) {
        this.incomeYesterday = incomeYesterday;
    }

    public BigDecimal getIncomeTotal() {
        return incomeTotal;
    }

    public void setIncomeTotal(BigDecimal incomeTotal) {
        this.incomeTotal = incomeTotal;
    }

    public BigDecimal getIncomePop() {
        return incomePop;
    }

    public void setIncomePop(BigDecimal incomePop) {
        this.incomePop = incomePop;
    }

    public String getIncomeRatioPop() {
        return incomeRatioPop;
    }

    public void setIncomeRatioPop(String incomeRatioPop) {
        this.incomeRatioPop = incomeRatioPop;
    }

    public BigDecimal getIncomeInvite() {
        return incomeInvite;
    }

    public void setIncomeInvite(BigDecimal incomeInvite) {
        this.incomeInvite = incomeInvite;
    }

    public String getIncomeRatioInvite() {
        return incomeRatioInvite;
    }

    public void setIncomeRatioInvite(String incomeRatioInvite) {
        this.incomeRatioInvite = incomeRatioInvite;
    }

    public List<Top5> getTop5() {
        return top5;
    }

    public void setTop5(List<Top5> top5) {
        this.top5 = top5;
    }

    public static class Top5{

        /**
         * rank : 1
         * mobile : +86 15268501481
         * incomeTotal : 10.0000
         */

        private Integer rank;
        private String mobile;
        private BigDecimal incomeTotal;

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public BigDecimal getIncomeTotal() {
            return incomeTotal;
        }

        public void setIncomeTotal(BigDecimal incomeTotal) {
            this.incomeTotal = incomeTotal;
        }
    }

}

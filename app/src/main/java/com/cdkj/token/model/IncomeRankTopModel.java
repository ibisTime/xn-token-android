package com.cdkj.token.model;


import java.math.BigDecimal;
import java.util.List;

public class IncomeRankTopModel {


    /**
     * rank : 2
     * mobile : +86 15268501481
     * incomeTotal : 10.2
     * top100 : [{"rank":"1","mobile":"+86 15268501481","incomeTotal":"10.2","photo":"pic"}]
     */

    private Integer rank;
    private String mobile;
    private BigDecimal incomeTotal;
    private List<Top100Bean> top100;

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

    public List<Top100Bean> getTop100() {
        return top100;
    }

    public void setTop100(List<Top100Bean> top100) {
        this.top100 = top100;
    }

    public static class Top100Bean {
        /**
         * rank : 1
         * mobile : +86 15268501481
         * incomeTotal : 10.2
         * photo : pic
         */

        private Integer rank;
        private String mobile;
        private BigDecimal incomeTotal;
        private String photo;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}

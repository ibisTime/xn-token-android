package com.cdkj.token.model;

/**
 * Created by cdkj on 2018/10/30.
 */

public class ChoiceCoinModel {


    /**
     * coin : {"symbol":"WAN","ename":"wancoin","cname":"万维币","type":"0","unit":18,"icon":"wan@3x_1529382562363.png","pic1":"wan@3x_1529382569806.png","pic2":"转入@3x_1529382576037.png","pic3":"出@3x_1529382615418.png","orderNo":0,"withdrawFee":1000000000000000,"withdrawFeeString":"1000000000000000","status":"0"}
     * isDisplay : 0
     */

    private CoinBean coin;
    private String isDisplay;

    public CoinBean getCoin() {
        return coin;
    }

    public void setCoin(CoinBean coin) {
        this.coin = coin;
    }

    public String getIsDisplay() {
        return isDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        this.isDisplay = isDisplay;
    }

    public static class CoinBean {
        /**
         * symbol : WAN
         * ename : wancoin
         * cname : 万维币
         * type : 0
         * unit : 18
         * icon : wan@3x_1529382562363.png
         * pic1 : wan@3x_1529382569806.png
         * pic2 : 转入@3x_1529382576037.png
         * pic3 : 出@3x_1529382615418.png
         * orderNo : 0
         * withdrawFee : 1000000000000000
         * withdrawFeeString : 1000000000000000
         * status : 0
         */

        private String symbol;
        private String ename;
        private String cname;
        private String type;
        private int unit;
        private String icon;
        private String pic1;
        private String pic2;
        private String pic3;
        private int orderNo;
        private long withdrawFee;
        private String withdrawFeeString;
        private String status;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUnit() {
            return unit;
        }

        public void setUnit(int unit) {
            this.unit = unit;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPic1() {
            return pic1;
        }

        public void setPic1(String pic1) {
            this.pic1 = pic1;
        }

        public String getPic2() {
            return pic2;
        }

        public void setPic2(String pic2) {
            this.pic2 = pic2;
        }

        public String getPic3() {
            return pic3;
        }

        public void setPic3(String pic3) {
            this.pic3 = pic3;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public long getWithdrawFee() {
            return withdrawFee;
        }

        public void setWithdrawFee(long withdrawFee) {
            this.withdrawFee = withdrawFee;
        }

        public String getWithdrawFeeString() {
            return withdrawFeeString;
        }

        public void setWithdrawFeeString(String withdrawFeeString) {
            this.withdrawFeeString = withdrawFeeString;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

package com.cdkj.token.model;

import java.util.List;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class MyGetRedPackageBean {


    /**
     * list : [{"bestHandCount":0,"code":"RP201807030335590066003","createDateTime":"Jul 3, 2018 3:35:59 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030333149149364","createDateTime":"Jul 3, 2018 3:33:14 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":2,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.01,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030332433712842","createDateTime":"Jul 3, 2018 3:32:43 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030326394547422","createDateTime":"Jul 3, 2018 3:26:39 AM","greeting":"糖包一响,黄金万两哈哈","receivedCount":0,"receivedNum":0,"sendNum":2,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030326236744520","createDateTime":"Jul 3, 2018 3:26:23 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":2,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030322159095779","createDateTime":"Jul 3, 2018 3:22:15 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030305350296811","createDateTime":"Jul 3, 2018 3:05:35 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030304056568418","createDateTime":"Jul 3, 2018 3:04:05 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030230473633217","createDateTime":"Jul 3, 2018 2:30:47 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"},{"bestHandCount":0,"code":"RP201807030230289875241","createDateTime":"Jul 3, 2018 2:30:28 AM","greeting":"糖包一响,黄金万两","receivedCount":0,"receivedNum":0,"sendNum":3,"singleCount":0,"status":"0","symbol":"WAN","totalCount":0.03,"type":"1","userId":"U201807021702422559546"}]
     * pageNO : 1.0
     * pageSize : 10.0
     * start : 0.0
     * totalCount : 33.0
     * totalPage : 4.0
     */

    private double pageNO;
    private double pageSize;
    private double start;
    private double totalCount;
    private double totalPage;
    private List<ListBean> list;

    public double getPageNO() {
        return pageNO;
    }

    public void setPageNO(double pageNO) {
        this.pageNO = pageNO;
    }

    public double getPageSize() {
        return pageSize;
    }

    public void setPageSize(double pageSize) {
        this.pageSize = pageSize;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(double totalCount) {
        this.totalCount = totalCount;
    }

    public double getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(double totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {

        /**
         * code : RP201807030422332791158
         * userId : U201807021702422559546
         * symbol : WAN
         * type : 1
         * totalCount : 0.03
         * singleCount : 0.0
         * greeting : 糖包一响,黄金万两
         * sendNum : 2.0
         * receivedNum : 0.0
         * receivedCount : 0.0
         * bestHandCount : 0.0
         * createDateTime : Jul 3, 2018 4:22:33 AM
         * status : 0
         * sendUserPhoto : ANDROID_1530559246149_362_442.jpg
         * sendUserNickname : THA8237
         * totalCountCNY : 0.0
         */

        private String code;
        private String userId;
        private String symbol;
        private String type;
        private double totalCount;
        private double singleCount;
        private String greeting;
        private double sendNum;
        private double receivedNum;
        private double receivedCount;
        private double bestHandCount;
        private String createDateTime;
        private String status;
        private String sendUserPhoto;
        private String sendUserNickname;
        private double totalCountCNY;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(double totalCount) {
            this.totalCount = totalCount;
        }

        public double getSingleCount() {
            return singleCount;
        }

        public void setSingleCount(double singleCount) {
            this.singleCount = singleCount;
        }

        public String getGreeting() {
            return greeting;
        }

        public void setGreeting(String greeting) {
            this.greeting = greeting;
        }

        public double getSendNum() {
            return sendNum;
        }

        public void setSendNum(double sendNum) {
            this.sendNum = sendNum;
        }

        public double getReceivedNum() {
            return receivedNum;
        }

        public void setReceivedNum(double receivedNum) {
            this.receivedNum = receivedNum;
        }

        public double getReceivedCount() {
            return receivedCount;
        }

        public void setReceivedCount(double receivedCount) {
            this.receivedCount = receivedCount;
        }

        public double getBestHandCount() {
            return bestHandCount;
        }

        public void setBestHandCount(double bestHandCount) {
            this.bestHandCount = bestHandCount;
        }

        public String getCreateDateTime() {
            return createDateTime;
        }

        public void setCreateDateTime(String createDateTime) {
            this.createDateTime = createDateTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSendUserPhoto() {
            return sendUserPhoto;
        }

        public void setSendUserPhoto(String sendUserPhoto) {
            this.sendUserPhoto = sendUserPhoto;
        }

        public String getSendUserNickname() {
            return sendUserNickname;
        }

        public void setSendUserNickname(String sendUserNickname) {
            this.sendUserNickname = sendUserNickname;
        }

        public double getTotalCountCNY() {
            return totalCountCNY;
        }

        public void setTotalCountCNY(double totalCountCNY) {
            this.totalCountCNY = totalCountCNY;
        }
    }
}

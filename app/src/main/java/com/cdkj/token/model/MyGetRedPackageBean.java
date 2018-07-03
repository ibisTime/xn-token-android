package com.cdkj.token.model;

/**
 * @author qi
 * @updateDts 2018/7/2
 */

public class MyGetRedPackageBean {


    /**
     * id : 51
     * userId : U201807031658173735279
     * redPacketCode : RP201807031743244882018
     * count : 1.615
     * createDatetime : Jul 3, 2018 5:44:05 PM
     * countCNY : 0
     * redPacketInfo : {"code":"RP201807031743244882018","userId":"U201807031719257548694","symbol":"LXT","type":"1","totalCount":4,"singleCount":0,"greeting":"红包一响,黄金万两","sendNum":3,"receivedNum":1,"receivedCount":1.615,"lastReceivedDatetime":"Jul 3, 2018 5:44:05 PM","bestHandUser":"U201807031658173735279","bestHandCount":1.615,"createDateTime":"Jul 3, 2018 5:43:24 PM","status":"1","sendUserNickname":"THA9999","totalCountCNY":0}
     */

    private String id;
    private String userId;
    private String redPacketCode;
    private double count;
    private String createDatetime;
    private int countCNY;
    private RedPacketInfoBean redPacketInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRedPacketCode() {
        return redPacketCode;
    }

    public void setRedPacketCode(String redPacketCode) {
        this.redPacketCode = redPacketCode;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public int getCountCNY() {
        return countCNY;
    }

    public void setCountCNY(int countCNY) {
        this.countCNY = countCNY;
    }

    public RedPacketInfoBean getRedPacketInfo() {
        return redPacketInfo;
    }

    public void setRedPacketInfo(RedPacketInfoBean redPacketInfo) {
        this.redPacketInfo = redPacketInfo;
    }

    public static class RedPacketInfoBean {
        /**
         * code : RP201807031743244882018
         * userId : U201807031719257548694
         * symbol : LXT
         * type : 1
         * totalCount : 4
         * singleCount : 0
         * greeting : 红包一响,黄金万两
         * sendNum : 3
         * receivedNum : 1
         * receivedCount : 1.615
         * lastReceivedDatetime : Jul 3, 2018 5:44:05 PM
         * bestHandUser : U201807031658173735279
         * bestHandCount : 1.615
         * createDateTime : Jul 3, 2018 5:43:24 PM
         * status : 1
         * sendUserNickname : THA9999
         * totalCountCNY : 0
         */

        private String code;
        private String userId;
        private String symbol;
        private String type;
        private int totalCount;
        private int singleCount;
        private String greeting;
        private int sendNum;
        private int receivedNum;
        private double receivedCount;
        private String lastReceivedDatetime;
        private String bestHandUser;
        private double bestHandCount;
        private String createDateTime;
        private String status;
        private String sendUserNickname;
        private String sendUserPhoto;
        private int totalCountCNY;

        public String getSendUserPhoto() {
            return sendUserPhoto;
        }

        public void setSendUserPhoto(String sendUserPhoto) {
            this.sendUserPhoto = sendUserPhoto;
        }

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

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getSingleCount() {
            return singleCount;
        }

        public void setSingleCount(int singleCount) {
            this.singleCount = singleCount;
        }

        public String getGreeting() {
            return greeting;
        }

        public void setGreeting(String greeting) {
            this.greeting = greeting;
        }

        public int getSendNum() {
            return sendNum;
        }

        public void setSendNum(int sendNum) {
            this.sendNum = sendNum;
        }

        public int getReceivedNum() {
            return receivedNum;
        }

        public void setReceivedNum(int receivedNum) {
            this.receivedNum = receivedNum;
        }

        public double getReceivedCount() {
            return receivedCount;
        }

        public void setReceivedCount(double receivedCount) {
            this.receivedCount = receivedCount;
        }

        public String getLastReceivedDatetime() {
            return lastReceivedDatetime;
        }

        public void setLastReceivedDatetime(String lastReceivedDatetime) {
            this.lastReceivedDatetime = lastReceivedDatetime;
        }

        public String getBestHandUser() {
            return bestHandUser;
        }

        public void setBestHandUser(String bestHandUser) {
            this.bestHandUser = bestHandUser;
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

        public String getSendUserNickname() {
            return sendUserNickname;
        }

        public void setSendUserNickname(String sendUserNickname) {
            this.sendUserNickname = sendUserNickname;
        }

        public int getTotalCountCNY() {
            return totalCountCNY;
        }

        public void setTotalCountCNY(int totalCountCNY) {
            this.totalCountCNY = totalCountCNY;
        }
    }
}

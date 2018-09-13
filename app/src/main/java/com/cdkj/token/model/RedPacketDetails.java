package com.cdkj.token.model;

import java.util.List;

/**
 * Created by cdkj on 2018/9/13.
 */

public class RedPacketDetails {

    /**
     * code : RP201809131503172613068
     * userId : U201808312015330514271
     * symbol : ETH
     * type : 1
     * totalCount : 10.0
     * singleCount : 0.0
     * greeting : 红包一响,黄金万两
     * sendNum : 5.0
     * receivedNum : 1.0
     * receivedCount : 3.97
     * lastReceivedDatetime : Sep 13, 2018 3:04:22 PM
     * bestHandUser : U201808301855520323902
     * bestHandCount : 3.97
     * createDateTime : Sep 13, 2018 3:03:17 PM
     * status : 1
     * sendUserNickname : THA1712
     * bestHandUserNickname : THA1702
     * isReceived : 0
     * totalCountCNY : 13488.478
     * receiverList : [{"id":"2","userId":"U201808301855520323902","redPacketCode":"RP201809131503172613068","count":3.97,"createDatetime":"Sep 13, 2018 3:04:22 PM","userNickname":"THA1702","countCNY":5354.925766}]
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
    private String lastReceivedDatetime;
    private String bestHandUser;
    private double bestHandCount;
    private String createDateTime;
    private String status;
    private String sendUserNickname;
    private String bestHandUserNickname;
    private String sendUserPhoto;
    private String isReceived;
    private double totalCountCNY;
    private List<ReceiverListBean> receiverList;

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

    public String getBestHandUserNickname() {
        return bestHandUserNickname;
    }

    public void setBestHandUserNickname(String bestHandUserNickname) {
        this.bestHandUserNickname = bestHandUserNickname;
    }

    public String getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(String isReceived) {
        this.isReceived = isReceived;
    }

    public double getTotalCountCNY() {
        return totalCountCNY;
    }

    public void setTotalCountCNY(double totalCountCNY) {
        this.totalCountCNY = totalCountCNY;
    }

    public List<ReceiverListBean> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<ReceiverListBean> receiverList) {
        this.receiverList = receiverList;
    }

    public static class ReceiverListBean {
        /**
         * id : 2
         * userId : U201808301855520323902
         * redPacketCode : RP201809131503172613068
         * count : 3.97
         * createDatetime : Sep 13, 2018 3:04:22 PM
         * userNickname : THA1702
         * countCNY : 5354.925766
         */

        private String id;
        private String userId;
        private String redPacketCode;
        private double count;
        private String createDatetime;
        private String userNickname;
        private double countCNY;

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

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }

        public double getCountCNY() {
            return countCNY;
        }

        public void setCountCNY(double countCNY) {
            this.countCNY = countCNY;
        }
    }
}

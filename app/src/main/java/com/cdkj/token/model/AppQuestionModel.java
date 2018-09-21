package com.cdkj.token.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by cdkj on 2018/9/14.
 */

public class AppQuestionModel {


    /**
     * dapp : {"code":"dapp20180809001","name":"发红包","icon":"红包@3x_1535724770122.png","slogan":"发数字货币红包、查收发记录和提现","description":"红包规则\r\n\r\n       红包应用是THA钱包推出的一款，规则如下：\r\n\r\n1.        单个红包发送金额在0.001\u20141000000（一百万）个数字货币之间\r\n\r\n2.        红包个数限制在1-100个\r\n\r\n3.        手气红包，领取红包人个数随机，获得最多人为\u201c手气最佳\u201d\r\n\r\n4.        普通红包，领取红包人金额均等\r\n\r\n5.        领取红包的新用户自动成为发送红包用户的徒弟\r\n","action":"red_packet","location":"0","orderNo":0,"status":"1","updater":"Leo","updateDatetime":"Aug 31, 2018 10:12:57 PM","remark":"无","language":"ZH_CN"}
     * helpList : [{"id":2,"question":"怎么发红包啊update？","answer":"我要开始回答了，应该这样发红包update。","orderNo":"1","refType":"DAPP","refCode":"dapp20180809001","language":"ZH_CN"},{"id":2,"question":"怎么发红包啊update？","answer":"我要开始回答了，应该这样发红包update。","orderNo":"1","refType":"DAPP","refCode":"dapp20180809001","language":"ZH_CN"}]
     */

    private DappBean dapp;
    private List<HelpListBean> helpList;

    public DappBean getDapp() {
        return dapp;
    }

    public void setDapp(DappBean dapp) {
        this.dapp = dapp;
    }

    public List<HelpListBean> getHelpList() {
        return helpList;
    }

    public void setHelpList(List<HelpListBean> helpList) {
        this.helpList = helpList;
    }

    public static class DappBean {
        /**
         * code : dapp20180809001
         * name : 发红包
         * icon : 红包@3x_1535724770122.png
         * slogan : 发数字货币红包、查收发记录和提现
         * description : 红包规则

         红包应用是THA钱包推出的一款，规则如下：

         1.        单个红包发送金额在0.001—1000000（一百万）个数字货币之间

         2.        红包个数限制在1-100个

         3.        手气红包，领取红包人个数随机，获得最多人为“手气最佳”

         4.        普通红包，领取红包人金额均等

         5.        领取红包的新用户自动成为发送红包用户的徒弟

         * action : red_packet
         * location : 0
         * orderNo : 0
         * status : 1
         * updater : Leo
         * updateDatetime : Aug 31, 2018 10:12:57 PM
         * remark : 无
         * language : ZH_CN
         */

        private String code;
        private String name;
        private String icon;
        private String slogan;
        private String description;
        private String action;
        private String location;
        private int orderNo;
        private String status;
        private String updater;
        private String updateDatetime;
        private String remark;
        private String language;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSlogan() {
            return slogan;
        }

        public void setSlogan(String slogan) {
            this.slogan = slogan;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdater() {
            return updater;
        }

        public void setUpdater(String updater) {
            this.updater = updater;
        }

        public String getUpdateDatetime() {
            return updateDatetime;
        }

        public void setUpdateDatetime(String updateDatetime) {
            this.updateDatetime = updateDatetime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }

    public static class HelpListBean implements Parcelable {
        /**
         * id : 2
         * question : 怎么发红包啊update？
         * answer : 我要开始回答了，应该这样发红包update。
         * orderNo : 1
         * refType : DAPP
         * refCode : dapp20180809001
         * language : ZH_CN
         */

        private int id;
        private String question;
        private String answer;
        private String orderNo;
        private String refType;
        private String refCode;
        private String language;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getRefType() {
            return refType;
        }

        public void setRefType(String refType) {
            this.refType = refType;
        }

        public String getRefCode() {
            return refCode;
        }

        public void setRefCode(String refCode) {
            this.refCode = refCode;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.question);
            dest.writeString(this.answer);
            dest.writeString(this.orderNo);
            dest.writeString(this.refType);
            dest.writeString(this.refCode);
            dest.writeString(this.language);
        }

        public HelpListBean() {
        }

        protected HelpListBean(Parcel in) {
            this.id = in.readInt();
            this.question = in.readString();
            this.answer = in.readString();
            this.orderNo = in.readString();
            this.refType = in.readString();
            this.refCode = in.readString();
            this.language = in.readString();
        }

        public static final Parcelable.Creator<HelpListBean> CREATOR = new Parcelable.Creator<HelpListBean>() {
            @Override
            public HelpListBean createFromParcel(Parcel source) {
                return new HelpListBean(source);
            }

            @Override
            public HelpListBean[] newArray(int size) {
                return new HelpListBean[size];
            }
        };
    }
}

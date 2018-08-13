package com.cdkj.token.model;

/**
 * 推荐应用
 * Created by cdkj on 2018/8/13.
 */

public class RecommendAppModel {


    /**
     * code : DAPP234234
     * description : 首创玩法详细介绍
     * status : true
     * name : 首创玩法
     * icon : www.pic.com
     * slogan : 首创玩法概要介绍
     * language : 中文
     * action : none
     * location : 推荐
     * orderNo : 2
     * updater : admin
     * updateDatetime : 2018/8/9
     * remark : 无
     */

    private String code;
    private String description;
    private boolean status;
    private String name;
    private String icon;
    private String slogan;
    private String language;
    private String action;
    private String location;
    private int orderNo;
    private String updater;
    private String updateDatetime;
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
}

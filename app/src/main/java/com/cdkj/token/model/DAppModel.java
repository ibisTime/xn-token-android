package com.cdkj.token.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cdkj on 2018/12/11.
 */

public class DAppModel implements Serializable {


    /**
     * id : 5.0
     * category : 0
     * name : 强力球
     * company : 网易Leo
     * picList : 众筹图片4_1544508710419.jpg
     * picIcon : 众筹图片4_1544508883771.jpg
     * picScreenshot : -56dd96c481531f34_c_1535716086869.jpg
     * grade : 5.0
     * label : 0||1||2
     * desc : 应用描述应用描述应用描述应用描述应用描述应用描述应用描述
     * download : 1000
     * volume : 10
     * location : 0
     * orderNo : 1.0
     * action : 0
     * url : https://www.baidu.com
     * isTop : 0
     * status : 1
     * createDatetime : Dec 5, 2018 4:14:40 PM
     * labelList : ["博彩类","养成类","益智类"]
     * picScreenshotList : ["-56dd96c481531f34_c_1535716086869.jpg"]
     * language : ZH_CN
     */

    private int id;
    private String category;
    private String name;
    private String company;
    private String picList;
    private String picIcon;
    private String picScreenshot;
    private int grade;
    private String label;
    private String desc;
    private String download;
    private String volume;
    private String location;
    private double orderNo;
    private String action;
    private String url;
    private String isTop;
    private String status;
    private String createDatetime;
    private String language;
    private List<String> labelList;
    private List<String> picScreenshotList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
    }

    public String getPicIcon() {
        return picIcon;
    }

    public void setPicIcon(String picIcon) {
        this.picIcon = picIcon;
    }

    public String getPicScreenshot() {
        return picScreenshot;
    }

    public void setPicScreenshot(String picScreenshot) {
        this.picScreenshot = picScreenshot;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(double orderNo) {
        this.orderNo = orderNo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsTop() {
        return isTop;
    }

    public void setIsTop(String isTop) {
        this.isTop = isTop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getLabelList() {
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }

    public List<String> getPicScreenshotList() {
        return picScreenshotList;
    }

    public void setPicScreenshotList(List<String> picScreenshotList) {
        this.picScreenshotList = picScreenshotList;
    }
}

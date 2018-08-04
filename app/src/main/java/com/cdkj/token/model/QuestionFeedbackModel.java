package com.cdkj.token.model;

import java.math.BigDecimal;

/**
 * Created by cdkj on 2018/8/4.
 */

public class QuestionFeedbackModel {

    /**
     * code : BF201808042010314882750
     * deviceSystem : Android
     * description : 111
     * reappear : qqq
     * pic : ANDROID_1533383756400.jpg,ANDROID_1533383756894.jpg,ANDROID_1533383757222.jpg,ANDROID_1533383757794.jpg,ANDROID_1533383758362.jpg,ANDROID_1533383758982.jpg,ANDROID_1533383759481.jpg,ANDROID_1533383759738.jpg,ANDROID_1533384650247.jpg,ANDROID_1533384651165.jpg,ANDROID_1533384651558.jpg,ANDROID_1533384651868.jpg,ANDROID_1533384652591.jpg,ANDROID_1533384653298.jpg,ANDROID_1533384653770.jpg,ANDROID_1533384654092.jpg,ANDROID_1533384654696.jpg,
     * commitNote :
     * status : 0
     * commitUser : U201806141853019594911
     * commitDatetime : Aug 4, 2018 8:10:31 PM
     */

    private String code;
    private String deviceSystem;
    private String description;
    private String reappear;
    private String pic;
    private String commitNote;
    private String status;
    private String commitUser;
    private String commitDatetime;
    private String approveDatetime;
    private String payDatetime;
    private BigDecimal payAmount;
    private String repairVersionCode;
    private String level;//1严重 2 一般 3 优化

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    public String getApproveDatetime() {
        return approveDatetime;
    }

    public void setApproveDatetime(String approveDatetime) {
        this.approveDatetime = approveDatetime;
    }

    public String getRepairVersionCode() {
        return repairVersionCode;
    }

    public void setRepairVersionCode(String repairVersionCode) {
        this.repairVersionCode = repairVersionCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeviceSystem() {
        return deviceSystem;
    }

    public void setDeviceSystem(String deviceSystem) {
        this.deviceSystem = deviceSystem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReappear() {
        return reappear;
    }

    public void setReappear(String reappear) {
        this.reappear = reappear;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCommitNote() {
        return commitNote;
    }

    public void setCommitNote(String commitNote) {
        this.commitNote = commitNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser;
    }

    public String getCommitDatetime() {
        return commitDatetime;
    }

    public void setCommitDatetime(String commitDatetime) {
        this.commitDatetime = commitDatetime;
    }
}

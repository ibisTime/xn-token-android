package com.cdkj.baselibrary.model;

/**
 * 用于发送验证码
 * Created by cdkj on 2018/9/20.
 */

public class SendVerificationCode {

    private String phone;
    private String sessionID;
    private String bizType;
    private String kind;
    private String countryCode;

    public SendVerificationCode(String phone, String bizType, String kind, String countryCode) {
        this.phone = phone;
        this.bizType = bizType;
        this.kind = kind;
        this.countryCode = countryCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

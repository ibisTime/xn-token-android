package com.cdkj.baselibrary.model;

/**
 * Created by cdkj on 2017/6/9.
 */

public class UserLoginModel {

    private String userId;

    private String token;

    private String isRegister; //用于验证码登录时 用户是否时新用户

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package com.cdkj.token.model;

import org.litepal.crud.DataSupport;

/**
 * 钱包数据库
 * Created by cdkj on 2018/6/6.
 */

public class WalletDBModel extends DataSupport {

    private String passWord; //密码

    private String privataeKey;//私钥

    private String helpcenterEn;// 助记词 英文

    private String address;//地址

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPrivataeKey() {
        return privataeKey;
    }

    public void setPrivataeKey(String privataeKey) {
        this.privataeKey = privataeKey;
    }

    public String getHelpcenterEn() {
        return helpcenterEn;
    }

    public void setHelpcenterEn(String helpcenterEn) {
        this.helpcenterEn = helpcenterEn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

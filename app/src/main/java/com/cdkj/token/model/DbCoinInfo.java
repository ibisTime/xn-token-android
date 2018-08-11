package com.cdkj.token.model;

/**
 * 数据库单个币种
 * Created by cdkj on 2018/8/11.
 */

public class DbCoinInfo {


    public String address;//地址
    public String privateKey;//私钥

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}

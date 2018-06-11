package com.cdkj.token.model;

import org.litepal.crud.DataSupport;

/**
 * 钱包数据库
 * Created by cdkj on 2018/6/6.
 */
public class WalletDBModel extends DataSupport {

    private String privataeKey;//私钥

    private String helpWordsrEn;// 助记词 英文

    private String address;//地址

    private String coinType;//所属币类型

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getPrivataeKey() {
        return privataeKey;
    }

    public void setPrivataeKey(String privataeKey) {
        this.privataeKey = privataeKey;
    }

    public String getHelpWordsrEn() {
        return helpWordsrEn;
    }

    public void setHelpWordsrEn(String helpWordsrEn) {
        this.helpWordsrEn = helpWordsrEn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

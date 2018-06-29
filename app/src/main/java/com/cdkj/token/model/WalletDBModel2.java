package com.cdkj.token.model;

import org.litepal.crud.DataSupport;

/**
 * 钱包数据库
 * Created by cdkj on 2018/6/6.
 */
public class WalletDBModel2 extends DataSupport {


    private String userId;//私钥
    private String helpWordsrEn;// 助记词 英文
    private String walletPassWord;


    private String btcAddress;//地址
    private String btcPrivataeKey;//私钥

    private String ethAddress;//地址
    private String ethPrivataeKey;//私钥

    private String wanAddress;//地址
    private String wanPrivataeKey;//私钥


    public String getWalletPassWord() {
        return walletPassWord;
    }

    public void setWalletPassWord(String walletPassWord) {
        this.walletPassWord = walletPassWord;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHelpWordsrEn() {
        return helpWordsrEn;
    }

    public void setHelpWordsrEn(String helpWordsrEn) {
        this.helpWordsrEn = helpWordsrEn;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public String getBtcPrivataeKey() {
        return btcPrivataeKey;
    }

    public void setBtcPrivataeKey(String btcPrivataeKey) {
        this.btcPrivataeKey = btcPrivataeKey;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public String getEthPrivataeKey() {
        return ethPrivataeKey;
    }

    public void setEthPrivataeKey(String ethPrivataeKey) {
        this.ethPrivataeKey = ethPrivataeKey;
    }

    public String getWanAddress() {
        return wanAddress;
    }

    public void setWanAddress(String wanAddress) {
        this.wanAddress = wanAddress;
    }

    public String getWanPrivataeKey() {
        return wanPrivataeKey;
    }

    public void setWanPrivataeKey(String wanPrivataeKey) {
        this.wanPrivataeKey = wanPrivataeKey;
    }
}


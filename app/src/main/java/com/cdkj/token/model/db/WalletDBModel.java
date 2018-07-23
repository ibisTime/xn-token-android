package com.cdkj.token.model.db;

import org.litepal.crud.DataSupport;

/**
 * 钱包数据库 字段名  对应 WalletDBColumn类
 * Created by cdkj on 2018/6/6.
 */
public class WalletDBModel extends DataSupport {

    public String userId;
    public String walletName; //钱包名称
    public String helpWordsEn;// 助记词 英文
    public String walletPassWord;

    public String btcAddress;//地址
    public String btcPrivateKey;//私钥

    public String ethAddress;//地址
    public String ethPrivateKey;//私钥

    public String wanAddress;//地址
    public String wanPrivateKey;//私钥


    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHelpWordsEn() {
        return helpWordsEn;
    }

    public void setHelpWordsEn(String helpWordsEn) {
        this.helpWordsEn = helpWordsEn;
    }

    public String getWalletPassWord() {
        return walletPassWord;
    }

    public void setWalletPassWord(String walletPassWord) {
        this.walletPassWord = walletPassWord;
    }

    public String getBtcAddress() {
        return btcAddress;
    }

    public void setBtcAddress(String btcAddress) {
        this.btcAddress = btcAddress;
    }

    public String getBtcPrivateKey() {
        return btcPrivateKey;
    }

    public void setBtcPrivateKey(String btcPrivateKey) {
        this.btcPrivateKey = btcPrivateKey;
    }

    public String getEthAddress() {
        return ethAddress;
    }

    public void setEthAddress(String ethAddress) {
        this.ethAddress = ethAddress;
    }

    public String getEthPrivateKey() {
        return ethPrivateKey;
    }

    public void setEthPrivateKey(String ethPrivateKey) {
        this.ethPrivateKey = ethPrivateKey;
    }

    public String getWanAddress() {
        return wanAddress;
    }

    public void setWanAddress(String wanAddress) {
        this.wanAddress = wanAddress;
    }

    public String getWanPrivateKey() {
        return wanPrivateKey;
    }

    public void setWanPrivateKey(String wanPrivateKey) {
        this.wanPrivateKey = wanPrivateKey;
    }
}


package com.cdkj.token.model;

/**
 * 用于储存钱包资产界面所有数据
 * Created by cdkj on 2018/8/30.
 */

public class WalletFragmentAllData {

    private CoinModel walletData;  //中心化钱包

    private BalanceListModel privateWalletData;//去中心化钱包

    private String bulletinString="";//公告数据

    public CoinModel getWalletData() {
        return walletData;
    }

    public void setWalletData(CoinModel walletData) {
        this.walletData = walletData;
    }

    public BalanceListModel getPrivateWalletData() {
        return privateWalletData;
    }

    public void setPrivateWalletData(BalanceListModel privateWalletData) {
        this.privateWalletData = privateWalletData;
    }

    public String getBulletinString() {
        return bulletinString;
    }

    public void setBulletinString(String bulletinString) {
        this.bulletinString = bulletinString;
    }
}

package com.cdkj.token.model;

import org.litepal.annotation.Encrypt;
import org.litepal.crud.DataSupport;

/**
 * Created by cdkj on 2018/6/11.
 */

public class WalletInfoDBModel extends DataSupport {

    private String walletPassWord; //钱包密码

    public String getWalletPassWord() {
        return walletPassWord;
    }

    public void setWalletPassWord(String walletPassWord) {
        this.walletPassWord = walletPassWord;
    }
}

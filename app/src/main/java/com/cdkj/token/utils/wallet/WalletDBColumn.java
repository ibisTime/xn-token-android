package com.cdkj.token.utils.wallet;

/**
 * 钱包数据库 字段对应 WalletDBModel 类
 * Created by cdkj on 2018/6/30.
 */

public class WalletDBColumn {

    public static final String FINDUSER_SQL = "select * from WalletDBModel where " + WalletDBColumn.USERID + "=?";

    public static final String UPDATE_PWD_SQL = "update WalletDBModel set " + WalletDBColumn.WALLETPASSWORD + "=?  where " + WalletDBColumn.USERID + " = ?";

    public static final String USERID = "userid";

    public static final String WALLETPASSWORD = "walletpassword";

    public static final String HELPWORDSREN = "helpwordsren";

    public static final String BTCADDRESS = "btcaddress";

    public static final String BTCPRIVATEKEY = "btcprivatekey";

    public static final String ETHADDRESS = "ethaddress";

    public static final String ETHPRIVATEKEY = "ethprivatekey";

    public static final String WANADDRESS = "wanaddress";

    public static final String WANPRIVATEKEY = "wanprivatekey";


}

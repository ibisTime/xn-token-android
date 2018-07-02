package com.cdkj.token.utils.wallet;

/**
 * 钱包数据库 字段对应 WalletDBModel 类
 * Created by cdkj on 2018/6/30.
 */

public class WalletDBColumn {

    //查找用户钱包数据
    public static final String FINDUSER_SQL = "select * from walletdbmodel where " + WalletDBColumn.USERID + "=?";
    //查找用户选择币种
    public static final String FINDUSER_COIN_SQL = "select * from userconfigdbmodel where " + WalletDBColumn.USERID + "=?";
    //删除用户钱包
    public static final String FIND_USER_SQL = WalletDBColumn.USERID + "=?";

    public static final String USERID = "userid";

    public static final String WALLETPASSWORD = "walletpassword";

    public static final String HELPWORDSEN = "helpwordsen";

    public static final String BTCADDRESS = "btcaddress";

    public static final String BTCPRIVATEKEY = "btcprivatekey";

    public static final String ETHADDRESS = "ethaddress";

    public static final String ETHPRIVATEKEY = "ethprivatekey";

    public static final String WANADDRESS = "wanaddress";

    public static final String WANPRIVATEKEY = "wanprivatekey";

    public static final String CHOOSECOINIDS = "choosecoins";

    public static final String ISCHOOSED = "ischoosed";


}

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


    /**
     * 根据币种名获取某种币种的属性
     *
     * @param attributes
     * @return
     */
    public static String getLocalCoinAttributesSqlByCoinSymbol(String attributes) {
        return "select " + attributes + " from localcoindbmodel where " + WalletDBColumn.COIN_SYMBOL + "=?";
    }

    //删除用户钱包
    public static final String FIND_USER_SQL = WalletDBColumn.USERID + "=?";

    public static final String DELETE_LOCAL_COIN = "symbol=?";

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

    public static final String WALLET_NAME = "walletname";

    public static final String BTC_ADDRESS = "btcaddress";

    public static final String BTC_PRIVATEKEY = "btcprivatekey";

    public static final String COIN_SYMBOL = "symbol";

    public static final String COIN_UNIT = "unit";

    public static final String COIN_TYPE = "type";

    public static final String COIN_CONTRACTADDRESS = "contractaddress";


}

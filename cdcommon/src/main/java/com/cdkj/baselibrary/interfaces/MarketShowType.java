package com.cdkj.baselibrary.interfaces;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

/**
 * 根据用户选择的币种类型（美元，人民币 韩币）显示不同行情金额
 * Created by cdkj on 2018/8/30.
 */

public interface MarketShowType {


    String _getMarketStringUSD();//美元

    String _getMarketStringCNY();//人民币

    String _getMarketStringKRW();//韩元

    default String getMarketStringByLocalSymbol() {
        String localCoin = SPUtilHelper.getLocalCoinType();
        if (TextUtils.equals(localCoin, MyConfig.LOCAL_COIN_CNY)) {
            if (TextUtils.isEmpty(_getMarketStringUSD())) {
                return "0";
            }
            return _getMarketStringUSD();
        } else if (TextUtils.equals(localCoin, MyConfig.LOCAL_COIN_USD)) {
            if (TextUtils.isEmpty(_getMarketStringCNY())) {
                return "0";
            }
            return _getMarketStringCNY();

        } else if (TextUtils.equals(localCoin, MyConfig.LOCAL_COIN_KRW)) {
            if (TextUtils.isEmpty(_getMarketStringKRW())) {
                return "0";
            }
            return _getMarketStringKRW();
        }
        return "";
    }

}

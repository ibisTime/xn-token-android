package com.cdkj.baselibrary.interfaces;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

import java.math.BigDecimal;

/**
 *  根据用户选择的币种类型（美元，人民币 韩币）显示不同金额
 * Created by cdkj on 2018/8/30.
 */

public interface AmountShowType {

    String _getAmountStringUSD();//美元

    String _getAmountStringCNY();//人民币

    String _getAmountStringKRW();//韩币


    default String getAmountStringByLocalSymbol() {
        String localCoin = SPUtilHelper.getLocalCoinType();
        if (TextUtils.equals(localCoin, MyConfig.LOCAL_COIN_CNY)) {
            if (TextUtils.isEmpty(_getAmountStringUSD())) {
                return "0";
            }
            return _getAmountStringUSD();
        } else if (TextUtils.equals(localCoin, MyConfig.LOCAL_COIN_USD)) {
            if (TextUtils.isEmpty(_getAmountStringCNY())) {
                return "0";
            }

            return _getAmountStringCNY();
        } else if (TextUtils.equals(localCoin, MyConfig.LOCAL_COIN_KRW)) {
            if (TextUtils.isEmpty(_getAmountStringKRW())) {
                return "0";
            }
            return _getAmountStringKRW();
        }

        return "";
    }

}

package com.cdkj.baselibrary.interfaces;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.StringUtils;

/**
 * 根据用户选择的币种类型（美元，人民币 韩币）显示不同金额
 * Created by cdkj on 2018/8/30.
 */

public interface AmountShowTypeInterface {

    String _getAmountStringUSD();//美元

    String _getAmountStringCNY();//人民币

    String _getAmountStringKRW();//韩币


    default String getAmountStringByLocalMarket() {
        String localCoin = SPUtilHelper.getLocalMarketSymbol();

        if (TextUtils.isEmpty(localCoin)) {
            return "";
        }

        switch (localCoin) {
            case AppConfig.LOCAL_MARKET_CNY:
                return StringUtils.checkEmptyReturnZeor(_getAmountStringCNY());
            case AppConfig.LOCAL_MARKET_USD:
                return StringUtils.checkEmptyReturnZeor(_getAmountStringUSD());
            case AppConfig.LOCAL_MARKET_KRW:
                return StringUtils.checkEmptyReturnZeor(_getAmountStringKRW());

            default:
                return "";
        }
    }


}

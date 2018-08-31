package com.cdkj.baselibrary.interfaces;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

/**
 * 根据用户选择的币种类型（美元，人民币 韩币）显示不同行情金额
 * Created by cdkj on 2018/8/30.
 */

public interface MarketShowTypeInterface {

    String _getMarketStringUSD();//美元

    String _getMarketStringCNY();//人民币

    String _getMarketStringKRW();//韩元

    default String getMarketStringByLocalSymbol() {
        String localCoin = SPUtilHelper.getLocalMarketSymbol();

        if (TextUtils.isEmpty(localCoin)) {
            return "";
        }

        switch (localCoin) {
            case AppConfig.LOCAL_MARKET_CNY:
                if (TextUtils.isEmpty(_getMarketStringCNY())) {
                    return "0";
                }

                return _getMarketStringCNY();

            case AppConfig.LOCAL_MARKET_USD:
                if (TextUtils.isEmpty(_getMarketStringUSD())) {
                    return "0";
                }
                return _getMarketStringUSD();
            case AppConfig.LOCAL_MARKET_KRW:
                if (TextUtils.isEmpty(_getMarketStringKRW())) {
                    return "0";
                }
                return _getMarketStringKRW();

            default:
                return "";
        }

    }

}

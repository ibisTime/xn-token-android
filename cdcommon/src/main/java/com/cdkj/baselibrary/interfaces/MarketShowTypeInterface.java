package com.cdkj.baselibrary.interfaces;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.StringUtils;

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
                return StringUtils.checkEmptyReturnZeor(_getMarketStringCNY());
            case AppConfig.LOCAL_MARKET_USD:
                return StringUtils.checkEmptyReturnZeor(_getMarketStringUSD());
            case AppConfig.LOCAL_MARKET_KRW:
                return StringUtils.checkEmptyReturnZeor(_getMarketStringKRW());
            default:
                return "";
        }
    }

}

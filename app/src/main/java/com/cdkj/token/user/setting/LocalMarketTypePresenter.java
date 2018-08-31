package com.cdkj.token.user.setting;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;

/**
 * 本地行情选择
 * Created by cdkj on 2018/8/31.
 */

public class LocalMarketTypePresenter {


    interface LocalMarketTypeChangeCallBack {
        void onChangeSuccess();
    }

    /**
     * 改变行情币种
     *
     * @param chooseMarketType
     */
    public void changeMarketType(String chooseMarketType, LocalMarketTypeChangeCallBack localMarketTypeChangeCallBack) {
        if (canChangeMarketType(chooseMarketType)) {
            return;
        }
        SPUtilHelper.saveLocalMarketSymbol(chooseMarketType);
        if (localMarketTypeChangeCallBack != null) {
            localMarketTypeChangeCallBack.onChangeSuccess();
        }
    }

    /**
     * 根据用户选择行情类型判断是否可以改变行情
     * 用户选择的和本地的一致无法进行修改
     *
     * @param
     */
    private boolean canChangeMarketType(String chooseMarketType) {
        return TextUtils.equals(SPUtilHelper.getLocalMarketSymbol(), chooseMarketType);
    }

}

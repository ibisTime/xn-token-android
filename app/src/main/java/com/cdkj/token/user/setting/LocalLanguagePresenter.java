package com.cdkj.token.user.setting;

import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

/**
 * 本地语言选择
 * Created by cdkj on 2018/8/31.
 */

public class LocalLanguagePresenter {


    interface LocalLanguageChangeCallBack {
        void onChangeSuccess();
    }

    /**
     * 改变行情币种
     *
     * @param chooseLanguage
     */
    public void changeLanguage(String chooseLanguage, LocalLanguageChangeCallBack localLanguageChangeCallBack) {
        if (canChangeMarketType(chooseLanguage)) {
            return;
        }
        SPUtilHelper.saveLanguage(chooseLanguage);
        if (localLanguageChangeCallBack != null) {
            localLanguageChangeCallBack.onChangeSuccess();
        }
    }

    /**
     * 用户选择的语言和本地的一致无法进行修改
     *
     * @param
     */
    private boolean canChangeMarketType(String chooseLanguage) {
        return TextUtils.equals(SPUtilHelper.getLanguage(), chooseLanguage);
    }

}

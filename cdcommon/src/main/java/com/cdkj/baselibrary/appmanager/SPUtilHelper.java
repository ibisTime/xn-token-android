package com.cdkj.baselibrary.appmanager;


import android.content.Context;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.SPUtils;

/**
 * SPUtils 工具辅助类
 */

public class SPUtilHelper {

    public static final String USD = "USD";
    public static final String HKD = "HKD";

    private static final String USERTOKEN = "user_toke";
    private static final String USERID = "user_id";
    private static final String SECRET_USERID = "secret_user_id";
    private static final String LOCATIONINFO = "location_info";

    // app运行环境
    public static final String BUILD_TYPE_KEY = "build_type";
    public static final String BUILD_TYPE_TEST = "build_type_test";
    public static final String BUILD_TYPE_DEBUG = "build_type_debug";
    public static final String BUILD_TYPE_RELEASE = "build_type_release";

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean isLogin(Context context, boolean canopenmain) {
        if (TextUtils.isEmpty(getUserId())) {
            SPUtilHelper.logOutClear();
            // 路由跳转登录页面
            CdRouteHelper.openLogin(canopenmain);
            return false;
        }

        return true;
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public static boolean isLoginNoStart() {
        if (TextUtils.isEmpty(getUserId())) {
            return false;
        }
        return true;
    }


    /**
     * 退出登录清除数据
     */
    public static void logOutClear() {
        saveUserId("");
        saveUserToken("");

        saveRealName("");
        saveUserName("");
        saveUserPhoto("");
        saveUserEmail("");
        saveUserPhoneNum("");
        saveTradePwdFlag(false);
        saveGoogleAuthFlag(false);
    }


    /**
     * 设置APP运行环境
     *
     * @param s
     */
    public static void setAPPBuildType(String s) {
        SPUtils.put(CdApplication.getContext(), BUILD_TYPE_KEY, s);
    }

    /**
     * 获取APP运行环境，默认DEBUG
     *
     * @param
     */
    public static String getAPPBuildType() {
        return SPUtils.getString(CdApplication.getContext(), BUILD_TYPE_KEY, BUILD_TYPE_DEBUG);
    }

    /**
     * 设置语言
     *
     * @param s
     */
    public static void saveLanguage(String s) {
        SPUtils.put(CdApplication.getContext(), "language", s);
    }

    /**
     * 获取语言
     */
    public static String getLanguage() {
        return SPUtils.getString(CdApplication.getContext(), "language", "");
    }

    /**
     * 设置用户token
     *
     * @param s
     */
    public static void saveUserToken(String s) {
        SPUtils.put(CdApplication.getContext(), USERTOKEN, s);
    }

    /**
     * 设置用户token
     *
     * @param
     */
    public static String getUserToken() {
        return SPUtils.getString(CdApplication.getContext(), USERTOKEN, "");
    }


    /**
     * 设置用户UserId
     *
     * @param s
     */
    public static void saveUserId(String s) {
        SPUtils.put(CdApplication.getContext(), USERID, s);
    }

    /**
     * 设置用户UserId
     *
     * @param
     */
    public static String getUserId() {
        return SPUtils.getString(CdApplication.getContext(), USERID, "");

    }

    /**
     * 设置用户UserId
     *
     * @param s
     */
    public static void saveSecretUserId(String s) {
        SPUtils.put(CdApplication.getContext(), SECRET_USERID, s);
    }

    /**
     * 设置用户UserId
     *
     * @param
     */
    public static String getSecretUserId() {
        return SPUtils.getString(CdApplication.getContext(), SECRET_USERID, "");

    }


    /**
     * 设置用户手机号码
     *
     * @param s
     */
    public static void saveUserPhoneNum(String s) {
        SPUtils.put(CdApplication.getContext(), "user_phone", s);
    }

    /**
     * 获取用户手机号
     */
    public static String getUserPhoneNum() {
        return SPUtils.getString(CdApplication.getContext(), "user_phone", "");
    }

    /**
     * 设置七牛url
     *
     * @param s
     */
    public static void saveQiniuUrl(String s) {
        if (TextUtils.isEmpty(s)) {
            s = MyConfig.IMGURL;
        } else {
            s = "http://" + s + "/";
        }
        SPUtils.put(CdApplication.getContext(), "qiniu_url", s);
    }

    /**
     * 获取七牛url
     */
    public static String getQiniuUrl() {

        String url = SPUtils.getString(CdApplication.getContext(), "qiniu_url", MyConfig.IMGURL);

        if (TextUtils.isEmpty(url)) {
            return MyConfig.IMGURL;
        }

        return url;
    }

    /**
     * 设置用户昵称
     *
     * @param s
     */
    public static void saveUserName(String s) {
        SPUtils.put(CdApplication.getContext(), "user_name", s);
    }

    /**
     * 获取用户昵称
     */
    public static String getUserName() {
        return SPUtils.getString(CdApplication.getContext(), "user_name", "");
    }

    /**
     * 设置用户真实姓名
     *
     * @param s
     */
    public static void saveRealName(String s) {
        SPUtils.put(CdApplication.getContext(), "real_name", s);
    }

    /**
     * 获取用户真实姓名
     */
    public static String getRealName() {
        return SPUtils.getString(CdApplication.getContext(), "real_name", "");
    }

    /**
     * 设置用户资金密码Flag
     *
     * @param s
     */
    public static void saveTradePwdFlag(boolean s) {
        SPUtils.put(CdApplication.getContext(), "trade_pwd", s);
    }

    /**
     * 获取用户资金密码Flag
     */
    public static boolean getTradePwdFlag() {
        return SPUtils.getBoolean(CdApplication.getContext(), "trade_pwd", false);
    }

    /**
     * 设置用户谷歌验证flag
     *
     * @param s
     */
    public static void saveGoogleAuthFlag(boolean s) {
        SPUtils.put(CdApplication.getContext(), "google_flag", s);
    }

    /**
     * 获取用户谷歌验证flag
     */
    public static boolean getGoogleAuthFlag() {
        return SPUtils.getBoolean(CdApplication.getContext(), "google_flag", false);
    }


    /**
     * 设置用户昵称
     *
     * @param s
     */
    public static void saveUserPhoto(String s) {
        SPUtils.put(CdApplication.getContext(), "user_photo", s);
    }

    /**
     * 获取用户昵称
     */
    public static String getUserPhoto() {
        return SPUtils.getString(CdApplication.getContext(), "user_photo", "");
    }


    /**
     * 设置用户昵称
     *
     * @param s
     */
    public static void saveUserEmail(String s) {
        SPUtils.put(CdApplication.getContext(), "user_email", s);
    }

    /**
     * 获取用户昵称
     */
    public static String getUserEmail() {
        return SPUtils.getString(CdApplication.getContext(), "user_email", "");
    }


    /**
     * 设置汇率
     *
     * @param s
     */
    public static void saveRate(String c, String s) {
        SPUtils.put(CdApplication.getContext(), "rate_" + c, s);
    }

    /**
     * 获取汇率
     */
    public static String getRate(String c) {
        return SPUtils.getString(CdApplication.getContext(), "rate_" + c, "");
    }

    /**
     * 设置行情Coin情况
     *
     * @param coin
     */
    public static void saveMarketCoinTemp(String coin, String lastPrice, String temp) {
        SPUtils.put(CdApplication.getContext(), "coin_temp_" + coin, lastPrice + "_" + temp);
    }

    public static void saveMarketCoinDate(String coin, String lastPrice, String date) {
        SPUtils.put(CdApplication.getContext(), "coin_date_" + coin, lastPrice + "_" + date);
    }

    /**
     * 设置行情Coin情况
     *
     * @param coin
     */
    public static void saveMarketCoin(String coin, Double lastPrice) {
        SPUtils.put(CdApplication.getContext(), "coin_market_" + coin, lastPrice);
    }

    public static String getMarketCoin(String coin) {
        return SPUtils.getString(CdApplication.getContext(), "coin_market_" + coin, "");
    }

    /**
     * 设置需要打开的订单
     *
     * @param orderId
     */
    public static void savePushOrder(String orderId) {
        SPUtils.put(CdApplication.getContext(), "push_order", orderId);
    }

    public static String getPushOrder() {
        return SPUtils.getString(CdApplication.getContext(), "push_order", "");
    }

    public static void clearPushOrder() {
        SPUtils.put(CdApplication.getContext(), "push_order", "");
    }

}

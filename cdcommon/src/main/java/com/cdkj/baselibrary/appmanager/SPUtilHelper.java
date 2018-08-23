package com.cdkj.baselibrary.appmanager;


import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.SPUtils;

import static com.cdkj.baselibrary.appmanager.MyConfig.BASE_URL_TEST;
import static com.cdkj.baselibrary.appmanager.MyConfig.BUILD_TYPE_TEST;

/**
 * SPUtils 工具辅助类
 */

public class SPUtilHelper {


    private static final String USERTOKEN = "user_toke";
    private static final String USERID = "user_id";
    private static final String SECRET_USERID = "secret_user_id";
    public static final String BUILD_TYPE_KEY = "build_type";

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
     * 获取APP运行环境，默认测试
     *
     * @param
     */
    public static String getAPPBuildType() {
        return SPUtils.getString(CdApplication.getContext(), BUILD_TYPE_KEY, BUILD_TYPE_TEST);
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
     * 获取语言 默认英语
     */
    public static String getLanguage() {
        return SPUtils.getString(CdApplication.getContext(), "language", MyConfig.ENGLISH);
    }


    /**
     * 设置本地货币类型（CNY USD）
     *
     * @param s
     */
    public static void saveLocalCoinType(String s) {
        SPUtils.put(CdApplication.getContext(), "local_coin_type", s);
    }

    /**
     * 设置本地货币类型（CNY USD）
     */
    public static String getLocalCoinType() {
        return SPUtils.getString(CdApplication.getContext(), "local_coin_type", "CNY");
    }


    /**
     * 设置用户选择的国家
     *
     * @param s
     */

    public static void saveCountryInterCode(String s) {
        SPUtils.put(CdApplication.getContext(), "country_code", s);
    }

    /**
     * 获取国家编号
     */
    public static String getCountryInterCode() {
        return SPUtils.getString(CdApplication.getContext(), "country_code", "0086");
    }

    /**
     * 设置用户选择的国家旗
     *
     * @param s
     */

    public static void saveCountryFlag(String s) {
        SPUtils.put(CdApplication.getContext(), "country_flag", s);
    }

    /**
     * 获取国家编号
     */
    public static String getCountryCode() {
        return SPUtils.getString(CdApplication.getContext(), "country_code_2", "GJ201808130191");
    }

    /**
     * 设置用户选择的国家编号
     *
     * @param s
     */

    public static void saveCountryCode(String s) {
        SPUtils.put(CdApplication.getContext(), "country_code_2", s);
    }


    /**
     * 获取国家编号
     */
    public static String getCountryFlag() {
        return SPUtils.getString(CdApplication.getContext(), "country_flag", "China.png");
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
     * 设置用户登录密码Flag
     *
     * @param s
     */
    public static void saveLoginPwdFlag(boolean s) {
        SPUtils.put(CdApplication.getContext(), "LoginPwdFlag", s);
    }

    /**
     * 获取用户登录密码Flag
     */
    public static boolean getLoginPwdFlag() {
        return SPUtils.getBoolean(CdApplication.getContext(), "LoginPwdFlag", false);
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
     * 设置用户手势密码
     *
     * @param s
     */
    public static void saveUserPatternPwd(String s) {
        SPUtils.put(CdApplication.getContext(), "pattern_key", s);
    }

    /**
     * 获取设置用户手势密码
     */
    public static String getUserPatternPwd() {
        return SPUtils.getString(CdApplication.getContext(), "pattern_key", "");
    }

    /**
     * 创建钱包时的钱包信息暂时备份，备份之后会被清除（用户只有在备份之后这个钱包信息才被认可）
     *
     * @param s
     */
    public static void createWalletCache(String s) {
        SPUtils.put(CdApplication.getContext(), "create_wallet_cache", s);
    }

    /**
     * 获取创建钱包时的钱包信息暂时备份
     */
    public static String getWalletCache() {
        return SPUtils.getString(CdApplication.getContext(), "create_wallet_cache", "");
    }

    /**
     * 钱包界面资产显示还是隐藏
     *
     * @param isShow
     */
    public static void saveIsAssetsShow(boolean isShow) {
        SPUtils.put(CdApplication.getContext(), "is_assets_show", isShow);
    }

    /**
     * 钱包界面资产显示还是隐藏
     */
    public static boolean isAssetsShow() {
        return SPUtils.getBoolean(CdApplication.getContext(), "is_assets_show", true);
    }

    /**
     * 是否设置过用户手势密码
     */
    public static boolean isSetPatternPwd() {
        return !TextUtils.isEmpty(getUserPatternPwd());
    }

}

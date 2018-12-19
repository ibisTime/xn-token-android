package com.cdkj.baselibrary.appmanager;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 路由管理
 * Created by cdkj on 2017/10/12.
 */

public class CdRouteHelper {
    //跳转到登录页面
    public static final String APPLOGIN = "/app/login";
    public static final String APPMAIN = "/app/main";

    public static final String APP_COUNTRY_SELECT = "/app/country_select";
    //启动页
    public static final String APPSTART = "/app/start";
    //找回登录密码
    public static final String FINDPWD = "/commen/findpwd";
    //修改电话号码
    public static final String UPDATEPHONE = "/commen/UPDATEPHONE";

    //获取数据标志
    public static final String DATASIGN = "dataSign";
    public static final String DATASIGN2 = "dataSign2";
    public static final String DATASIGN3 = "dataSign3";
    public static final String DATASIGN4 = "dataSign4";


    /**
     * 打开登录界面
     *
     * @param canopenmain 打开后是否跳转到主页
     */
    public static void openLogin(boolean canopenmain) {
        ARouter.getInstance().build(APPLOGIN)
                .withBoolean(DATASIGN, canopenmain)
                .greenChannel()                                       //不使用任何拦截器
                .navigation();
    }

    /**
     * 打开主页
     */
    public static void openMain() {
        ARouter.getInstance().build(APPMAIN)
                .greenChannel()                                       //不使用任何拦截器
                .navigation();
    }

    /**
     * 打开登录界面
     *
     * @param isSave 选择后是否保存到本地
     */
    public static void openCountrySelect(boolean isSave) {
        ARouter.getInstance().build(APP_COUNTRY_SELECT)
                .withBoolean(DATASIGN, isSave)
                .greenChannel()                                       //不使用任何拦截器
                .navigation();
    }

    /**
     * 打开找回登录密码界面
     *
     * @param phoneNum 用户手机号码
     */
    public static void openFindPwdActivity(String phoneNum) {
        ARouter.getInstance().build(FINDPWD)
                .withString(DATASIGN, phoneNum)
                .navigation();
    }

    /**
     * 路由跳转开始页面
     */
    public static void openStar() {
        // 路由跳转开始页面
        ARouter.getInstance().build(APPSTART)
                .navigation();
    }

    /**
     * 修改用户手机号
     */
    public static void openUpdatePhoneActivity() {
        ARouter.getInstance().build(UPDATEPHONE)
                .navigation();
    }


}

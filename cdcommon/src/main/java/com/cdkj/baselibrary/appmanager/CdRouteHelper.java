package com.cdkj.baselibrary.appmanager;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * 路由管理
 * Created by cdkj on 2017/10/12.
 */

public class CdRouteHelper {
    //跳转到登录页面
    public static final String APPLOGIN = "/app/login";
    //启动页
    public static final String APPSTART = "/app/start";
    //找回登录密码
    public static final String FINDPWD = "/commen/findpwd";
    //修改电话号码
    public static final String PAYPWDMODIFY = "/commen/PayPwdModify";
    public static final String UPDATEBANKCARD = "/commen/UPDATEBANKCARD";
    public static final String UPDATEPHONE = "/commen/UPDATEPHONE";
    public static final String WEBVIEWACTIVITY = "/commen/webView";

    //获取数据标志
    public static final String DATASIGN = "dataSign";


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
     *
     */
    public static void openStar() {
        // 路由跳转开始页面
        ARouter.getInstance().build("/user/start")
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

package com.cdkj.baselibrary.appmanager;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.core.Zendesk;
import zendesk.support.Support;
import zendesk.support.UiConfig;
import zendesk.support.guide.HelpCenterActivity;

/**
 * 集成第三方管理
 * Created by cdlk on 2018/8/23.
 */

public class OtherLibManager {


    /**
     * 初始化zendesk
     *
     * @param context
     */
    public static void initZengDesk(Context context) {
        Zendesk.INSTANCE.init(context, AppConfig.getZenDeskUrl(),
                "71d2ca9aba0cccc12deebfbdd352fbae8c53cd8999dd10bc",
                "mobile_sdk_client_7af3526c83d0c1999bc3");

        Identity identity = new AnonymousIdentity();
        Zendesk.INSTANCE.setIdentity(identity);
        Support.INSTANCE.init(Zendesk.INSTANCE);

    }

    /**
     * 打开帮助中心
     */
    public static void openZengDeskHelpCenter(Activity activity) {

        UiConfig helpCenterConfig = HelpCenterActivity.builder().withContactUsButtonVisible(false).config();

        HelpCenterActivity.builder()
                .show(activity,helpCenterConfig);
    }


    /**
     * 友盟状态参数设置
     *
     * @param context
     */
    public static void initUmen(Context context, String umengKey) {
        if (context == null) {
            return;
        }
        MobclickAgent.setCatchUncaughtExceptions(false);  //关闭友盟错误统计
        //友盟
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, umengKey);
        UMConfigure.setLogEnabled(LogUtil.isLog); //是否使用集成测试
        UMConfigure.setEncryptEnabled(true);
        //友盟统计场景设置
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);//EScenarioType. E_UM_NORMAL　　普通统计场景类型
    }

    /**
     * 友盟账号统计
     *
     * @param userid
     */
    public static void uemProfileSignIn(String userid) {
        if (TextUtils.isEmpty(userid)) {
            return;
        }
        MobclickAgent.onProfileSignIn(userid);
    }

    /**
     * 友盟账号退出
     */
    public static void uemProfileSignOff() {
        MobclickAgent.onProfileSignOff();
    }


    /**
     * 友盟账号统计
     *
     * @param activity
     */
    public static void uemonResume(Activity activity) {
        MobclickAgent.onResume(activity);
    }

    /**
     * 友盟账号统计
     *
     * @param activity
     */
    public static void uemonPause(Activity activity) {
        MobclickAgent.onPause(activity);
    }

    /**
     * 友盟自定义计数事件
     *
     * @param context
     * @param EventId
     */

    public void uemOnEvent(Context context, String EventId) {
        if (context == null || TextUtils.isEmpty(EventId)) {
            return;
        }
        MobclickAgent.onEvent(context, EventId);
    }

    /**
     * 友盟自定义统计事件
     */
    public void uemOnEventValue(Context context, String eventId, int price) {

        if (context == null || TextUtils.isEmpty(eventId)) {
            return;
        }

        MobclickAgent.onEventValue(context, eventId, null, price);  //本次订单金额
    }


}

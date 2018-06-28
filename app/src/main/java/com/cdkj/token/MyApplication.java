package com.cdkj.token;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.utils.LogUtil;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

/**
 * Created by lei on 2017/10/20.
 */

public class MyApplication extends Application {

    private final int pushId = 1;

    public static Application application;


    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
        LogUtil.isLog = BuildConfig.IS_DEBUG;
        CdApplication.initialize(this);

        EventBus.builder().throwSubscriberException(LogUtil.isLog).installDefaultEventBus();



        if ( LogUtil.isLog) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化

        initLitePal();
        initZXing();


    }

    private void initLitePal() {
        LitePal.initialize(this);
        LitePal.aesKey("tha_wallet");
    }

    private void initZXing() {
        ZXingLibrary.initDisplayOpinion(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getInstance() {
        return application;
    }

}

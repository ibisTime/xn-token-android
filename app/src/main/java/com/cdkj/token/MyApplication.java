package com.cdkj.token;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.common.AppFrontBackHelper;
import com.cdkj.token.model.PatternLockCheckFinish;
import com.cdkj.token.user.pattern_lock.PatternLockCheckActivity;
import com.cdkj.token.utils.BGAGlideImageLoader2;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zendesk.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import cn.bingoogolapple.photopicker.imageloader.BGAImage;

/**
 * Created by lei on 2017/10/20.
 */
public class MyApplication extends Application {

    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LogUtil.isLog = BuildConfig.IS_DEBUG;

        init();

        AppFrontBackHelper helper = new AppFrontBackHelper();

        helper.register(MyApplication.application, new AppFrontBackHelper.OnAppStatusListener() {

            @Override
            public void onFront() {
                if (SPUtilHelper.isLoginNoStart() && SPUtilHelper.isSetPatternPwd()) {  //已经登录了 并且已经设置过手势密码
                    EventBus.getDefault().post(new PatternLockCheckFinish());
                    PatternLockCheckActivity.open(MyApplication.application);
                }
            }

            @Override
            public void onBack() {

            }
        });

    }


    void init() {
        initLitePal();
        CdApplication.initialize(this);
        if (LogUtil.isLog) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); //
        initZXing();
        EventBus.builder().throwSubscriberException(LogUtil.isLog).installDefaultEventBus();

        BGAImage.setImageLoader(new BGAGlideImageLoader2());

        OtherLibManager.initUmeng(this, BuildConfig.umeng);

        Logger.setLoggable(true);
        OtherLibManager.initZendesk(this);
    }

    private void initLitePal() {
        LitePal.initialize(this);
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

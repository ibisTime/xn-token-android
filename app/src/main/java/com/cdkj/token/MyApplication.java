package com.cdkj.token;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.token.common.AppFrontBackHelper;
import com.cdkj.token.model.PatternLockCheckFinish;
import com.cdkj.token.user.pattern_lock.PatternLockCheckActivity;
import com.cdkj.token.utils.BGAGlideImageLoader2;
import com.hjq.toast.ToastUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zendesk.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.Locale;

import cn.bingoogolapple.photopicker.imageloader.BGAImage;

import static com.cdkj.baselibrary.appmanager.AppConfig.ENGLISH;
import static com.cdkj.baselibrary.appmanager.AppConfig.KOREA;
import static com.cdkj.baselibrary.appmanager.AppConfig.SIMPLIFIED;
import static com.cdkj.baselibrary.appmanager.AppConfig.getUserLanguageLocal;

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

        if (SPUtilHelper.isFirstOpen()) {
            //如果是第一次进入就根据  手机当前系统语言设置对应的app语言  如果不是第一次那么就不用设置
            initLanguage();
//            SPUtilHelper.saveFirstOpen();
        }
        OtherLibManager.initZendesk(this);

        ToastUtils.init(this);

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

    private void initLanguage() {
        Locale locale = SystemUtils.getSystemLanguage(this);
        String country = locale.toString();
        String language = locale.getLanguage();
        LogUtil.E("初始化语言,当前语言为:" + language + "_" + country);
//        ToastUtil.show(this, "当前语言为:" + language + "_" + country);
        if (ENGLISH.equalsIgnoreCase(language)) {
            SPUtilHelper.saveLanguage(ENGLISH);
        } else if (KOREA.equalsIgnoreCase(language)) {
            SPUtilHelper.saveLanguage(KOREA);
        } else if ("ZH".equalsIgnoreCase(language)) {
            //仅仅根据 getLanguage() 无法全面的了解当前的系统语言信息，比如简体中文和繁体中文的 Language 都是 zh，所以还需要 getCountry() 方法获取地区信息，我们就能得到 zh-CN 和 zh-HK/zh-TW
            SPUtilHelper.saveLanguage(SIMPLIFIED);
        } else {
            //如果不是这三种语言  默认就是  英语
            SPUtilHelper.saveLanguage(ENGLISH);
        }
        AppUtils.setAppLanguage(this, getUserLanguageLocal());   //设置语言
    }

}

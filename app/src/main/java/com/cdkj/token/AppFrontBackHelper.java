package com.cdkj.token;

/**
 * Created by cdkj on 2018/7/26.
 */


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.cdkj.token.user.login.StartActivity;

/**
 * 应用前后台状态监听帮助类，仅在Application中使用
 * <p>
 * Created by dway on 2018/1/30.
 */


public class AppFrontBackHelper {


    private OnAppStatusListener mOnAppStatusListener;


    public AppFrontBackHelper() {


    }

    /**
     * 注册状态监听，仅在Application中使用
     *
     * @param application
     * @param listener
     */

    public void register(Application application, OnAppStatusListener listener) {
        mOnAppStatusListener = listener;
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    public void unRegister(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }


    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        //打开的Activity数量统计

        private int activityStartCount = 0;


        @Override

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {


        }


        @Override

        public void onActivityStarted(Activity activity) {
            if (activity instanceof StartActivity) {  //开始启动页面不用执行
                return;
            }

            activityStartCount++;
            //数值从0变到1说明是从后台切到前台
            if (activityStartCount == 1) {
                //从后台切到前台
                if (mOnAppStatusListener != null) {
                    mOnAppStatusListener.onFront();
                }
            }
        }


        @Override

        public void onActivityResumed(Activity activity) {

        }


        @Override

        public void onActivityPaused(Activity activity) {

        }

        @Override

        public void onActivityStopped(Activity activity) {

            if (activity instanceof StartActivity) {
                return;
            }

            activityStartCount--;

            //数值从1到0说明是从前台切到后台

            if (activityStartCount == 0) {
                //从前台切到后台
                if (mOnAppStatusListener != null) {
                    mOnAppStatusListener.onBack();
                }

            }

        }


        @Override

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }


        @Override

        public void onActivityDestroyed(Activity activity) {


        }

    };


    public interface OnAppStatusListener {

        void onFront();

        void onBack();

    }


}

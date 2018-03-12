package com.cdkj.baselibrary.nets;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 用于处理服务器 错误码
 * Created by Administrator on 2016-09-06.
 */
public class OnOkFailure {

    public static void StartDoFailure(Context context,String errorMessage) {
        SPUtilHelper.logOutClear();
        EventBus.getDefault().post(EventTags.AllFINISH);

        ToastUtil.show(context,errorMessage);
        // 路由跳转登录页面
        ARouter.getInstance().build("/user/login")
                .withBoolean("canOpenMain",false)
                .navigation();
    }

}

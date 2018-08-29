package com.cdkj.token.user.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserLanguageBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import zendesk.core.AnonymousIdentity;
import zendesk.core.Identity;
import zendesk.core.Zendesk;
import zendesk.support.Support;
import zendesk.support.UiConfig;
import zendesk.support.guide.HelpCenterActivity;

import static com.cdkj.baselibrary.appmanager.MyConfig.ENGLISH;
import static com.cdkj.baselibrary.appmanager.MyConfig.KOREA;
import static com.cdkj.baselibrary.appmanager.MyConfig.SIMPLIFIED;
import static com.cdkj.baselibrary.appmanager.MyConfig.getUserLanguageLocal;

/**
 * 语言设置
 * Created by lei on 2017/12/7.
 */

public class UserLanguageActivity extends AbsStatusBarTranslucentActivity {

    private ActivityUserLanguageBinding mBinding;
    private String url;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserLanguageActivity.class));
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_language, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(getStrRes(R.string.user_title_language));
        setPageBgImage(R.drawable.my_bg);
        setView(SPUtilHelper.getLanguage());
        initListener();
        url = "https://hzcl.zendesk.com";
    }

    private void initListener() {
        //简体中文
        mBinding.llSimple.setOnClickListener(view -> {
            if (TextUtils.equals(SPUtilHelper.getLanguage(), MyConfig.SIMPLIFIED)) {
                return;
            }
            url = "https://hzcl.zendesk.com";
            initView();
            mBinding.ivSimple.setVisibility(View.VISIBLE);
            sendEventBusAndFinishAll(SIMPLIFIED);

        });

        mBinding.llEnglish.setOnClickListener(view -> {
            if (TextUtils.equals(SPUtilHelper.getLanguage(), MyConfig.ENGLISH)) {
                return;
            }
            url = "https://hzcl.zendesk.com/hc/en-us/";
            initView();
            mBinding.ivEnglish.setVisibility(View.VISIBLE);
            sendEventBusAndFinishAll(ENGLISH);
        });

        mBinding.llKorea.setOnClickListener(v -> {
            if (TextUtils.equals(SPUtilHelper.getLanguage(), MyConfig.KOREA)) {
                return;
            }
            url = "https://hzcl.zendesk.com/hc/ko/";
            initView();
            mBinding.ivKorea.setVisibility(View.VISIBLE);
            sendEventBusAndFinishAll(KOREA);
        });

        mBinding.linLayoutHelpCenter.setOnClickListener(view -> {


            Zendesk.INSTANCE.init(this, url,
                    "1abb5d09d1ae5884d0f88f76a4368847ee01bffed4f92181",
                    "mobile_sdk_client_6e8e6247d8e39ba2b3d6");

            Identity identity = new AnonymousIdentity();
            Zendesk.INSTANCE.setIdentity(identity);
            Support.INSTANCE.init(Zendesk.INSTANCE);

            HelpCenterActivity.builder()
                    .show(this);
        });

    }

    private void sendEventBusAndFinishAll(String language) {
        SPUtilHelper.saveLanguage(language);
        AppUtils.setAppLanguage(this, getUserLanguageLocal());   //设置语言
//        EventBus.getDefault().post(new AllFinishEvent());
//        MainActivity.open(this);
//        finish();
    }

    private void initView() {
        mBinding.ivSimple.setVisibility(View.GONE);
        mBinding.ivEnglish.setVisibility(View.GONE);
        mBinding.ivKorea.setVisibility(View.GONE);
    }

    private void setView(String language) {
        initView();
        switch (language) {
            case SIMPLIFIED:
                mBinding.ivSimple.setVisibility(View.VISIBLE);
                break;
            case ENGLISH:
                mBinding.ivEnglish.setVisibility(View.VISIBLE);
                break;
            case KOREA:
                mBinding.ivKorea.setVisibility(View.VISIBLE);
                break;

            //以下兼容1.6.0之前 勿改动
            case "simplified":
                mBinding.ivSimple.setVisibility(View.VISIBLE);
                break;
            case "english":
                mBinding.ivEnglish.setVisibility(View.VISIBLE);
                break;
            case "korea":
                mBinding.ivKorea.setVisibility(View.VISIBLE);
                break;

        }
    }

}

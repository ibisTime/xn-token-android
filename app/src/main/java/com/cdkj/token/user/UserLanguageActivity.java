package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserLanguageBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import static com.cdkj.baselibrary.appmanager.MyConfig.ENGLISH;
import static com.cdkj.baselibrary.appmanager.MyConfig.KOREA;
import static com.cdkj.baselibrary.appmanager.MyConfig.SIMPLIFIED;
import static com.cdkj.baselibrary.appmanager.MyConfig.getUserLanguageLocal;

/**
 * Created by lei on 2017/12/7.
 */

public class UserLanguageActivity extends AbsBaseActivity {

    private ActivityUserLanguageBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserLanguageActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_language, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_language));
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();

    }

    private void init() {
        setView(SPUtilHelper.getLanguage());
    }

    private void initListener() {
        //简体中午
        mBinding.llSimple.setOnClickListener(view -> {
            initView();
            mBinding.ivSimple.setBackgroundResource(R.mipmap.choice_confirm);
            sendEventBusAndFinishAll(SIMPLIFIED);
        });

        mBinding.llEnglish.setOnClickListener(view -> {
            initView();
            mBinding.ivEnglish.setBackgroundResource(R.mipmap.choice_confirm);
            sendEventBusAndFinishAll(ENGLISH);
        });

        mBinding.llKorea.setOnClickListener(v -> {
            initView();
            mBinding.ivKorea.setBackgroundResource(R.mipmap.choice_confirm);
            sendEventBusAndFinishAll(KOREA);
        });

    }

    private void sendEventBusAndFinishAll(String language) {
        SPUtilHelper.saveLanguage(language);
        EventBus.getDefault().post(new AllFinishEvent());
        AppUtils.setAppLanguage(this, getUserLanguageLocal());   //设置语言
        MainActivity.open(this);
        finish();
    }

    private void initView() {
        mBinding.ivSimple.setBackgroundResource(R.mipmap.choice_cancel);
        mBinding.ivEnglish.setBackgroundResource(R.mipmap.choice_cancel);
        mBinding.ivTradition.setBackgroundResource(R.mipmap.choice_cancel);
        mBinding.ivKorea.setBackgroundResource(R.mipmap.choice_cancel);
    }

    private void setView(String language) {
        initView();
        switch (language) {
            case SIMPLIFIED:
                mBinding.ivSimple.setBackgroundResource(R.mipmap.choice_confirm);
                break;
            case ENGLISH:
                mBinding.ivEnglish.setBackgroundResource(R.mipmap.choice_confirm);
                break;
            case KOREA:
                mBinding.ivKorea.setBackgroundResource(R.mipmap.choice_confirm);
                break;
        }
    }

}

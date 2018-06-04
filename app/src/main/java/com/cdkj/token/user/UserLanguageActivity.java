package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserLanguageBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import static com.cdkj.baselibrary.appmanager.EventTags.EVENT_REFRESH_LANGUAGE;

/**
 * Created by lei on 2017/12/7.
 */

public class UserLanguageActivity extends AbsBaseActivity {

    private ActivityUserLanguageBinding mBinding;

    public static void open(Context context){
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

    private void init(){
        Locale locale = getResources().getConfiguration().locale;

        setView(locale.getLanguage(), locale.getCountry());
    }

    private void initListener() {
        mBinding.llSimple.setOnClickListener(view -> {
            sendEventBus(SIMPLIFIED);

            initView();
            mBinding.ivSimple.setBackgroundResource(R.mipmap.choice_confirm);
        });

        mBinding.llEnglish.setOnClickListener(view -> {
            sendEventBus(ENGLISH);

            initView();
            mBinding.ivEnglish.setBackgroundResource(R.mipmap.choice_confirm);
        });

        mBinding.llTradition.setOnClickListener(view -> {
            sendEventBus(TRADITIONAL);

            initView();
            mBinding.ivTradition.setBackgroundResource(R.mipmap.choice_confirm);
        });
    }

    private void sendEventBus(String language){
        SPUtilHelper.saveLanguage(language);

        EventBusModel model = new EventBusModel();
        model.setTag(EVENT_REFRESH_LANGUAGE);
        EventBus.getDefault().post(model);
    }

    private void initView(){
        mBinding.ivSimple.setBackgroundResource(R.mipmap.choice_cancel);
        mBinding.ivEnglish.setBackgroundResource(R.mipmap.choice_cancel);
        mBinding.ivTradition.setBackgroundResource(R.mipmap.choice_cancel);
    }

    private void setView(String language, String country){
        initView();
        switch (language){

            case "zh":
                if (country.equals("CN")){ // 简体
                    mBinding.ivSimple.setBackgroundResource(R.mipmap.choice_confirm);
                }else {
                    mBinding.ivTradition.setBackgroundResource(R.mipmap.choice_confirm);
                }
                break;

            case "en":
                mBinding.ivEnglish.setBackgroundResource(R.mipmap.choice_confirm);
                break;

        }
    }

}

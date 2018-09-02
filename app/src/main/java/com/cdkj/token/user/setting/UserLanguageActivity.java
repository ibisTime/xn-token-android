package com.cdkj.token.user.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserLanguageBinding;

import org.greenrobot.eventbus.EventBus;

import static com.cdkj.baselibrary.appmanager.AppConfig.ENGLISH;
import static com.cdkj.baselibrary.appmanager.AppConfig.KOREA;
import static com.cdkj.baselibrary.appmanager.AppConfig.SIMPLIFIED;
import static com.cdkj.baselibrary.appmanager.AppConfig.getUserLanguageLocal;


/**
 * 语言设置
 * Created by lei on 2017/12/7.
 */

public class UserLanguageActivity extends AbsStatusBarTranslucentActivity {

    private ActivityUserLanguageBinding mBinding;

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
        initChangeListener();


    }

    private void initChangeListener() {

        LocalLanguagePresenter localLanguagePresenter = new LocalLanguagePresenter();

        //简体中文
        mBinding.llSimple.setOnClickListener(view -> {

            localLanguagePresenter.changeLanguage(AppConfig.SIMPLIFIED, () -> {
                initView();
                mBinding.ivSimple.setVisibility(View.VISIBLE);
                setLanguageAndRestartMain();
            });
        });

        mBinding.llEnglish.setOnClickListener(view -> {

            localLanguagePresenter.changeLanguage(AppConfig.ENGLISH, () -> {
                initView();
                mBinding.ivEnglish.setVisibility(View.VISIBLE);
                setLanguageAndRestartMain();
            });

        });

        mBinding.llKorea.setOnClickListener(v -> {

            localLanguagePresenter.changeLanguage(AppConfig.KOREA, () -> {
                initView();
                mBinding.ivKorea.setVisibility(View.VISIBLE);
                setLanguageAndRestartMain();
            });


        });

    }


    /**
     * 重新启动主页
     */
    private void setLanguageAndRestartMain() {
        OtherLibManager.initZengDesk(this);
        AppUtils.setAppLanguage(this, getUserLanguageLocal());   //设置语言
        EventBus.getDefault().post(new AllFinishEvent());
        MainActivity.open(this);
        finish();
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

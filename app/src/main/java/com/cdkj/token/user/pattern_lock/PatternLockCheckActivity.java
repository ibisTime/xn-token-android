package com.cdkj.token.user.pattern_lock;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityPatternLockCheckBinding;
import com.cdkj.token.model.PatternLockCheckFinish;
import com.cdkj.token.user.login.SignInActivity2;
import com.cdkj.token.views.pattern_lock.OnPatternChangeListener;
import com.cdkj.token.views.pattern_lock.PatternHelper;
import com.cdkj.token.views.pattern_lock.PatternLockView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


/**
 * 手势密码锁验证
 * Created by cdkj on 2018/7/26.
 */

public class PatternLockCheckActivity extends AbsLoadActivity {

    private ActivityPatternLockCheckBinding mBinding;

    private PatternHelper patternHelper;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PatternLockCheckActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_pattern_lock_check, null, false);

        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        
        ImgUtils.loadLogo(this, SPUtilHelper.getUserPhoto(), mBinding.imgLogo);

        mBinding.tvUserPhone.setText(SPUtilHelper.getUserName());

        initPatternListener();

        initClickListener();

    }


    private void initClickListener() {

        mBinding.tvChangeLogin.setOnClickListener(view -> {
            toSignInPage();
            finish();
        });

    }

    void initPatternListener() {
        patternHelper = new PatternHelper();
        mBinding.patternLockView.setOnPatternChangedListener(new OnPatternChangeListener() {
            @Override
            public void onStart(PatternLockView view) {
            }

            @Override
            public void onChange(PatternLockView view, List<Integer> hitList) {
            }

            @Override
            public void onComplete(PatternLockView view, List<Integer> hitList) {
                boolean isOk = isPatternOk(hitList);
                view.updateStatus(!isOk);
                updateMsg();
            }

            @Override
            public void onClear(PatternLockView view) {
                if (patternHelper.isFinish()) {
                    if (patternHelper.isAllClear()) {
                        toSignInPage();
                    }
                    finish();
                }
            }
        });
    }

    /**
     * 跳向登录界面
     */
    void toSignInPage() {
        SPUtilHelper.logOutClear();
        OtherLibManager.uemProfileSignOff();
        EventBus.getDefault().post(new AllFinishEvent());
        SignInActivity2.open(PatternLockCheckActivity.this, true);
    }


    private void updateMsg() {
        mBinding.textMsg.setText(this.patternHelper.getMessage());

        if (!this.patternHelper.isOk()) {
            TranslateAnimation animation = new TranslateAnimation(0, -15, 0, 0); //错误时执行抖动动画
            animation.setInterpolator(new LinearInterpolator());
            animation.setDuration(150);
            animation.setRepeatCount(2);
            animation.setRepeatMode(Animation.REVERSE);
            mBinding.textMsg.startAnimation(animation);
        }
    }

    private boolean isPatternOk(List<Integer> hitList) {
        this.patternHelper.validateForChecking(hitList);
        return this.patternHelper.isOk();
    }

    @Override
    public void onBackPressed() {

    }

    @Subscribe
    public void patternLockCheckFinish(PatternLockCheckFinish finish) {
        this.finish();
    }
}

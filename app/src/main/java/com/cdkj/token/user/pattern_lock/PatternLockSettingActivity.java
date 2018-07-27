package com.cdkj.token.user.pattern_lock;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityPatternLockSettingBinding;
import com.cdkj.token.views.pattern_lock.OnPatternChangeListener;
import com.cdkj.token.views.pattern_lock.PatternHelper;
import com.cdkj.token.views.pattern_lock.PatternLockView;

import java.util.List;

/**
 * 手势密码锁
 * Created by cdkj on 2018/7/26.
 */

public class PatternLockSettingActivity extends AbsLoadActivity {


    private ActivityPatternLockSettingBinding mBinding;

    private PatternHelper patternHelper;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PatternLockSettingActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_pattern_lock_setting, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.set_pattern_pwd);
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
                mBinding.patternIndicatorView.updateState(hitList, !isOk);
                updateMsg();
            }

            @Override
            public void onClear(PatternLockView view) {
                if (patternHelper.isFinish()) {
                    finish();
                }
            }
        });
    }


    private void updateMsg() {
        mBinding.textMsg.setText(this.patternHelper.getMessage());
        mBinding.textMsg.setTextColor(this.patternHelper.isOk() ?
                getResources().getColor(R.color.gray_666666) :
                getResources().getColor(R.color.red));

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
        this.patternHelper.validateForSetting(hitList);
        return this.patternHelper.isOk();
    }

}

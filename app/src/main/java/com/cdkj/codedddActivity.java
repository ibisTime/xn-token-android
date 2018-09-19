package com.cdkj;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCodeddddBinding;

/**
 * Created by cdkj on 2018/9/19.
 */

public class codedddActivity extends AbsLoadActivity {

    private ActivityCodeddddBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, codedddActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_codedddd, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBinding.dragBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mBinding.swipeCaptchaView.setCurrentSwipeValue(DisplayHelper.dp2px(codedddActivity.this,i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件

                mBinding.dragBar.setMax(mBinding.swipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mBinding.swipeCaptchaView.matchCaptcha();
            }
        });

    }

}

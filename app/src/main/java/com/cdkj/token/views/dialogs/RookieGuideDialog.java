package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogRookieGuideBinding;

/**
 * 新手指导
 * Created by cdkj on 2018/7/20.
 */

public class RookieGuideDialog extends Dialog {

    private Activity mActivity;

    private DialogRookieGuideBinding mBinding;


    public RookieGuideDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_rookie_guide, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
        int screenHeight = DisplayHelper.getScreenHeight(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout(screenWidth, screenHeight);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        initListener();
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

    private void initListener() {
        mBinding.btnSure.setOnClickListener(view -> {
            mBinding.llGuide1.setVisibility(View.GONE);
            mBinding.llGuide2.setVisibility(View.VISIBLE);
        });

        mBinding.btnSure2.setOnClickListener(view -> {
            SPUtilHelper.saveRookieGuideShowed();
            dismiss();
        });
    }
}

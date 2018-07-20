package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogInfoSureBinding;

/**
 * 提示信息确认
 * Created by cdkj on 2018/7/20.
 */

public class InfoSureDialog extends Dialog {

    private Activity mActivity;

    private DialogInfoSureBinding mBinding;


    public InfoSureDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_info_sure, null, false);
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
        getWindow().setLayout((int) (screenWidth * 0.9f), (int) (screenHeight * 0.6));
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

    public InfoSureDialog setInfoTitle(String title) {
        if (mBinding == null) return this;
        mBinding.tvTitle.setText(title);
        return this;
    }

    public InfoSureDialog setInfoContent(String content) {
        if (mBinding == null) return this;
        mBinding.tvContent.setText(content);
        return this;
    }


    private void initListener() {
        mBinding.btnSure.setOnClickListener(view -> {
            dismiss();
        });
    }
}

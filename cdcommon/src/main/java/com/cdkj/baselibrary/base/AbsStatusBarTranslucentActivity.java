package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.ActivityAbsStatusBarBinding;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;


/**
 * 背景沉浸状态栏
 */
public abstract class AbsStatusBarTranslucentActivity extends BaseActivity {

    protected ActivityAbsStatusBarBinding mBaseBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIStatusBarHelper.setStatusBarLightMode(this);
        UIStatusBarHelper.translucent(this);

        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_abs_status_bar);

        View contentView = addContentView();
        if (contentView != null) {
            mBaseBinding.linLayoutRoot.addView(contentView);
        }

        mBaseBinding.imgBack.setOnClickListener(view -> finish());

        afterCreate(savedInstanceState);
    }

    /**
     * 添加要显示的View
     */
    public abstract View addContentView();

    /**
     * activity的初始化工作
     */
    public abstract void afterCreate(Bundle savedInstanceState);

    public void setPageBgImage(@DrawableRes int imgId) {
        mBaseBinding.linLayoutRoot.setBackgroundResource(imgId);
    }

    public void setTitleBgImage(@DrawableRes int imgId) {
        mBaseBinding.fraLayoutTitle.setBackgroundResource(imgId);
    }

    public void setMidTitle(String titleString) {
        mBaseBinding.tvTitle.setText(titleString);
    }

    public void setMidTitle(@StringRes int titleString) {
        mBaseBinding.tvTitle.setText(titleString);
    }

    public void sheShowTitle(boolean isShow) {
        mBaseBinding.fraLayoutTitle.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setWhiteTitle() {
        mBaseBinding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        mBaseBinding.imgBack.setImageResource(R.drawable.back_white);
    }

    public void setStatusBarWhite() {
        UIStatusBarHelper.setStatusBarDarkMode(this);
    }

}

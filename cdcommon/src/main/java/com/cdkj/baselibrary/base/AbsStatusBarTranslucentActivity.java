package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.ActivityAbsBaseLoadBinding;
import com.cdkj.baselibrary.databinding.ActivityAbsStatusBarBinding;
import com.cdkj.baselibrary.utils.ImgUtils;


/**
 * 背景沉浸状态栏
 */
public abstract class AbsStatusBarTranslucentActivity extends BaseActivity {

    protected ActivityAbsStatusBarBinding mBaseBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_abs_status_bar);

        View contentView = addContentView();
        if(contentView!=null){
            mBaseBinding.linLayoutRoot.addView(contentView);
        }

        mBaseBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

    public void setMidTitle(String titleString) {
        mBaseBinding.tvTitle.setText(titleString);
    }

    public void setMidTitle(@StringRes int titleString) {
        mBaseBinding.tvTitle.setText(titleString);
    }

}

package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.ActivityAbsBaseLoadBinding;


/**
 * 带空页面，错误页面显示的BaseActivity 通过AbsBaseActivityj界面操作封装成View而来
 */
public abstract class AbsStatusBarTranslucentActivity extends BaseActivity {
    protected ActivityAbsBaseLoadBinding mBaseBinding;

    /**
     * 布局文件xml的resId,无需添加标题栏、加载、错误及空页面
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_abs_base_load);

        mBaseBinding.contentView.addContentView(addMainView());

    }



    /**
     * 添加要显示的View
     */
    public abstract View addMainView();




}

package com.cdkj.token.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.cdkj.baselibrary.utils.DisplayHelper;

/**
 * 登录界面卡片样式布局
 * Created by cdkj on 2018/7/2.
 */

public class SignInPageCardLayout extends FrameLayout {

    public SignInPageCardLayout(@NonNull Context context) {
        this(context, null);
    }

    public SignInPageCardLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInPageCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int viewTopHeight = getChildAt(1).getHeight();

        View viewBottom = getChildAt(0);

        LayoutParams layoutParams = (FrameLayout.LayoutParams) viewBottom.getLayoutParams();

        layoutParams.topMargin = viewTopHeight - DisplayHelper.dpToPx(10);//-10dp向上隐藏

        viewBottom.setLayoutParams(layoutParams);

    }

}

package com.cdkj.token.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.cdkj.baselibrary.utils.DisplayHelper;

/**
 * 布局切换
 * Created by cdkj on 2018/6/28.
 */

public class CardChangeLayout extends FrameLayout {

    private View view1;
    private View view2;

    private static final int VEL_THRESHOLD = 500; // 滑动速度的阈值，超过这个绝对值认为是左右
    private static final int DISTANCE_THRESHOLD = 250; // 单位是像素，当左右滑动速度不够时，通过这个阈值来判定是应该左滑还是右滑动

    private boolean isView2;

    public CardChangeLayout(@NonNull Context context) {
        this(context, null);
    }

    public CardChangeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public CardChangeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view1 = getChildAt(0);
        view2 = getChildAt(1);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //设置缩放
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            float scale = (float) (view.getWidth() - DisplayHelper.dp2px(getContext(), 40 * (getChildCount() - i))) / (float) (view.getWidth());
            view.setPivotX(view.getWidth() / 2f);
            view.setPivotY(view.getHeight() / 2f);
            view.setScaleX(scale);
            view.setScaleY(scale);

            if (i == 0) {
                view.setTranslationX(50);
            }
        }
    }

    @Override
    public void computeScroll() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        View v = getChildAt(getChildCount() - 1); //获取表面View

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                view2.setTranslationZ(0);
                break;
        }

        return true;
    }


}

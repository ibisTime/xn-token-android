package com.cdkj.token.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.cdkj.baselibrary.utils.DisplayHelper;

/**
 * 布局切换
 * Created by cdkj on 2018/6/28.
 */
public class DrapScrollLayout extends FrameLayout {

    private ScrollView scrollView; //底部View

    /* 拖拽工具类 */
    private final ViewDragHelper mDragHelper;

    public DrapScrollLayout(@NonNull Context context) {
        this(context, null);
    }

    public DrapScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrapScrollLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper
                .create(this, 10f, new DragHelperCallback());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        scrollView = (ScrollView) getChildAt(0);
    }


    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {

            ViewCompat.postInvalidateOnAnimation(this);

        }
    }


    /* touch事件的拦截与处理都交给mDraghelper来处理 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        return shouldIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // 统一交给mDragHelper处理，由DragHelperCallback实现拖动效果
        try {
            mDragHelper.processTouchEvent(e);
        } catch (Exception e1) {
        }

        return true;
    }


    /**
     * 这是拖拽效果的主要逻辑
     */
    private class DragHelperCallback extends ViewDragHelper.Callback {


        @Override
        public boolean tryCaptureView(View child, int pointerId) {  //捕获子View
            return false;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {   //松手 当前被捕获的View释放之后回调
            if (mDragHelper.smoothSlideViewTo(releasedChild, 0, 0)) {  //回弹效果
                ViewCompat.postInvalidateOnAnimation(DrapScrollLayout.this);
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {  //可移动范围
            return top;
        }

    }


}

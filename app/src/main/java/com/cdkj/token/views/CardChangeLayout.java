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

import com.cdkj.baselibrary.utils.DisplayHelper;

/**
 * 布局切换
 * Created by cdkj on 2018/6/28.
 */
public class CardChangeLayout extends FrameLayout {

    private View childBottomView; //底部View
    private View childTopView;//顶部View

    private boolean isTryChildTopView;//当前捕获的View 是不是顶层View

    /* 拖拽工具类 */
    private final ViewDragHelper mDragHelper;
    private GestureDetectorCompat gestureDetector;//手势辅助
    private static final int VEL_THRESHOLD = 200; // 滑动速度的阈值，超过这个绝对值认为是左右
    private static final int DISTANCE_THRESHOLD = 250; // 单位是像素，当左右滑动速度不够时，通过这个阈值来判定是应该左滑还是右滑动

    private int margin;  //子View  左右margin

    private int viewRightMargin; // 顶部View右边距

    public ChangeCallBack changeCallBack;

    private AnimatorSet animatorSet;

    private int animatorDuration = 300; //动画执行时长 毫秒

    private float viewScaleSize = 1f; //底部View缩放比例

    private float buttomViewRightTranslationX; //底部View向右移动距离


    public static final int TOPVIEW = 1;
    public static final int BOTTOMVIEW = 0;


    public void setChangeCallBack(ChangeCallBack changeCallBack) {
        this.changeCallBack = changeCallBack;
    }

    public CardChangeLayout(@NonNull Context context) {
        this(context, null);
    }

    public CardChangeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardChangeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDragHelper = ViewDragHelper
                .create(this, 10f, new DragHelperCallback());
        gestureDetector = new GestureDetectorCompat(context,
                new YScrollDetector());

        margin = DisplayHelper.dp2px(getContext(), 12);
        viewRightMargin = DisplayHelper.dp2px(getContext(), 12);

        buttomViewRightTranslationX = viewRightMargin * viewScaleSize; // 底部View向右移动距离
//        buttomViewRightTranslationX = viewRightMargin + viewRightMargin * viewScaleSize + margin; // 底部View向右移动距离
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childBottomView = getChildAt(0);
        childTopView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            int childWidth = sizeWidth - viewRightMargin - margin * 2; //子View宽度= 父控件宽度-右边距 -(左右边距之和)
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                    childWidth, MeasureSpec.EXACTLY);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    sizeHeight, MeasureSpec.EXACTLY);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
        setMeasuredDimension(sizeWidth, sizeHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);

            childView.layout(0 + margin, 0, childView.getMeasuredWidth() + margin, childView.getMeasuredHeight());

            if (i != childCount - 1) {  //不是最后一个View都进行缩放 （底部View）
                childView.setPivotX(childView.getWidth() / 2f);  //设置缩放中心点
                childView.setPivotY(childView.getHeight() / 2f);
                childView.setScaleX(viewScaleSize);
                childView.setScaleY(viewScaleSize);

                if (!isAnimatorRunning()) {
                    childView.setTranslationX(buttomViewRightTranslationX);  //向右移动 底部View超出效果
                }
            }
        }
    }


    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {

            ViewCompat.postInvalidateOnAnimation(this);

        } else {

            if (childBottomView.getLeft() == margin && childTopView.getLeft() == margin || isAnimatorRunning()) {  //第一次绘制 或在动画中
                return;
            }

            if (changeCallBack != null) {
                if (!changeCallBack.onChangeStart(isTryChildTopView ? BOTTOMVIEW : TOPVIEW)) {
                    if (mDragHelper.smoothSlideViewTo(isTryChildTopView ? childTopView : childBottomView, margin, 0)) {  //回弹效果
                        ViewCompat.postInvalidateOnAnimation(CardChangeLayout.this);
                    }
                    return;
                }
            }

            if (isTryChildTopView && childTopView.getLeft() < 0 || childTopView.getLeft() >= getWidth()) {    //顶部View 执行位移动画
                startAnimator(childTopView, childBottomView);
            } else if (!isTryChildTopView && childBottomView.getLeft() < 0 || childBottomView.getLeft() >= getWidth()) {
                startAnimator(childBottomView, childTopView);
            }


        }
    }

    /**
     * 是否正在交换动画中
     *
     * @return
     */
    public boolean isChanging() {
        return mDragHelper.continueSettling(true) || isAnimatorRunning();
    }


    /**
     * 组合动画
     *
     * @param translationView （顶部的View 移动到屏幕右边后做向右平移动画）
     * @param scaleView       （底部部的View 做放大动画的同时移动到topView的位置）
     */
    private void startAnimator(View translationView, View scaleView) {
        animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(scaleView, "scaleX", viewScaleSize, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(scaleView, "scaleY", viewScaleSize, 1f);

        //底部View移动到顶层View 的位置
        ObjectAnimator scaleViewTranslationX = ObjectAnimator.ofFloat(scaleView, "translationX", buttomViewRightTranslationX, 0f);

        ObjectAnimator translationViewTranslationX = ObjectAnimator.ofFloat(translationView, "translationX", translationView.getLeft(), buttomViewRightTranslationX);

        scaleView.setPivotX(scaleView.getWidth() / 2f);  //缩放点
        scaleView.setPivotY(scaleView.getHeight() / 2f);

        animatorSet.setDuration(animatorDuration);

        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                updateViewLayout(scaleView, scaleView.getLayoutParams());   //更新布局
                if (changeCallBack != null) {
                    changeCallBack.onChangeEnd(translationView == childTopView ? BOTTOMVIEW : TOPVIEW);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        animatorSet.playTogether(translationViewTranslationX, scaleViewTranslationX);

        animatorSet.play(scaleX).with(scaleY);

        bringChildToFront(scaleView); //顶部View 和底部View 交换位置

        animatorSet.start();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);  //处理滑动冲突所有事件交由当前View消费
        return super.dispatchTouchEvent(ev);
    }

    /* touch事件的拦截与处理都交给mDraghelper来处理 */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean yScroll = gestureDetector.onTouchEvent(ev);
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        int action = ev.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            mDragHelper.processTouchEvent(ev);
        }

        return shouldIntercept && yScroll;
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
        public void onViewPositionChanged(View changedView, int left, int top, //坐标发生变化时
                                          int dx, int dy) {

        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {  //捕获子View
            if (isAnimatorRunning() || mDragHelper.continueSettling(true)
                    || childBottomView.getLeft() < 0 || childTopView.getLeft() < 0
                    || childBottomView.getLeft() >= getWidth() || childTopView.getLeft() >= getWidth()) { //在动画中或移动中禁止捕获
                return false;
            }
            isTryChildTopView = child == childTopView;
            return true;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {   //松手 当前被捕获的View释放之后回调

            int finalLeft = margin; // 默认是粘到最顶端

            if (xvel < -VEL_THRESHOLD || releasedChild.getLeft() < -DISTANCE_THRESHOLD) { //左滑
                finalLeft = -getWidth();
            } else if (xvel > VEL_THRESHOLD || releasedChild.getLeft() > DISTANCE_THRESHOLD) {
                finalLeft = getWidth();
            }

            if (mDragHelper.smoothSlideViewTo(releasedChild, finalLeft, 0)) {  //回弹效果
                ViewCompat.postInvalidateOnAnimation(CardChangeLayout.this);
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {  //可移动范围
            return 0;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return left;
        }


    }

    /**
     * 动画是否在执行
     *
     * @return
     */
    public boolean isAnimatorRunning() {

        if (animatorSet == null) {
            return false;
        }
        return animatorSet.isRunning();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animatorSet != null) {
            animatorSet.addListener(null);
            animatorSet.cancel();
        }
    }

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
                                float dy) {
            // 垂直滑动时dx>dy，才被认定是左右拖动
            return Math.abs(dx) > Math.abs(dy);
        }
    }

    public interface ChangeCallBack {

        boolean onChangeStart(int index);  //卡片交换之前回调 返回值确定是否允许切换

        void onChangeEnd(int index);//布局动画切换结束
    }


}

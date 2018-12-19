package com.cdkj.token.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**重写Viewpager禁止左右滑动

 */
public class EnabledViewpager extends ViewPager {

    /**
     * 上一次x坐标
     */
    private float beforeX;

    private boolean isPagingEnabled = true;

    public EnabledViewpager(Context context) {
        super(context);
    }

    public EnabledViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
    @Override
    public int getCurrentItem() {
        return PagerAdapter.POSITION_NONE;
    }

    //-----禁止左滑-------左滑：上一次坐标 > 当前坐标
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isPagingEnabled){
            return super.dispatchTouchEvent(ev);
        }else  {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    beforeX = ev.getX();
                    break;

                case MotionEvent.ACTION_MOVE:
                    float motionValue = ev.getX() - beforeX;
                    if (motionValue < 0) {//禁止左滑
                        return true;
                    }
                    beforeX = ev.getX();

                    break;

                default:
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }

    }

}

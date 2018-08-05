package com.cdkj.baselibrary.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * 处理PotoView和ViewPager冲突问题
 * Created by cdkj on 2017/10/24.
 */

public class PhotoViewPager extends ViewPager {

    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写该方法来解决冲突问题
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //写自己要处理的error包括报错日志
            e.printStackTrace();
            Log.e("TAG", "onInterceptTouchEvent: ");

            return false;
        }
    }
}
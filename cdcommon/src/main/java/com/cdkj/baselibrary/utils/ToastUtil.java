package com.cdkj.baselibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.hjq.toast.ToastUtils;

/**
 * 吐司
 *
 * @author mxy
 * @time 2016/9/6
 */
public class ToastUtil {

    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);

        if (mToast != null) {
            mToast.setText(text);
        } else {
            ToastUtils.show(text);
            mToast = ToastUtils.getToast();
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.BOTTOM, 0, DisplayHelper.dp2px(mContext, 40));
//            mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
        }

        mHandler.postDelayed(r, 1500);

        mToast.show();
    }

    public static void show(Context context, String info) {

        if (context == null) {
            return;
        }

        try {
            showToast(context, info, Toast.LENGTH_LONG);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void show(Context context, int info) {
        if (context == null) {
            return;
        }
        try {
            showToast(context, info + "", Toast.LENGTH_LONG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

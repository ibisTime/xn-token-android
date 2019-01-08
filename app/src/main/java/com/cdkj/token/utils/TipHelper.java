package com.cdkj.token.utils;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;
 
/**
 *震动工具类
 */
public class TipHelper {

    /**
     *
     * @param activity
     * @param milliseconds  震动时长  单位毫秒
     */
    public static void Vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }
 
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
    
}

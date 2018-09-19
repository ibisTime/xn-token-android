package com.li.verification;

import android.app.Activity;
import android.content.Context;

import com.alibaba.verificationsdk.ui.VerifyActivity;
import com.alibaba.verificationsdk.ui.VerifyType;
import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.SecurityInit;

/**
 * Created by 李先俊 on 2018/9/19.
 */

public class VerificationUtils {

    public static void Initialize(Context context) {

        try {
            SecurityInit.Initialize(context);
        } catch (JAQException e) {
            e.printStackTrace();
        }

    }

    public static void startSimpleVerifyUI(Activity activity) {

        VerifyActivity.startSimpleVerifyUI(activity, VerifyType.NOCAPTCHA, "", "", null);

    }
}

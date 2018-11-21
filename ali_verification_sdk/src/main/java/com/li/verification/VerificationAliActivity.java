package com.li.verification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.alibaba.verificationsdk.ui.IActivityCallback;
import com.alibaba.verificationsdk.ui.VerifyActivity;
import com.alibaba.verificationsdk.ui.VerifyType;
import com.alibaba.wireless.security.jaq.JAQException;
import com.alibaba.wireless.security.jaq.SecurityInit;

import java.util.Map;

/**
 * Created by cdkj on 2018/9/19.
 */

public class VerificationAliActivity extends Activity {

    public static String SESSIONID = "sessionID";
    public static int RESULT_CODE = 22;

    public static void open(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, VerificationAliActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            SecurityInit.Initialize(this);
        } catch (JAQException e) {
            e.printStackTrace();
        }

        VerifyActivity.startSimpleVerifyUI(VerificationAliActivity.this,
                VerifyType.NOCAPTCHA, "0335", null, new IActivityCallback() {
                    @Override
                    public void onNotifyBackPressed() {
                        finish();
                    }

                    @Override
                    public void onResult(int retInt, Map<String, String> code) {
                        switch (retInt) {
                            case VerifyActivity.VERIFY_SUCC:
                                String sessionID = code.get("sessionID");
                                Intent intent = new Intent();
                                intent.putExtra(SESSIONID, sessionID);
                                setResult(RESULT_CODE, intent);
                                break;

                            case VerifyActivity.VERIFY_FAILED:
                                Toast.makeText(VerificationAliActivity.this, R.string.verify_failed, Toast.LENGTH_LONG).show();
                                break;

                            default:
                                break;
                        }
                        finish();
                    }
                });
    }


}

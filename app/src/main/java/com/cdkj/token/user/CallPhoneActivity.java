package com.cdkj.token.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.token.R;

/**
 * 拨打电话
 * Created by cdkj on 2018/2/24.
 */

public class CallPhoneActivity extends BaseActivity {


    private PermissionHelper mPermissionHelper;
    private String mobile;


    /**
     * @param context
     * @param mobile  要拨打的电话
     */
    public static void open(Context context, String mobile) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CallPhoneActivity.class);
        intent.putExtra("mobile", mobile);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionHelper = new PermissionHelper(this);

        if (getIntent() != null) {
            mobile = getIntent().getStringExtra("mobile");
        }

        if (TextUtils.isEmpty(mobile)) {
            finish();
            return;
        }

        showCallPhoneDialog(mobile, view -> {
            finish();
        }, view -> {
            permissionRequest();
        });

    }

    /**
     *     <uses-permission android:name="android.permission.CALL_PHONE" />
     * 拨打电话权限请求
     */
    private void permissionRequest() {
        mPermissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {

                AppUtils.callPhonePage(CallPhoneActivity.this, mobile);
                finish();
            }

            @Override
            public void doAfterDenied(String... permission) {
                showSureDialog(getString(R.string.no_phone_permission), view -> finish());
            }
        }, Manifest.permission.CALL_PHONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void showCallPhoneDialog(String str, CommonDialog.OnNegativeListener onNegativeListener, CommonDialog.OnPositiveListener onPositiveListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(getStrRes(R.string.call_phone)).setContentMsg(str)
                .setPositiveBtn(getStrRes(R.string.sure), onPositiveListener)
                .setNegativeBtn(getStrRes(R.string.cancel), onNegativeListener, false);

        commonDialog.show();
    }



    protected void showSureDialog(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(getStrRes(R.string.tip)).setContentMsg(str)
                .setPositiveBtn(getStrRes(R.string.sure), onPositiveListener);
//        commonDialog.getContentView().setGravity(Gravity.CENTER);

        commonDialog.show();
    }


}

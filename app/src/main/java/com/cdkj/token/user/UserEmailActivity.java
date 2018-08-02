package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserEmailBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/25.
 */

public class UserEmailActivity extends AbsActivity {

    private ActivityUserEmailBinding mBinding;

    private String email;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String email) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserEmailActivity.class);
        intent.putExtra("email", email);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_email, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);

        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
            if (email == null || email.equals("")) {
                setTopTitle(getStrRes(R.string.user_title_email_bind));
            } else {
                setTopTitle(getStrRes(R.string.user_title_email_modify));
                mBinding.edtEmail.setText(email);
                mBinding.edtEmail.setHint(email);
            }
        }

        initListener();
    }

    private void initListener() {
        mBinding.btnSend.setOnClickListener(view -> {

            if (check("code"))
                sendEmailCode();

        });

        mBinding.btnConfirm.setOnClickListener(v -> {

            if (check("all")) {
                modifyEmail();
            }
        });
    }

    private boolean check(String type) {
        if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_hint));
            return false;
        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_code_hint));
                return false;
            }
        }

        return true;
    }


    /**
     * 修改邮箱
     */
    public void sendEmailCode() {
        Map<String, String> map = new HashMap<>();
        map.put("bizType", "805081");
        map.put("email", mBinding.edtEmail.getText().toString());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805954", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    if (data.isSuccess()) {
                        UITipDialog.showInfoNoIcon(UserEmailActivity.this,getString(R.string.email_code_send_success));

                        //启动倒计时
                        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
                        //改变ui
                        mBinding.btnSend.setBackgroundResource(R.drawable.corner_sign_btn_gray);
                    } else {
                        UITipDialog.showInfoNoIcon(UserEmailActivity.this,getString(R.string.email_code_send_failure));
                    }
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 修改邮箱
     */
    public void modifyEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("captcha", mBinding.edtCode.getText().toString());
        map.put("email", mBinding.edtEmail.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805081", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    SPUtilHelper.saveUserEmail(mBinding.edtEmail.getText().toString().trim());

                    if (TextUtils.isEmpty(email)) {

                        UITipDialog.showSuccess(UserEmailActivity.this, getStrRes(R.string.user_email_bind_success), dialogInterface -> {
                            finish();
                        });

                    } else {
                        UITipDialog.showSuccess(UserEmailActivity.this, getStrRes(R.string.user_email_modify_success), dialogInterface -> {
                            finish();
                        });
                    }
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


}

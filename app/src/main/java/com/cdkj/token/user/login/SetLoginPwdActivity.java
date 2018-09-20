package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityFindPasswordBinding;

import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;

/**
 * 设置密码和修改密码
 */
public class SetLoginPwdActivity extends AbsActivity implements SendCodeInterface {

    private ActivityFindPasswordBinding mBinding;


    private SendPhoneCodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SetLoginPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_find_password, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {


        if (SPUtilHelper.getLoginPwdFlag()) { // 设置过支付密码
            mBinding.tvTitle.setText(R.string.user_setting_password);
        } else {
            mBinding.tvTitle.setText(R.string.set_login_pwd);
        }

        mBinding.edtMobile.setDownImgVisibilityGone();
        mBinding.edtMobile.getEditText().setEnabled(false);

        setSubLeftImgState(true);
        mSendCOdePresenter = new SendPhoneCodePresenter(this, this);

        mBinding.edtMobile.getEditText().setText(SPUtilHelper.getUserPhoneNum());

        initListener();

        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtRePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        ImgUtils.loadActImg(this, SPUtilHelper.getCountryFlag(), mBinding.edtMobile.getLeftImage());
    }


    /**
     *
     */
    private void initListener() {
        //国家区号选择
//        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
//            CountryCodeListActivity.open(this, true);
//        });
        mBinding.tvFinish.setOnClickListener(view -> finish());

        //发送验证码
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtMobile.getText())) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(com.cdkj.baselibrary.R.string.activity_paypwd_mobile_hint));
                    return;
                }

                String phone = mBinding.edtMobile.getText().toString().trim();
                SendVerificationCode sendVerificationCode = new SendVerificationCode(
                        phone, "805063", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode());
                mSendCOdePresenter.openVerificationActivity(sendVerificationCode);
            }
        });


        //确定
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString())) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(R.string.activity_find_mobile_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(R.string.activity_find_code_hint));
                    return;
                }

//                if (SPUtilHelper.getGoogleAuthFlag()){
//                    if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())){
//                        showToast(getString(R.string.activity_find_google_hint));
//                        return;
//                    }
//                }

                if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString())) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(R.string.activity_find_password_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString())) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(R.string.activity_find_repassword_hint));
                    return;
                }

                if (mBinding.edtPassword.getText().length() < 6) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(R.string.activity_find_password_format_hint));
                    return;
                }

                if (!mBinding.edtPassword.getText().toString().equals(mBinding.edtRePassword.getText().toString())) {
                    UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, getString(R.string.activity_find_repassword_format_hint));
                    return;
                }

                findPwdReqeust();
            }
        });
    }


    /**
     * 找回密码请求
     */
    private void findPwdReqeust() {

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("mobile", mBinding.edtMobile.getText().toString());
        hashMap.put("newLoginPwd", mBinding.edtPassword.getText().toString());
        hashMap.put("smsCaptcha", mBinding.edtCode.getText().toString());
        hashMap.put("kind", AppConfig.USERTYPE);
//        hashMap.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        hashMap.put("systemCode", AppConfig.SYSTEMCODE);
        hashMap.put("companyCode", AppConfig.COMPANYCODE);
        hashMap.put("interCode", SPUtilHelper.getCountryInterCode());
        hashMap.put("countryCode", SPUtilHelper.getCountryCode());
        Call call = RetrofitUtils.getBaseAPiService().successRequest("805063", StringUtils.getRequestJsonString(hashMap));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(SetLoginPwdActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    if (SPUtilHelper.getLoginPwdFlag()) {
                        showToast(getString(R.string.activity_find_success));
                    } else {
                        showToast(getString(R.string.login_pwd_sed_success));
                    }
                    SPUtilHelper.saveLoginPwdFlag(true);
                    finish();
                } else {
                    if (SPUtilHelper.getLoginPwdFlag()) {
                        showToast(getString(R.string.activity_find_failure));
                    } else {
                        showToast(getString(R.string.activity_setting_failure));
                    }


                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }


    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray,
                ContextCompat.getColor(this, R.color.btn_blue), ContextCompat.getColor(this, R.color.white)));

    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showInfoNoIcon(SetLoginPwdActivity.this, msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSendCOdePresenter != null) {
            mSendCOdePresenter.clear();
            mSendCOdePresenter = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCOdePresenter != null) {
            mSendCOdePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }


}

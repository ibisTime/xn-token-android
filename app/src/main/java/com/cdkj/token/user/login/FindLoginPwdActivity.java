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
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityFindPasswordBinding;
import com.cdkj.token.user.CountryCodeListActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;

/**
 * 找回密码
 */
public class FindLoginPwdActivity extends AbsActivity implements SendCodeInterface {

    private ActivityFindPasswordBinding mBinding;

    private String mPhoneNumber;

    private SendPhoneCodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String mPhoneNumber) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FindLoginPwdActivity.class);
        intent.putExtra("phonenumber", mPhoneNumber);
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
        setTopTitle(getString(R.string.activity_find_title));
        setSubLeftImgState(true);
        mSendCOdePresenter = new SendPhoneCodePresenter(this);
        if (getIntent() != null) {
            mPhoneNumber = getIntent().getStringExtra("phonenumber");
        }
        mBinding.edtMobile.getEditText().setText(mPhoneNumber);

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
        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(this, true);
        });
        mBinding.tvFinish.setOnClickListener(view -> finish());

        //发送验证码
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendCOdePresenter.sendCodeRequest(mBinding.edtMobile.getText().toString(), "805063", AppConfig.USERTYPE, SPUtilHelper.getCountryInterCode(), FindLoginPwdActivity.this);
            }
        });


        //确定
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString())) {
                    UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, getString(R.string.activity_find_mobile_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                    UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, getString(R.string.activity_find_code_hint));
                    return;
                }

//                if (SPUtilHelper.getGoogleAuthFlag()){
//                    if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())){
//                        showToast(getString(R.string.activity_find_google_hint));
//                        return;
//                    }
//                }

                if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString())) {
                    UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, getString(R.string.activity_find_password_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString())) {
                    UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, getString(R.string.activity_find_repassword_hint));
                    return;
                }

                if (mBinding.edtPassword.getText().length() < 6) {
                    UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, getString(R.string.activity_find_password_format_hint));
                    return;
                }

                if (!mBinding.edtPassword.getText().toString().equals(mBinding.edtRePassword.getText().toString())) {
                    UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, getString(R.string.activity_find_repassword_format_hint));
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
        hashMap.put("companyCode", AppConfig.COMPANYCODE);
        hashMap.put("interCode", SPUtilHelper.getCountryInterCode());
        hashMap.put("countryCode", SPUtilHelper.getCountryCode());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805063", StringUtils.getJsonToString(hashMap));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(FindLoginPwdActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(FindLoginPwdActivity.this, getString(R.string.activity_find_success), dialogInterface -> {
                        finish();
                    });

                } else {
                    UITipDialog.showFail(FindLoginPwdActivity.this, getString(R.string.activity_find_failure));
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
        UITipDialog.showInfoNoIcon(FindLoginPwdActivity.this, msg);
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


}

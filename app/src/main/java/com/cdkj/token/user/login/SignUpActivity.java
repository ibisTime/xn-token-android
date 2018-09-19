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
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivitySignUpBinding;
import com.cdkj.token.user.CountryCodeListActivity;
import com.li.verification.VerificationAliActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


public class SignUpActivity extends AbsActivity implements SendCodeInterface {


    private SendPhoneCodePresenter mPresenter;
    private ActivitySignUpBinding mBinding;

    public int AL_IVERIFICATION_REQUEST_CODE = 100;
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_up, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_sign_up));
        setSubLeftImgState(true);

        mPresenter = new SendPhoneCodePresenter(this);

        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtRePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        ImgUtils.loadActImg(this, SPUtilHelper.getCountryFlag(), mBinding.edtMobile.getLeftImage());
    }


    private void initListener() {
        //国家区号选择
        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(this, true);
        });
        mBinding.tvFinish.setOnClickListener(view -> finish());

        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (check("code")) {
                VerificationAliActivity.open(this, AL_IVERIFICATION_REQUEST_CODE);
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check("all")) {
                signUp();
            }
        });

    }

    private boolean check(String type) {

        if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
            return false;
        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_code_hint));
                return false;
            }

            if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_hint));
                return false;
            }
            if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_hint));
                return false;
            }
            if (!mBinding.edtRePassword.getText().toString().trim().equals(mBinding.edtPassword.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_two_hint));
                return false;
            }

        }


        return true;
    }

    private void signUp() {
        Map<String, Object> map = new HashMap<>();
//        map.put("nickname", mBinding.edtNick.getText().toString().trim());
        map.put("kind", "C");
//        map.put("userRefereeKind", "C");
//        map.put("userReferee", mBinding.edtReferee.getText().toString().trim());
        map.put("mobile", mBinding.edtMobile.getText().toString().trim());
        map.put("loginPwd", mBinding.edtPassword.getText().toString().trim());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString().trim());
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);
        map.put("countryCode", SPUtilHelper.getCountryCode());
        map.put("interCode", SPUtilHelper.getCountryInterCode());
        map.put("client", "android");
        Call call = RetrofitUtils.createApi(MyApi.class).signUp("805041", StringUtils.objectToJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {

            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) || !TextUtils.isEmpty(data.getUserId())) {
                    showToast(getStrRes(R.string.user_sign_up_success));

                    SPUtilHelper.saveUserId(data.getUserId());
                    SPUtilHelper.saveUserToken(data.getToken());
                    SPUtilHelper.saveUserPhoneNum(mBinding.edtMobile.getText().toString().trim());
                    OtherLibManager.uemProfileSignIn(data.getUserId());
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面

                    MainActivity.open(SignUpActivity.this);
                    finish();
                } else {
                    UITipDialog.showInfoNoIcon(SignUpActivity.this, getStrRes(R.string.user_sign_up_failure));
                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray,
                ContextCompat.getColor(this, R.color.btn_blue), ContextCompat.getColor(this, R.color.white)));

    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showInfoNoIcon(this, msg);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (requestCode == AL_IVERIFICATION_REQUEST_CODE && resultCode == VerificationAliActivity.RESULT_CODE) {
            String sessionid = getIntent().getStringExtra(VerificationAliActivity.SESSIONID);
            mPresenter.sendCodeRequest(mBinding.edtMobile.getText().toString().trim(), sessionid, "805041", "C", SPUtilHelper.getCountryInterCode(), this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }

}

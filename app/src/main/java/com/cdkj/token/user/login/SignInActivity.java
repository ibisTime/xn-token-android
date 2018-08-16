package com.cdkj.token.user.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.activitys.AppBuildTypeActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySignInBinding;
import com.cdkj.token.user.CountryCodeListActivity;

import java.util.HashMap;

import retrofit2.Call;

@Route(path = CdRouteHelper.APPLOGIN)
public class SignInActivity extends AbsStatusBarTranslucentActivity implements LoginInterface, SendCodeInterface {

    private boolean canOpenMain;

    private LoginPresenter mPresenter;
    private SendPhoneCodePresenter mSendPhoneCodePresenter;
    private ActivitySignInBinding mBinding;

    private final String CODE_LOGIN_CODE = "805044";//验证码登录接口编号

    private int changeDevCount = 0;//用于记录研发或测试环境切换条件


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, boolean canOpenMain) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, canOpenMain);
        context.startActivity(intent);
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        sheShowTitle(false);

        setPageBgImage(R.drawable.sign_in_bg);

        mPresenter = new LoginPresenter(this);
        mSendPhoneCodePresenter = new SendPhoneCodePresenter(this);
        init();
        initEditInputType();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeDevCount = 0;
        mBinding.edtUsername.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        ImgUtils.loadActImg(this, SPUtilHelper.getCountryFlag(), mBinding.edtUsername.getLeftImage());
    }

    private void initEditInputType() {
        mBinding.edtUsername.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void init() {
        if (getIntent() == null)
            return;

        canOpenMain = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);
    }

    private void initListener() {
        //改变登录方式
        mBinding.tvChangeLogin.setOnClickListener(v -> {
            if (isCodeLogin()) {    //显示密码登录
                mBinding.edtCode.setVisibility(View.GONE);
                mBinding.edtPassword.setVisibility(View.VISIBLE);
                mBinding.tvChangeLogin.setText(R.string.code_login);

            } else {                                                     //显示验证码登录
                mBinding.edtCode.setVisibility(View.VISIBLE);
                mBinding.edtPassword.setVisibility(View.GONE);
                mBinding.tvChangeLogin.setText(R.string.account_login);
            }

        });

        //发送验证码
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtUsername.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
                return;
            }

            mSendPhoneCodePresenter.sendCodeRequest(mBinding.edtUsername.getText().toString().trim(), CODE_LOGIN_CODE, "C", SPUtilHelper.getCountryInterCode(), this);
        });

        //登录
        mBinding.btnConfirm.setOnClickListener(v -> {
            checkInputAndLogin();
        });

        //找回密码
        mBinding.edtPassword.getRughtText().setOnClickListener(v -> {
            FindLoginPwdActivity.open(this, mBinding.edtUsername.getText().toString().trim());
        });

        //注册
        mBinding.tvToSignUp.setOnClickListener(view -> {
            SignUpActivity.open(this);
        });

        //国家区号选择
        mBinding.edtUsername.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(this, true);
        });


        /**
         * 切换环境
         */
        if (LogUtil.isLog) {
            mBinding.imgTha.setOnClickListener(view -> {
                changeDevCount++;
                if (changeDevCount > 8) {           //连续点击8次以上才可以切换环境
                    AppBuildTypeActivity.open(this);
                }
            });
        }

    }

    /**
     * 检测输入并登录
     */
    private void checkInputAndLogin() {

        if (TextUtils.isEmpty(mBinding.edtUsername.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
            return;
        }

        if (isCodeLogin()) {
            if (TextUtils.isEmpty(mBinding.edtCode.getEditText().getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_code_hint));
                return;
            }
            codeLoginRequest();

        } else {

            if (mBinding.edtPassword.getText().toString().trim().length() < 6) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_format_hint));
                return;
            }

            mPresenter.login(mBinding.edtUsername.getText().toString(), mBinding.edtPassword.getText().toString(), SPUtilHelper.getCountryInterCode(), this);
        }
    }


    /**
     * 验证码登录
     */
    public void codeLoginRequest() {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("countryCode", SPUtilHelper.getCountryCode());
        hashMap.put("mobile", mBinding.edtUsername.getEditText().getText().toString());
        hashMap.put("smsCaptcha", mBinding.edtCode.getEditText().getText().toString());
        hashMap.put("systemCode", MyConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);
        hashMap.put("interCode", SPUtilHelper.getCountryInterCode());
        Call call = RetrofitUtils.getBaseAPiService().userLogin(CODE_LOGIN_CODE, StringUtils.getJsonToString(hashMap));

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {
            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) && !TextUtils.isEmpty(data.getUserId())) {
                    loginSuccessNext(data);
                } else {
                    disMissLoading();
                    UITipDialog.showInfoNoIcon(SignInActivity.this, SucMessage);
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 登录成功下一步
     *
     * @param user
     */
    public void loginSuccessNext(UserLoginModel user) {
        SPUtilHelper.saveUserId(user.getUserId());
        SPUtilHelper.saveUserToken(user.getToken());
        SPUtilHelper.saveUserPhoneNum(mBinding.edtUsername.getText().toString().trim());

        if (canOpenMain) {
            MainActivity.open(this);
        }
        finish();
    }

    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {
        loginSuccessNext(user);
    }


    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoading();
        showToast(msg);
    }


    /**
     * 是否是验证码登录
     *
     * @return
     */
    public boolean isCodeLogin() {
        return mBinding.edtCode.getVisibility() == View.VISIBLE;
    }

    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
        disMissLoading();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }


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
        disMissLoading();
    }


}

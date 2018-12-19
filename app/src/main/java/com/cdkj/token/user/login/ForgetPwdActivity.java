package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityForgetPwdBinding;
import com.cdkj.token.interfaces.UserTabLayoutInterface;
import com.cdkj.token.user.CountryCodeListActivity;
import com.cdkj.token.views.UserTableLayout;
import com.cdkj.token.wallet.trade_pwd.TradePwdActivity;

import static com.cdkj.token.views.UserTableLayout.LEFT;

/**
 * Created by cdkj on 2018/11/26.
 */

public class ForgetPwdActivity extends AbsStatusBarTranslucentActivity implements UserTabLayoutInterface, SendCodeInterface {

    // RC(REQUEST_CODE)
    public final static String RC_LOGIN_PWD_FIND_OUT = "login_pwd_find_out"; // 找回登陆密码
    public final static String RC_LOGIN_PWD_MODIFY = "login_pwd_modify"; // 修改登陆密码
    public final static String RC_TRADE_PWD_MODIFY = "trade_pwd_modify"; // 修改资金密码

    private ActivityForgetPwdBinding mBinding;

    private SendPhoneCodePresenter mSendCodePresenter;



    private String mobile = "";
    private String email = "";
    private String code = "";

    public static void open(Context context, String mobile, String email, String code) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, mobile);
        intent.putExtra(CdRouteHelper.DATASIGN2, email);
        intent.putExtra(CdRouteHelper.DATASIGN3, code);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_forget_pwd,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setWhiteTitle();
        setPageBgImage(R.mipmap.app_page_bg_new);

        init();
        initView();
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mBinding){
            mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        }
    }

    private void init() {
        mSendCodePresenter = new SendPhoneCodePresenter(this, this);

        if (getIntent() != null) {
            mobile = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
            email = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);
            code = getIntent().getStringExtra(CdRouteHelper.DATASIGN3);
        }
        mBinding.edtMobile.getEditText().setText(mobile);
        mBinding.edtEmail.getEditText().setText(email);

        // 设置标题
        switch (code){
            case RC_LOGIN_PWD_FIND_OUT: // 找回登录密码: 忘记密码
                setMidTitle(R.string.user_forget_title);
                break;

            case RC_LOGIN_PWD_MODIFY: // 修改登录密码
                setMidTitle(R.string.user_setting_password);
                break;

            case RC_TRADE_PWD_MODIFY: // 修改资金密码
                setMidTitle(R.string.activity_paypwd_title);
                break;
        }
    }

    private void initView() {
        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));

        if (TextUtils.equals(RC_LOGIN_PWD_MODIFY, code) || TextUtils.equals(RC_TRADE_PWD_MODIFY, code)){
            // 修改登陆密码 和 修改资金密码时:

            // 邮箱和手机不可输入
            mBinding.edtMobile.getEditText().setEnabled(false);
            mBinding.edtEmail.getEditText().setEnabled(false);

            // 切换Tab隐藏，只显示 邮箱 或 手机 其中一种方式
            mBinding.tlSignUP.setVisibility(View.GONE);
            // 是否有手机号，没有则替换为邮箱
            if (TextUtils.isEmpty(mobile)){
                mBinding.tlSignUP.setPosition(UserTableLayout.RIGHT);
                mBinding.edtMobile.setVisibility(View.GONE);
                mBinding.edtEmail.setVisibility(View.VISIBLE);
            }else {
                mBinding.tlSignUP.setPosition(UserTableLayout.LEFT);
                mBinding.edtMobile.setVisibility(View.VISIBLE);
                mBinding.edtEmail.setVisibility(View.GONE);
            }
        }

    }

    private void initListener() {
        mBinding.tlSignUP.setInterface(this);

        //国家区号选择
        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(this, true);
        });

        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (!check("code")) {
                return;
            }

            String account;
            SendVerificationCode sendVerificationCode;
            if (mBinding.tlSignUP.getPosition() == LEFT){
                account = mBinding.edtMobile.getText().toString().trim();
            }else {
                account = mBinding.edtEmail.getText().toString().trim();
            }

            sendVerificationCode = new SendVerificationCode(
                    account, code, "C", SPUtilHelper.getCountryInterCode());

            mSendCodePresenter.openVerificationActivity(sendVerificationCode);
        });

        mBinding.btnNext.setOnClickListener(view -> {
            if (!check("all")) {
                return;
            }

            String account;
            if (mBinding.tlSignUP.getPosition() == LEFT){
                account = mBinding.edtMobile.getText().toString().trim();
            }else {
                account = mBinding.edtEmail.getText().toString().trim();
            }

            if (TextUtils.isEmpty(code)){
                return;
            }

            switch (code){

                case RC_LOGIN_PWD_FIND_OUT: // 找回登录密码
                case RC_LOGIN_PWD_MODIFY: // 修改登录密码
                    FindOutPwdActivity.open(this, account, mBinding.edtCode.getText().toString().trim());
                    break;

                case RC_TRADE_PWD_MODIFY: // 修改资金密码
                    TradePwdActivity.open(this, TradePwdActivity.FiND_OUT, account, mBinding.edtCode.getText().toString().trim());
                    break;

            }


        });
    }

    private boolean check(String type) {

        if (mBinding.tlSignUP.getPosition() == LEFT){

            if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
                return false;
            }

        }else {

            if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_hint));
                return false;
            }

            if (!StringUtils.isEmail(mBinding.edtEmail.getText().toString().trim())){
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_email_hint2));
                return false;
            }

        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_code_hint));
                return false;
            }

        }

        return true;
    }

    @Override
    public void onLeftTabListener() {
        mBinding.edtMobile.requestFocus();

        mBinding.edtMobile.setVisibility(View.VISIBLE);
        mBinding.edtEmail.setVisibility(View.GONE);
    }

    @Override
    public void onRightTabListener() {
        mBinding.edtEmail.requestFocus();

        mBinding.edtMobile.setVisibility(View.GONE);
        mBinding.edtEmail.setVisibility(View.VISIBLE);
    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60,
                mBinding.edtCode.getSendCodeBtn(),
                R.drawable.btn_code_light_bg,
                R.drawable.btn_code_dark_bg,
                ContextCompat.getColor(this, R.color.white),
                ContextCompat.getColor(this, R.color.white)));

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
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCodePresenter != null) {
            mSendCodePresenter.clear();
        }
    }
}

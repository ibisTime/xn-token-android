package com.cdkj.token.user.login.signup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentSignUpStep1Binding;
import com.cdkj.token.interfaces.UserTabLayoutInterface;
import com.cdkj.token.model.SignUpInfoModel;
import com.cdkj.token.user.CountryCodeListActivity;
import com.cdkj.token.user.login.SignUpActivity2;

import static com.cdkj.token.views.UserTableLayout.LEFT;

/**
 * Created by cdkj on 2018/11/21.
 */

public class SignUpStep1Fragment extends BaseLazyFragment implements UserTabLayoutInterface, SendCodeInterface {

    private FragmentSignUpStep1Binding mBinding;

    private SendPhoneCodePresenter mSendCodePresenter;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static SignUpStep1Fragment getInstance() {
        SignUpStep1Fragment fragment = new SignUpStep1Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_sign_up_step1, null ,false);

        init();
        initView();
        initListener();

        return mBinding.getRoot();
    }

    private void init() {
        mSendCodePresenter = new SendPhoneCodePresenter(this, mActivity);
    }

    private void initView() {
        InputFilter[] filters = {new InputFilter.LengthFilter(6)};
        mBinding.edtCode.getEditText().setFilters(filters);

        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
    }

    private void initListener() {
        mBinding.tlSignUP.setInterface(this);

        //国家区号选择
        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(mActivity, true);
        });

        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (!check("code")) {
                return;
            }

            SendVerificationCode sendVerificationCode;
            if (mBinding.tlSignUP.getPosition() == LEFT){

                String phone = mBinding.edtMobile.getText().toString().trim();
                sendVerificationCode = new SendVerificationCode(
                        phone, "805045", "C", SPUtilHelper.getCountryInterCode());

            }else {
                String email = mBinding.edtEmail.getText().toString().trim();
                sendVerificationCode = new SendVerificationCode(
                        email, "805046", "C", SPUtilHelper.getCountryInterCode());

            }


            mSendCodePresenter.openVerificationActivity(sendVerificationCode);
        });

        mBinding.btnNext.setOnClickListener(view -> {
            if (!check("all")) {
                return;
            }

            SignUpActivity2 activity = (SignUpActivity2) getActivity();
            if (null != activity){

                String account;
                if (mBinding.tlSignUP.getPosition() == LEFT){
                    account = mBinding.edtMobile.getText().toString().trim();
                }else {
                    account = mBinding.edtEmail.getText().toString().trim();
                }

                SignUpInfoModel mSignUpInfoModel = activity.getSignUpInfo();
                mSignUpInfoModel.setAccount(account);
                mSignUpInfoModel.setSmsCaptcha(mBinding.edtCode.getText().toString().trim());
                mSignUpInfoModel.setCountryCode(SPUtilHelper.getCountryInterCode());

                activity.setCurrentItem(1);
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mBinding){
            mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {


    }


    private boolean check(String type) {

        if (mBinding.tlSignUP.getPosition() == LEFT){
            if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_mobile_hint));
                return false;
            }
        }else {
            if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_email_hint));
                return false;
            }

            if (!StringUtils.isEmail(mBinding.edtEmail.getText().toString().trim())){
                UITipDialog.showInfoNoIcon(mActivity, getString(R.string.user_email_hint2));
                return false;
            }
        }


        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_code_hint));
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
                ContextCompat.getColor(mActivity, R.color.white),
                ContextCompat.getColor(mActivity, R.color.white)));

    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showInfoNoIcon(mActivity, msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSendCodePresenter != null) {
            mSendCodePresenter.clear();
            mSendCodePresenter = null;
        }
    }
}

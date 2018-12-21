package com.cdkj.token.user.login.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentSignUpStep2Binding;
import com.cdkj.token.model.SignUpInfoModel;
import com.cdkj.token.user.login.SignUpActivity2;

/**
 * Created by cdkj on 2018/11/21.
 */

public class SignUpStep2Fragment extends BaseLazyFragment {

    private FragmentSignUpStep2Binding mBinding;

    private boolean isRule1Pass = false;
    private boolean isRule2Pass = false;
    private boolean isRule3Pass = false;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static SignUpStep2Fragment getInstance() {
        SignUpStep2Fragment fragment = new SignUpStep2Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_sign_up_step2, null ,false);

        initView();
        initListener();
        
        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtRePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }


    private void initListener() {

        mBinding.btnNext.setOnClickListener(view -> {
            if (!check()) {
                return;
            }

            SignUpActivity2 activity = (SignUpActivity2) getActivity();
            if (null != activity){

                SignUpInfoModel mSignUpInfoModel = activity.getSignUpInfo();
                mSignUpInfoModel.setLoginPwd(mBinding.edtRePassword.getText().toString().trim());

                activity.setCurrentItem(2);
            }

        });

        mBinding.edtPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable))
                    return;

                String pwd = editable.toString();
                if (pwd.equals(mBinding.edtRePassword.getText())){
                    isRule1Pass = checkRule1(pwd);
                    isRule2Pass = checkRule2(pwd);
                    isRule3Pass = checkRule3(pwd);
                } else {
                    isRule1Pass = false;
                    isRule2Pass = false;
                    isRule3Pass = false;
                }

                mBinding.srlRule1.changeRuleState(isRule1Pass);
                mBinding.srlRule2.changeRuleState(isRule2Pass);
                mBinding.srlRule3.changeRuleState(isRule3Pass);

                if (isRule1Pass){
                    mBinding.tvLevel.setText(R.string.sign_up_low);
                }

                if (isRule1Pass && isRule2Pass || isRule1Pass && isRule3Pass){
                    mBinding.tvLevel.setText(R.string.sign_up_mid);
                }

                if (isRule1Pass && isRule2Pass && isRule3Pass){
                    mBinding.tvLevel.setText(R.string.sign_up_high);
                }

                if (!isRule1Pass && !isRule2Pass && !isRule3Pass){
                    mBinding.tvLevel.setText("");
                }
            }
        });

        mBinding.edtRePassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable))
                    return;

                String pwd = editable.toString();
                if (pwd.equals(mBinding.edtPassword.getText())){
                    isRule1Pass = checkRule1(pwd);
                    isRule2Pass = checkRule2(pwd);
                    isRule3Pass = checkRule3(pwd);
                } else {
                    isRule1Pass = false;
                    isRule2Pass = false;
                    isRule3Pass = false;
                }

                mBinding.srlRule1.changeRuleState(isRule1Pass);
                mBinding.srlRule2.changeRuleState(isRule2Pass);
                mBinding.srlRule3.changeRuleState(isRule3Pass);

                if (isRule1Pass){
                    mBinding.tvLevel.setText(R.string.sign_up_low);
                }

                if (isRule1Pass && isRule2Pass || isRule1Pass && isRule3Pass){
                    mBinding.tvLevel.setText(R.string.sign_up_mid);
                }

                if (isRule1Pass && isRule2Pass && isRule3Pass){
                    mBinding.tvLevel.setText(R.string.sign_up_high);
                }

                if (!isRule1Pass && !isRule2Pass && !isRule3Pass){
                    mBinding.tvLevel.setText("");
                }

            }
        });
    }

    private boolean checkRule1(String pwd){

        String rule = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
        if (pwd.matches(rule)){
            return true;
        }else {
            return false;
        }

    }

    private boolean checkRule2(String pwd){

        char[] c = pwd.toCharArray();
        for(int i=0;i<c.length;i++){
            int asiiValue=(int)c[i];
            if(asiiValue >=97 && asiiValue<=122){
                return true;
            }

        }

        return false;
    }

    private boolean checkRule3(String pwd){

        char[] c = pwd.toCharArray();
        for(int i=0;i<c.length;i++){
            int asiiValue=(int)c[i];
            if(asiiValue >=65 && asiiValue<=90){
                return true;
            }
        }

        return false;
    }

    private boolean check(){
        if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_password_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_repassword_hint));
            return false;
        }

        if (!mBinding.edtRePassword.getText().toString().trim().equals(mBinding.edtPassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_repassword_two_hint));
            return false;
        }

        if (!isRule1Pass){
            UITipDialog.showInfoNoIcon(mActivity, getString(R.string.signup_pwd_rule1));
            return false;
        }

        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

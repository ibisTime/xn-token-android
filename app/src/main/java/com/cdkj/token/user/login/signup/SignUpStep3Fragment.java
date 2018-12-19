package com.cdkj.token.user.login.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentSignUpStep3Binding;
import com.cdkj.token.model.SignUpInfoModel;
import com.cdkj.token.user.login.SignUpActivity2;
import com.cdkj.token.user.login.SignUpSuccess;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/11/21.
 */

public class SignUpStep3Fragment extends BaseLazyFragment {

    private FragmentSignUpStep3Binding mBinding;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static SignUpStep3Fragment getInstance() {
        SignUpStep3Fragment fragment = new SignUpStep3Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_sign_up_step3, null ,false);

        initView();
        initListener();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.edtTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtReTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initListener() {
        mBinding.btnSuccess.setOnClickListener(view -> {
            if (check()){

                SignUpActivity2 activity = (SignUpActivity2) getActivity();

                if (null != activity){

                    SignUpInfoModel mSignUpInfoModel = activity.getSignUpInfo();
                    signUp(activity,mSignUpInfoModel);
                }

            }
        });

    }

    private boolean check(){
        if (TextUtils.isEmpty(mBinding.edtTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_password_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtReTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_repassword_hint));
            return false;
        }

        if (!mBinding.edtTradePassword.getText().toString().trim().equals(mBinding.edtReTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_repassword_two_hint));
            return false;
        }

        if (mBinding.edtTradePassword.getText().toString().trim().length() < 6) {
            UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_password_format_hint));
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

    private boolean isEmail(String account){
        return account.contains("@");
    }

    private void signUp(SignUpActivity2 activity, SignUpInfoModel mSignUpInfoModel) {
        Map<String, Object> map = new HashMap<>();
        map.put("kind", "C");
        map.put(isEmail(mSignUpInfoModel.getAccount()) ? "email" :"mobile", mSignUpInfoModel.getAccount());
        map.put("loginPwd", mSignUpInfoModel.getLoginPwd());
        map.put("smsCaptcha", mSignUpInfoModel.getSmsCaptcha());
        map.put("smsCaptcha", mSignUpInfoModel.getSmsCaptcha());
        map.put("tradePwd", mBinding.edtTradePassword.getText().toString().trim());
        map.put("companyCode", AppConfig.COMPANYCODE);
        map.put("countryCode", SPUtilHelper.getCountryCode());
        map.put("interCode", SPUtilHelper.getCountryInterCode());
        Call call = RetrofitUtils.createApi(MyApi.class).signUp(isEmail(mSignUpInfoModel.getAccount()) ? "805046" :"805045", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(activity) {

            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) || !TextUtils.isEmpty(data.getUserId())) {

                    SPUtilHelper.saveUserId(data.getUserId());
                    SPUtilHelper.saveUserToken(data.getToken());
                    if (isEmail(mSignUpInfoModel.getAccount())){
                        SPUtilHelper.saveUserEmail(mSignUpInfoModel.getAccount());
                    }else {
                        SPUtilHelper.saveUserPhoneNum(mSignUpInfoModel.getAccount());
                    }

                    OtherLibManager.uemProfileSignIn(data.getUserId());
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面

                    SignUpSuccess.open(mActivity);
                    activity.finish();
                } else {
                    UITipDialog.showInfoNoIcon(mActivity, getStrRes(R.string.user_sign_up_failure));
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}

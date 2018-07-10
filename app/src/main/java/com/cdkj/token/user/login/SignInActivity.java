package com.cdkj.token.user.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySignInBinding;
import com.cdkj.token.user.CountryCodeListActivity;

@Route(path = CdRouteHelper.APPLOGIN)
public class SignInActivity extends AbsBaseActivity implements LoginInterface {

    private boolean canOpenMain;

    private LoginPresenter mPresenter;
    private ActivitySignInBinding mBinding;

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
        intent.putExtra("canOpenMain", canOpenMain);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        UIStatusBarHelper.translucent(this);
        setTopLineState(true);
        setSubLeftImgState(true);

        mPresenter = new LoginPresenter(this);

        init();
        initEditInputType();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.tvCountryName.setText(SPUtilHelper.getCountry());
        mBinding.edtUsername.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryCode()));
    }

    private void initEditInputType() {
        mBinding.edtUsername.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void init() {
        if (getIntent() == null)
            return;

        canOpenMain = getIntent().getBooleanExtra("canOpenMain", false);
    }

    private void initListener() {
        //登录
        mBinding.btnConfirm.setOnClickListener(v -> {
            if (check()) {
                mPresenter.login(mBinding.edtUsername.getText().toString(), mBinding.edtPassword.getText().toString(), this);
            }

        });

        //找回密码
        mBinding.tvForget.setOnClickListener(v -> {
            FindPwdActivity.open(this, mBinding.edtUsername.getText().toString().trim());
        });

        //注册
        mBinding.tvToSignUp.setOnClickListener(view -> {
            SignUpActivity.open(this);
        });

        //国家区号选择
        mBinding.linLayoutCountryCode.setOnClickListener(view -> {
            CountryCodeListActivity.open(this,true);
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtUsername.getText().toString().trim())) {
            showToast(getStrRes(R.string.user_mobile_hint));
            return false;
        }

        if (mBinding.edtPassword.getText().toString().trim().length() < 6) {
            showToast(getStrRes(R.string.user_password_format_hint));
            return false;
        }

        return true;
    }


    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {

        SPUtilHelper.saveUserId(user.getUserId());
        SPUtilHelper.saveUserToken(user.getToken());
        SPUtilHelper.saveUserPhoneNum(mBinding.edtUsername.getText().toString().trim());

        MainActivity.open(this);
        finish();
    }

    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoading();
        showToast(msg);
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
    protected boolean canFinish() {
        if (canOpenMain) {
            MainActivity.open(this);
            finish();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (canOpenMain) {
            MainActivity.open(this);
            finish();
        } else {
            finish();
        }
    }


}

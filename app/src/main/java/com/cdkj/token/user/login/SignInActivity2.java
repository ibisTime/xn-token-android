package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.activitys.AppBuildTypeActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySignIn2Binding;
import com.cdkj.token.interfaces.UserTabLayoutInterface;
import com.cdkj.token.user.CountryCodeListActivity;

import static com.cdkj.token.views.UserTableLayout.LEFT;

/**
 * Created by cdkj on 2018/11/26.
 */
@Route(path = CdRouteHelper.APPLOGIN)
public class SignInActivity2 extends AbsStatusBarTranslucentActivity implements UserTabLayoutInterface, LoginInterface {

    private ActivitySignIn2Binding mBinding;

    private boolean skipToMain; // 登录动作结束后是否跳转到主页
    private LoginPresenter mPresenter;

    private int changeDevCount = 0;//用于记录研发或测试环境切换条件

    public static void open(Context context, boolean skipToMain) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignInActivity2.class);
        intent.putExtra(CdRouteHelper.DATASIGN, skipToMain);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in2,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setWhiteTitle();
        setMidTitle(R.string.activity_sign_in_title);
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
        mPresenter = new LoginPresenter(this);
        skipToMain = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);
    }


    private void initView() {
        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryInterCode()));
        mBinding.edtPwd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initListener(){

        mBinding.tlSignUP.setInterface(this);

        mBinding.btnSignIn.setOnClickListener(view -> {
            checkInputAndLogin();
        });

        mBinding.tvSignUP.setOnClickListener(view -> {
            SignUpActivity2.open(this);
        });

        mBinding.tvForget.setOnClickListener(view -> {
            ForgetPwdActivity.open(this,
                    mBinding.edtMobile.getText().toString().trim(),
                    mBinding.edtMobile.getText().toString().trim(),
                    ForgetPwdActivity.RC_LOGIN_PWD_FIND_OUT);
        });

        //国家区号选择
        mBinding.edtMobile.getLeftRootView().setOnClickListener(view -> {
            CountryCodeListActivity.open(this, true);
        });

        /**
         * 切换环境
         */
        if (LogUtil.isLog) {
            mBinding.imgTha.setVisibility(View.VISIBLE);
            mBinding.imgTha.setOnClickListener(view -> {
                changeDevCount++;
                if (changeDevCount > 8) {           //连续点击8次以上才可以切换环境
                    AppBuildTypeActivity.open(this);
                }
            });
        }

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

    /**
     * 检测输入并登录
     */
    private void checkInputAndLogin() {

        String loginName;
        if (mBinding.tlSignUP.getPosition() == LEFT){
            if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_mobile_hint));
                return;
            }
            loginName = mBinding.edtMobile.getText().toString().trim();
        }else {
            if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString().trim())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_email_hint));
                return;
            }

            if (!StringUtils.isEmail(mBinding.edtEmail.getText().toString().trim())){
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_email_hint2));
                return;
            }

            loginName = mBinding.edtEmail.getText().toString().trim();
        }

        if (mBinding.edtPwd.getText().toString().trim().length() < 6) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_format_hint));
            return;
        }

        mPresenter.login(loginName, mBinding.edtPwd.getText().toString().trim(), SPUtilHelper.getCountryInterCode(), this);
    }

    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {
        loginSuccessNext(user);
    }


    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoadingDialog();
        showToast(msg);
    }
    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
        disMissLoadingDialog();
    }

    /**
     * 登录成功下一步
     *
     * @param user
     */
    public void loginSuccessNext(UserLoginModel user) {
        SPUtilHelper.saveUserId(user.getUserId());
        SPUtilHelper.saveUserToken(user.getToken());
        SPUtilHelper.saveUserPhoneNum(mBinding.edtMobile.getText().toString().trim());
        SPUtilHelper.saveUserEmail(mBinding.edtEmail.getText().toString().trim());
        OtherLibManager.uemProfileSignIn(user.getUserId());

        if (skipToMain) {
            MainActivity.open(this);
        }
        finish();
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
}

package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.activitys.UpdatePhoneActivity;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserSecurityBinding;
import com.cdkj.token.user.login.SetLoginPwdActivity;
import com.cdkj.token.user.login.SignInActivity;
import com.cdkj.token.user.pattern_lock.PatternLockSettingActivity;
import com.cdkj.token.user.setting.UserLanguageActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户安全
 * Created by lei on 2017/11/1.
 */

public class UserSecurityActivity extends AbsStatusBarTranslucentActivity {

    private ActivityUserSecurityBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserSecurityActivity.class));
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_security, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(R.string.accounts_and_security);
        setPageBgImage(R.drawable.my_bg);
        initListener();

    }


    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {

        mBinding.switchView.setChecked(SPUtilHelper.isSetPatternPwd());

        mBinding.tvMail.setText(SPUtilHelper.getUserEmail());

        if (!SPUtilHelper.getGoogleAuthFlag()) { // 未打开谷歌验证
            mBinding.tvGoogle.setText(getStrRes(R.string.user_google_close));
        } else {
            mBinding.tvGoogle.setText(getStrRes(R.string.user_google_open));
        }


        if (SPUtilHelper.getLoginPwdFlag()) { // 未打开谷歌验证
            mBinding.tvPwdState.setText(getStrRes(R.string.user_setting_password));
        } else {
            mBinding.tvPwdState.setText(R.string.set_login_pwd);
        }


    }

    private void initListener() {

        //开启关闭手势密码
        mBinding.switchView.setOnClickListener(view -> {
            if (mBinding.switchView.isChecked()) {
                PatternLockSettingActivity.open(this);
            } else {
                SPUtilHelper.saveUserPatternPwd("");
            }
            mBinding.switchView.setChecked(false);
        });

        //资金密码
        mBinding.llTradePwd.setOnClickListener(view -> {
            PayPwdModifyActivity.open(this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
        });

//        mBinding.llIdentity.setOnClickListener(view -> {
//            if (SPUtilHelper.getRealName() == null || SPUtilHelper.getRealName().equals("")) {
//                AuthenticateActivity.open(this);
//            } else {
//                showToast(getStrRes(R.string.user_identity_success));
//            }
//        });

        //绑定邮箱
        mBinding.llMail.setOnClickListener(view -> {
//            UserEmailActivity.open(this, SPUtilHelper.getUserEmail());
        });

        //修改手机号
        mBinding.llMobile.setOnClickListener(view -> {
            UpdatePhoneActivity.open(this);
        });

        //登录密码
        mBinding.llPassword.setOnClickListener(view -> {

            SetLoginPwdActivity.open(this);

//            UpdateLoginPasswordActivity.open(this);

        });

        mBinding.llGoogle.setOnClickListener(view -> {
            if (!SPUtilHelper.getGoogleAuthFlag()) { // 未打开谷歌验证
                UserGoogleActivity.open(this, "open");
            } else {
                GoogleCodeSetActivity.open(this);
            }
        });


        mBinding.llLanguage.setOnClickListener(view -> {
            UserLanguageActivity.open(this);
        });


        mBinding.btnConfirm.setOnClickListener(view -> {
            showDoubleWarnListen(getStrRes(R.string.user_setting_sign_out) + "?", view1 -> {
                SPUtilHelper.logOutClear();
                OtherLibManager.uemProfileSignOff();
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                SignInActivity.open(UserSecurityActivity.this, true);
                finish();
            });
        });
    }

}

package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.activitys.UpdatePhoneActivity;
import com.cdkj.baselibrary.appmanager.OtherLibManager;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserSecurityBinding;
import com.cdkj.token.user.guide.GuideActivity;
import com.cdkj.token.user.login.ForgetPwdActivity;
import com.cdkj.token.user.pattern_lock.PatternLockSettingActivity;
import com.cdkj.token.user.setting.UserLanguageActivity;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * 账户安全
 * Created by lei on 2017/11/1.
 */

public class UserSecurityActivity extends AbsActivity {

    private ActivityUserSecurityBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserSecurityActivity.class));
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_security, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.accounts_and_security));
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
            ForgetPwdActivity.open(this,
                    SPUtilHelper.getUserPhoneNum(),
                    SPUtilHelper.getUserEmail(),
                    ForgetPwdActivity.RC_PAY_PWD_MODIFY);
        });

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

            ForgetPwdActivity.open(this,
                    SPUtilHelper.getUserPhoneNum(),
                    SPUtilHelper.getUserEmail(),
                    ForgetPwdActivity.RC_LOGIN_PWD_MODIFY);

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

                if ( WalletHelper.isUserAddedWallet(WalletHelper.WALLET_USER)){ // 有私钥钱包

                    SPUtilHelper.logOutClear();
                    OtherLibManager.uemProfileSignOff();
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                    MainActivity.open(UserSecurityActivity.this);
                    finish();

                }else { // 没有私钥钱包

                    SPUtilHelper.logOutClear();
                    OtherLibManager.uemProfileSignOff();
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                    GuideActivity.open(UserSecurityActivity.this);
                    finish();

                }


            });
        });
    }

}

package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreatePassWordBinding;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.password.PassWordLayout;

/**
 * 修改钱包密码
 * Created by cdkj on 2018/6/7.
 */

public class WalletPasswordModifyActivity extends AbsBaseLoadActivity {

    private ActivityCreatePassWordBinding mBinding;

    private String mPassWord;//用户输入的密码

    private boolean isCheckOldPassWordState;//是否处于检测旧密码状态

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletPasswordModifyActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_pass_word, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.modify_password);
        isCheckOldPassWordState = true;
        mBinding.tvTips.setText(R.string.please_check_transaction);
        initPassWordVIewListener();
    }

    private void initPassWordVIewListener() {

        mBinding.passWordLayout.passWordLayout.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {
            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {

                if (isCheckOldPassWordState) {              //检测旧密码状态
                    checkOldPassword(pwd);
                    return;
                }

                updatePassword(pwd);
            }
        });
    }

    /**
     * 检测旧密码
     *
     * @param pwd
     */
    private void checkOldPassword(String pwd) {
        if (WalletHelper.checkOldPasswordByUserId(pwd, SPUtilHelper.getUserId())) {
            isCheckOldPassWordState = false;
            mBinding.passWordLayout.passWordLayout.removeAllPwd();
            mBinding.tvTips.setText(R.string.please_set_transaction_password);
        } else {
            isCheckOldPassWordState = true;
            mBinding.passWordLayout.passWordLayout.removeAllPwd();
            UITipDialog.showFail(WalletPasswordModifyActivity.this, getString(R.string.transaction_password_error));
        }
    }

    /**
     * 修改密码
     *
     * @param pwd
     */
    private void updatePassword(String pwd) {
        if (TextUtils.isEmpty(mPassWord)) {
            mPassWord = pwd;
            mBinding.passWordLayout.passWordLayout.removeAllPwd();
            mBinding.tvTips.setText(R.string.please_check_transaction);
            return;
        }

        if (!TextUtils.equals(mPassWord, pwd)) { //两次输入密码不一致
            mPassWord = null;
            mBinding.passWordLayout.passWordLayout.removeAllPwd();
            mBinding.tvTips.setText(R.string.please_set_transaction_pass_word);
            UITipDialog.showInfo(WalletPasswordModifyActivity.this, getString(R.string.password_error));

        } else {               //两次密码输入一致
            try {
                WalletHelper.changeWalletPassWord(mPassWord, SPUtilHelper.getUserId());
                UITipDialog.showSuccess(WalletPasswordModifyActivity.this, getString(R.string.update_password_success), dialogInterface -> {
                    finish();
                });

            } catch (Exception e) {
                UITipDialog.showSuccess(WalletPasswordModifyActivity.this, getString(R.string.update_password_fail), dialogInterface -> {
                    finish();
                });
            }
        }
    }

}

package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreatePassWordBinding;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.views.password.PassWordLayout;

/**
 * 钱包密码验证
 * Created by cdkj on 2018/6/7.
 */

public class WalletBackupPasswordCheckActivity extends AbsBaseLoadActivity {

    private ActivityCreatePassWordBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletBackupPasswordCheckActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_pass_word, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);
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
                checkOldPassword(pwd);
            }
        });
    }

    /**
     * 检测旧密码
     *
     * @param pwd
     */
    private void checkOldPassword(String pwd) {
        if (TextUtils.equals(pwd, WalletHelper.getWalletPassword())) {
            WalletBackupStartActivity.open(this);
            finish();
        } else {
            mBinding.passWordLayout.passWordLayout.removeAllPwd();
            UITipDialog.showFail(WalletBackupPasswordCheckActivity.this, getString(R.string.transaction_password_error));
        }
    }


}

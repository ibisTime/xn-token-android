package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.wallet.AbsWalletPassWordCheckActivity;

/**
 * 钱包密码验证
 * Created by cdkj on 2018/6/7.
 */

public class WalletBackupPasswordCheckActivity extends AbsWalletPassWordCheckActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletBackupPasswordCheckActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);
        mBinding.tvTips.setText(R.string.please_check_transaction);
        initPassWordVIewListener();
    }


    @Override
    public void checkPassWord(boolean isSuccess) {
        if (isSuccess) {
            BackupWalletStartActivity.open(this);
            finish();
        } else {
            mBinding.passWordLayout.passWordLayout.removeAllPwd();
            UITipDialog.showFail(WalletBackupPasswordCheckActivity.this, getString(R.string.transaction_password_error));
        }
    }


}

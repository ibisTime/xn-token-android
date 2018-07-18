package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletBackupStartBinding;
import com.cdkj.token.wallet.create_guide.WalletHelpWordsShowActivity;

/**
 * 钱包备份开始界面
 * Created by cdkj on 2018/6/7.
 */

public class WalletBackupStartActivity extends AbsLoadActivity {

    private ActivityWalletBackupStartBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletBackupStartActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_backup_start, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);
        initClickListener();
    }

    private void initClickListener() {
        mBinding.btnNowBackup.setOnClickListener(view -> {
            WalletHelpWordsShowActivity.open(this,true);
            finish();
        });
    }
}

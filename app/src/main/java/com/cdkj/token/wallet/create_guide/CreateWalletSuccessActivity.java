package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreateWalletSuccessBinding;

/**
 * 创建钱包成功
 * Created by cdkj on 2018/6/6.
 */

public class CreateWalletSuccessActivity extends AbsLoadActivity {

    private ActivityCreateWalletSuccessBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreateWalletSuccessActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_wallet_success, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBinding.btnNowBackup.setOnClickListener(view -> {
            WalletHelpWordsShowActivity.open(this, false);
            finish();
        });
    }
}

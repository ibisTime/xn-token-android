package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserWalletBinding;
import com.cdkj.token.wallet.WalletPasswordModifyActivity;
import com.cdkj.token.wallet.backup_guide.WalletBackupStartActivity;

/**
 * Created by cdkj on 2018/5/26.
 */

public class UserWalletActivity extends AbsBaseActivity {

    private ActivityUserWalletBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserWalletActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_wallet, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getStrRes(R.string.user_title_wallet));
        setSubLeftImgState(true);

        initClickListener();

    }


    private void initClickListener() {
        mBinding.llBackUp.setOnClickListener(view -> WalletBackupStartActivity.open(this));
        mBinding.llModify.setOnClickListener(view -> WalletPasswordModifyActivity.open(this));
    }
}

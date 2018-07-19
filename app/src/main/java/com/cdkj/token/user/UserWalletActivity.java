package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserWalletBinding;
import com.cdkj.token.wallet.WalletPasswordModifyActivity;
import com.cdkj.token.wallet.backup_guide.WalletBackupPasswordCheckActivity;
import com.cdkj.token.wallet.delete_guide.WallteDeleteStartActivity;
import com.cdkj.token.wallet.export.WalletExportPasswordCheckActivity;

/**
 * Created by cdkj on 2018/5/26.
 */

public class UserWalletActivity extends AbsLoadActivity {

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

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.wallet_tool));

        mBaseBinding.titleView.setLeftTitle("");

        mBaseBinding.titleView.setLeftImg(R.drawable.back_black);

        initClickListener();
    }


    private void initClickListener() {
        mBinding.llBackUp.setOnClickListener(view -> WalletBackupPasswordCheckActivity.open(this));
        mBinding.llModify.setOnClickListener(view -> WalletPasswordModifyActivity.open(this));
        mBinding.btnDelete.setOnClickListener(view -> WallteDeleteStartActivity.open(this));
        mBinding.llOut.setOnClickListener(view -> WalletExportPasswordCheckActivity.open(this));
    }
}

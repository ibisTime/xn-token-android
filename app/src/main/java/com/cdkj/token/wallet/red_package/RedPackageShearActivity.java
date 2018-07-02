package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityRedPackageShearBinding;


/**
 * 红包分享
 */
public class RedPackageShearActivity extends AbsBaseLoadActivity {
    ActivityRedPackageShearBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPackageShearActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_red_package_shear, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
mBinding.llOpen.setOnClickListener(view -> RedPackageHistoryActivity.open(this));
    }
}

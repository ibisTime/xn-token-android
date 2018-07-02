package com.cdkj.token.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityLocalCoinTypeBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

/**
 * 本地币种选择
 * Created by cdkj on 2018/7/2.
 */

public class LocalCoinTypeChooseActivity extends AbsBaseActivity {

    public ActivityLocalCoinTypeBinding mBinding;

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_local_coin_type, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        //选择人民币
        mBinding.linLayoutCny.setOnClickListener(view -> {
            SPUtilHelper.saveLocalCoinType(WalletHelper.LOCAL_COIN_CNY);
        });

        //选择美元
        mBinding.linLayoutUsd.setOnClickListener(view -> {
            SPUtilHelper.saveLocalCoinType(WalletHelper.LOCAL_COIN_USD);
        });
    }
}

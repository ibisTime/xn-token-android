package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityLocalCoinTypeBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * 本地币种选择
 * Created by cdkj on 2018/7/2.
 */

public class LocalCoinTypeChooseActivity extends AbsStatusBarTranslucentActivity {

    public ActivityLocalCoinTypeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, LocalCoinTypeChooseActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_local_coin_type, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setMidTitle(R.string.local_coin);
        setPageBgImage(R.drawable.my_bg);

        if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_CNY)) {
            showCny();
        } else {
            showUsd();
        }

        //选择人民币
        mBinding.linLayoutCny.setOnClickListener(view -> {
            if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_CNY)) {
                return;
            }
            showCny();
            SPUtilHelper.saveLocalCoinType(WalletHelper.LOCAL_COIN_CNY);
            finishActivity();
        });

        //选择美元
        mBinding.linLayoutUsd.setOnClickListener(view -> {
            if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_USD)) {
                return;
            }
            showUsd();
            SPUtilHelper.saveLocalCoinType(WalletHelper.LOCAL_COIN_USD);
            finishActivity();
        });
    }

    void showUsd() {
        mBinding.imgUsd.setVisibility(View.VISIBLE);
        mBinding.imgCny.setVisibility(View.GONE);
    }

    void showCny() {
        mBinding.imgCny.setVisibility(View.VISIBLE);
        mBinding.imgUsd.setVisibility(View.GONE);
    }

    public void finishActivity() {
        EventBus.getDefault().post(new AllFinishEvent());
        //刷新界面
        MainActivity.open(this);
        finish();
    }
}

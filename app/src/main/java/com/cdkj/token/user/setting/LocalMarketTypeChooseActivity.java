package com.cdkj.token.user.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityLocalCoinTypeBinding;

import org.greenrobot.eventbus.EventBus;

/**
 * 本地行情币种选择
 * USD
 * CNY
 * KRW
 * Created by cdkj on 2018/7/2.
 */

public class LocalMarketTypeChooseActivity extends AbsStatusBarTranslucentActivity {

    public ActivityLocalCoinTypeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, LocalMarketTypeChooseActivity.class);
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

        LocalMarketTypePresenter localMarketTypePresenter = new LocalMarketTypePresenter();

        if (AppConfig.isLocalMarketCNY(SPUtilHelper.getLocalMarketSymbol())) {
            showCny();
        } else if (AppConfig.isLocalMarketUSD(SPUtilHelper.getLocalMarketSymbol())) {
            showUsd();
        } else {
            showKRW();
        }

        //选择人民币
        mBinding.linLayoutCny.setOnClickListener(view -> {
            localMarketTypePresenter.changeMarketType(AppConfig.LOCAL_MARKET_CNY, () -> {
                showCny();
                finishActivity();
            });
        });

        //选择美元
        mBinding.linLayoutUsd.setOnClickListener(view -> {
            localMarketTypePresenter.changeMarketType(AppConfig.LOCAL_MARKET_USD, () -> {
                showUsd();
                finishActivity();
            });
        });

        //选择韩元
        mBinding.linLayoutUsd.setOnClickListener(view -> {
            localMarketTypePresenter.changeMarketType(AppConfig.LOCAL_MARKET_KRW, () -> {
                showKRW();
                finishActivity();
            });
        });
    }

    void showUsd() {
        mBinding.imgUsd.setVisibility(View.VISIBLE);
        mBinding.imgCny.setVisibility(View.GONE);
        mBinding.imgKrw.setVisibility(View.GONE);
    }

    void showCny() {
        mBinding.imgCny.setVisibility(View.VISIBLE);
        mBinding.imgUsd.setVisibility(View.GONE);
        mBinding.imgKrw.setVisibility(View.GONE);
    }

    void showKRW() {
        mBinding.imgCny.setVisibility(View.GONE);
        mBinding.imgUsd.setVisibility(View.GONE);
        mBinding.imgKrw.setVisibility(View.VISIBLE);
    }

    public void finishActivity() {
        EventBus.getDefault().post(new AllFinishEvent());
        //刷新界面
        MainActivity.open(this);
        finish();
    }
}

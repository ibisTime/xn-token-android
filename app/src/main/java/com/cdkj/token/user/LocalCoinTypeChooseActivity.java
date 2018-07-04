package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityLocalCoinTypeBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.greenrobot.eventbus.EventBus;

import static com.cdkj.baselibrary.appmanager.EventTags.EVENT_REFRESH_LANGUAGE;

/**
 * 本地币种选择
 * Created by cdkj on 2018/7/2.
 */

public class LocalCoinTypeChooseActivity extends AbsBaseActivity {

    public ActivityLocalCoinTypeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, LocalCoinTypeChooseActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_local_coin_type, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTitle(getString(R.string.local_coin));

        if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_CNY)) {
            mBinding.imgCny.setBackgroundResource(R.mipmap.choice_confirm);
            mBinding.imgUsd.setBackgroundResource(R.mipmap.deal_unchoose);
        } else {
            mBinding.imgUsd.setBackgroundResource(R.mipmap.choice_confirm);
            mBinding.imgCny.setBackgroundResource(R.mipmap.deal_unchoose);
        }

        //选择人民币
        mBinding.linLayoutCny.setOnClickListener(view -> {
            mBinding.imgCny.setBackgroundResource(R.mipmap.choice_confirm);
            mBinding.imgUsd.setBackgroundResource(R.mipmap.deal_unchoose);
            SPUtilHelper.saveLocalCoinType(WalletHelper.LOCAL_COIN_CNY);
            finishActivity();
        });

        //选择美元
        mBinding.linLayoutUsd.setOnClickListener(view -> {
            mBinding.imgUsd.setBackgroundResource(R.mipmap.choice_confirm);
            mBinding.imgCny.setBackgroundResource(R.mipmap.deal_unchoose);
            SPUtilHelper.saveLocalCoinType(WalletHelper.LOCAL_COIN_USD);
            finishActivity();
        });
    }

    public void finishActivity() {
        EventBusModel model = new EventBusModel();
        model.setTag(EVENT_REFRESH_LANGUAGE);
        EventBus.getDefault().post(model);
        //刷新界面
        MainActivity.open(this);
    }
}

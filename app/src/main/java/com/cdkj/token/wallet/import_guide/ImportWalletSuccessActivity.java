package com.cdkj.token.wallet.import_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityImportWalletSuccessBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.greenrobot.eventbus.EventBus;

/**
 * 创建钱包成功
 * Created by cdkj on 2018/6/6.
 */

public class ImportWalletSuccessActivity extends AbsLoadActivity {

    private ActivityImportWalletSuccessBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ImportWalletSuccessActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_import_wallet_success, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initClickListener();
    }

    private void initClickListener() {
        mBinding.btnNowUse.setOnClickListener(view -> {

            EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
            MainActivity.open(this);
            finish();
        });

    }
}

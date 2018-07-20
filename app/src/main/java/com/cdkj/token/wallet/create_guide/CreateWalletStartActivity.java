package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreateWalletStartBinding;
import com.cdkj.token.databinding.ActivityImportStartBinding;

/**
 * 创建钱包开始界面
 * Created by cdkj on 2018/7/20.
 */

public class CreateWalletStartActivity extends AbsLoadActivity {

    private ActivityCreateWalletStartBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreateWalletStartActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_wallet_start, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.title_bg_blue));

        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(R.string.create_wallet);

        initClickListener();

    }

    private void initClickListener() {

    }
}

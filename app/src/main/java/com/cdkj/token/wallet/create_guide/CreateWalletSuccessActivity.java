package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreateWalletSuccess2Binding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.wallet.backup_guide.BackupWalletActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * 创建钱包成功
 * Created by cdkj on 2018/6/6.
 */

public class CreateWalletSuccessActivity extends AbsStatusBarTranslucentActivity {

    private ActivityCreateWalletSuccess2Binding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreateWalletSuccessActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_wallet_success2, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setMidTitle(R.string.wallet_backup_memonic);
        setWhiteTitle();
        setPageBgImage(R.mipmap.app_page_bg_new);

        //立即备份
        mBinding.btnNowBackup.setOnClickListener(view -> {
//            BackupWalletStartActivity.open(this, false);
            BackupWalletActivity.open(this,false);
            finish();
        });

        //稍后备份
        mBinding.btnLater.setOnClickListener(view -> {

            WalletDBModel walletDBModel = JSON.parseObject(SPUtilHelper.getWalletCache(), WalletDBModel.class);
            if (walletDBModel.save()){
                SPUtilHelper.createWalletCache("");  //清除缓存
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                MainActivity.open(CreateWalletSuccessActivity.this);
                finish();

            } else {
                showSureDialog(getString(R.string.wallet_create_fail), null);
            }

        });
    }

    @Override
    public void onBackPressed() {

    }
}

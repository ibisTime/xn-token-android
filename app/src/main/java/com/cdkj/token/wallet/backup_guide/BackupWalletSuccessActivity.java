package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityBackupWalletSuccessBinding;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by cdkj on 2018/11/27.
 */

public class BackupWalletSuccessActivity extends AbsStatusBarTranslucentActivity {

    public final static int CREATE = 0;
    public final static int RECOVER = 1;

    private ActivityBackupWalletSuccessBinding mBinding;

    private int fromWay;

    public static void open(Context context, int fromWay) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletSuccessActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, fromWay);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_backup_wallet_success,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setWhiteTitle();
        setPageBgImage(R.mipmap.app_page_bg_new);

        init();
        initView();
        initListener();
    }

    private void init(){
        fromWay = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);
    }

    private void initView() {

        if (fromWay == CREATE){
            mBinding.tvTip.setText(R.string.wallet_back_up_hint);
        } else {
            mBinding.tvTip.setText(R.string.wallet_recover_hint);
        }

    }

    private void initListener() {
        mBinding.btnSuccess.setOnClickListener(view -> {
            EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
            MainActivity.open(this);
            finish();
        });
    }

}

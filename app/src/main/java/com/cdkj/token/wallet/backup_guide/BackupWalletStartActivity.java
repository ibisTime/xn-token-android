package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.WindowManager;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.HelpWordsGridAdapter;
import com.cdkj.token.databinding.ActivityBackupWalletStartBinding;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.recycler.GridDivider;

import org.spongycastle.asn1.esf.SPuri;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包备份开始界面
 * Created by cdkj on 2018/6/7.
 */

public class BackupWalletStartActivity extends AbsLoadActivity {

    private ActivityBackupWalletStartBinding mBinding;
    private boolean isFromBackup;

    /**
     * @param context
     * @param isFromBackup 是否来自备份界面
     */
    public static void open(Context context, boolean isFromBackup) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletStartActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromBackup);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
//        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_backup_start, null, false);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_backup_wallet_start, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        //禁止截屏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        setStatusBarBlue();

        setTitleBgBlue();

        isFromBackup = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);

        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);

        initClickListener();

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        setWordsData();
    }

    /**
     * 设置数据显示
     */
    void setWordsData() {
        List<String> wordsList = WalletHelper.getHelpWordsListByUserId(SPUtilHelper.getUserId());
        if (wordsList != null && wordsList.size() > 0) {
            mBinding.recyclerView.addItemDecoration(new GridDivider(this, DisplayHelper.dp2px(this, 1), ContextCompat.getColor(this, R.color.gray_dee0e5)));
            mBinding.recyclerView.setAdapter(new HelpWordsGridAdapter(wordsList));
        }
    }

    private void initClickListener() {

        mBinding.btnNext.setOnClickListener(view -> {
            BackupWalletWordsCheckActivity.open(this,isFromBackup);
            finish();
        });
    }


}

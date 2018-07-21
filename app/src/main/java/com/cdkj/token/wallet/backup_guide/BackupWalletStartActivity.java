package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.adapter.HelpWordsGridAdapter;
import com.cdkj.token.databinding.ActivityBackupWalletStartBinding;
import com.cdkj.token.views.recycler.GridDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包备份开始界面
 * Created by cdkj on 2018/6/7.
 */

public class BackupWalletStartActivity extends AbsLoadActivity {

    private ActivityBackupWalletStartBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletStartActivity.class);
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

        setStatusBarBlue();

        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);
        initClickListener();

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mBinding.recyclerView.addItemDecoration(new GridDivider(this,10, ContextCompat.getColor(this,R.color.red)));

        List<String> strings = new ArrayList<>();

        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("d");
        strings.add("dddddd");
        strings.add("ffffffffd");

        mBinding.recyclerView.setAdapter(new HelpWordsGridAdapter(strings));


    }

    private void initClickListener() {

//        mBinding.btnNowBackup.setOnClickListener(view -> {
//            WalletHelpWordsShowActivity.open(this, true);
//            finish();
//        });
    }
}

package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.HelpWordsGridAdapter;
import com.cdkj.token.databinding.ActivityBackupWalletStartBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.dialogs.BackupPromptDialog;
import com.cdkj.token.views.recycler.GridDivider;

import java.util.List;

/**
 * 钱包备份开始界面
 * Created by cdkj on 2018/6/7.
 */

public class BackupWalletStartActivity extends AbsLoadActivity {

    private ActivityBackupWalletStartBinding mBinding;
    private boolean isFromWalletToolBackup;


    /**
     * @param context
     * @param isFromWalletToolBackup 是否来自钱包工具备份界面
     */
    public static void open(Context context, boolean isFromWalletToolBackup) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletStartActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromWalletToolBackup);
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

        isFromWalletToolBackup = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);

        mBaseBinding.titleView.setMidTitle(R.string.wallet_backup);

        initClickListener();

        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        setWordsData();

        //截图提示弹框
        new BackupPromptDialog(this).show();
    }

    /**
     * 设置数据显示
     */
    void setWordsData() {

        List<String> wordsList = getWordsisFromBackup();

        if (wordsList != null && wordsList.size() > 0) {
            mBinding.recyclerView.addItemDecoration(new GridDivider(this, DisplayHelper.dp2px(this, 1), ContextCompat.getColor(this, R.color.gray_dee0e5)));
            mBinding.recyclerView.setAdapter(new HelpWordsGridAdapter(wordsList));
        }
    }


    /**
     * 不同界面获取不同数据
     *
     * @return
     */
    public List<String> getWordsisFromBackup() {
        if (isFromWalletToolBackup) {
            return WalletHelper.getHelpWordsListByUserId(WalletHelper.WALLET_USER);
        }

        WalletDBModel walletDBModel = JSON.parseObject(SPUtilHelper.getWalletCache(), WalletDBModel.class);

        if (walletDBModel == null) return null;

        return StringUtils.splitAsList(walletDBModel.getHelpWordsEn(), WalletHelper.HELPWORD_SPACE_SYMBOL);
    }


    private void initClickListener() {

        mBinding.btnNext.setOnClickListener(view -> {
//            BackupWalletWordsCheckActivity.open(this, isFromWalletToolBackup);
            finish();
        });
    }


}

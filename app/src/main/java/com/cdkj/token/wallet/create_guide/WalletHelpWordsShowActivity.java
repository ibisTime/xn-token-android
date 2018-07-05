package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletWordsShowBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.List;

import static com.cdkj.baselibrary.utils.StringUtils.SPACE_SYMBOL;

/**
 * 助记词显示
 * Created by cdkj on 2018/6/6.
 */

public class WalletHelpWordsShowActivity extends AbsBaseLoadActivity {

    private ActivityWalletWordsShowBinding mBinding;

    private boolean isFromBackup;

    /**
     * @param context
     * @param isFromBackup 是否来自备份界面
     */
    public static void open(Context context, boolean isFromBackup) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletHelpWordsShowActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromBackup);
        context.startActivity(intent);
    }


    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_words_show, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        isFromBackup = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);
        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg(getString(R.string.dont_screenshot))
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), null);

        commonDialog.show();

        List<String> wordList = WalletHelper.getHelpWordsListByUserId(SPUtilHelper.getUserId());

        if (wordList != null) {
            mBinding.tvWords.setText(StringUtils.listToString(wordList, SPACE_SYMBOL));
        }

        mBinding.btnNowBackup.setOnClickListener(view -> {
            WalletBackupCheckActivity.open(this, isFromBackup);
            finish();
        });
    }
}

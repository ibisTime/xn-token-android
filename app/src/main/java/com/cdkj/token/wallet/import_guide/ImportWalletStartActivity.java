package com.cdkj.token.wallet.import_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityImportStartBinding;

/**
 * 导入开始界面
 * Created by cdkj on 2018/7/20.
 */

public class ImportWalletStartActivity extends AbsLoadActivity {

    private ActivityImportStartBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ImportWalletStartActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_import_start, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.title_bg_blue));

        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(R.string.import_wallet);

        initClickListener();

    }

    private void initClickListener() {

    }
}

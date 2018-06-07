package com.cdkj.token.wallet.delete_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityDeleteWallteStartBinding;

/**
 * 钱包删除
 * Created by cdkj on 2018/6/7.
 */

public class WallteDeleteStartActivity extends AbsBaseLoadActivity {

    private ActivityDeleteWallteStartBinding mBindng;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WallteDeleteStartActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBindng = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_delete_wallte_start, null, false);
        return mBindng.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.delete_wallet);

        mBindng.btnNowCheck.setOnClickListener(view -> {
            WalletDeleteWordsInputActivity.open(this);
            finish();
        });
    }
}

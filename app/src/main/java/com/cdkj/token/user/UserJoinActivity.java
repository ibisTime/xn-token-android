package com.cdkj.token.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserJoinBinding;
import com.cdkj.token.wallet.coin_detail.WalletAddressShowActivity;

/**
 * Created by cdkj on 2018/5/26.
 */

public class UserJoinActivity extends AbsBaseActivity {

    private ActivityUserJoinBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserJoinActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_join, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_join));
        setSubLeftImgState(true);
        mBinding.tvFacebook.setText("@THAWallet");
        mBinding.tvTwitter.setText("@THAWallet");

        initClickListener();
    }

    private void initClickListener() {

        mBinding.llFacebook.setOnClickListener(v -> copyText(mBinding.tvFacebook.getText().toString()));

        mBinding.llTwitter.setOnClickListener(v -> copyText(mBinding.tvTwitter.getText().toString()));

        mBinding.llTelegram.setOnClickListener(v -> copyText(mBinding.tvTelegram.getText().toString()));

        mBinding.llWechat.setOnClickListener(v -> copyText(mBinding.tvWechat.getText().toString()));

    }


    private void copyText(String text) {
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, text);
            cmb.setPrimaryClip(clipData);
            ToastUtil.show(UserJoinActivity.this, getString(R.string.copy_success));
        }
    }

}

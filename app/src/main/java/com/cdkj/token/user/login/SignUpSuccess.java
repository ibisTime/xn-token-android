package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySignUpSuccessBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

/**
 * Created by cdkj on 2018/11/26.
 */

public class SignUpSuccess extends AbsStatusBarTranslucentActivity {

    private ActivitySignUpSuccessBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignUpSuccess.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_up_success,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        sheShowTitle(false);
        setPageBgImage(R.mipmap.app_page_bg_new);

        initListener();
    }

    private void initListener() {
        mBinding.btnNext.setOnClickListener(view -> {
            WalletHelper.checkLastVersionWalletUser();

            MainActivity.open(this);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // do notion
    }
}

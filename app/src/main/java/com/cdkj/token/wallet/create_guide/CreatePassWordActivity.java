package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreatePassWordBinding;
import com.cdkj.token.views.password.PassWordLayout;

/**
 * 创建密码
 * Created by cdkj on 2018/6/5.
 */

public class CreatePassWordActivity extends AbsBaseLoadActivity {

    private ActivityCreatePassWordBinding mBinding;

    private String mPassWord;//用户输入的密码


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreatePassWordActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_pass_word, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.create_wallet);
        initPassWordVIewListener();
    }

    private void initPassWordVIewListener() {

        mBinding.passWordLayout.passWordLayout.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {
            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {
                if (TextUtils.isEmpty(mPassWord)) {
                    mPassWord = pwd;
                    mBinding.passWordLayout.passWordLayout.removeAllPwd();
                    mBinding.tvTips.setText(R.string.please_check_transaction);
                    return;
                }

                if (!TextUtils.equals(mPassWord, pwd)) { //两次输入密码不一致
                    mPassWord = "";
                    mBinding.passWordLayout.passWordLayout.removeAllPwd();
                    mBinding.tvTips.setText(R.string.please_set_transaction_pass_word);
                    UITipDialog.showInfo(CreatePassWordActivity.this, getString(R.string.password_error));

                } else {               //两次密码输入一致

                }

            }
        });

    }
}

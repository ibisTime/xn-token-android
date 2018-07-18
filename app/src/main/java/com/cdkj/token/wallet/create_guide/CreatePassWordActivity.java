package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreatePassWordBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.password.PassWordLayout;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建密码
 * Created by cdkj on 2018/6/5.
 */

public class CreatePassWordActivity extends AbsLoadActivity {

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
                    createMnemonicWordsAsyn(pwd);
                }
            }
        });

    }

    /**
     * 异步创建助记词并保存
     *
     * @param password
     */
    public void createMnemonicWordsAsyn(String password) {
        showLoadingDialog();
        mSubscription.add(
                Observable.just(password)
                        .subscribeOn(Schedulers.newThread())
                        .map(pass -> {

                            WalletDBModel walletDBModel2 = WalletHelper.createEthPrivateKey();

                            walletDBModel2.setWalletPassWord(WalletHelper.encrypt(pass));  //TODO 缺少BTC

                            walletDBModel2.setUserId(SPUtilHelper.getUserId());

                            return walletDBModel2.save(); //
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> disMissLoading())
                        .subscribe(isSave -> {
                            if (isSave) {
                                CreateWalletSuccessActivity.open(CreatePassWordActivity.this);
                            } else {
                                ToastUtil.show(this, getString(R.string.wallet_create_fail));
                            }
                            finish();
                        }, throwable -> {
                            ToastUtil.show(this, getString(R.string.wallet_create_fail));
                            finish();
                        })
        );

    }

}

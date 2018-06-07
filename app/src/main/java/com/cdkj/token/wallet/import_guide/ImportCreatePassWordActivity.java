package com.cdkj.token.wallet.import_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreatePassWordBinding;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.views.password.PassWordLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建密码 （导入钱包）
 * Created by cdkj on 2018/6/5.
 */

public class ImportCreatePassWordActivity extends AbsBaseLoadActivity {

    private ActivityCreatePassWordBinding mBinding;

    private String mPassWord;//用户输入的密码

    private List<String> mWords;


    /**
     * @param context
     * @param words   用户输入的助记词
     */
    public static void open(Context context, ArrayList<String> words) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ImportCreatePassWordActivity.class);
        intent.putStringArrayListExtra(CdRouteHelper.DATASIGN, words);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_pass_word, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.import_wallet);
        mWords = new ArrayList<>();

        mWords = getIntent().getStringArrayListExtra(CdRouteHelper.DATASIGN);

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
                    UITipDialog.showInfo(ImportCreatePassWordActivity.this, getString(R.string.password_error));

                } else {               //两次密码输入一致
                    createMnemonicWordsAsyn(pwd);
                }
            }
        });
    }

    /**
     * 异步创建助记词并保存
     *
     * @param paword
     */
    public void createMnemonicWordsAsyn(String paword) {
        showLoadingDialog();
        mSubscription.add(
                Observable.just(paword)
                        .subscribeOn(Schedulers.newThread())
                        .map(password -> WalletHelper.createWalletInfobyMnenonic(mWords))
                        .filter(walletDBModel -> walletDBModel != null)
                        .map(walletDBModel -> {
                            walletDBModel.setPassWord(paword);
                            return walletDBModel.save();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> disMissLoading())
                        .subscribe(isSave -> {
                            if (isSave) {
                                ImportWalletSuccessActivity.open(ImportCreatePassWordActivity.this);
                            } else {
                                ToastUtil.show(this, getString(R.string.wallet_import_fail));
                            }
                            finish();
                        }, throwable -> {
                            ToastUtil.show(this, getString(R.string.wallet_import_fail));
                            finish();
                        })
        );

    }


}

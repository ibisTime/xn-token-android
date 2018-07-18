package com.cdkj.token.wallet.import_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletWordsCheckInputBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 钱包导入单词验证
 * Created by cdkj on 2018/6/6.
 */

public class WalletImportWordsInputActivity extends AbsLoadActivity {

    private ActivityWalletWordsCheckInputBinding mBinding;
    private ArrayList<String> words;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletImportWordsInputActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_words_check_input, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.import_wallet);
        initClickListener();
    }


    private void initClickListener() {

        mBinding.btnNowCheck.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mBinding.editWords.getText().toString())) {

                UITipDialog.showInfo(this, getString(R.string.please_input_help_words));

                return;
            }

            showLoadingDialog();
            mSubscription.add(
                    Observable.just(StringUtils.mergeSpace(mBinding.editWords.getText().toString().trim()))
                            .subscribeOn(Schedulers.newThread())
                            .map(s -> {
                                words = StringUtils.splitAsArrayList(s, StringUtils.SPACE_SYMBOL);
                                return words;
                            })
                            .map(wds -> WalletHelper.checkMnenonic(wds))

                            .observeOn(AndroidSchedulers.mainThread())

                            .doOnComplete(() -> disMissLoading())

                            .subscribe(isPass -> {
                                if (isPass) {
                                    ImportCreatePassWordActivity.open(this, words);
                                } else {
                                    UITipDialog.showFail(WalletImportWordsInputActivity.this, getString(R.string.check_words_fail));
                                }

                            }, throwable -> {
                                UITipDialog.showFail(WalletImportWordsInputActivity.this, getString(R.string.check_words_fail));
                            })


            );

        });

    }


}

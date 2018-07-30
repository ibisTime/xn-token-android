package com.cdkj.token.wallet.import_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityImportStartBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.user.WebViewImgBgActivity;
import com.cdkj.token.utils.ThaAppConstant;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.baselibrary.utils.StringUtils.stringFilter;

/**
 * 导入开始界面
 * Created by cdkj on 2018/7/20.
 */

public class ImportWalletStartActivity extends AbsLoadActivity {

    private ActivityImportStartBinding mBinding;

    private ArrayList<String> mWordList;
    private String mPassword;

    private boolean isAggree = true; //同意隐私条款

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

        setStatusBarBlue();
        setTitleBgBlue();

        initEditText();

        mBaseBinding.titleView.setMidTitle(R.string.import_wallet);

        initClickListener();

    }

    void initEditText() {
        mBinding.editPassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mBinding.editRepassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mBinding.editPassword.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mBinding.editRepassword.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

        mBinding.editWalletName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mBinding.editWalletName.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    mBinding.editWalletName.getEditText().setText(str);
                    //设置新的光标所在位置
                    mBinding.editWalletName.getEditText().setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void initClickListener() {

        //同意阅读
        mBinding.fraLayoutAggree.setOnClickListener(view -> {
            if (isAggree) {
                mBinding.imgAggree.setImageResource(R.drawable.deal_unchoose);
            } else {
                mBinding.imgAggree.setImageResource(R.drawable.deal_choose);
            }
            isAggree = !isAggree;
        });

        //导入
        mBinding.btnImport.setOnClickListener(view -> {

            mPassword = mBinding.editPassword.getText();

            if (checkCanPass()) return;

            checkWordsAndSave();

        });

        //隐私协议
        mBinding.tvRead.setOnClickListener(view -> {
            WebViewImgBgActivity.openkey(this, getString(R.string.privacy_agreement), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_PRIVACY));
        });

        //什么是助记词
        mBinding.tvWahtIsWords.setOnClickListener(view -> {
            WebViewImgBgActivity.openkey(this, getString(R.string.what_is_the_words), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_MNEMONIC));
        });


    }

    /**
     * 检测输入状态
     *
     * @return
     */
    private boolean checkCanPass() {
        if (TextUtils.isEmpty(mBinding.editWords.getText().toString())) {
            UITipDialog.showInfoNoIcon(this, getString(R.string.please_input_help_words));
            return true;
        }
        if (TextUtils.isEmpty(mBinding.editWalletName.getText().toString())) {
            UITipDialog.showInfoNoIcon(this, getString(R.string.wallet_name_input_hint));
            return true;
        }

        if (TextUtils.isEmpty(mPassword)) {
            UITipDialog.showInfoNoIcon(this, getString(R.string.user_password_hint));
            return true;
        }
        if (mPassword.length() < 6) {
            UITipDialog.showInfoNoIcon(this, getString(R.string.user_password_format_hint));
            return true;
        }

        if (TextUtils.isEmpty(mBinding.editRepassword.getText())) {
            UITipDialog.showInfoNoIcon(this, getString(R.string.activity_find_repassword_hint));
            return true;
        }

        if (!TextUtils.equals(mPassword, mBinding.editRepassword.getText())) { //两次输入密码不一致
            UITipDialog.showInfoNoIcon(ImportWalletStartActivity.this, getString(R.string.password_error));
            return true;
        }

        if (!isAggree) {
            UITipDialog.showInfoNoIcon(ImportWalletStartActivity.this, getString(R.string.please_read_and_aggree));
            return true;
        }
        return false;
    }

    /**
     * 验证组记词
     */
    private void checkWordsAndSave() {
        showLoadingDialog();
        mSubscription.add(
                Observable.just(StringUtils.mergeSpace(mBinding.editWords.getText().toString().trim()))
                        .subscribeOn(Schedulers.newThread())
                        .map(s -> StringUtils.mergeSpace(s))
                        .map(s -> {
                            mWordList = StringUtils.splitAsArrayList(s, StringUtils.SPACE_SYMBOL);
                            return mWordList;
                        })
                        .map(wds -> WalletHelper.checkMnenonic(wds))
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .filter(isPass -> {
                            if (!isPass) {
                                UITipDialog.showFail(this, getString(R.string.wallet_import_fail));
                            }
                            return isPass == true;
                        })
                        .subscribeOn(Schedulers.newThread())
                        .map(isPass -> {
                            WalletDBModel dbModel2 = WalletHelper.createEthAndWanPrivateKeybyMnenonic(mWordList); //TODO 导入缺少BTC
                            dbModel2.setWalletPassWord(WalletHelper.encrypt(mPassword));
                            dbModel2.setUserId(SPUtilHelper.getUserId());
                            dbModel2.setWalletName(mBinding.editWalletName.getText());
                            return dbModel2.save();
                        })

                        .observeOn(AndroidSchedulers.mainThread())

                        .doFinally(() -> disMissLoading())

                        .subscribe(isSave -> {

                            if (isSave) {
                                ImportWalletSuccessActivity.open(this);
                                finish();
                            } else {
                                UITipDialog.showFail(this, getString(R.string.wallet_import_fail));
                            }

                        }, throwable -> {
                            ToastUtil.show(ImportWalletStartActivity.this, getString(R.string.check_words_fail));
                        })


        );
    }


}

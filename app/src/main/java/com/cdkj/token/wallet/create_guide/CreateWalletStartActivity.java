package com.cdkj.token.wallet.create_guide;

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

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreateWalletStartBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.user.WebViewImgBgActivity;
import com.cdkj.token.utils.ThaAppConstant;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.import_guide.ImportWalletStartActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.baselibrary.utils.StringUtils.stringFilter;

/**
 * 创建钱包开始界面
 * Created by cdkj on 2018/7/20.
 */

public class CreateWalletStartActivity extends AbsLoadActivity {

    private ActivityCreateWalletStartBinding mBinding;

    private String mPassword;

    private boolean isAggree = true;//同意条款

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreateWalletStartActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_wallet_start, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setStatusBarBlue();
        setTitleBgBlue();

        initEditText();

        mBaseBinding.titleView.setMidTitle(R.string.create_wallet);

        initClickListener();

    }

    void initEditText() {
        mBinding.editPassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mBinding.editRepassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mBinding.editPassword.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mBinding.editRepassword.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        mBinding.editWalletName.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

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

        //导入钱包
        mBinding.tvImportWallet.setOnClickListener(view -> {
            ImportWalletStartActivity.open(this);
            finish();
        });

        //创建钱包
        mBinding.btnCreateWallet.setOnClickListener(view -> {
            mPassword = mBinding.editPassword.getText();
            if (checkCanPass()) {
                return;
            }
            createMnemonicWordsAsyn(mPassword);
        });

        //隐私协议
        mBinding.tvRead.setOnClickListener(view -> {
            WebViewImgBgActivity.openkey(this, getString(R.string.privacy_agreement), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_PRIVACY));
        });

    }


    /**
     * 检测输入状态
     *
     * @return
     */
    private boolean checkCanPass() {
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
            UITipDialog.showInfoNoIcon(CreateWalletStartActivity.this, getString(R.string.password_error));
            return true;
        }

        if (!isAggree) {
            UITipDialog.showInfoNoIcon(CreateWalletStartActivity.this, getString(R.string.please_read_and_aggree));
            return true;
        }
        return false;
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

                            WalletDBModel walletDBModel2 = WalletHelper.createAllPrivateKey();

                            walletDBModel2.setWalletPassWord(WalletHelper.encrypt(pass));  //TODO 缺少BTC

                            walletDBModel2.setUserId(SPUtilHelper.getUserId());

                            walletDBModel2.setWalletName(mBinding.editWalletName.getText());

                            return JSON.toJSONString(walletDBModel2); //转为String暂存 用户只有进行备份之后才会使用

                        })
                        .observeOn(AndroidSchedulers.mainThread())

                        .doOnComplete(() -> disMissLoading())
                        .subscribe(walletString -> {
                            if (!TextUtils.isEmpty(walletString)) {
                                SPUtilHelper.createWalletCache(walletString); //转为String暂存 用户只有进行备份之后才会使用
                                CreateWalletSuccessActivity.open(CreateWalletStartActivity.this);
                                finish();
                            } else {
                                UITipDialog.showFail(this, getString(R.string.wallet_create_fail));
                            }
                        }, throwable -> {
                            ToastUtil.show(this, getString(R.string.wallet_create_fail));
                            finish();
                        })
        );

    }

}

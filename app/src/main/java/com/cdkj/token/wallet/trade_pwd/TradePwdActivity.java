package com.cdkj.token.wallet.trade_pwd;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.ThaAppConstant;
import com.cdkj.token.databinding.ActivityTradePwdBinding;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.dialogs.InfoSureDialog;
import com.cdkj.token.wallet.create_guide.CreateWalletSuccessActivity;
import com.cdkj.token.wallet.recover.RecoverWalletActivity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

/**
 * 交易密码
 * Created by cdkj on 2018/11/26.
 */

public class TradePwdActivity extends AbsStatusBarTranslucentActivity {

    public static int CREATE = 0;
    public static int RECOVER = 1;
    public static int MODIFY = 2;

    private ActivityTradePwdBinding mBinding;

    private int openWay;

    public static void open(Context context, int OpenWay) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TradePwdActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, OpenWay);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_trade_pwd, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {


        setWhiteTitle();
        setPageBgImage(R.mipmap.app_page_bg_new);

        init();
        initView();
        initListener();

    }

    private void init() {
        openWay = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);

        if (openWay == MODIFY) {
            setMidTitle(R.string.activity_tradepwd_title);
        } else {
            setMidTitle(R.string.activity_tradepwd_title_set);
            getPrivacy();
        }

    }

    private void initView() {

        mBinding.edtOldPwd.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtReTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        if (openWay == MODIFY) {
            mBinding.tvOldPwd.setVisibility(View.VISIBLE);
            mBinding.edtOldPwd.setVisibility(View.VISIBLE);
        }
    }


    private void initListener() {
        mBinding.btnNext.setOnClickListener(view -> {
            if (check()) {

                String pwd = mBinding.edtReTradePassword.getText().toString().trim();

                if (openWay == CREATE) {
                    createMnemonicWordsAsyn(pwd);
                } else if (openWay == RECOVER) {
                    RecoverWalletActivity.open(this, pwd);
                } else {
                    updatePassword(pwd);
                }

            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtReTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_hint));
            return false;
        }

        if (!mBinding.edtTradePassword.getText().toString().trim().equals(mBinding.edtReTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_two_hint));
            return false;
        }

        if (mBinding.edtTradePassword.getText().toString().trim().length() < 6) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_format_hint));
            return false;
        }

        if (openWay == MODIFY) {
            if (!WalletHelper.checkPasswordByUserId(mBinding.edtOldPwd.getText().toString(), WalletHelper.WALLET_USER)) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.old_pwd_error));
                return false;
            }
        }

        return true;
    }

    private void getPrivacy() {
        showLoadingDialog();

        Map<String, String> map = new HashMap<>();
        map.put("ckey", ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_PRIVACY));
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getRequestJsonString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(null) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                // 打开界面显示 隐私协议 弹窗
                new InfoSureDialog(TradePwdActivity.this).setInfoTitle(getString(R.string.privacy_agreement_title)).setShowWeb(true).setInfoContent(data.getCvalue()).show();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
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

                            WalletDBModel walletDBModel2 = WalletHelper.createAllPrivateKey();

                            walletDBModel2.setWalletPassWord(pass);

                            walletDBModel2.setUserId(WalletHelper.WALLET_USER);

                            walletDBModel2.setWalletName(WalletHelper.DEFAULT_PRI_WALLET);

                            return JSON.toJSONString(walletDBModel2); //转为String暂存 用户只有进行备份之后才会使用

                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> disMissLoadingDialog())
                        .subscribe(walletString -> {
                            if (!TextUtils.isEmpty(walletString)) {
                                SPUtilHelper.createWalletCache(walletString); //转为String暂存 用户只有进行备份之后才会使用
                                CreateWalletSuccessActivity.open(TradePwdActivity.this);
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

    /**
     * 修改密码
     *
     * @param
     */
    private void updatePassword(String password) {
        try {
            WalletHelper.updateWalletPassWord(password, WalletHelper.WALLET_USER);
            UITipDialog.showSuccess(TradePwdActivity.this, getString(R.string.update_password_success), dialogInterface -> {
                finish();
            });
        } catch (Exception e) {
            UITipDialog.showSuccess(TradePwdActivity.this, getString(R.string.update_password_fail), dialogInterface -> {
                finish();
            });
        }
    }
}

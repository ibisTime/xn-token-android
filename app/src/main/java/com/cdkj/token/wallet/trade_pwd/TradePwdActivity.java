package com.cdkj.token.wallet.trade_pwd;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
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
 * Created by cdkj on 2018/11/26.
 */

public class TradePwdActivity extends AbsStatusBarTranslucentActivity {

    public static int CREATE = 0;
    public static int RECOVER = 1;
    public static int FiND_OUT = 2;

    private ActivityTradePwdBinding mBinding;

    private int openWay;
    private String smsCode; // 仅修改密码时必传入
    private String account; // 仅修改密码时必传入

    public static void open(Context context, int OpenWay, String account, String smsCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TradePwdActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, OpenWay);
        intent.putExtra(CdRouteHelper.DATASIGN2, smsCode);
        intent.putExtra(CdRouteHelper.DATASIGN3, account);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_trade_pwd,null,false);
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

    private void init(){
        openWay = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);
        smsCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);
        account = getIntent().getStringExtra(CdRouteHelper.DATASIGN3);

        if (openWay == FiND_OUT){
            setMidTitle(R.string.activity_paypwd_title);
        } else {
            setMidTitle(R.string.activity_paypwd_title_set);
        }

        getPrivacy();
    }

    private void initView() {
        InputFilter[] filters = {new InputFilter.LengthFilter(6)};
        mBinding.edtTradePassword.getEditText().setFilters(filters);
        mBinding.edtReTradePassword.getEditText().setFilters(filters);

        mBinding.edtTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtReTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtGoogle.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        if (SPUtilHelper.getGoogleAuthFlag()){
            mBinding.tvGoogle.setVisibility(View.VISIBLE);
            mBinding.edtGoogle.setVisibility(View.VISIBLE);
        }
    }


    private void initListener() {
        mBinding.btnNext.setOnClickListener(view -> {
            if (check()){

                String pwd = mBinding.edtReTradePassword.getText().toString().trim();

                if (openWay == CREATE){
                    createMnemonicWordsAsyn(pwd);
                } else if (openWay == RECOVER){
                    RecoverWalletActivity.open(this, pwd);
                } else {
                    findOut(pwd);
                }

            }
        });
    }

    private boolean check(){
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

        if (SPUtilHelper.getGoogleAuthFlag()) {
            if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getString(com.cdkj.baselibrary.R.string.activity_paypwd_google_hint));
                return true;
            }
        }

        return true;
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


    private void getPrivacy(){
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
                new InfoSureDialog(TradePwdActivity.this).setInfoTitle(getString(R.string.privacy_agreement_title)).setInfoContent(data.getCvalue()).show();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private void findOut(String password) {

        Map<String, String> object = new HashMap<>();

        object.put("loginName", account);
        object.put("smsCaptcha", smsCode);
        object.put("newTradePwd", password);
        object.put("googleCaptcha", mBinding.edtGoogle.getText().toString());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805077", StringUtils.getRequestJsonString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (!data.isSuccess()) {
                    return;
                }

                SPUtilHelper.saveTradePwdFlag(true);

                UITipDialog.showSuccess(TradePwdActivity.this, getString(R.string.activity_paypwd_modify_sucess), dialogInterface -> finish());

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }
}

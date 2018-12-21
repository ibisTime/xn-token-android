package com.cdkj.token.wallet.trade_pwd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityPayPwdBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 修改资金密码
 * Created by cdkj on 2017/6/29.
 */
public class PayPwdActivity extends AbsStatusBarTranslucentActivity {

    public static final String REQUEST_CODE = "805077";

    private ActivityPayPwdBinding mBinding;

    private String smsCode;
    private String account;

    public static void open(Context context, String account, String smsCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PayPwdActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, smsCode);
        intent.putExtra(CdRouteHelper.DATASIGN2, account);
        context.startActivity(intent);
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_pay_pwd, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setWhiteTitle();
        setMidTitle(R.string.activity_paypwd_title);
        setPageBgImage(com.cdkj.token.R.mipmap.app_page_bg_new);

        init();
        initView();
        initListener();
    }

    private void init(){
        smsCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        account = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);

    }

    private void initView() {
        InputFilter[] filters = {new InputFilter.LengthFilter(6)};
        mBinding.edtTradePassword.getEditText().setFilters(filters);
        mBinding.edtReTradePassword.getEditText().setFilters(filters);

        mBinding.edtTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mBinding.edtReTradePassword.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        mBinding.edtGoogle.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        if (SPUtilHelper.getGoogleAuthFlag()){
            mBinding.tvGoogle.setVisibility(View.VISIBLE);
            mBinding.edtGoogle.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        mBinding.btnSuccess.setOnClickListener(view -> {
            if (check()){
                modify(mBinding.edtReTradePassword.getText().toString().trim());
            }
        });
    }

    /**
     * 验证输入状态
     *
     * @return 是否拦截
     */
    public boolean check() {

        if (TextUtils.isEmpty(mBinding.edtTradePassword.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdActivity.this, getString(R.string.activity_paypwd_pwd_hint));
            return false;
        }

        if (mBinding.edtTradePassword.getText().length() < 6) {
            UITipDialog.showInfoNoIcon(PayPwdActivity.this, getString(R.string.activity_paypwd_pwd_format_hint));
            return false;
        }


        if (TextUtils.isEmpty(mBinding.edtTradePassword.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdActivity.this, getString(R.string.activity_find_repassword_hint));
            return false;
        }

        if (!TextUtils.equals(mBinding.edtTradePassword.getText().toString().trim(), mBinding.edtTradePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(PayPwdActivity.this, getString(R.string.activity_find_repassword_format_hint));
            return false;
        }

        if (SPUtilHelper.getGoogleAuthFlag()) {
            if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
                UITipDialog.showInfoNoIcon(PayPwdActivity.this, getString(R.string.activity_paypwd_google_hint));
                return false;
            }
        }

        return true;
    }


    private void modify(String password) {

        Map<String, String> object = new HashMap<>();

        object.put("loginName", account);
        object.put("smsCaptcha", smsCode);
        object.put("newTradePwd", password);
        object.put("googleCaptcha", mBinding.edtGoogle.getText().toString());

        Call call = RetrofitUtils.getBaseAPiService().successRequest(REQUEST_CODE, StringUtils.getRequestJsonString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (!data.isSuccess()) {
                    return;
                }

                SPUtilHelper.saveTradePwdFlag(true);

                UITipDialog.showSuccess(PayPwdActivity.this, getString(R.string.activity_paypwd_modify_sucess), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }

}

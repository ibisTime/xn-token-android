package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityFindOutPwdBinding;

import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/11/26.
 */

public class FindOutPwdActivity extends AbsStatusBarTranslucentActivity {

    private ActivityFindOutPwdBinding mBinding;

    private String account;
    private String smsCode;

    public static void open(Context context, String account, String smsCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FindOutPwdActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, account);
        intent.putExtra(CdRouteHelper.DATASIGN2, smsCode);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_find_out_pwd,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setWhiteTitle();
        setMidTitle(getString(R.string.activity_find_title));
        setPageBgImage(R.mipmap.app_page_bg_new);

        init();
        initView();
        initListener();
    }

    private void init() {

        account = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        smsCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN2);

    }

    private void initView() {
        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtRePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initListener() {
        mBinding.btnSuccess.setOnClickListener(view -> {
            if (check()){
                findPwdReqeust();
            }
        });
    }

    private boolean check(){
        if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_password_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_hint));
            return false;
        }

        if (!mBinding.edtRePassword.getText().toString().trim().equals(mBinding.edtPassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_two_hint));
            return false;
        }

        if (!checkRule1(mBinding.edtRePassword.getText().toString().trim())){
            UITipDialog.showInfoNoIcon(this, getStrRes(R.string.user_repassword_size_hint));
            return false;
        }

        return true;
    }

    private boolean checkRule1(String pwd){

        if (pwd.length() >= 8 && pwd.length() <= 25){
            return true;
        }else {
            return false;
        }

    }


    /**
     * 找回密码请求
     */
    private void findPwdReqeust() {

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("loginName", account);
        hashMap.put("newLoginPwd", mBinding.edtPassword.getText().toString());
        hashMap.put("smsCaptcha", smsCode);
        hashMap.put("kind", AppConfig.USERTYPE);
//        hashMap.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        hashMap.put("systemCode", AppConfig.SYSTEMCODE);
        hashMap.put("companyCode", AppConfig.COMPANYCODE);
        hashMap.put("companyCode", AppConfig.COMPANYCODE);
        hashMap.put("interCode", SPUtilHelper.getCountryInterCode());
        hashMap.put("countryCode", SPUtilHelper.getCountryCode());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805076", StringUtils.getRequestJsonString(hashMap));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    UITipDialog.showSuccess(FindOutPwdActivity.this, getString(R.string.activity_find_success), dialogInterface -> {
                        finish();
                    });

                } else {
                    UITipDialog.showFail(FindOutPwdActivity.this, getString(R.string.activity_find_failure));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });


    }

}

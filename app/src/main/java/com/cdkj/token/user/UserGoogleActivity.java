package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.SendVerificationCode;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserGoogleBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.SystemUtils.copy;
import static com.cdkj.baselibrary.utils.SystemUtils.paste;

/**
 * Created by lei on 2017/12/6.
 */

public class UserGoogleActivity extends AbsActivity implements SendCodeInterface {

    public final static String REQ_OPEN_CODE = "805078";
    public final static String REQ_CLOSE_CODE = "805079";

    private SendPhoneCodePresenter mSendCodePresenter;

    private ActivityUserGoogleBinding mBinding;

    private String status;
    private String bizType;

    private String loginName; // 登录名：邮箱 或 手机

    public static void open(Context context, String status) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserGoogleActivity.class).putExtra("status", status));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_google, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_google));
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();

    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));

        //改变ui
        mBinding.btnSend.setBackgroundResource(R.drawable.corner_sign_btn_gray);
    }

    @Override
    public void CodeFailed(String code, String msg) {
        showToast(msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSendCodePresenter != null) {
            mSendCodePresenter.clear();
            mSendCodePresenter = null;
        }
    }

    private void init() {
        if (getIntent() == null)
            return;

        status = getIntent().getStringExtra("status");
        if (status.equals("close")) { // 关闭谷歌验证

            mBinding.llKey.setVisibility(View.GONE);
            mBinding.lineKey.setVisibility(View.GONE);

            mBinding.btnConfirm.setText(getStrRes(R.string.user_google_btn_close));

        } else if (status.equals("modify")) { // 修改谷歌验证
            // 获取密钥
            getGoogleKey();

            mBinding.btnConfirm.setText(getStrRes(R.string.user_google_btn_modify));
        } else { // 打开修改谷歌验证
            // 获取密钥
            getGoogleKey();
        }

        // 是否有手机号，没有则替换为邮箱
        if (TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum())){
            loginName = SPUtilHelper.getUserEmail();

            mBinding.tvSmsCodeTitle.setText(getStrRes(R.string.user_email_code));
            mBinding.edtCode.setHint(getStrRes(R.string.user_email_code_hint));
        }else {
            // 登录名为手机号码
            loginName = SPUtilHelper.getUserPhoneNum();
        }

        mSendCodePresenter = new SendPhoneCodePresenter(this, this);
    }

    private void initListener() {
        mBinding.btnCopy.setOnClickListener(view -> {
            copy(this, mBinding.tvKey.getText().toString());
        });

        mBinding.btnPaste.setOnClickListener(view -> {
            mBinding.edtGoogle.setText(paste(this));
        });

        mBinding.btnSend.setOnClickListener(view -> {

            if (status.equals("close")) {
                bizType = REQ_CLOSE_CODE;
            } else {
                bizType = REQ_OPEN_CODE;
            }


            if (TextUtils.isEmpty(loginName)){

            }

            SendVerificationCode sendVerificationCode = new SendVerificationCode(
                    loginName, bizType, "C", SPUtilHelper.getCountryInterCode());

            mSendCodePresenter.openVerificationActivity(sendVerificationCode);

        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check())
                if (status.equals("close")) { // 关闭谷歌验证
                    closeGoogleKey();
                } else { // 打开或者修改谷歌验证
                    openGoogleKey();
                }

        });
    }

    private void getGoogleKey() {

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805070", "{}");

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.tvKey.setText(data.getSecret());

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
            showToast(getStrRes(R.string.google_code_hint));
            return false;
        }

        if (!isNumeric(mBinding.edtGoogle.getText().toString())) {
            showToast(getStrRes(R.string.google_code_format_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
            showToast(getStrRes(R.string.sms_code_hint));
            return false;
        }

        return true;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void openGoogleKey() {

        Map<String, String> map = new HashMap<>();
        map.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        map.put("secret", mBinding.tvKey.getText().toString());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString());
        map.put("loginName", loginName);

        Call call = RetrofitUtils.getBaseAPiService().successRequest(REQ_OPEN_CODE, StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()) {
                    SPUtilHelper.saveGoogleAuthFlag(true);
                    finish();
                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private void closeGoogleKey() {

        Map<String, String> map = new HashMap<>();
        map.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString());
        map.put("loginName", loginName);
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest(REQ_CLOSE_CODE, StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()) {
                    SPUtilHelper.saveGoogleAuthFlag(false);
                    finish();
                }


            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mSendCodePresenter != null) {
            mSendCodePresenter.onActivityResult(requestCode, resultCode, data);
        }
    }
}

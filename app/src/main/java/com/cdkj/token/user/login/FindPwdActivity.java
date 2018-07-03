package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityFindPasswordBinding;
import com.cdkj.token.user.CountryCodeListActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import retrofit2.Call;

/**
 * 找回密码
 */
public class FindPwdActivity extends AbsBaseActivity implements SendCodeInterface {

    private ActivityFindPasswordBinding mBinding;

    private String mPhoneNumber;

    private SendPhoneCodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String mPhoneNumber) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FindPwdActivity.class);
        intent.putExtra("phonenumber", mPhoneNumber);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_find_password, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.activity_find_title));
        setSubLeftImgState(true);
        mSendCOdePresenter = new SendPhoneCodePresenter(this);
        if (getIntent() != null) {
            mPhoneNumber = getIntent().getStringExtra("phonenumber");
        }

        initListener();

        mBinding.edtPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtRePassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mBinding.edtMobile.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.tvCountryName.setText(SPUtilHelper.getCountry());
        mBinding.edtMobile.getLeftTextView().setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryCode()));
    }


    /**
     *
     */
    private void initListener() {
        //国家区号选择
        mBinding.linLayoutCountryCode.setOnClickListener(view -> {
            CountryCodeListActivity.open(this);
        });
        mBinding.tvFinish.setOnClickListener(view -> finish());

        //发送验证码
        mBinding.edtCode.getSendCodeBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendCOdePresenter.sendCodeRequest(mBinding.edtMobile.getText().toString(), "805063", MyConfig.USERTYPE, FindPwdActivity.this);
            }
        });


        //确定
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString())) {
                    showToast(getString(R.string.activity_find_mobile_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                    showToast(getString(R.string.activity_find_code_hint));
                    return;
                }

//                if (SPUtilHelper.getGoogleAuthFlag()){
//                    if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())){
//                        showToast(getString(R.string.activity_find_google_hint));
//                        return;
//                    }
//                }

                if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString())) {
                    showToast(getString(R.string.activity_find_password_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString())) {
                    showToast(getString(R.string.activity_find_repassword_hint));
                    return;
                }

                if (mBinding.edtPassword.getText().length() < 6) {
                    showToast(getString(R.string.activity_find_password_format_hint));
                    return;
                }

                if (!mBinding.edtPassword.getText().toString().equals(mBinding.edtRePassword.getText().toString())) {
                    showToast(getString(R.string.activity_find_repassword_format_hint));
                    return;
                }

                findPwdReqeust();
            }
        });
    }


    /**
     * 找回密码请求
     */
    private void findPwdReqeust() {

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("mobile", mBinding.edtMobile.getText().toString());
        hashMap.put("newLoginPwd", mBinding.edtPassword.getText().toString());
        hashMap.put("smsCaptcha", mBinding.edtCode.getText().toString());
        hashMap.put("kind", MyConfig.USERTYPE);
//        hashMap.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        hashMap.put("systemCode", MyConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);
        hashMap.put("interCode", SPUtilHelper.getCountryCode());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805063", StringUtils.getJsonToString(hashMap));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(FindPwdActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast(getString(R.string.activity_find_success));
                    finish();
                } else {
                    showToast(getString(R.string.activity_find_failure));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), com.cdkj.token.R.drawable.btn_code_blue_bg, com.cdkj.token.R.drawable.gray));

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
        disMissLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSendCOdePresenter != null) {
            mSendCOdePresenter.clear();
            mSendCOdePresenter = null;
        }
    }

    /**
     * @param count
     * @param btn
     * @param enableTrueRes  可以点击样式
     * @param enableFalseRes 不可以点击样式
     * @return
     */
    public Disposable startCodeDown(final int count, final Button btn, final int enableTrueRes, final int enableFalseRes) {
        return Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(count)//只发射开始的N项数据或者一定时间内的数据
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        btn.setEnabled(false);
                        btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count));
                    }
                })
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   btn.setEnabled(false);
                                   btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count - aLong));
                                   btn.setBackgroundResource(enableFalseRes);
                                   btn.setTextColor(ContextCompat.getColor(FindPwdActivity.this, com.cdkj.token.R.color.white));
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   btn.setEnabled(true);
                                   btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count));
                                   btn.setBackgroundResource(enableTrueRes);
                                   btn.setTextColor(ContextCompat.getColor(FindPwdActivity.this, com.cdkj.token.R.color.btn_blue));
                               }
                           }, new Action() {
                               @Override
                               public void run() throws Exception {
                                   btn.setEnabled(true);
                                   btn.setTextColor(ContextCompat.getColor(FindPwdActivity.this, com.cdkj.token.R.color.btn_blue));
                                   btn.setText(com.cdkj.baselibrary.R.string.resend_code);
                                   btn.setBackgroundResource(enableTrueRes);
                               }
                           }
                );
    }

}

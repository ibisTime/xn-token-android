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
import com.cdkj.baselibrary.activitys.AppBuildTypeActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivitySignUpBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import retrofit2.Call;


public class SignUpActivity extends AbsBaseActivity implements SendCodeInterface {

    private boolean agreeState = true;

    private SendPhoneCodePresenter mPresenter;
    private ActivitySignUpBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_up, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_sign_up));
        setSubLeftImgState(true);
        if (LogUtil.isLog) {
            setSubRightTitleAndClick(getStrRes(R.string.build_type), v -> {
                AppBuildTypeActivity.open(this);
            });
        }

        mPresenter = new SendPhoneCodePresenter(this);

        mBinding.edtCode.getSendCodeBtn().setInputType(InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);

        initListener();
    }

    private void initListener() {

        mBinding.tvFinish.setOnClickListener(view -> finish());

        mBinding.edtCode.getSendCodeBtn().setOnClickListener(view -> {
            if (check("code")) {
                mPresenter.sendCodeRequest(mBinding.edtMobile.getText().toString().trim(), "805041", "C", this);
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check("all")) {
                signUp();
            }
        });

    }

    private boolean check(String type) {

        if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())) {
            showToast(getStrRes(R.string.user_mobile_hint));
            return false;
        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())) {
                showToast(getStrRes(R.string.user_code_hint));
                return false;
            }
            if (mBinding.edtCode.getText().toString().trim().length() != 4) {
                showToast(getStrRes(R.string.user_code_format_hint));
                return false;
            }

            if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString().trim())) {
                showToast(getStrRes(R.string.user_password_hint));
                return false;
            }
            if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString().trim())) {
                showToast(getStrRes(R.string.user_repassword_hint));
                return false;
            }
            if (!mBinding.edtRePassword.getText().toString().trim().equals(mBinding.edtPassword.getText().toString().trim())) {
                showToast(getStrRes(R.string.user_repassword_two_hint));
                return false;
            }

            if (!agreeState) {
                showToast(getStrRes(R.string.user_protocol_hint));
                return false;
            }
        }


        return true;
    }

    private void signUp() {
        Map<String, Object> map = new HashMap<>();
//        map.put("nickname", mBinding.edtNick.getText().toString().trim());
        map.put("kind", "C");
        map.put("userRefereeKind", "C");
//        map.put("userReferee", mBinding.edtReferee.getText().toString().trim());
        map.put("mobile", mBinding.edtMobile.getText().toString().trim());
        map.put("loginPwd", mBinding.edtPassword.getText().toString().trim());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString().trim());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).signUp("805041", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {

            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) || !TextUtils.isEmpty(data.getUserId())) {
                    showToast(getStrRes(R.string.user_sign_up_success));

                    SPUtilHelper.saveUserId(data.getUserId());
                    SPUtilHelper.saveUserToken(data.getToken());
//                    SPUtilHelper.saveUserName(mBinding.edtNick.getText().toString().trim());
                    SPUtilHelper.saveUserPhoneNum(mBinding.edtMobile.getText().toString().trim());

                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面

                    MainActivity.open(SignUpActivity.this);
                    finish();
                } else {
                    showToast(getStrRes(R.string.user_sign_up_failure));
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(startCodeDown(60, mBinding.edtCode.getSendCodeBtn(), R.drawable.btn_code_blue_bg, R.drawable.gray));

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
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
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
                                   btn.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.white));
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   btn.setEnabled(true);
                                   btn.setText(CdApplication.getContext().getString(com.cdkj.baselibrary.R.string.code_down, count));
                                   btn.setBackgroundResource(enableTrueRes);
                                   btn.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.btn_blue));
                               }
                           }, new Action() {
                               @Override
                               public void run() throws Exception {
                                   btn.setEnabled(true);
                                   btn.setTextColor(ContextCompat.getColor(SignUpActivity.this, R.color.btn_blue));
                                   btn.setText(com.cdkj.baselibrary.R.string.resend_code);
                                   btn.setBackgroundResource(enableTrueRes);
                               }
                           }
                );
    }


}

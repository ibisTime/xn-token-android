package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPayPasswordBinding;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.CountrySelectEvent;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.SystemUtils.paste;

/**
 * 修改 设置 资金密码
 * Created by cdkj on 2017/6/29.
 */
public class PayPwdModifyActivity extends AbsActivity implements SendCodeInterface {

    private ActivityModifyPayPasswordBinding mBinding;

    private boolean mIsSetPwd;//是否设置过密码

    private SendPhoneCodePresenter mSendCoodePresenter;

    private String selectCountryCode;//用户选择的国家


    /**
     * @param context
     * @param isSetPwd//是否设置过资金密码
     */
    public static void open(Context context, boolean isSetPwd, String mobile) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PayPwdModifyActivity.class);
        intent.putExtra("isSetPwd", isSetPwd);
        intent.putExtra("mobile", mobile);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_pay_password, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        ImgUtils.loadActImg(this, SPUtilHelper.getCountryFlag(), mBinding.imgCountry);
        mBinding.tvCountryCode.setText(StringUtils.transformShowCountryCode(SPUtilHelper.getCountryCode()));

        selectCountryCode = SPUtilHelper.getCountryCode();

        setSubLeftImgState(true);

        mBinding.llGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);
        mBinding.lineGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);

        if (getIntent() != null) {
            mIsSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
            mBinding.edtPhone.setText(getIntent().getStringExtra("mobile"));
            mBinding.edtPhone.setSelection(mBinding.edtPhone.getText().length());
        }

        if (mIsSetPwd) {
            setTopTitle(getString(R.string.activity_paypwd_title));
        } else {
            setTopTitle(getString(R.string.activity_paypwd_title_set));
        }
        mSendCoodePresenter = new SendPhoneCodePresenter(this);
        setListener();
    }

    /**
     * 设置事件
     */
    private void setListener() {
//        mBinding.linLayoutCountryCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CdRouteHelper.openCountrySelect(false);
//            }
//        });
        //粘贴
        mBinding.btnPaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.edtGoogle.setText(paste(PayPwdModifyActivity.this));
            }
        });

//发送验证码
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bizType = "";
                if (mIsSetPwd) {
                    bizType = "805067";//修改
                } else {
                    bizType = "805066";

                }

                mSendCoodePresenter.sendCodeRequest(mBinding.edtPhone.getText().toString(), bizType, MyConfig.USERTYPE, selectCountryCode, PayPwdModifyActivity.this);
            }
        });
//确认
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInputState()) return;

                setPwd();

            }
        });
    }

    /**
     * 验证输入状态
     *
     * @return 是否拦截
     */
    public boolean checkInputState() {
        if (TextUtils.isEmpty(mBinding.edtPhone.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_mobile_hint));
            return true;
        }
        if (TextUtils.isEmpty(mBinding.edtCode.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_code_hint));
            return true;
        }
        if (SPUtilHelper.getGoogleAuthFlag()) {
            if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
                UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_google_hint));
                return true;
            }
        }
        if (TextUtils.isEmpty(mBinding.edtPassword.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_pwd_hint));
            return true;
        }
        if (mBinding.edtPassword.getText().length() < 6) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_paypwd_pwd_format_hint));
            return true;
        }


        if (TextUtils.isEmpty(mBinding.edtRepassword.getText())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_find_repassword_hint));
            return true;
        }

        if (!TextUtils.equals(mBinding.edtPassword.getText().toString().trim(), mBinding.edtRepassword.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, getString(R.string.activity_find_repassword_format_hint));
            return true;
        }
        return false;
    }


    private void setPwd() {

        Map<String, String> object = new HashMap<>();

        object.put("userId", SPUtilHelper.getUserId());
        object.put("token", SPUtilHelper.getUserToken());
        object.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        if (mIsSetPwd) {
            object.put("newTradePwd", mBinding.edtPassword.getText().toString().trim());
        } else {
            object.put("tradePwd", mBinding.edtPassword.getText().toString().trim());
        }

        object.put("smsCaptcha", mBinding.edtCode.getText().toString().toString());

        String code = "";
        if (mIsSetPwd) {  //修改
            code = "805067";
        } else {
            code = "805066";
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest(code, StringUtils.getJsonToString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (!data.isSuccess()) {
                    return;
                }

                SPUtilHelper.saveTradePwdFlag(true);

                String dialotString = "";

                if (mIsSetPwd) {
                    dialotString = getString(R.string.activity_paypwd_modify_sucess);
                } else {
                    dialotString = getString(R.string.activity_paypwd_set_success);
                }

                UITipDialog.showSuccess(PayPwdModifyActivity.this, dialotString, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    @Override
    public void CodeSuccess(String msg) {
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
    }

    @Override
    public void CodeFailed(String code, String msg) {
        UITipDialog.showInfoNoIcon(PayPwdModifyActivity.this, msg);
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
        if (mSendCoodePresenter != null) {
            mSendCoodePresenter.clear();
            mSendCoodePresenter = null;
        }
    }

    @Subscribe
    public void countrySelectEvent(CountrySelectEvent countrySelectEvent) {
        if (countrySelectEvent == null) return;
        mBinding.tvCountryCode.setText(StringUtils.transformShowCountryCode(countrySelectEvent.getCountryCode()));
        selectCountryCode = countrySelectEvent.getCountryCode();

    }
}

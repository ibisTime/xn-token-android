package com.cdkj.token.interfaces;

import android.app.Activity;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.user.login.ForgetPwdActivity;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 获取用户信息
 * Created by cdkj on 2017/8/8.
 */

public class UserInfoPresenter {

    private UserInfoInterface mListener;
    private Call call;

    private Activity activity;

    public UserInfoPresenter(UserInfoInterface mListener, Activity activity) {
        this.mListener = mListener;
        this.activity = activity;
    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest() {

        if (!SPUtilHelper.isLoginNoStart()) {
            if (mListener != null) {
                mListener.onFinishedGetUserInfo(null, "");
            }
            return;
        }

        if (mListener != null) {
            mListener.onStartGetUserInfo();
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(activity) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {

                if (data == null) {

                    if (mListener != null) {
                        mListener.onFinishedGetUserInfo(null, "");
                    }

                    return;
                }

                //TODO 用户信息保存优化
                SPUtilHelper.saveSecretUserId(data.getSecretUserId());
                SPUtilHelper.saveUserPhoto(data.getPhoto());
                SPUtilHelper.saveUserEmail(data.getEmail());
                SPUtilHelper.saveUserName(data.getNickname());
                SPUtilHelper.saveRealName(data.getRealName());
                SPUtilHelper.saveUserPhoneNum(data.getMobile());
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveLoginPwdFlag(data.isLoginPwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());

                if (mListener != null) {
                    mListener.onFinishedGetUserInfo(data, "");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                if (mListener != null) {
                    mListener.onFinishedGetUserInfo(null, errorMessage);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取用户信息密码是否设置
     */
    public void getUserInfoPayPwdStateRequest() {

        if (!SPUtilHelper.isLoginNoStart()) {
            if (mListener != null) {
                mListener.onFinishedGetUserInfo(null, "");
            }
            return;
        }

        if (mListener != null) {
            mListener.onStartGetUserInfo();
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(activity) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {

                if (data == null) {

                    if (mListener != null) {
                        mListener.onFinishedGetUserInfo(null, "");
                    }

                    return;
                }

                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveLoginPwdFlag(data.isLoginPwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());

                if (mListener != null) {
                    mListener.onFinishedGetUserInfo(data, "");
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                if (mListener != null) {
                    mListener.onFinishedGetUserInfo(null, errorMessage);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 判断用户是否设置过支付密码没有设置过跳转到设置界面
     *
     * @return
     */
    public boolean checkPayPwdAndStartSetPage() {

        boolean isSet = SPUtilHelper.getTradePwdFlag();

        if (!isSet) {
            showDoubleWarnListen(activity.getString(R.string.please_set_account_money_password), view -> {

                ForgetPwdActivity.open(activity,
                        SPUtilHelper.getUserPhoneNum(),
                        SPUtilHelper.getUserEmail(),
                        ForgetPwdActivity.RC_TRADE_PWD_MODIFY);


//                PayPwdModifyActivity.open(activity, false, SPUtilHelper.getUserPhoneNum());    //跳转设置支付密码界面
            });
        }
        return isSet;
    }

    //处理持有对象
    public void clear() {
        if (this.call != null) {
            this.call.cancel();
            this.call = null;
        }

        this.mListener = null;
        this.activity = null;
    }


    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (activity == null || activity.isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(activity).builder()
                .setTitle(activity.getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg(str)
                .setPositiveBtn(activity.getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(activity.getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);

        commonDialog.show();
    }


}

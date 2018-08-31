package com.cdkj.baselibrary.interfaces;

import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by cdkj on 2017/8/8.
 */

public class LoginPresenter {

    private LoginInterface mListener;
    private Context mContext;
    private Call call;

    public LoginPresenter(LoginInterface view) {
        this.mListener = view;
    }

    //处理登录逻辑
    public void login(String username, String password, String countryCode, Context context) {
        this.mContext = context;
        if (TextUtils.isEmpty(username)) {
            ToastUtil.show(context, CdApplication.getContext().getString(R.string.please_input_account));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show(context, CdApplication.getContext().getString(R.string.activity_find_password_hint));
            return;
        }


        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("loginName", username);
        hashMap.put("loginPwd", password);
        hashMap.put("interCode", SPUtilHelper.getCountryInterCode());
        hashMap.put("countryCode", SPUtilHelper.getCountryCode());
        hashMap.put("kind", AppConfig.USERTYPE);
        hashMap.put("systemCode", AppConfig.SYSTEMCODE);

        call = RetrofitUtils.getBaseAPiService().userLogin("805050", StringUtils.getJsonToString(hashMap));

        mListener.StartLogin();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(mContext) {
            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) && !TextUtils.isEmpty(data.getUserId())) {
                    mListener.LoginSuccess(data, CdApplication.getContext().getString(R.string.sign_in_success));
                } else {
                    mListener.LoginFailed("0", CdApplication.getContext().getString(R.string.sign_in_fail));
                }
            }

            @Override
            protected void onFinish() {
                mListener.EndLogin();
            }
        });
    }

    //处理持有对象
    public void clear() {
        if (this.call != null) {
            this.call.cancel();
            this.call = null;
        }
        this.mListener = null;
        this.mContext = null;
    }


}

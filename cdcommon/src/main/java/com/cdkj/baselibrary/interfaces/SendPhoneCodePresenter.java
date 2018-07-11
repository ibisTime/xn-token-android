package com.cdkj.baselibrary.interfaces;

import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 发送验证码
 * Created by cdkj on 2017/8/8.
 */
public class SendPhoneCodePresenter {

    private SendCodeInterface mListener;
    private Context mContext;
    private Call call;

    private String countryCode;

    public SendPhoneCodePresenter(SendCodeInterface view) {
        this.mListener = view;
    }

    //发送验证码
    public void sendCodeRequest(String phone, String bizType, String kind, String countryCode, Context context) {
        this.mContext = context;
        this.countryCode = countryCode;
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, mContext.getString(R.string.activity_mobile_mobile_hint));
            return;
        }
        if (TextUtils.isEmpty(countryCode)) return;

        request(phone, bizType, kind);
    }

    /**
     * 请求
     */
    private void request(String phone, String bizType, String kind) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("systemCode", MyConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);
        hashMap.put("mobile", phone);
        hashMap.put("bizType", bizType);
        hashMap.put("kind", kind);
        hashMap.put("interCode", countryCode); //国际区号

        call = RetrofitUtils.getBaseAPiService().successRequest("805950", StringUtils.getJsonToString(hashMap));

        mListener.StartSend();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mContext) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    ToastUtil.show(mContext, mContext.getString(R.string.smscode_send_success));
                    mListener.CodeSuccess(mContext.getString(R.string.smscode_send_success));
                } else {
                    mListener.CodeFailed("", mContext.getString(R.string.smscode_send_success));
                }
            }


            @Override
            protected void onFinish() {
                mListener.EndSend();
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

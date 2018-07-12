package com.cdkj.baselibrary.nets;

import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.utils.LogUtil;

import java.lang.ref.SoftReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cdkj.baselibrary.nets.NetHelper.DATA_NULL;
import static com.cdkj.baselibrary.nets.NetHelper.NETERRORCODE4;
import static com.cdkj.baselibrary.nets.NetHelper.REQUESTFECODE4;
import static com.cdkj.baselibrary.nets.NetHelper.REQUESTOK;
import static com.cdkj.baselibrary.nets.NetHelper.getThrowableStateCode;
import static com.cdkj.baselibrary.nets.NetHelper.getThrowableStateString;

/**
 * 网络请求回调
 * Created by Administrator on 2016/9/3.
 */
public abstract class BaseResponseModelCallBack<T> implements Callback<BaseResponseModel<T>> {

    private Context context;

    public BaseResponseModelCallBack(Context context) {
        SoftReference<Context> mS = new SoftReference<>(context);
        this.context = mS.get();
    }

    @Override
    public void onResponse(Call<BaseResponseModel<T>> call, Response<BaseResponseModel<T>> response) {
        onFinish();

        if (response == null || response.body() == null) {
            onReqFailure(DATA_NULL, CdApplication.getContext().getString(R.string.net_data_is_null));
            return;
        }
        BaseResponseModel t = response.body();
        checkState(t);      //根据返回错误的状态码实现相应的操作

//        if (response.isSuccessful()) {
//
//            try {
//                BaseResponseModel t = response.body();
//                checkState(t);      //根据返回错误的状态码实现相应的操作
//            } catch (Exception e) {
//                if (LogUtil.isLog) {
//                    onReqFailure(NETERRORCODE4, "未知错误" + e);
//                } else {
//                    onReqFailure(NETERRORCODE4, CdApplication.getContext().getString(R.string.error_unknown));
//                }
//            }
//
//        } else {
//            onReqFailure(NETERRORCODE4, CdApplication.getContext().getString(R.string.net_req_fail));
//        }

    }

    @Override
    public void onFailure(Call<BaseResponseModel<T>> call, Throwable t) {

        if (call.isCanceled()) {                //如果是主动请求取消的就不执行
            return;
        }

        onFinish();

        if (!NetUtils.isNetworkConnected(CdApplication.getContext())) {
            onNoNet(CdApplication.getContext().getString(R.string.no_net));
        }

        onReqFailure(getThrowableStateCode(t), getThrowableStateString(t));

    }

    /**
     * 检查错误码
     *
     * @param baseModelNew 根据返回错误的状态码实现相应的操作
     */
    protected void checkState(BaseResponseModel baseModelNew) {

        String state = baseModelNew.getErrorCode();

        LogUtil.E("登录"+TextUtils.equals(state,REQUESTFECODE4));

        if (TextUtils.equals(state,REQUESTOK)) { //请求成功

            T t = (T) baseModelNew.getData();

            if (t == null) {
                onReqFailure(DATA_NULL, CdApplication.getContext().getString(R.string.net_data_is_null));
                return;
            }

            onSuccess(t, baseModelNew.getErrorInfo());

        } else if (TextUtils.equals(state,REQUESTFECODE4)) {
            onLoginFailure(context, baseModelNew.getErrorInfo());
        } else {
            onReqFailure(state, baseModelNew.getErrorInfo());
        }

    }


    /**
     * 请求成功
     *
     * @param data
     */
    protected abstract void onSuccess(T data, String SucMessage);

    /**
     * 请求失败
     *
     * @param errorCode
     * @param errorMessage
     */
    protected void onReqFailure(String errorCode, String errorMessage) {
        NetHelper.onReqFailure(context, errorCode, errorMessage);
    }

    /**
     * 重新登录
     *
     * @param
     */
    protected void onLoginFailure(Context context, String errorMessage) {
        NetHelper.onLoginFailure(context, errorMessage);
    }


    /**
     * 请求结束 无论请求成功或者失败都会被调用
     */
    protected abstract void onFinish();

    /**
     * 无网络
     */
    protected void onNoNet(String msg) {
        NetHelper.onNoNet(context, msg);
    }

}

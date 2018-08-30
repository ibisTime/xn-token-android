package com.cdkj.baselibrary.nets.rx;

import android.content.Context;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.nets.NetHelper;
import com.cdkj.baselibrary.nets.NetUtils;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.baselibrary.nets.NetHelper.DATA_NULL;
import static com.cdkj.baselibrary.nets.NetHelper.NETERRORCODE4;
import static com.cdkj.baselibrary.nets.NetHelper.REQUESTFECODE4;
import static com.cdkj.baselibrary.nets.NetHelper.REQUESTOK;
import static com.cdkj.baselibrary.nets.NetHelper.getThrowableStateCode;
import static com.cdkj.baselibrary.nets.NetHelper.getThrowableStateString;


/**
 * Retrofit帮助类
 */
//TODO 网络请求处理接口抽取
public class RxTransformerHelper {

    /**
     * 业务错误过滤器
     */
    private static <T> Predicate<T> verifyBusiness(Context context) {
        return response -> {

            BaseResponseModel baseResponse = (BaseResponseModel) response;

            String responseCode = baseResponse.getErrorCode();

            boolean isSuccess = REQUESTOK.equals(responseCode);

            if (!isSuccess) {
                NetHelper.onReqFailure(context, responseCode, baseResponse.getErrorInfo());
            }

            return isSuccess;
        };
    }

    /**
     * 非空过滤器（自定义）
     */
    private static <T> Predicate<T> verifyNotEmpty(Context context) {
        return response -> {
            if (response == null) {
                return false;
            }
            BaseResponseModel baseResponse = (BaseResponseModel) response;
            T t = (T) baseResponse.getData();
            if (t == null) {
                NetHelper.onReqFailure(context, DATA_NULL, CdApplication.getContext().getString(R.string.net_data_is_null));
                return false;
            }

            return true;
        };
    }

    /**
     * 非空过滤器（自定义）
     */
    private static <T> Predicate<T> verifyNotResponseModel(Context context) {
        return response -> {

            boolean isSuccess = response instanceof BaseResponseModel;

            if (!isSuccess) {
                NetHelper.onReqFailure(context, NETERRORCODE4, CdApplication.getContext().getString(R.string.net_req_fail));
            }

            return isSuccess;

        };
    }

    /**
     * 错误提醒处理
     *
     * @param context
     * @param <T>
     * @return
     */
    private static <T> Function<Throwable, T> doError(Context context) {
        return throwable -> {
            throwable.printStackTrace();
            if (!NetUtils.isNetworkConnected(context)) {
                NetHelper.onNoNet(context, CdApplication.getContext().getString(R.string.no_net));
            } else {
                NetHelper.onReqFailure(context, getThrowableStateCode(throwable), getThrowableStateString(throwable));
            }
            return null;
        };
    }

    /**
     * sessionId 过期的过滤器
     */
    private static <T> Predicate<T> verifyResultCode(Context context) {
        return response -> {
            BaseResponseModel baseResponse = (BaseResponseModel) response;
            String state = baseResponse.getErrorCode();
            if (REQUESTFECODE4.equals(state)) {
                NetHelper.onLoginFailure(context, baseResponse.getErrorInfo());
                return false;
            }

            return true;
        };
    }

    /**
     * 优先使用这个，可以继续使用操作符
     */
    private static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 聚合了session过滤器,业务过滤器及合并操作 自定义错误回调
     */
    public static <T> ObservableTransformer<T, T>
    applySchedulersAndAllFilter(Context context) {
        return observable -> observable
                .compose(applySchedulers())
                .filter(verifyNotResponseModel(context))
                .filter(verifyNotEmpty(context))
                .filter(verifyResultCode(context))
                .filter(verifyBusiness(context))
                .onErrorReturn(doError(context));
    }

    /**
     * 聚合了session过滤器,简单业务过滤器及合并操作及自定义的错误返回 T对象
     */
    public static <T> ObservableTransformer<BaseResponseModel<T>, T>
    applySchedulersResult(Context context) {
        return observable -> observable
                .compose(applySchedulersAndAllFilter(context))
                .map(t -> t.getData());
    }


    /**
     * 聚合了session过滤器,简单业务过滤器及合并操作
     * 结果返回包含BaseModel的对象
     */
    public static <T> ObservableTransformer<T, T> applySchedulerAndAllFilter(Context context) {
        return applySchedulersAndAllFilter(context);
    }
}


package com.cdkj.token.interfaces;

import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.db.LocalCoinDbModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.cdkj.token.utils.CoinUtil.updateLocalCoinList;

/**
 * 本地币种缓存
 * Created by cdkj on 2017/8/8.
 */

public class LocalCoinCachePresenter {

    private LocalCoinCacheInterface mListener;
    private Call call;
    protected CompositeDisposable mSubscription;

    public LocalCoinCachePresenter(LocalCoinCacheInterface mListener) {
        this.mListener = mListener;
        mSubscription = new CompositeDisposable();
    }

    /**
     * 处理缓存逻辑
     *
     * @param context
     * @param isCallBack 是否执行回调
     */
    public void getCoinList(Context context, boolean isCallBack) {

        Map<String, String> map = new HashMap<>();
        map.put("type", "");
        map.put("ename", "");
        map.put("cname", "");
        map.put("symbol", "");
        map.put("status", "0"); // 0已发布，1已撤下
        map.put("contractAddress", "");

        Call call = RetrofitUtils.createApi(MyApi.class).getCoinList("802267", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseListCallBack<LocalCoinDbModel>(context) {
            @Override
            protected void onSuccess(List<LocalCoinDbModel> data, String SucMessage) {
                saveCoinAsync(data,isCallBack);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                if (mListener != null && isCallBack) {
                    mListener.cacheEnd(null);
                }
            }

            @Override
            protected void onNoNet(String msg) {

            }

            @Override
            protected void onFinish() {
            }
        });
    }


    /**
     * 异步缓存币种
     *
     * @param data
     */
    private void saveCoinAsync(List<LocalCoinDbModel> data, boolean isCallBack) {
        mSubscription.add(Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .map(localCoinDbModels -> {
                    updateLocalCoinList(localCoinDbModels);
                    return localCoinDbModels;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (mListener != null && isCallBack) {
                        mListener.cacheEnd(s);
                    }
                }, throwable -> {
                    if (mListener != null && isCallBack) {
                        mListener.cacheEnd(null);
                    }
                }));
    }

    //处理持有对象
    public void clear() {
        if (this.call != null) {
            this.call.cancel();
            this.call = null;
        }
        if (mSubscription != null) {
            mSubscription.clear();
            mSubscription.dispose();
        }
        this.mListener = null;
    }


}

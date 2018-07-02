package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.db.UserChooseCoinDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

@Route(path = CdRouteHelper.APPSTART)
public class StartActivity extends BaseActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, StartActivity.class));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity
        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.white));
        setContentView(R.layout.activity_start);

        getCoinList();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        getQiniu();
    }

    private void nextTo() {
        mSubscription.add(Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isLogin -> {//延迟两秒进行跳转
                    if (!SPUtilHelper.isLogin(this, false)) {
                        finish();
                        return;
                    }
                    MainActivity.open(this);
                    finish();
                }, Throwable::printStackTrace));
    }


    private void getCoinList() {

        Map<String, String> map = new HashMap<>();
        map.put("type", "");
        map.put("ename", "");
        map.put("cname", "");
        map.put("symbol", "");
        map.put("status", "0"); // 0已发布，1已撤下
        map.put("contractAddress", "");

        Call call = RetrofitUtils.createApi(MyApi.class).getCoinList("802267", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseListCallBack<LocalCoinDbModel>(this) {

            @Override
            protected void onSuccess(List<LocalCoinDbModel> data, String SucMessage) {
                saveCoinAsync(data);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                getQiniu();
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
    private void saveCoinAsync(List<LocalCoinDbModel> data) {
        if (data == null) {
            getQiniu();
            return;
        }
        mSubscription.add(Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .map(localCoinDbModels -> {
                    // 如果数据库已有数据，清空重新加载
                    if (DataSupport.isExist(LocalCoinDbModel.class)) {
                        DataSupport.deleteAll(LocalCoinDbModel.class);
                    }
                    DataSupport.saveAll(localCoinDbModels);
                    return 0;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    getQiniu();
                }, throwable -> {
                    getQiniu();
                }));

    }


    /**
     * 获取七牛服务器链接
     */
    public void getQiniu() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "qiniu_domain");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                SPUtilHelper.saveQiniuUrl(data.getCvalue());
                nextTo();
            }

            @Override
            protected void onNoNet(String msg) {

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                nextTo();
            }

            @Override
            protected void onFinish() {
            }
        });
    }

}

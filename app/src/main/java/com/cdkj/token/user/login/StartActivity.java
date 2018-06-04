package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.SystemParameterModel;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

        setContentView(R.layout.activity_start);
//        open();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getQiniu();
    }

    private void open() {

        mSubscription.add(Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {//延迟两秒进行跳转

//                    if (SPUtilHelper.isLoginNoStart()) {
//                        MainActivity.open(this);
//                    } else {
//                        SignInActivity.open(this, false);
//                    }

                    MainActivity.open(this);
                    finish();

                }, Throwable::printStackTrace));
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
                if (data == null)
                    return;

                SPUtilHelper.saveQiniuUrl("http://" + data.getCvalue() + "/");

                getCoinList();

            }

            @Override
            protected void onNoNet(String msg) {
                open();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
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

        call.enqueue(new BaseResponseListCallBack<BaseCoinModel>(this) {

            @Override
            protected void onSuccess(List<BaseCoinModel> data, String SucMessage) {
                if (data == null)
                    return;

                // 如果数据库已有数据，清空重新加载
                if (DataSupport.isExist(BaseCoinModel.class))
                    DataSupport.deleteAll(BaseCoinModel.class);

                // 初始化交易界面默认所选择的币
                data.get(0).setChoose(true);
                DataSupport.saveAll(data);

                open();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                // 如果数据库已有数据，直接加载数据库
                if (DataSupport.isExist(BaseCoinModel.class)) {
                    open();
                } else {
                    ToastUtil.show(StartActivity.this, "无法连接服务器，请检查网络");
                }
            }

            @Override
            protected void onFinish() {
            }
        });
    }

}

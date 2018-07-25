package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.model.IpCountryInfo;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.views.dialogs.UserPayPasswordInputDialog;
import com.cdkj.token.wallet.backup_guide.BackupWalletStartActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        getQiniuAndNextTo();  //获取七牛地址
    }

    /**
     * 获取IP地址并匹配国家
     */
    private void getIpAndCheckCountry() {
        mSubscription.add(Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> {
                    return SystemUtils.getPublicIp(false);
                })
                .subscribe(s -> {
                    getCountryInfo(s);
                }, throwable -> {
                    checkCountryAfterNext();
                }));
    }

    /**
     * 获取国家信息
     */
    private void getCountryInfo(String ip) {

        Call<String> call = RetrofitUtils.createApi(MyApi.class).getCountryInfoByIp("http://ip.taobao.com/service/getIpInfo.php?ip=" + ip);

        addCall(call);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    IpCountryInfo ipCountryInfo = JSON.parseObject(response.body(), IpCountryInfo.class);
                    if (ipCountryInfo != null && ipCountryInfo.getData() != null) {
                        getCountryListRequestAndCheck(ipCountryInfo.getData().getCountry_id());
                    }

                } catch (Exception e) {
                    checkCountryAfterNext();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                checkCountryAfterNext();
            }
        });

    }

    /**
     * 获取七牛之后 判断用户是否登录 没登陆进行国家匹配
     */
    private void qiniuAfternext() {
        if (SPUtilHelper.isLoginNoStart()) {
            MainActivity.open(this);
            finish();
        } else {
            getIpAndCheckCountry();
        }
    }

    /**
     * 进行国家匹配 之后
     */
    private void checkCountryAfterNext() {
        SignInActivity.open(this, true);
        finish();
    }


    /**
     * 获取七牛服务器链接
     */
    public void getQiniuAndNextTo() {
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
                qiniuAfternext();
            }

            @Override
            protected void onNoNet(String msg) {

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                qiniuAfternext();
            }

            @Override
            protected void onFinish() {
            }
        });
    }


    /**
     * 获取国家列表
     */
    public void getCountryListRequestAndCheck(String countryId) {

        Map<String, String> map = new HashMap<>();

        map.put("status", "1");//status

        Call<BaseResponseListModel<CountryCodeMode>> call = RetrofitUtils.createApi(MyApi.class).getCountryList("801120", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<CountryCodeMode>(this) {
            @Override
            protected void onSuccess(List<CountryCodeMode> data, String SucMessage) {
                checkCountryAndSave(data, countryId);
                checkCountryAfterNext();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                checkCountryAfterNext();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 通过传入的Id比对国家并保存国家信息
     *
     * @param data
     * @param countryId
     */
    private void checkCountryAndSave(List<CountryCodeMode> data, String countryId) {
        for (CountryCodeMode datum : data) {
            if (datum == null) continue;
            if (TextUtils.equals(countryId, datum.getInterSimpleCode())) {
                SPUtilHelper.saveCountryCode(datum.getInterCode());
                SPUtilHelper.saveCountryFlag(datum.getPic());
                break;
            }
        }
    }


}

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
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.model.IpCountryInfo;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.VersionModel;
import com.cdkj.token.utils.wallet.WalletDBAegisUtils;
import com.cdkj.token.wallet.create_guide.CreateWalletStartActivity;
import com.cdkj.token.wallet.import_guide.ImportWalletStartActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cdkj.token.utils.UpdateUtil.isForceUpload;
import static com.cdkj.token.utils.UpdateUtil.startWeb;
import static com.cdkj.token.utils.wallet.WalletHelper.HELPWORD_SPACE_SYMBOL;


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

        getVersion();

    }


    /**
     * 获取IP地址并匹配国家
     */
//    private void getIpAndCheckCountry() {
//        mSubscription.add(Observable.just("")
//                .subscribeOn(Schedulers.newThread())
//                .map(s -> {
//                    return SystemUtils.getPublicIp(false);
//                })
//                .subscribe(s -> {
//                    getCountryInfo(s);
//                }, throwable -> {
//                    checkCountryAfterNext();
//                }));
//    }

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
            SignInActivity.open(this, true);
            finish();
//            getIpAndCheckCountry();
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
                SPUtilHelper.saveCountryInterCode(datum.getInterCode());
                SPUtilHelper.saveCountryCode(datum.getCode());
                SPUtilHelper.saveCountryFlag(datum.getPic());
                MyConfig.changeLanguageForCountry(this, datum.getCode());
                break;
            }
        }
    }

    /**
     * 获取最新版本
     *
     * @return
     */
    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "android-c");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("660918", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(this) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null) {
                    checkDbAegis();
                    return;
                }

                if (data.getVersion() > AppUtils.getAppVersionCode(StartActivity.this)) {  //版本号不一致说明有更新
                    showUploadDialog(data);
                } else {
                    checkDbAegis();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                checkDbAegis();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 显示更新dialog
     *
     * @param versionModel
     */
    private void showUploadDialog(VersionModel versionModel) {

        if (isForceUpload(versionModel.getForceUpdate())) { // 强制更新

            showSureDialog(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(StartActivity.this, versionModel.getDownloadUrl());
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                finish();
            });

        } else {
            showDoubleWarnListen(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(StartActivity.this, versionModel.getDownloadUrl());
            }, view -> {
                checkDbAegis();
            });
        }
    }

    /**
     * 数据库兼容检测
     */
    private void checkDbAegis() {

        mSubscription.add(Observable.just("")
                .observeOn(Schedulers.newThread())
                .flatMap(s -> Observable.fromIterable(WalletDBAegisUtils.findBtcInfoEmptyData()))
                .subscribe(s -> {
                    WalletDBAegisUtils.createBTCInfoAndUpdate(s.getUserId(), StringUtils.splitAsList(s.getHelpWordsEn(), HELPWORD_SPACE_SYMBOL));
                }, throwable -> getQiniuAndNextTo(), () -> getQiniuAndNextTo()));

    }


    protected void showDoubleWarnListen(String title, String content, CommonDialog.OnPositiveListener onPositiveListener, CommonDialog.OnNegativeListener onNegativeListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(title).setContentMsg(content)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), onNegativeListener, false);

        commonDialog.show();
    }

    /**
     * 只显示确认弹框的按钮
     *
     * @param title
     * @param str
     * @param onPositiveListener
     */
    protected void showSureDialog(String title, String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (this == null || isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(title)
                .setContentMsg(str)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener);

        commonDialog.show();
    }


}

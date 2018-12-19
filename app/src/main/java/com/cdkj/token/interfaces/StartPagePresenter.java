package com.cdkj.token.interfaces;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MyApplication;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.VersionModel;
import com.cdkj.token.utils.wallet.WalletDBAegisUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.cdkj.token.utils.wallet.WalletHelper.HELPWORD_SPACE_SYMBOL;

/**
 * Created by cdkj on 2018/8/29.
 */


/**
 * 启动页
 * 检测数据库兼容---检测更新---获取七牛url
 * ---是否登录
 * ——————是  进入主页
 * ——————否   取国家列表  取列表第一位国家信息并保存(空数据默认为中国)  进入登录页
 */
//TODO 启动请求嵌套优化
public class StartPagePresenter {

    public StartPageView startPageView;

    protected CompositeDisposable mSubscription;

    public StartPagePresenter(StartPageView startPageView) {
        this.startPageView = startPageView;
        mSubscription = new CompositeDisposable();
    }


    /**
     * 开始
     */
    public void start() {
        checkDBAegis();
    }

    /**
     * 当有更新提示的时候，用户选择了不进行更新，则继续进行
     */
    public void refuseUpdate() {
        getQiNiuUrl();
    }


    /**
     * 获取更新，下一步是获取七牛
     */
    private void checkVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "android-c");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("660918", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(null) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null) {
                    refuseUpdate();
                    return;
                }

                if (data.getVersion() > AppUtils.getAppVersionCode(MyApplication.getInstance())) {  //版本号不一致说明有更新

                    if (startPageView != null) {
                        startPageView.versionUpdateDialog(data);
                    }

                } else {
                    refuseUpdate();
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                refuseUpdate();
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 检测数据库升级兼容，下一步检查版本更新
     */
    private void checkDBAegis() {
        mSubscription.add(Observable.just("")
                .observeOn(Schedulers.newThread())
                .flatMap(s -> Observable.fromIterable(WalletDBAegisUtils.findBtcInfoEmptyData()))
                .subscribe(s -> {
                    WalletDBAegisUtils.createBTCInfoAndUpdate(s.getUserId(), StringUtils.splitAsList(s.getHelpWordsEn(), HELPWORD_SPACE_SYMBOL));
                }, throwable -> checkVersion(), () -> checkVersion()));

    }

    /**
     * 获取七牛URL，下一步判断是否登录，已经登录直接进入主页，未登录获取国家列表
     */
    private void getQiNiuUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "qiniu_domain");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getRequestJsonString(map));
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(null) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                SPUtilHelper.saveQiniuUrl(data.getCvalue());
                qiniuAfter();
            }

            @Override
            protected void onNoNet(String msg) {

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                qiniuAfter();
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 获取国家列表，保存列表第一位国家信息并启动登录界面
     */
    private void getCountryList() {
        Map<String, String> map = new HashMap<>();

        map.put("status", "1");//status

        Call<BaseResponseListModel<CountryCodeMode>> call = RetrofitUtils.createApi(MyApi.class).getCountryList("801120", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<CountryCodeMode>(null) {
            @Override
            protected void onSuccess(List<CountryCodeMode> data, String SucMessage) {
                checkCountryAndSave(data);
                startLogin();
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                startLogin();
            }

            @Override
            protected void onFinish() {

            }
        });
    }


    /**
     * 获取第一个国家并保存国家信息
     *
     * @param data
     */
    private void checkCountryAndSave(List<CountryCodeMode> data) {
        if (data == null || data.isEmpty()) return;
        CountryCodeMode countryCodeMode = data.get(0);
        if (countryCodeMode == null) return;
        SPUtilHelper.saveCountryInterCode(countryCodeMode.getInterCode());
        SPUtilHelper.saveCountryCode(countryCodeMode.getCode());
        SPUtilHelper.saveCountryFlag(countryCodeMode.getPic());
        AppConfig.changeLanguageForCountry(MyApplication.getInstance(), countryCodeMode.getInterCode());
        AppConfig.changeLocalCoinTypeForCountry(countryCodeMode.getInterCode());
    }

    /**
     * 获取七牛之后 判断用户是否登录 没登录进行国家匹配
     */
    private void qiniuAfter() {
        if (SPUtilHelper.isLoginNoStart()) {
            startMain();
        } else {
            getCountryList();
        }
    }

    /**
     * 启动主页
     */
    private void startMain() {
        if (startPageView != null) {
            startPageView.startMain();
        }
    }

    /**
     * 启动登录页
     */
    private void startLogin() {
        if (startPageView != null) {
            startPageView.startLogin();
        }
    }


    /**
     * 添加网络请求对象
     *
     * @param call
     */
    private void addCall(Call call) {
        if (startPageView != null) {
            startPageView.addNetCall(call);
        }
    }

    //处理持有对象
    public void clear() {
        if (mSubscription != null) {
            mSubscription.clear();
            mSubscription.dispose();
        }
    }


}

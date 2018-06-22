package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.utils.CipherUtils;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.wallet.IntoWalletBeforeActivity;
import com.cdkj.token.wallet.create_guide.WalletBackupCheckActivity;
import com.cdkj.token.wallet.import_guide.WalletImportWordsInputActivity;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.crud.DataSupport;
import org.litepal.util.cipher.CipherUtil;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

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

        getQiniu();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        getQiniu();
    }

    private void nextTo() {
        mSubscription.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map(aLong -> /*WalletHelper.isWalletFirstCheck() &&*/ WalletHelper.isHaveWalletCache())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ishave -> {//延迟两秒进行跳转
                    if (ishave) {  //如果已经有了钱包 并且通过验证
                        MainActivity.open(this);
                    } else {
                        IntoWalletBeforeActivity.open(this);
                    }
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

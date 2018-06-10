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
import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.security.SecureRandom;
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

        setContentView(R.layout.activity_start);
//        makeMnemonic();
//        open();
        getQiniu();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getQiniu();
    }

    private void open() {

        mSubscription.add(Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map(aLong -> WalletHelper.isHaveWalletCache())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ishave -> {//延迟两秒进行跳转
                    if (ishave) {  //如果已经有了钱包
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
                SPUtilHelper.saveQiniuUrl("http://" + data.getCvalue() + "/");
            }

            @Override
            protected void onFinish() {
                disMissLoading();
                getCoinList();
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

    // 生成助记词
    private List<String> makeMnemonic() {

        List<String> mnemonicList = null;

        try {

            List<String> defaultMnenonic = new ArrayList<>();
            defaultMnenonic.add("club");
            defaultMnenonic.add("baby");
            defaultMnenonic.add("index");
            defaultMnenonic.add("hint");
            defaultMnenonic.add("library");
            defaultMnenonic.add("vendor");
            defaultMnenonic.add("judge");
            defaultMnenonic.add("napkin");
            defaultMnenonic.add("media");
            defaultMnenonic.add("bullet");
            defaultMnenonic.add("action");
            defaultMnenonic.add("wine");

            // 钱包种子
            DeterministicSeed seed1 = new DeterministicSeed(new SecureRandom(),
                    128, "", Utils.currentTimeSeconds());

            // 助记词
            mnemonicList = seed1.getMnemonicCode();

            DeterministicKeyChain keyChain1 = DeterministicKeyChain.builder()
                    .seed(seed1).build();

            List<ChildNumber> keyPath = HDUtils.parsePath("M/44H/60H/0H/0/0");

            // DeterministicKey key2 =
            // keyChain2.getKey(KeyPurpose.RECEIVE_FUNDS);
            DeterministicKey key1 = keyChain1.getKeyByPath(keyPath, true);
            BigInteger privKey1 = key1.getPrivKey();

            Credentials credentials1 = Credentials
                    .create(privKey1.toString(16));

            System.out.println(seed1.getMnemonicCode());

            System.out.println("seed1=" + seed1.toHexString());

            System.out.println("privateKey1:" + key1.getPrivateKeyAsHex());

            System.out.println("address1: " + credentials1.getAddress());

            DeterministicSeed seed2 = new DeterministicSeed(defaultMnenonic,
                    null, "", Utils.currentTimeSeconds());

            DeterministicKeyChain keyChain2 = DeterministicKeyChain.builder()
                    .seed(seed2).build();

            DeterministicKey key2 = keyChain2.getKeyByPath(keyPath, true);
            BigInteger privKey2 = key2.getPrivKey();

            Credentials credentials2 = Credentials
                    .create(privKey2.toString(16));

            System.out.println("seed2=" + seed2.toHexString());

            System.out.println("privateKey2:" + key2.getPrivateKeyAsHex());

            System.out.println("address2: " + credentials2.getAddress());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mnemonicList;
    }

}

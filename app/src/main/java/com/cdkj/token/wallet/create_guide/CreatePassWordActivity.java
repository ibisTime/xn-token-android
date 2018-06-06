package com.cdkj.token.wallet.create_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityCreatePassWordBinding;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.views.password.PassWordLayout;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.crud.DataSupport;
import org.spongycastle.jcajce.provider.digest.MD5;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 创建密码
 * Created by cdkj on 2018/6/5.
 */

public class CreatePassWordActivity extends AbsBaseLoadActivity {

    private ActivityCreatePassWordBinding mBinding;

    private String mPassWord;//用户输入的密码


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CreatePassWordActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_create_pass_word, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.create_wallet);
        initPassWordVIewListener();
    }

    private void initPassWordVIewListener() {

        mBinding.passWordLayout.passWordLayout.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {
            }

            @Override
            public void onNull() {

            }

            @Override
            public void onFinished(String pwd) {
                if (TextUtils.isEmpty(mPassWord)) {
                    mPassWord = pwd;
                    mBinding.passWordLayout.passWordLayout.removeAllPwd();
                    mBinding.tvTips.setText(R.string.please_check_transaction);
                    return;
                }

                if (!TextUtils.equals(mPassWord, pwd)) { //两次输入密码不一致
                    mPassWord = "";
                    mBinding.passWordLayout.passWordLayout.removeAllPwd();
                    mBinding.tvTips.setText(R.string.please_set_transaction_pass_word);
                    UITipDialog.showInfo(CreatePassWordActivity.this, getString(R.string.password_error));

                } else {               //两次密码输入一致
                    createMnemonicWordsAsyn(pwd);
                }
            }
        });

    }

    /**
     * 异步创建助记词并保存
     *
     * @param paword
     */
    public void createMnemonicWordsAsyn(String paword) {
        showLoadingDialog();
        mSubscription.add(
                Observable.just(paword)
                        .subscribeOn(Schedulers.newThread())
                        .map(password -> WalletHelper.createWalletInfobyPassWord(password))
                        .filter(walletDBModel -> walletDBModel != null)
                        .map(walletDBModel -> walletDBModel.save())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> disMissLoading())
                        .subscribe(isSave -> {
                            if (isSave) {
                                CreateWalletSuccessActivity.open(CreatePassWordActivity.this);
                            } else {
                                ToastUtil.show(this, getString(R.string.wallet_create_fail));
                            }
                            finish();
                        }, throwable -> {
                            ToastUtil.show(this, getString(R.string.wallet_create_fail));
                            finish();
                        })
        );

    }


/*    public void test2() {

        // 钱包种子
        DeterministicSeed seed1 = new DeterministicSeed(new SecureRandom(),
                128, "", Utils.currentTimeSeconds());

        // 助记词
        List<String> mnemonicList = seed1.getMnemonicCode();

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

    }


    // 生成助记词
    private List<String> makeMnemonic() {

        List<String> mnemonicList = null;

        try {

            List<String> defaultMnenonic = new ArrayList<>();
            defaultMnenonic.add("assume");
            defaultMnenonic.add("arrive");
            defaultMnenonic.add("acid");
            defaultMnenonic.add("apple");
            defaultMnenonic.add("badge");
            defaultMnenonic.add("small");
            defaultMnenonic.add("logic");
            defaultMnenonic.add("right");
            defaultMnenonic.add("say");
            defaultMnenonic.add("smile");
            defaultMnenonic.add("shy");
            defaultMnenonic.add("trouble");

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
    }*/

}

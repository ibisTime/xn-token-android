package com.cdkj.token.utils;

import android.content.ContentValues;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.SPUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MyApplication;
import com.cdkj.token.R;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.model.WalletDBModel;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.crud.DataSupport;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import static org.litepal.crud.DataSupport.findLast;

/**
 * 钱包工具类
 * Created by cdkj on 2018/6/6.
 */

public class WalletHelper {

    //助记词分隔符
    public final static String HELPWORD_SIGN = ",";
    public final static String HDPATH = "M/44H/60H/0H/0/0";//生成助记词和解析时使用


    public final static String COIN_ETH = "eth";// 币种类型
    public final static String COIN_WAN = "wan";// 币种类型

    /**
     * 获取本地配置币种
     */
    public static List<LocalCoinModel> getConfigLocalCoinList() {

        String[] coinType = MyApplication.getInstance().getResources().getStringArray(R.array.coins);

        List<LocalCoinModel> localCoinModels = new ArrayList<>();

        String configStr = WalletHelper.getWalletCoinConfig();

        for (String type : coinType) {

            if (configStr.indexOf(type) == -1) {//如果不存在配置 则不添加
                continue;
            }

            LocalCoinModel localCoinModel = new LocalCoinModel();
            localCoinModel.setCoinType(type);
            localCoinModel.setCoinEName(getCoinENameByType(type));
            localCoinModel.setCoinCName(getCoinCNameByType(type));
            localCoinModel.setCoinShortName(getCoinShrotNameByType(type));
            localCoinModels.add(localCoinModel);

        }
        return localCoinModels;
    }

    /**
     * 获取本地所有币种
     */
    public static List<LocalCoinModel> getLocalCoinList() {

        String[] coinType = MyApplication.getInstance().getResources().getStringArray(R.array.coins);

        List<LocalCoinModel> localCoinModels = new ArrayList<>();

        for (String type : coinType) {

            LocalCoinModel localCoinModel = new LocalCoinModel();
            localCoinModel.setCoinType(type);
            localCoinModel.setCoinEName(getCoinENameByType(type));
            localCoinModel.setCoinCName(getCoinCNameByType(type));
            localCoinModel.setCoinShortName(getCoinShrotNameByType(type));
            localCoinModels.add(localCoinModel);
        }
        return localCoinModels;
    }

    /**
     * 根据币种类型获取要显示的icon
     *
     * @param type
     * @return
     */
    public static int getCoinIconByType(String type) {
        if (TextUtils.isEmpty(type)) {
            return R.drawable.default_pic;
        }

        switch (type) {
            case COIN_ETH:
                return R.drawable.eth_icon;
            case COIN_WAN:
                return R.drawable.wan_icon;
        }
        return R.drawable.default_pic;

    }

    /**
     * 根据币种类型获取eName
     *
     * @param type
     * @return
     */
    public static String getCoinENameByType(String type) {
        if (TextUtils.isEmpty(type)) {
            return "";
        }
        switch (type) {
            case COIN_ETH:
                return MyApplication.getInstance().getString(R.string.coin_eth_ename);
            case COIN_WAN:
                return MyApplication.getInstance().getString(R.string.coin_wan_ename);
        }
        return "";

    }

    /**
     * 根据币种类型获取CName
     *
     * @param type
     * @return
     */
    public static String getCoinCNameByType(String type) {
        if (TextUtils.isEmpty(type)) {
            return "";
        }
        switch (type) {
            case COIN_ETH:
                return MyApplication.getInstance().getString(R.string.coin_eth_cname);
            case COIN_WAN:
                return MyApplication.getInstance().getString(R.string.coin_wan_came);
        }
        return "";

    }

    /**
     * 根据币种类型获取CName
     *
     * @param type
     * @return
     */
    public static String getCoinShrotNameByType(String type) {
        if (TextUtils.isEmpty(type)) {
            return "";
        }
        switch (type) {
            case COIN_ETH:
                return "ETH";
            case COIN_WAN:
                return "WAN";
        }
        return "";

    }


    /**
     * 保存用户币种配置
     *
     * @param config 保存数据为币种type组成的字符串用，分割  1,2,3...
     */
    public static void saveWalletCoinConfig(String config) {
        SPUtils.put(MyApplication.getInstance(), "coinConfig", config);
    }

    /**
     * 获取用户币种配置
     */
    public static String getWalletCoinConfig() {
        return SPUtils.getString(MyApplication.getInstance(), "coinConfig", "");
    }

    /**
     * 删除用户币种配置
     */
    public static void removeWalletCoinConfig() {
        SPUtils.remove(MyApplication.getInstance(), "coinConfig");
    }


    /**
     * 查询币种是否在配置中
     *
     * @param coinType
     * @return
     */
    public static boolean queryTypeInWalletConfig(String coinType) {
        return getWalletCoinConfig().indexOf(coinType) != -1;
    }


    /**
     * 根据单词获取ETH助记词等信息
     *
     * @param password
     * @return
     */
    public static WalletDBModel createWalletInfobyPassWord(String password) {
        // 钱包种子
        DeterministicSeed seed1 = new DeterministicSeed(new SecureRandom(),
                128, "", Utils.currentTimeSeconds());

        // 助记词
        List<String> mnemonicList = seed1.getMnemonicCode();

        DeterministicKeyChain keyChain1 = DeterministicKeyChain.builder()
                .seed(seed1).build();

        List<ChildNumber> keyPath = HDUtils.parsePath(HDPATH);


        DeterministicKey key1 = keyChain1.getKeyByPath(keyPath, true);
        BigInteger privKey1 = key1.getPrivKey();

        Credentials credentials1 = Credentials
                .create(privKey1.toString(16));


        System.out.println("privateKey__2:" + key1.getPrivateKeyAsHex());

        System.out.println("address___2: " + credentials1.getAddress());

        WalletDBModel walletDBModel = new WalletDBModel();
        walletDBModel.setAddress(credentials1.getAddress());
        walletDBModel.setPassWord(password);
        walletDBModel.setHelpcenterEn(StringUtils.listToString(mnemonicList, HELPWORD_SIGN)); //储存下来 用，分割
        walletDBModel.setPrivataeKey(key1.getPrivateKeyAsHex());


        return walletDBModel;
    }


    /**
     * 获取保存的助记词列表
     *
     * @return
     */
    public static List<String> getHelpWordsList() {
        WalletDBModel walletDBModel = findLast(WalletDBModel.class);
        if (walletDBModel == null) {
            return new ArrayList<>();
        }
        return StringUtils.splitAsList(walletDBModel.getHelpcenterEn(), HELPWORD_SIGN);
    }

    /**
     * 获取保存的助记词列表
     *
     * @param step 分隔符
     * @return
     */
    public static String getHelpWords(String step) {
        return StringUtils.listToString(getHelpWordsList(), step);
    }

    /**
     * 清除缓存数据
     */
    public static void clearCache() {
        removeWalletCoinConfig();
        DataSupport.deleteAll(WalletDBModel.class);
    }


    /**
     * 验证助记词
     *
     * @param defaultMnenonic
     * @return
     */
    public static boolean checkMnenonic(List<String> defaultMnenonic) {
        try {
            new MnemonicCode().check(defaultMnenonic);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据助记词生成ETH私钥和地址
     *
     * @param defaultMnenonic
     */
    public static WalletDBModel createWalletInfobyMnenonic(List<String> defaultMnenonic) {

        DeterministicSeed seed = new DeterministicSeed(defaultMnenonic,
                null, "", Utils.currentTimeSeconds());

        DeterministicKeyChain keyChain2 = DeterministicKeyChain.builder()
                .seed(seed).build();
        List<ChildNumber> keyPath = HDUtils.parsePath(HDPATH);
        DeterministicKey key = keyChain2.getKeyByPath(keyPath, true);
        BigInteger privKey = key.getPrivKey();

        Credentials credentials = Credentials
                .create(privKey.toString(16));

        WalletDBModel walletDBModel = new WalletDBModel();
        walletDBModel.setAddress(credentials.getAddress());
        walletDBModel.setHelpcenterEn(StringUtils.listToString(defaultMnenonic, HELPWORD_SIGN)); //储存下来 用，分割
        walletDBModel.setPrivataeKey(key.getPrivateKeyAsHex());

        return walletDBModel;
    }

    /**
     * 获取私钥和地址
     *
     * @param
     */
    public static WalletDBModel getPrivateKeyAndAddress() {
        WalletDBModel walletDBModel = DataSupport.findLast(WalletDBModel.class);
        return walletDBModel;
    }

    /**
     * 判断是否有钱包储存
     *
     * @return
     */
    public static boolean isHaveWalletCache() {
        return DataSupport.findAll(WalletDBModel.class).size() > 0;
    }

    /**
     * 修改用户钱包密码
     *
     * @param password
     * @return
     */
    public static void changeWalletPassWord(String password) {
        ContentValues values = new ContentValues();
        values.put("passWord", password);
        DataSupport.updateAll(WalletDBModel.class, values);
    }

    /**
     * 获取用户钱包密码
     *
     * @param
     * @return
     */
    public static String getWalletPassword() {
        try {
            return findLast(WalletDBModel.class).getPassWord();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 判断用户输入的助记词是否和本地的相同
     *
     * @param words
     * @return
     */
    public static boolean checkCacheWords(String words) {

        try {
            String mwords = findLast(WalletDBModel.class).getHelpcenterEn();
            return TextUtils.equals(words, mwords);

        } catch (Exception e) {

        }
        return false;
    }


}

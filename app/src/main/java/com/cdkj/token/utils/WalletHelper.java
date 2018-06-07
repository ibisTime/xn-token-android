package com.cdkj.token.utils;

import android.content.ContentValues;

import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.model.WalletDBModel;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.crud.DataSupport;
import org.web3j.crypto.Credentials;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * 钱包辅助
 * Created by cdkj on 2018/6/6.
 */

public class WalletHelper {

    //助记词分隔符
    public static String HELPWORD_SIGN = ",";

    /**
     * 根据单词获取助记词等信息
     *
     * @param password
     * @return
     */
    public static WalletDBModel createWalletInfobyPassWord(String password) {
        DeterministicSeed seed1 = new DeterministicSeed(new SecureRandom(),
                128, "", Utils.currentTimeSeconds());

        // 助记词
        List<String> mnemonicList = seed1.getMnemonicCode();

        // 钱包主秘钥
        DeterministicKey key = HDKeyDerivation
                .createMasterPrivateKey(seed1.getSeedBytes());

        Credentials credentials1 = Credentials
                .create(key.getPrivKey().toString(16));

        WalletDBModel walletDBModel = new WalletDBModel();
        walletDBModel.setAddress(credentials1.getAddress());
        walletDBModel.setPassWord(password);
        walletDBModel.setHelpcenterEn(StringUtils.listToString(mnemonicList, HELPWORD_SIGN)); //储存下来 用，分割
        walletDBModel.setPrivataeKey(key.getPrivateKeyAsHex());

        return walletDBModel;
    }


    /**
     * 获取保存的助记词列表
     *
     * @return
     */
    public static List<String> getHelpWordsList() {
        WalletDBModel walletDBModel = DataSupport.findLast(WalletDBModel.class);
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
     * 根据助记词生成私钥和地址
     *
     * @param defaultMnenonic
     */
    public static WalletDBModel createWalletInfobyMnenonic(List<String> defaultMnenonic) {

        DeterministicSeed seed = new DeterministicSeed(defaultMnenonic,
                null, "", Utils.currentTimeSeconds());

        DeterministicKey key = HDKeyDerivation
                .createMasterPrivateKey(seed.getSeedBytes());

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
            return DataSupport.findLast(WalletDBModel.class).getPassWord();
        } catch (Exception e) {
        }
        return "";
    }

}

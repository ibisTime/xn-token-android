package com.cdkj.token.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.SPUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MyApplication;
import com.cdkj.token.R;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.model.WalletInfoDBModel;
import com.cdkj.token.utils.wan.WanRawTransaction;
import com.cdkj.token.utils.wan.WanTransactionEncoder;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.crud.DataSupport;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static com.cdkj.token.utils.AccountUtil.UNIT_MIN;
import static com.cdkj.token.utils.AccountUtil.UNIT_POW;
import static org.litepal.crud.DataSupport.findLast;

/**
 * 钱包工具类
 * Created by cdkj on 2018/6/6.
 */
public class WalletHelper {

    //助记词分隔符
    public final static String HELPWORD_SIGN = " ";
    public final static String HDPATH = "M/44H/60H/0H/0/0";//生成助记词和解析时使用

    //        public final static String WEB3J_URL = "https://mainnet.infura.io/ZJR3JJlmLyf5mg4A9UxA";//
    public final static String WEB3J_URL = "https://rinkeby.infura.io/qfyZa8diWhk28tT9Cwft";//

    public final static String WEB3J_URL_WAN = "http://120.26.6.213:8546";//

    public final static String TO_BROWSER_URL = "https://rinkeby.etherscan.io/tx/";//跳向区块链浏览器

    public final static String WAN_TO_BROWSER_URL = "http://47.104.61.26/block/trans/";//跳向区块链浏览器

    public final static String WALLPASS = "tha_etc";//


    //TODO 币种使用枚举类
    public final static String COIN_ETH = "ETH";// 币种类型 ETH
    public final static String COIN_WAN = "WAN";// 币种类型 WAN

    //本地币种类型
    public final static String[] COIN_COUNT = new String[]{COIN_ETH, COIN_WAN};// 币种类型


    /**
     * 是否第一次配置
     *
     * @param isFirst
     */
    public static void saveFirstConfig(boolean isFirst) {
        SPUtils.put(MyApplication.getInstance(), "isFirstConfig", isFirst);
    }

    /**
     * 是否第一次配置
     *
     * @param
     */
    public static boolean getFirstConfig() {
        return SPUtils.getBoolean(MyApplication.getInstance(), "isFirstConfig", true);
    }

    /**
     * 获取本地配置币种
     */
    public static List<LocalCoinModel> getConfigLocalCoinList() {

        String[] coinType = COIN_COUNT;

        List<LocalCoinModel> localCoinModels = new ArrayList<>();

        String configStr = WalletHelper.getWalletCoinConfig();

        for (String type : coinType) {

            if (!getFirstConfig() && configStr.indexOf(type) == -1) {//如果不存在配置 则不添加
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

        String[] coinType = COIN_COUNT;

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
     * @param
     * @return
     */
    public static boolean createWalletInfobyPassWord(String coinType) throws Exception {
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
        walletDBModel.setHelpWordsrEn(encrypt(StringUtils.listToString(mnemonicList, HELPWORD_SIGN))); //储存下来 用，分割
        walletDBModel.setCoinType(coinType);
        walletDBModel.setPrivataeKey(encrypt(key1.getPrivateKeyAsHex()));

        return walletDBModel.save();
    }

    /**
     * 加密
     *
     * @param privateKeyAsHex
     * @return
     * @throws Exception
     */
    private static String encrypt(String privateKeyAsHex) {
        try {
            return CipherUtils.encrypt(privateKeyAsHex, WALLPASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKeyAsHex;
    }

    /**
     * 解密
     *
     * @param privateKeyAsHex
     * @return
     * @throws Exception
     */
    private static String decrypt(String privateKeyAsHex) {
        try {
            return CipherUtils.decrypt(privateKeyAsHex, WALLPASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return privateKeyAsHex;
    }


    /**
     * 根据币种获取保存的助记词列表
     *
     * @param coinType
     * @return
     */
    public static List<String> getHelpWordsListByCoinType(String coinType) {

        Cursor cursor = getCursorByCoinTypeSQL(coinType);

        if (cursor != null && cursor.moveToFirst()) {

            return StringUtils.splitAsList(cursor.getString(cursor.getColumnIndex("helpwordsren")), HELPWORD_SIGN);
        }

        return new ArrayList<>();
    }

    /**
     * 根据币种类型获取数据库Cursor
     *
     * @param coinType
     * @return
     */
    private static Cursor getCursorByCoinTypeSQL(String coinType) {
        return DataSupport.findBySQL("select * from WalletDBModel where coinType=?", coinType);
    }

    /**
     * 获取私钥和地址
     *
     * @param coinType
     * @return
     */
    public static WalletDBModel getPrivateKeyAndAddressByCoinType(String coinType) {
        Cursor cursor = getCursorByCoinTypeSQL(coinType);
        WalletDBModel walletDBModel = new WalletDBModel();
        if (cursor != null && cursor.moveToFirst()) {
            walletDBModel.setPrivataeKey(decrypt(cursor.getString(cursor.getColumnIndex("privataekey"))));
            walletDBModel.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        }

        return walletDBModel;
    }

    /**
     * 获取保存的助记词列表
     *
     * @return
     */
    public static String getHelpWordsByCoinType(String coinType) {
        Cursor cursor = getCursorByCoinTypeSQL(coinType);

        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("helpwordsren"));
        }

        return "";

    }

    /**
     * 清除缓存数据
     */
    public static void clearCache() {
        removeWalletCoinConfig();
        DataSupport.deleteAll(WalletDBModel.class);
        DataSupport.deleteAll(WalletInfoDBModel.class);
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
    public static boolean createWalletInfobyMnenonic(List<String> defaultMnenonic, String coinType) {

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
        walletDBModel.setHelpWordsrEn(encrypt(StringUtils.listToString(defaultMnenonic, HELPWORD_SIGN))); //储存下来 用，分割
        walletDBModel.setPrivataeKey(encrypt(key.getPrivateKeyAsHex()));
        walletDBModel.setCoinType(coinType);

        return walletDBModel.save();
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
        values.put("walletPassWord", EncryptionString(password));
        DataSupport.updateAll(WalletInfoDBModel.class, values);
    }

    /**
     * 保存用户钱包密码
     *
     * @param password
     * @return
     */
    public static boolean saveWalletPassWord(String password) {
        WalletInfoDBModel values = new WalletInfoDBModel();
        values.setWalletPassWord(EncryptionString(password));
        return values.save();
    }

    /**
     * 获取用户钱包密码
     *
     * @param
     * @return
     */
    public static String getWalletPassword() {
        try {
            return findLast(WalletInfoDBModel.class).getWalletPassWord();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 检测旧密码
     *
     * @param pwd
     */
    public static boolean checkOldPassword(String pwd) {
        return TextUtils.equals(WalletHelper.EncryptionString(pwd), WalletHelper.getWalletPassword());
    }

    /**
     * 判断用户输入的助记词是否和本地的相同
     *
     * @param words
     * @return
     */
    public static boolean checkCacheWords(String words) {

        try {
            String mwords = encrypt(findLast(WalletDBModel.class).getHelpWordsrEn());
            return TextUtils.equals(words, mwords);

        } catch (Exception e) {

        }
        return false;
    }


    /**
     * @param walletDBModel 包含私钥 地址等信息
     * @param toAddress
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static EthSendTransaction transfer2(WalletDBModel walletDBModel, String toAddress, String money) throws IOException, ExecutionException, InterruptedException {


        Web3j web3j = Web3jFactory.build(new HttpService(WEB3J_URL));

//        Credentials credentials = WalletUtils.loadBip39Credentials(
//                "", walletDBModel.getHelpWordsrEn());

        Credentials credentials = Credentials.create(walletDBModel.getPrivataeKey());

        EthGetTransactionCount ethGetTransactionCount = web3j
                .ethGetTransactionCount(walletDBModel.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync().get();
        //
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        // TODO 动态获取
        BigInteger gasLimit = BigInteger.valueOf(21000);
        //矿工费
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

        BigInteger priceValue = new BigDecimal(money).multiply(UNIT_MIN.pow(UNIT_POW)).toBigInteger(); //需要转账的金额

        // 本地签名的
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, toAddress,
                priceValue, "");

//        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
//                nonce, gasPrice, gasLimit, toAddress, price);


        // 签名
        byte[] signedMessage = TransactionEncoder.signMessage(
                rawTransaction, credentials);

        String txHash = Numeric.toHexString(signedMessage);

        EthSendTransaction ethSendTransaction = web3j
                .ethSendRawTransaction(txHash).sendAsync().get(); //sendAsync().get()

        return ethSendTransaction;
//        if (ethSendTransaction.getError() != null) {
//            // failure
//        }
//        txHash = ethSendTransaction.getTransactionHash();
    }


    /**
     * 根据地址查询余额
     *
     * @param address
     * @return
     * @throws IOException
     */
    public static BigInteger getBalance(String address) throws IOException {
        Web3j web3j = Web3jFactory.build(new HttpService(WEB3J_URL));
        EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        BigInteger balance = ethGetBalance.getBalance();
        return balance;
    }

    /**
     * 转账同步请求 需在子线程中操作
     *
     * @param to
     * @param money
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */

    public static EthSendTransaction transfer(WalletDBModel walletDBModel, String to, String money, BigInteger GAS_LIMIT) throws ExecutionException, InterruptedException, IOException {

        Web3j web3j = Web3jFactory.build(new HttpService(WEB3J_URL));
        //转账人账户地址
        String ownAddress = walletDBModel.getAddress();
        //被转人账户地址
        String toAddress = to;

        //转账人私钥
        Credentials credentials = Credentials.create(walletDBModel.getPrivataeKey());

        //getNonce（交易的笔数）
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                ownAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        BigInteger GAS_PRICE = web3j.ethGasPrice().send().getGasPrice();

        //创建交易，这里是转x个以太币
        BigInteger priceValue = new BigDecimal(money).multiply(UNIT_MIN.pow(UNIT_POW)).toBigInteger(); //需要转账的金额

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, toAddress, priceValue);

        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();//sendAsync().get()
//        String transactionHash = ethSendTransaction.getTransactionHash();
        //获得到transactionHash后就可以到以太坊的网站上查询这笔交易的状态了
        return ethSendTransaction;
    }

    /**
     * 转账同步请求 需在子线程中操作
     *
     * @param to
     * @param money
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */

    public static EthSendTransaction transferWan(WalletDBModel walletDBModel, String to, String money, BigInteger GAS_LIMIT) throws ExecutionException, InterruptedException, IOException {

        Web3j web3j = Web3jFactory.build(new HttpService(WEB3J_URL_WAN));
        //转账人账户地址
        String ownAddress = walletDBModel.getAddress();
        //被转人账户地址
        String toAddress = to;

        //转账人私钥
        Credentials credentials = Credentials.create(walletDBModel.getPrivataeKey());

        //getNonce（交易的笔数）
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                ownAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        BigInteger GAS_PRICE = web3j.ethGasPrice().send().getGasPrice();

        //创建交易
        BigInteger priceValue = new BigDecimal(money).multiply(UNIT_MIN.pow(UNIT_POW)).toBigInteger(); //需要转账的金额

        WanRawTransaction rawTransaction = WanRawTransaction.createTransaction(
                nonce, GAS_PRICE, GAS_LIMIT, toAddress, priceValue, "");

        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = WanTransactionEncoder.signMessage(rawTransaction, credentials);

        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();//sendAsync().get()
//        String transactionHash = ethSendTransaction.getTransactionHash();
        //获得到transactionHash后就可以到以太坊的网站上查询这笔交易的状态了
        return ethSendTransaction;
    }


    /**
     * 获取手续费（矿工费）
     */
    public static BigInteger getGasLimitValue() throws Exception {
        Web3j web3j = Web3jFactory.build(new HttpService(WEB3J_URL));
        BigInteger gaslimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        return gasPrice.multiply(gaslimit);
    }

    /**
     * 获取手续费（矿工费）
     */
    public static BigInteger getGasValue() throws Exception {
        Web3j web3j = Web3jFactory.build(new HttpService(WEB3J_URL));
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        return gasPrice;
    }

    /**
     *
     */
    public static BigInteger getGasLimit() {
        BigInteger gaslimit = BigInteger.valueOf(21000);
        return gaslimit;
    }


    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * 对字符串进行加密
     *
     * @param str
     * @return
     */
    public static String EncryptionString(String str) {
        if (str == null) {
            return "";
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            return str;
        }
    }


}

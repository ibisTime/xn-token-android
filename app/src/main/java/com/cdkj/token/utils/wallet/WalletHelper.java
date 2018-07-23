package com.cdkj.token.utils.wallet;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.SPUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.UserConfigDBModel;
import com.cdkj.token.model.db.WalletDBModel;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.baselibrary.appmanager.MyConfig.NODE_DEV;
import static com.cdkj.baselibrary.appmanager.MyConfig.NODE_REALSE;
import static com.cdkj.baselibrary.appmanager.MyConfig.getThisNodeType;
import static com.cdkj.baselibrary.utils.StringUtils.SPACE_SYMBOL;
import static com.cdkj.token.utils.AccountUtil.UNIT_MIN;
import static com.cdkj.token.utils.AccountUtil.UNIT_POW;
import static com.cdkj.token.utils.wallet.WalletDBColumn.DELETE_LOCAL_COIN;
import static com.cdkj.token.utils.wallet.WalletDBColumn.FINDUSER_COIN_SQL;
import static com.cdkj.token.utils.wallet.WalletDBColumn.FINDUSER_SQL;
import static com.cdkj.token.utils.wallet.WalletDBColumn.FIND_USER_SQL;
import static com.cdkj.token.utils.wallet.WalletDBColumn.WALLETPASSWORD;
import static com.cdkj.token.utils.wallet.WalletDBColumn.WALLET_NAME;

/**
 * 钱包工具类
 * Created by cdkj on 2018/6/6.
 */
//TODO 加密方法抽取
public class WalletHelper {

    //助记词分隔符
    public final static String HELPWORD_SPACE_SYMBOL = ",";

    public final static String HDPATH = "M/44H/60H/0H/0/0";//生成助记词和解析时使用

    public final static String WALLPASS = "tha_etc";//


    //TODO 币种使用枚举类
    public final static String COIN_ETH = "ETH";// 币种类型 ETH
    public final static String COIN_WAN = "WAN";// 币种类型 WAN
    public final static String COIN_BTC = "BTC";// 币种类型 BTC

    public final static String LOCAL_COIN_CNY = "CNY";// 币种显示类型 人民币
    public final static String LOCAL_COIN_USD = "USD";// 币种显示类型 美元

    public final static String LOCAL_COIN_USD_SYMBOL = "$";// 币种显示类型 美元
    public final static String LOCAL_COIN_CNY_SYMBOL = MoneyUtils.MONEYSING;// 币种显示类型 美元

    /**
     * 根据本地货币获取显示货币符号
     *
     * @param localCoin
     * @return
     */

    public static String getMoneySymbol(String localCoin) {
        if (TextUtils.equals(localCoin, WalletHelper.LOCAL_COIN_CNY)) {
            return LOCAL_COIN_CNY_SYMBOL;
        } else if (TextUtils.equals(localCoin, WalletHelper.LOCAL_COIN_USD)) {
            return LOCAL_COIN_USD_SYMBOL;
        }
        return LOCAL_COIN_CNY_SYMBOL;
    }

    /**
     * 根据币种和环境类型获取节点url
     *
     * @param coinType
     * @return
     */
    public static String getNodeUrlByCoinType(String coinType) {

        if (TextUtils.isEmpty(coinType)) {
            return "";
        }

        String devUrl = "";
        String url = "";

        switch (coinType.toUpperCase()) {
            case COIN_ETH:
                devUrl = "https://rinkeby.infura.io/qfyZa8diWhk28tT9Cwft";
                url = "https://mainnet.infura.io/qfyZa8diWhk28tT9Cwft";
                break;

            case COIN_WAN:
                devUrl = "http://120.26.6.213:8546";
                url = "http://47.75.165.70:8546";
                break;
        }

        switch (getThisNodeType()) {
            case NODE_DEV:
                return devUrl;
            case NODE_REALSE:
                return url;
        }

        return url;
    }

    /**
     * 根据币种和环境类型获取浏览器url
     *
     * @param coinType
     * @return
     */
    public static String getBrowserUrlByCoinType(String coinType) {

        if (TextUtils.isEmpty(coinType)) {
            return "";
        }

        String devUrl = "";
        String url = "";

        switch (coinType.toUpperCase()) {
            case COIN_ETH:
                devUrl = "https://rinkeby.etherscan.io/tx/";
                url = "https://etherscan.io/tx/";
                break;

            case COIN_WAN:
                devUrl = "http://47.104.61.26/block/trans/";
                url = "https://www.wanscan.org/tx/";
                break;
        }

        switch (getThisNodeType()) {
            case NODE_DEV:
                return devUrl;
            case NODE_REALSE:
                return url;
        }

        return url;
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

        switch (type.toUpperCase()) {
            case COIN_ETH:
                return R.drawable.eth_icon;
            case COIN_WAN:
                return R.drawable.wan_icon;
        }
        return R.drawable.default_pic;

    }


    /**
     * 用户是否通过第一次验证（创建或导入钱包时）
     *
     * @param isCheck
     */
    public static void saveWalletFirstCheck(boolean isCheck) {
        SPUtils.put(CdApplication.getContext(), "wallet_first_check", isCheck);
    }

    /**
     * 用户是否通过第一次验证（创建或导入钱包时）
     *
     * @param
     */
    public static boolean isWalletFirstCheck() {
        return SPUtils.getBoolean(CdApplication.getContext(), "wallet_first_check", false);
    }


    /**
     * 根据userId获取用户选择的币种
     *
     * @return userId
     */
    public static String getUserChooseCoinSymbolString(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return "";
        }
        Cursor cursor = DataSupport.findBySQL(FINDUSER_COIN_SQL, userId);

        String string = "";

        if (cursor != null && cursor.moveToFirst()) {

            try {
                string = cursor.getString(cursor.getColumnIndex(WalletDBColumn.CHOOSECOINIDS));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        return string;
    }

    /**
     * 根据userId更新用户选择的币种
     *
     * @return userId
     */
    public static void updateUserChooseCoinString(String coins, String userId) {
        ContentValues values = new ContentValues();
        values.put(WalletDBColumn.CHOOSECOINIDS, coins);
        DataSupport.updateAll(UserConfigDBModel.class, values, FIND_USER_SQL, userId);
    }

    //获取本地所有缓存币种
    public static List<LocalCoinDbModel> getLocalCoinList() {
        return DataSupport.findAll(LocalCoinDbModel.class);
    }

    public static boolean deleteLocalCoinBySymbol(String symbol) {
        return DataSupport.deleteAll(LocalCoinDbModel.class, DELETE_LOCAL_COIN, symbol) > 0;
    }


    //获取本地所有缓存币种
    public static Disposable getLocalCoinListAsync(LocalCoinListGetListener listGetListener) {
        return Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> WalletHelper.getLocalCoinList())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (listGetListener != null) {
                        listGetListener.onGetLocalCoinList(s);
                    }
                }, throwable -> {
                    if (listGetListener != null) {
                        listGetListener.onGetLocalCoinList(null);
                    }
                });
    }


    /**
     * 用户是否添加过自选 添加返回true
     *
     * @return userId
     */
    public static boolean userIsCoinChoosed(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        Cursor cursor = DataSupport.findBySQL(FINDUSER_COIN_SQL, userId);

        int choose = 0;

        if (cursor != null && cursor.moveToFirst()) {

            try {
                choose = cursor.getInt(cursor.getColumnIndex(WalletDBColumn.ISCHOOSED));
            } catch (Exception e) {

            } finally {
                cursor.close();
            }
        }

        return choose == 1;
    }


    /**
     * 创建Eth币种私钥 、地址、助记词
     *
     * @param
     * @return
     */
    public static WalletDBModel createEthPrivateKey() throws Exception {
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

        WalletDBModel walletDBModel = new WalletDBModel();

        walletDBModel.setHelpWordsEn(encrypt(StringUtils.listToString(mnemonicList, HELPWORD_SPACE_SYMBOL))); //储存下来 用，分割

        walletDBModel.setEthAddress(encrypt(credentials1.getAddress()));
        walletDBModel.setEthPrivateKey(encrypt(key1.getPrivateKeyAsHex()));

        walletDBModel.setWanAddress(encrypt(credentials1.getAddress()));
        walletDBModel.setWanPrivateKey(encrypt(key1.getPrivateKeyAsHex()));

        return walletDBModel;
    }

    /**
     * 加密
     *
     * @param privateKeyAsHex
     * @return
     * @throws Exception
     */
    public static String encrypt(String privateKeyAsHex) {
//        try {
//            return CipherUtils.encrypt(privateKeyAsHex, WALLPASS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return privateKeyAsHex;
    }

    /**
     * 解密
     *
     * @param privateKeyAsHex
     * @return
     * @throws Exception
     */
    public static String decrypt(String privateKeyAsHex) {
//        try {
//            return CipherUtils.decrypt(privateKeyAsHex, WALLPASS);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return privateKeyAsHex;
    }


    /**
     * 根据币种获取保存的助记词列表
     *
     * @param userId
     * @return
     */
    public static List<String> getHelpWordsListByUserId(String userId) {

        Cursor cursor = getUserInfoCursorByUserId(userId);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return StringUtils.splitAsList(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.HELPWORDSEN))), HELPWORD_SPACE_SYMBOL);
            }

        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }

        return new ArrayList<>();
    }


    /**
     * 根据用户Id获取用户钱包信息 （私钥 助记词等）
     *
     * @param userId
     * @return
     */
    public static WalletDBModel getUserWalletInfoByUsreId(String userId) {

        Cursor cursor = getUserInfoCursorByUserId(userId);

        WalletDBModel walletDBModel = new WalletDBModel();

        try {
            if (cursor != null && cursor.moveToFirst()) {

                walletDBModel.setUserId(userId);

                walletDBModel.setWalletPassWord(decrypt(cursor.getString(cursor.getColumnIndex(WALLETPASSWORD))));
                walletDBModel.setHelpWordsEn(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.HELPWORDSEN))));

                walletDBModel.setBtcAddress(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.BTCADDRESS))));
                walletDBModel.setBtcPrivateKey(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.BTCPRIVATEKEY))));

                walletDBModel.setEthAddress(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.ETHADDRESS))));
                walletDBModel.setEthPrivateKey(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.ETHPRIVATEKEY))));


                walletDBModel.setWanAddress(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.WANADDRESS))));
                walletDBModel.setWanPrivateKey(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.WANPRIVATEKEY))));


                walletDBModel.setWalletName(decrypt(cursor.getString(cursor.getColumnIndex(WalletDBColumn.WALLET_NAME))));


            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return walletDBModel;
    }

    /**
     * 检测数据库是否有userId 用户数据
     *
     * @param userId
     * @return
     */
    public static boolean isUserAddedWallet(String userId) {
        Cursor cursor = getUserInfoCursorByUserId(userId);
        boolean ishas = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        return ishas;
    }

    /**
     * 根据币种类型获取币种私钥
     *
     * @param walletDBModel2
     * @param coinType
     * @return
     */
    public static String getPrivateKeyByCoinType(WalletDBModel walletDBModel2, String coinType) {

        if (walletDBModel2 == null || TextUtils.isEmpty(coinType)) {
            return "";
        }
        switch (coinType.toUpperCase()) {
            case COIN_ETH:
                return walletDBModel2.getEthPrivateKey();
            case COIN_WAN:
                return walletDBModel2.getWanPrivateKey();
            case COIN_BTC:
                return walletDBModel2.getBtcPrivateKey();
        }
        return "";
    }

    /**
     * 根据币种类型获取币种私钥
     *
     * @param walletDBModel2
     * @param coinType
     * @return
     */
    public static String getAddressByCoinType(WalletDBModel walletDBModel2, String coinType) {

        if (walletDBModel2 == null || TextUtils.isEmpty(coinType)) {
            return "";
        }
        switch (coinType) {
            case COIN_ETH:
                return walletDBModel2.getEthAddress();
            case COIN_WAN:
                return walletDBModel2.getWanAddress();
            case COIN_BTC:
                return walletDBModel2.getBtcAddress();
        }
        return "";
    }

    /**
     * 清除缓存数据
     */
    public static void clearCache() {

    }

    /**
     * 删除用户钱包
     *
     * @param userId
     */
    public static boolean deleteUserWallet(String userId) {
        int deleteWalletCount = DataSupport.deleteAll(WalletDBModel.class, FIND_USER_SQL, userId);
        int deleteChooseCount = DataSupport.deleteAll(UserConfigDBModel.class, FIND_USER_SQL, userId); //删除自选数据
        return deleteWalletCount > 0 && deleteChooseCount >= 0;
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
     * 根据助记词生成ETH WAN 私钥和地址
     *
     * @param defaultMnenonic
     */
    public static WalletDBModel createEthAndWanPrivateKeybyMnenonic(List<String> defaultMnenonic) {

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
        walletDBModel.setHelpWordsEn(encrypt(StringUtils.listToString(defaultMnenonic, HELPWORD_SPACE_SYMBOL))); //储存下来 用，分割

        walletDBModel.setEthAddress(credentials.getAddress());
        walletDBModel.setEthPrivateKey(encrypt(key.getPrivateKeyAsHex()));

        walletDBModel.setWanAddress(credentials.getAddress());
        walletDBModel.setWanPrivateKey(encrypt(key.getPrivateKeyAsHex()));


        return walletDBModel;
    }


    /**
     * 修改用户钱包密码
     *
     * @param password
     * @return
     */
    public static boolean updateWalletPassWord(String password, String userId) {
        ContentValues values = new ContentValues();
        values.put(WALLETPASSWORD, encrypt(password));
        return DataSupport.updateAll(WalletDBModel.class, values, FIND_USER_SQL, userId) > 0;
    }


    /**
     * 获取用户钱包密码
     *
     * @param
     * @return
     */
    public static String getWalletPasswordByUserId(String uerId) {

        Cursor cursor = getUserInfoCursorByUserId(uerId);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                String password = cursor.getString(cursor.getColumnIndex(WALLETPASSWORD));
                return decrypt(password);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return "";
    }

    /**
     * 获取用户钱包名称
     *
     * @param
     * @return
     */
    public static String getWalletNameByUserId(String uerId) {

        Cursor cursor = getUserInfoCursorByUserId(uerId);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndex(WALLET_NAME));
                return name;
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


        return "";
    }

    public static Cursor getUserInfoCursorByUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        return DataSupport.findBySQL(FINDUSER_SQL, userId);
    }


    /**
     * 检测旧密码
     *
     * @param pwd
     */
    public static boolean checkOldPasswordByUserId(String pwd, String userId) {
        return TextUtils.equals(pwd, WalletHelper.getWalletPasswordByUserId(userId));
    }

    /**
     * 判断用户输入的助记词是否和本地的相同
     *
     * @param words
     * @return
     */
    public static boolean checkCacheWords(String words, String userId) {

        List<String> wordsList = getHelpWordsListByUserId(userId);

        if (wordsList != null) {
            return TextUtils.equals(StringUtils.listToString(wordsList, SPACE_SYMBOL), words);
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


        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(COIN_ETH)));

//        Credentials credentials = WalletUtils.loadBip39Credentials(
//                "", walletDBModel.getHelpWordsrEn());

        Credentials credentials = Credentials.create(walletDBModel.getEthPrivateKey());

        EthGetTransactionCount ethGetTransactionCount = web3j
                .ethGetTransactionCount(walletDBModel.getEthAddress(), DefaultBlockParameterName.LATEST)
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
        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(COIN_ETH)));
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

        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(COIN_ETH)));
        //转账人账户地址
        String ownAddress = walletDBModel.getEthAddress();
        //被转人账户地址
        String toAddress = to;

        //转账人私钥
        Credentials credentials = Credentials.create(walletDBModel.getEthPrivateKey());

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

        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(COIN_WAN)));
        //转账人账户地址
        String ownAddress = walletDBModel.getWanAddress();
        //被转人账户地址
        String toAddress = to;

        //转账人私钥
        Credentials credentials = Credentials.create(walletDBModel.getWanPrivateKey());

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
        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(COIN_ETH)));
        BigInteger gaslimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        return gasPrice.multiply(gaslimit);
    }

    /**
     * 获取手续费（矿工费）
     */
    public static BigInteger getGasValue(String coinType) throws Exception {
        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(coinType)));
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


    /**
     * 获取本地币种类型
     *
     * @param
     * @return
     */
    public static String getShowLocalCoinType() {
        return SPUtilHelper.getLocalCoinType();
    }

    //获取本地币种监听
    public interface LocalCoinListGetListener {
        void onGetLocalCoinList(List<LocalCoinDbModel> localCoinDbModels);
    }


}

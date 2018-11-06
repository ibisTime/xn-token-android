package com.cdkj.token.utils.wallet;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.model.BtcSignUTXO;
import com.cdkj.token.model.DbCoinInfo;
import com.cdkj.token.model.UTXOModel;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.UserConfigDBModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wan.WanRawTransaction;
import com.cdkj.token.utils.wan.WanRawTransactionManager;
import com.cdkj.token.utils.wan.WanTransactionEncoder;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.litepal.crud.DataSupport;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
import org.web3j.tx.Contract;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import foundation.omni.CurrencyID;
import foundation.omni.OmniIndivisibleValue;
import foundation.omni.OmniValue;
import foundation.omni.tx.RawTxBuilder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.cdkj.baselibrary.appmanager.AppConfig.NODE_DEV;
import static com.cdkj.baselibrary.appmanager.AppConfig.NODE_REALSE;
import static com.cdkj.baselibrary.appmanager.AppConfig.getThisNodeType;
import static com.cdkj.token.utils.AmountUtil.ETH_UNIT_UNIT;
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
//TODO 工具类方法优化 转账交易方法入参优化
//TODO 转账方法流程逻辑封装 除了单币种转账 还有一键划转， 如果逻辑改变，需要修改好几处地方，不方便
public class WalletHelper {

    //助记词分隔符
    public final static String HELPWORD_SPACE_SYMBOL = ",";

    public final static String HDPATHETH = "M/44H/60H/0H/0/0";//ETH生成助记词和解析时使用

    public final static String HDPATHBTC = "M/44H/0H/0H/0/0";//BTC生成助记词和解析时使用
    public final static String HDPATHBTC_TEST = "M/44H/1H/0H/0/0";//BTC生成助记词和解析时使用


    public final static String COIN_ETH = "ETH";// 币种类型 ETH
    public final static String COIN_WAN = "WAN";// 币种类型 WAN
    public final static String COIN_BTC = "BTC";// 币种类型 BTC
    public final static String COIN_USDT = "USDT";// 币种类型 USDT


    //ETH 节点地址
    public final static String ETH_NODE_URL = "https://mainnet.infura.io/qfyZa8diWhk28tT9Cwft";
    public final static String ETH_NODE_URL_DEV = "https://rinkeby.infura.io/qfyZa8diWhk28tT9Cwft";

    //WAN 节点地址
    public final static String WAN_NODE_URL = "http://47.75.165.70:8546";
    public final static String WAN_NODE_URL_DEV = "http://120.26.6.213:8546";


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
                devUrl = ETH_NODE_URL_DEV;
                url = ETH_NODE_URL;
                break;

            case COIN_WAN:
                devUrl = WAN_NODE_URL_DEV;
                url = WAN_NODE_URL;
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

            case COIN_BTC:
                devUrl = "https://testnet.blockchain.info/search?search=";
                url = "https://www.blockchain.com/btc/tx/";
                break;

            case COIN_USDT:
                devUrl = "https://omniexplorer.info/tx/";
                url = "https://omniexplorer.info/tx/";
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
     * 获取比特币MainNetParams
     *
     * @return
     */
    private static AbstractBitcoinNetParams getBtcMainNetParams() {
        switch (getThisNodeType()) {
            case NODE_DEV:
                return RegTestParams.get();
            default:
                return MainNetParams.get();
        }
    }

    /**
     * 获取比特币MainNetParams
     *
     * @return
     */
    private static String getBtcHDPath() {
        switch (getThisNodeType()) {
            case NODE_DEV:
                return HDPATHBTC_TEST;
            default:
                return HDPATHBTC;
        }
    }

    /**
     * 获取比特币MainNetParams
     *
     * @return
     */
    private static CurrencyID getUSDTCurrencyID() {
        switch (getThisNodeType()) {
            case NODE_DEV:
                return CurrencyID.TOMNI;
            default:
                return CurrencyID.USDT;
        }
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
    public static Disposable getLocalCoinListAsync(LocalCoinListGetListener listGetListener) {
        return Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> LocalCoinDBUtils.getLocalCoinList())
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
     * 创建助记词
     *
     * @return
     */
    public static List<String> createMnemonic() throws Exception {
        // 钱包种子
        DeterministicSeed seed = new DeterministicSeed(new SecureRandom(),
                128, "", Utils.currentTimeSeconds());

        return seed.getMnemonicCode();
    }


    /**
     * lxttest BTC地址生成
     * 根据助记词创建btc信息
     *
     * @param defaultMnenonic
     * @return
     */
    public static DbCoinInfo createBTCInfoByMnemonic(List<String> defaultMnenonic) {

        if (!checkMnenonic(defaultMnenonic)) {
            return null;
        }

        DbCoinInfo dbCoinInfo = new DbCoinInfo();

        //钱包种子
        DeterministicSeed seed = new DeterministicSeed(defaultMnenonic,
                null, "", Utils.currentTimeSeconds());

        DeterministicKeyChain keyChain = DeterministicKeyChain.builder()
                .seed(seed).build();
        List<ChildNumber> keyPathBTC = HDUtils.parsePath(getBtcHDPath());

        DeterministicKey keyBTC = keyChain.getKeyByPath(keyPathBTC, true);

        String addressBTC = keyBTC.toAddress(getBtcMainNetParams()).toString();
        String privateKeyBTC = keyBTC.getPrivateKeyAsHex();

//        BigInteger privkeybtc = keyChain.getKeyByPath(keyPathBTC, true).getPrivKey();
//        ECKey ecKey = ECKey.fromPrivate(privkeybtc);
//        String privateKeyBTC = ecKey.getPrivateKeyEncoded(getBtcMainNetParams()).toString();
//        String addressBTC = ecKey.toAddress(getBtcMainNetParams()).toString();

//        DeterministicKey keyBTC = HDKeyDerivation
//                .createMasterPrivateKey(seed.getSeedBytes());
//        String privateKeyBTC = keyBTC.getPrivateKeyEncoded(getBtcMainNetParams()).toString();
//        String addressBTC = keyBTC.toAddress(getBtcMainNetParams()).toString();

        dbCoinInfo.setAddress(addressBTC);
        dbCoinInfo.setPrivateKey(privateKeyBTC);

        return dbCoinInfo;

    }


    /**
     * 创建币种私钥 、地址、助记词
     *
     * @param
     * @return
     */
    public static WalletDBModel createAllPrivateKey() throws Exception {
        // 钱包种子
        DeterministicSeed seed = new DeterministicSeed(new SecureRandom(),
                128, "", Utils.currentTimeSeconds());

        // 助记词
        List<String> mnemonicList = seed.getMnemonicCode();

        DeterministicKeyChain keyChain = DeterministicKeyChain.builder()
                .seed(seed).build();


        List<ChildNumber> keyPathETH = HDUtils.parsePath(HDPATHETH);

        DeterministicKey keyEth = keyChain.getKeyByPath(keyPathETH, true);

        Credentials credentialsETH = Credentials
                .create(keyEth.getPrivKey().toString(16));

        WalletDBModel walletDBModel = new WalletDBModel();

        walletDBModel.setHelpWordsEn(StringUtils.listToString(mnemonicList, HELPWORD_SPACE_SYMBOL)); //储存下来 用，分割


        //ETH WAN
        walletDBModel.setEthAddress(credentialsETH.getAddress());
        walletDBModel.setEthPrivateKey(keyEth.getPrivateKeyAsHex());

        walletDBModel.setWanAddress(credentialsETH.getAddress());
        walletDBModel.setWanPrivateKey(keyEth.getPrivateKeyAsHex());


        //BTC

        List<ChildNumber> keyPathBTC = HDUtils.parsePath(getBtcHDPath());

        DeterministicKey keyBTC = keyChain.getKeyByPath(keyPathBTC, true);

        walletDBModel.setBtcAddress(keyBTC.toAddress(getBtcMainNetParams()).toString());
        walletDBModel.setBtcPrivateKey(keyBTC.getPrivateKeyAsHex());

//        // 钱包主秘钥
//        DeterministicKey keyBTC = HDKeyDerivation
//                .createMasterPrivateKey(seed.getSeedBytes());
////
//        String privateKeyBTC = keyBTC.getPrivateKeyEncoded(getBtcMainNetParams()).toString();
//
//        String addressBTC = keyBTC.toAddress(getBtcMainNetParams()).toString();
//
//        walletDBModel.setBtcAddress(addressBTC);
//        walletDBModel.setBtcPrivateKey(privateKeyBTC);



        return walletDBModel;
    }


    /**
     * 根据助记词生成ETH WAN BTC私钥和地址
     *
     * @param defaultMnenonic
     */
    public static WalletDBModel createAllCoinPrivateKeybyMnenonic(List<String> defaultMnenonic) {


        DeterministicSeed seed = new DeterministicSeed(defaultMnenonic,
                null, "", Utils.currentTimeSeconds());

        DeterministicKeyChain keyChain = DeterministicKeyChain.builder()
                .seed(seed).build();

        List<ChildNumber> keyPathETH = HDUtils.parsePath(HDPATHETH);
        DeterministicKey key = keyChain.getKeyByPath(keyPathETH, true);
        BigInteger priKey = key.getPrivKey();

        Credentials credentials = Credentials
                .create(priKey.toString(16));

        WalletDBModel walletDBModel = new WalletDBModel();
        //储存下来 用，分割
        walletDBModel.setHelpWordsEn(StringUtils.listToString(defaultMnenonic, HELPWORD_SPACE_SYMBOL));

        //ETH WAN
        walletDBModel.setEthAddress(credentials.getAddress());
        walletDBModel.setEthPrivateKey(key.getPrivateKeyAsHex());

        walletDBModel.setWanAddress(credentials.getAddress());
        walletDBModel.setWanPrivateKey(key.getPrivateKeyAsHex());


        //__________________________1____________________________________

        List<ChildNumber> keyPathBTC = HDUtils.parsePath(getBtcHDPath());

        DeterministicKey keyBTC = keyChain.getKeyByPath(keyPathBTC, true);

        walletDBModel.setBtcAddress(keyBTC.toAddress(getBtcMainNetParams()).toString());
        walletDBModel.setBtcPrivateKey(keyBTC.getPrivateKeyAsHex());

//
//        LogUtil.E("地址  " + credentials2.getAddress());
//        LogUtil.E("私钥  " + keyEth.getPrivateKeyAsHex());
//
//        LogUtil.E("________");

        //————————————————————2
//        DeterministicKey keyBTC = HDKeyDerivation
//                .createMasterPrivateKey(seed.getSeedBytes());
//
//        String privateKeyBTC = keyBTC.getPrivateKeyEncoded(getBtcMainNetParams()).toString();
//
//        String addressBTC = keyBTC.toAddress(getBtcMainNetParams()).toString();
//
//        walletDBModel.setBtcAddress(addressBTC);
//        walletDBModel.setBtcPrivateKey(privateKeyBTC);

        //__________3

//        BigInteger privkeybtc = keyChain2.getKeyByPath(keyPathBTC, true).getPrivKey();
//
//        ECKey ecKey = ECKey.fromPrivate(privkeybtc);
//
//        String privateKeyBTC3 = ecKey.getPrivateKeyEncoded(MainNetParams.get()).toString();
//
//        String addressBTC3 = ecKey.toAddress(MainNetParams.get()).toString();
//
//        LogUtil.E("地址3  " + addressBTC3);
//        LogUtil.E("私钥3  " + privateKeyBTC3);


        return walletDBModel;
    }


    /**
     * 获取保存的助记词列表
     *
     * @param userId
     * @return
     */
    public static List<String> getHelpWordsListByUserId(String userId) {

        Cursor cursor = getUserInfoCursorByUserId(userId);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                return StringUtils.splitAsList(cursor.getString(cursor.getColumnIndex(WalletDBColumn.HELPWORDSEN)), HELPWORD_SPACE_SYMBOL);
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

                walletDBModel.setWalletPassWord(cursor.getString(cursor.getColumnIndex(WALLETPASSWORD)));
                walletDBModel.setHelpWordsEn(cursor.getString(cursor.getColumnIndex(WalletDBColumn.HELPWORDSEN)));

                walletDBModel.setBtcAddress(cursor.getString(cursor.getColumnIndex(WalletDBColumn.BTCADDRESS)));
                walletDBModel.setBtcPrivateKey(cursor.getString(cursor.getColumnIndex(WalletDBColumn.BTCPRIVATEKEY)));

                walletDBModel.setEthAddress(cursor.getString(cursor.getColumnIndex(WalletDBColumn.ETHADDRESS)));
                walletDBModel.setEthPrivateKey(cursor.getString(cursor.getColumnIndex(WalletDBColumn.ETHPRIVATEKEY)));


                walletDBModel.setWanAddress(cursor.getString(cursor.getColumnIndex(WalletDBColumn.WANADDRESS)));
                walletDBModel.setWanPrivateKey(cursor.getString(cursor.getColumnIndex(WalletDBColumn.WANPRIVATEKEY)));

                walletDBModel.setWalletName(cursor.getString(cursor.getColumnIndex(WalletDBColumn.WALLET_NAME)));
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

        if (TextUtils.isEmpty(userId)) {
            return false;
        }

        Cursor cursor = getUserInfoCursorByUserId(userId);

        boolean ishas = cursor != null && cursor.getCount() > 0;

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
     * @param coinType
     * @return
     */
    public static String getPrivateKeyByCoinType(String userId, String coinType) {

        WalletDBModel walletDBModel2 =
                WalletHelper.getUserWalletInfoByUsreId(userId);

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
     * @param mnenonic
     * @return
     */
    public static boolean checkMnenonic(List<String> mnenonic) {
        try {
            new MnemonicCode().check(mnenonic);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 修改用户钱包密码
     *
     * @param password
     * @return
     */
    public static boolean updateWalletPassWord(String password, String userId) {
        ContentValues values = new ContentValues();
        values.put(WALLETPASSWORD, password);
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
                return password;
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

    /**
     * 根据用户Id获取钱包表 Cursor
     *
     * @param userId
     * @return
     */
    public static Cursor getUserInfoCursorByUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }

        return DataSupport.findBySQL(FINDUSER_SQL, userId);
    }


    /**
     * 检测钱包密码是否正确
     *
     * @param pwd
     */
    public static boolean checkPasswordByUserId(String pwd, String userId) {
        return TextUtils.equals(pwd, WalletHelper.getWalletPasswordByUserId(userId));
    }


    /**
     * 根据地址查询balance
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

    public static String transferForEth(WalletDBModel walletDBModel, String to, String money, BigInteger gas_limit, BigInteger gas_price) throws ExecutionException, InterruptedException, IOException {

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


        //创建交易，这里是转x个以太币
        BigInteger priceValue = new BigDecimal(money).multiply(BigDecimal.TEN.pow(ETH_UNIT_UNIT)).toBigInteger(); //需要转账的金额

        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                nonce, gas_price, gas_limit, toAddress, priceValue);

        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();//sendAsync().get()

        if (ethSendTransaction == null || ethSendTransaction.getError() != null) {
            return "";
        }

        String transactionHash = ethSendTransaction.getTransactionHash();

        return transactionHash;
    }

    /**
     * 转账同步请求 需在子线程中操作
     *
     * @param toAddress
     * @param money
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws IOException
     */

    public static String transferForWan(WalletDBModel walletDBModel, String toAddress, String money, BigInteger gas_limit, BigInteger gas_price) throws ExecutionException, InterruptedException, IOException {

        Web3j web3j = Web3jFactory.build(new HttpService(getNodeUrlByCoinType(COIN_WAN)));
        //转账人账户地址
        String fromAddress = walletDBModel.getWanAddress();

        //转账人私钥
        Credentials credentials = Credentials.create(walletDBModel.getWanPrivateKey());

        //getNonce（交易的笔数）
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                fromAddress, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        //创建交易
        BigInteger priceValue = new BigDecimal(money).multiply(BigDecimal.TEN.pow(ETH_UNIT_UNIT)).toBigInteger(); //需要转账的金额

        WanRawTransaction rawTransaction = WanRawTransaction.createTransaction(
                nonce, gas_price, gas_limit, toAddress, priceValue, "");

        //签名Transaction，这里要对交易做签名
        byte[] signedMessage = WanTransactionEncoder.signMessage(rawTransaction, credentials);

        String hexValue = Numeric.toHexString(signedMessage);

        //发送交易
        EthSendTransaction ethSendTransaction =
                web3j.ethSendRawTransaction(hexValue).sendAsync().get();//sendAsync().get()


        if (ethSendTransaction == null || ethSendTransaction.getError() != null) {
            LogUtil.E("交易失败" + ethSendTransaction.getError().getMessage());
            return "";
        }


        String transactionHash = ethSendTransaction.getTransactionHash();


        return transactionHash;
    }


    /**
     * 对btc交易进行签名
     *
     * @param unSpentBTCList utxolist
     * @param from
     * @param to
     * @param privateKey
     * @param value          交易金额
     * @param fee            手续费
     * @return
     * @throws Exception
     */
    public static String signBTCTransactionData(@NonNull List<UTXOModel> unSpentBTCList, @NonNull String from, @NonNull String to, @NonNull String privateKey, long value, long fee) throws Exception {

        Transaction transaction = new Transaction(getBtcMainNetParams());

        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(getBtcMainNetParams(), privateKey);

        ECKey ecKey = dumpedPrivateKey.getKey();

        long totalMoney = 0;

        List<BtcSignUTXO> utxos = new ArrayList<>();
        //遍历未花费列表，组装合适的item
        for (UTXOModel us : unSpentBTCList) {

            if (us == null || us.getCount() == null) {
                continue;
            }

            if (totalMoney >= (value + fee)) {
                break;
            }

            BtcSignUTXO utxo = new BtcSignUTXO(Sha256Hash.wrap(us.getTxid()), us.getVout().longValue(),

                    new Script(Hex.decode(us.getScriptPubKey())));

            utxos.add(utxo);

            totalMoney += us.getCount().longValue();
        }

        transaction.addOutput(Coin.valueOf(value), Address.fromBase58(getBtcMainNetParams(), to));

        // transaction.
        //消费列表总金额 - 已经转账的金额 - 手续费 就等于需要返回给自己的金额了
        long balance = totalMoney - value - fee;
        //输出-转给自己
        if (balance > 0) {
            transaction.addOutput(Coin.valueOf(balance), Address.fromBase58(getBtcMainNetParams(), from));
        }
        //输入未消费列表项
        for (BtcSignUTXO utxo : utxos) {

            TransactionOutPoint outPoint = new TransactionOutPoint(getBtcMainNetParams(), utxo.getIndex(), utxo.getHash());

            transaction.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, true);
        }

        return Hex.toHexString(transaction.bitcoinSerialize());
    }


    /**
     * eth token币转账
     *
     * @param walletDBModel
     * @param toAddress
     * @param contractAddress
     * @param gasPrice
     * @return
     * @throws Exception
     */
    public static String transferForEthTokenCoin(@NonNull WalletDBModel walletDBModel, @NonNull String toAddress,
                                                 @NonNull BigInteger priceValue, @NonNull String contractAddress, @NonNull BigInteger gasPrice) throws Exception {

        Credentials credentials = Credentials.create(walletDBModel.getEthPrivateKey());

        BigInteger gasLimit = BigInteger.valueOf(210000);

        LogUtil.E("交易" + gasPrice.toString());

        // 合约方法创建并encode
        Function function = new Function("transfer",

                Arrays.<Type>asList(

                        new org.web3j.abi.datatypes.Address(toAddress),

                        new Uint256(priceValue)),  //最小精度

                Collections.<TypeReference<?>>emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        // 获取发送地址当前交易数量
        EthGetTransactionCount ethGetTransactionCount = getWeb3jClient(COIN_ETH)

                .ethGetTransactionCount(walletDBModel.getEthAddress(),

                        DefaultBlockParameterName.LATEST)

                .sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        // 未签名的交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, contractAddress, BigInteger.ZERO,
                encodedFunction);

        // 签名并发送
        RawTransactionManager rawTransactionManager = new RawTransactionManager(
                getWeb3jClient(COIN_ETH), credentials);

        EthSendTransaction ethSendTransaction = rawTransactionManager
                .signAndSend(rawTransaction);

        if (ethSendTransaction == null || ethSendTransaction.getError() != null) {
            return "";
        }

        String hash = ethSendTransaction.getTransactionHash();

        return hash;
    }

    /**
     * wan token币转账
     *
     * @param walletDBModel
     * @param toAddress
     * @param contractAddress
     * @param gasPrice
     * @return
     * @throws Exception
     */
    public static String transferForWanTokenCoin(@NonNull WalletDBModel walletDBModel, @NonNull String toAddress,
                                                 @NonNull BigInteger priceValue, @NonNull String contractAddress, @NonNull BigInteger gasPrice) throws Exception {

        Credentials credentials = Credentials.create(walletDBModel.getEthPrivateKey());

        BigInteger gasLimit = BigInteger.valueOf(210000);


        // 合约方法创建并encode
        Function function = new Function("transfer",

                Arrays.<Type>asList(

                        new org.web3j.abi.datatypes.Address(toAddress),

                        new Uint256(priceValue)),  //最小精度

                Collections.<TypeReference<?>>emptyList());

        String encodedFunction = FunctionEncoder.encode(function);

        // 获取发送地址当前交易数量
        EthGetTransactionCount ethGetTransactionCount = getWeb3jClient(COIN_WAN)

                .ethGetTransactionCount(walletDBModel.getWanAddress(),

                        DefaultBlockParameterName.LATEST)

                .sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();


        // 未签名的交易
        WanRawTransaction rawTransaction = WanRawTransaction.createTransaction(
                nonce, gasPrice, gasLimit, contractAddress, BigInteger.ZERO,
                encodedFunction);

        //签名Transaction，这里要对交易做签名
        // 签名并发送
        WanRawTransactionManager rawTransactionManager = new WanRawTransactionManager(
                getWeb3jClient(COIN_WAN), credentials);

        EthSendTransaction ethSendTransaction = rawTransactionManager
                .signAndSend(rawTransaction);

        if (ethSendTransaction == null || ethSendTransaction.getError() != null) {

            return "";
        }

        String hash = ethSendTransaction.getTransactionHash();

        return hash;
    }

    public static BigInteger getUnitAmountValue(String amount, String coin) {
        BigInteger priceValue = new BigDecimal(amount).multiply(LocalCoinDBUtils.getLocalCoinUnit(coin)).toBigInteger(); //需要转账的金额
        return priceValue;
    }


    /**
     * 获取手续费（矿工费）
     */
    public static BigInteger getGasValue(String coin) throws Exception {
        Web3j web3j = getWeb3jClient(coin);
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
        return gasPrice;
    }

    /**
     * 获取默认gaslimit
     */
    public static BigInteger getDeflutGasLimit() {
        BigInteger gaslimit = BigInteger.valueOf(21000);
        return gaslimit;
    }

    /**
     * 获取Web3j对象
     *
     * @return
     */
    public static Web3j getWeb3jClient(String coin) {
        return Web3jFactory.build(new HttpService(getNodeUrlByCoinType(coin)));
    }


    /**
     * 获取默认GAS_PRICE
     */
    public static BigInteger getDefluteGasPrice() {
        return Contract.GAS_PRICE;
    }


    //获取本地币种监听
    public interface LocalCoinListGetListener {
        void onGetLocalCoinList(List<LocalCoinDbModel> localCoinDbModels);
    }

    /**
     * 判断btc地址是否合法
     *
     * @param address
     * @return
     */
    public static boolean verifyBTCAddress(String address) {
        NetworkParameters params = getBtcMainNetParams();
        try {
            Address.fromBase58(params, address);
            return true;
        } catch (AddressFormatException e) {
            return false;
        }

    }


    /***
     *获取矿工费
     * @param unSpentBTCList 未交易的utxo
     * @param value      要交易的金额
     * @param rate sta/byte
     * @return -1发送的value超出了你的余额
     *
     * 148 x inputNum + 34 x outputNum + 10
     */
    public static long getBtcFee(List<UTXOModel> unSpentBTCList, long value, int rate) {

        long fee = 0L;

        int inputNum = 0;  //合适的utxo数量

        long totalMoney = 0; //utxo数量总值

        for (UTXOModel us : unSpentBTCList) {

            inputNum++;

            totalMoney += us.getCount().longValue();

            if (totalMoney > value) {

                fee = (148 * inputNum + 34 * 1 + 10) * rate;//不用设置找零时只有一个输出

                if (totalMoney == (value + fee))

                    return fee;

                else if (totalMoney > (value + fee)) {

                    fee = (148 * inputNum + 34 * 2 + 10) * rate;     //2=设置找零输出 +转账地址输出

                    if (totalMoney >= (value + fee))

                        return fee;
                }
            }
        }
        return -1;
    }

    public static String signUSDTTransactionData(@NonNull List<UTXOModel> unSpentBTCList,  @NonNull String from, @NonNull String to,
                                                 @NonNull String privateKey, long amount, int rate) {

        // 构建BTC交易
        Transaction tx = new Transaction(getBtcMainNetParams());

        // omni交易，必须转移的最小的btc数量，固定不用变
        long btcValue = Transaction.MIN_NONDUST_OUTPUT.longValue();

        // 获取from地址的UTXO,并按金额从大到小排序


        // 私钥
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(getBtcMainNetParams(), privateKey);
        ECKey ecKey = dumpedPrivateKey.getKey();

        // 构建来去方地址
        Address fromAddress = ecKey.toAddress(getBtcMainNetParams());
        Address toAddress = Address.fromBase58(getBtcMainNetParams(), to);

        Script omniScript = getOpreturn(getUSDTCurrencyID(), OmniIndivisibleValue.of(amount));

        // 转给对方546比特币（最小值，必须放在第一个output）
        tx.addOutput(Coin.valueOf(btcValue), toAddress);
        tx.addOutput(Coin.valueOf(0L), omniScript);

        long sumInputValue = 0; // input总金额
        int sumInputCount = 0; // input总个数
        boolean isChange = false; // 是否需要找零
        long changeValue = 0; // 找零金额
        List<UTXOModel> inputUtxos = new ArrayList<>();

        for (UTXOModel utxo : unSpentBTCList) {
            if (utxo == null || utxo.getCount() == null) {
                continue;
            }

            inputUtxos.add(utxo);
            sumInputCount++;
            sumInputValue += utxo.getCount().longValue();

            if (sumInputValue > btcValue) {

                // 转账后剩余金额
                long leftValue = sumInputValue - btcValue;

                // 预估矿工费
                long fee = calMinerFee(sumInputCount, 2, rate);

                // 剩下的钱刚好等于矿工费，不找零
                if (leftValue == fee) {
                    break;
                } else if (leftValue > fee) {

                    // 重新预估矿工费
                    fee = calMinerFee(sumInputCount, 3, rate);

                    // 剩下的钱比矿工费还多就找零，否则说明剩下的钱比矿工费才多了一点点，直接付给矿工算了
                    if (leftValue > fee) {
                        isChange = true;
                        changeValue = leftValue - fee;
                    }
                    break;

                }

            }

        }

        if (sumInputValue <= btcValue) {
//            throw new BizException(EBizErrorCode.DEFAULT.getCode(), "btc余额不足");
        }

        // 输出-转给自己
        if (isChange) {
            tx.addOutput(Coin.valueOf(changeValue), fromAddress);
        }

        // 输入未消费列表项
        for (UTXOModel utxo : inputUtxos) {
            Sha256Hash hash = Sha256Hash.wrap(utxo.getTxid());
            TransactionOutPoint outPoint = new TransactionOutPoint(
                    getBtcMainNetParams(), utxo.getVout().longValue(), hash);
            Script script = new Script(Hex.decode(utxo.getScriptPubKey()));
            tx.addSignedInput(outPoint, script, ecKey, Transaction.SigHash.ALL,
                    true);
        }

        // 解析出待广播的签名后的原始交易数据
        return Hex.toHexString(tx.bitcoinSerialize());
    }


    /**
     * 预估本次交易矿工费
     * @param inCount
     * @param outCount
     * @return
     * @create: 2018年2月22日 下午5:28:04 xieyj
     * @history:
     */
    public static long calMinerFee(int inCount, int outCount, int rate) {

        // 预估交易大小(bytes)
        // int preSize = inCount * 148 + outCount * 34 + 10;

        // 计算出手续费
        // long preFee = (preSize * 100000) / feePerByte
        // 组装Output，设置找零账户
        // 如何估算手续费，先预先给一个size,然后拿这个size进行签名
        // 对签名的数据进行解码，拿到真实大小，然后进行矿工费的修正
        int preSize = (148 * inCount + 34 * outCount + 10);
//        int feePerByte = preSize * preSize / rate;

        // 计算出手续费
        int preFee = preSize * rate;
        return preFee;
    }

    public static Script getOpreturn(CurrencyID currencyID, OmniValue amount) {
        // payload字符串的前缀，有这个才能解析出该交易是基于omni协议的交易
        String omniPrefix = "6f6d6e69";
        // 构建payload，创建opretrun，作为交易的一个特殊输出，就可以被识别为omni交易
        RawTxBuilder builder = new RawTxBuilder();
        String txHex = omniPrefix
                + builder.createSimpleSendHex(currencyID, amount);
        byte[] payload = hexToBinary(txHex);
        return ScriptBuilder.createOpReturnScript(payload);
    }

    /**
     * Convert a hexadecimal string representation of binary data
     * to byte array.
     *
     * @param hex Hexadecimal string
     * @return binary data
     */
    public static byte[] hexToBinary(String hex) {
        int length = hex.length();
        byte[] bin = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            int hi = Character.digit(hex.charAt(i), 16);
            int lo = Character.digit(hex.charAt(i + 1), 16);
            bin[i / 2] = (byte) ((hi << 4) + lo);
        }
        return bin;
    }

}

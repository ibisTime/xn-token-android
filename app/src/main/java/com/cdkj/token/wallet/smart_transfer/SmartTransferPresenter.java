package com.cdkj.token.wallet.smart_transfer;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.mvp.BaseMVPModel;
import com.cdkj.baselibrary.base.mvp.BasePresenter;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MyApplication;
import com.cdkj.token.R;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.BtcFeesModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.UTXOModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.bitcoinj.core.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * TODO BTC WAN ETH 的一些数据来源不同且一些操作也不同，但是基本流程一样是否可以封装不同的Presenter切换不同的币种就是切换不同Presenter
 * Created by cdkj on 2018/9/10.
 */

public class SmartTransferPresenter extends BasePresenter<SmartTransferView> implements SmartTransferSource.SmartTransferModelCallBack, UserInfoInterface {

    private Activity activity;

    public SmartTransferSource smartTransferModel;

    private boolean isPrivateWallet;//是否私钥钱包划转

    private CoinModel coinModel;
    private CoinModel.AccountListBean selectCoinData;
    private UserInfoPresenter userInfoPresenter;

    private BigInteger mGasPrice;//获取的燃料单位费用
    private BigInteger transferGasPrice;//计算后转账矿工费用

    private BigDecimal minBtcFee;//btc最小矿工费
    private BigDecimal maxBtcFee;//btc最大矿工费

    private String amountString;//要划转金额


    public SmartTransferPresenter(Activity activity) {
        this.activity = activity;
        userInfoPresenter = new UserInfoPresenter(this, activity);
    }

    public void getUserInfo() {
        userInfoPresenter.getUserInfoRequest();
    }


    /**
     * 检测是否设置过资金密码
     */
    public void showPayPasswordDialog() {

        if (isPrivateWallet) {        //私钥钱包直接显示密码框
            getMvpView().showPayPwdDialog(isPrivateWallet);
            return;
        }

        if (!userInfoPresenter.checkPayPwdAndStartSetPage()) { //中心化钱包是否设置过支付密码 没有设置先弹出设置框
            return;
        }
        getMvpView().showPayPwdDialog(isPrivateWallet);
    }


    /**
     * 根据seekBar滑动计算矿工费
     *
     * @param i
     */
    public void setFeesBySeekBarChange(int i) {

        if (selectCoinData == null) return;

        getMvpView().seekBarChange();

        if (LocalCoinDBUtils.isBTCChain(selectCoinData.getCurrency())) {

            if (maxBtcFee == null || minBtcFee == null) return;

            float progress = i / 100f;

            BigDecimal progressBigDecimal = new BigDecimal(progress);

            BigDecimal lilmit = maxBtcFee.subtract(minBtcFee).multiply(progressBigDecimal);

            transferGasPrice = lilmit.add(minBtcFee).toBigInteger();

        } else {
            if (mGasPrice == null) return;
            //最小矿工费  最大最小是GasPrice上下浮动15%
            BigDecimal minGasPrice = new BigDecimal(mGasPrice).multiply(new BigDecimal(0.85));
            //最大矿工费
            BigDecimal maxGasPrice = new BigDecimal(mGasPrice).multiply(new BigDecimal(1.15));

            float Progress = i / 100f;

            BigDecimal ProgressBigDecimal = new BigDecimal(Progress);

            BigDecimal lilmit = maxGasPrice.subtract(minGasPrice).multiply(ProgressBigDecimal);

            transferGasPrice = ((lilmit.add(minGasPrice)).toBigInteger());
        }

        getMvpView().setFee(new BigDecimal(transferGasPrice));
    }


    /**
     * 划转交易（中心化到私用）
     */
    public void transfer(Context context, String password, String amount, String googleCode) {

        amountString = amount;

        if (isViewDetached() || selectCoinData == null) {
            return;
        }

        try {
            smartTransferModel.transfer(context, password, amount, googleCode, selectCoinData);
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().transferFail(isPrivateWallet);
        }

    }

    /**
     * 划转交易(私有 到中心化)
     */
    public void transferPrivate(String amount) {
        amountString = amount;
        if (isViewDetached() || selectCoinData == null) {
            return;
        }

        String coinSymbol = selectCoinData.getCurrency();

        //BTC 先获取UTXO 获取到后进行签名
        if (LocalCoinDBUtils.isBTC(coinSymbol)) {
            WalletDBModel userWalletIn = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());
            if (userWalletIn == null) return;
            smartTransferModel.getBTCUTXO(userWalletIn.getBtcAddress());
            return;
        }else if (LocalCoinDBUtils.isUSDT(coinSymbol)){
            WalletDBModel userWalletIn = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());
            if (userWalletIn == null) return;
            smartTransferModel.getUSDTUTXO(userWalletIn.getBtcAddress());
            return;
        }
        String toAddress = getAccountAddressBySymbol(coinSymbol);

        smartTransferModel.transferPrivate(coinSymbol, toAddress, amount, transferGasPrice);
    }

    /**
     * 从账户列表里获取中心化钱包地址
     *
     * @return
     */
    String getAccountAddressBySymbol(String symbol) {
        String toAddress = "";
        for (CoinModel.AccountListBean accountListBean : coinModel.getAccountList()) {
            if (TextUtils.equals(symbol, accountListBean.getCurrency())) {
                toAddress = accountListBean.getCoinAddress();
                break;
            }
        }
        return toAddress;
    }


    /**
     * 获取币种
     *
     * @param context
     */
    public void getCoins(Context context) {
        if (isViewDetached()) {
            return;
        }
        smartTransferModel.getAllCoins(context);
    }


    /**
     * 私钥钱包划转
     *
     * @param isPrivate
     */
    public void setPrivateWallet(boolean isPrivate) {
        isPrivateWallet = isPrivate;
        if (isPrivate) {
            getMvpView().resetFeeBarProgress();
            getMvpView().setPrivatePage();
            getMvpView().showGoogleEdit(false);
        } else {
            getMvpView().setCenterPage();
            getMvpView().showGoogleEdit(SPUtilHelper.getGoogleAuthFlag());
        }
    }

    public boolean isPrivateWallet() {
        return isPrivateWallet;
    }

    /**
     * 切换选择转账类型
     */
    public void togglePrivateStatus() {
        isPrivateWallet = !isPrivateWallet;
        setPrivateWallet(isPrivateWallet);

        if (selectCoinData == null) {
            return;
        }

        getBalanceByCoin(selectCoinData);
        getFeeByCoin(selectCoinData.getCurrency());
    }

    /**
     * 点击币种(获取余额 获取矿工费)
     *
     * @param position
     */
    public void selectCoin(int position) {
        if (coinModel == null) {
            return;
        }
        List<CoinModel.AccountListBean> accountList = coinModel.getAccountList();

        if (StringUtils.checkPostionCrossingInList(accountList, position)) {
            return;
        }

        selectCoinData = accountList.get(position);

        if (selectCoinData == null) {
            return;
        }


        getMvpView().setSelectCoin(selectCoinData);

        getBalanceByCoin(selectCoinData);

        getFeeByCoin(selectCoinData.getCurrency());
    }


    /**
     * 切换余额
     */
    void getBalanceByCoin(CoinModel.AccountListBean selectCoinData) {
        if (selectCoinData == null) {
            return;
        }
        if (isPrivateWallet) {
            String address = LocalCoinDBUtils.getAddressByCoin(selectCoinData.getCurrency(), SPUtilHelper.getUserId());
            smartTransferModel.getPrivateCoinBalanceBySymbol(selectCoinData.getCurrency(), address);
        } else {
            balanceData(selectCoinData.getAmount());
        }
    }


    /**
     * 切换手续费
     */
    void getFeeByCoin(String coinSymbol) {
        if (!isPrivateWallet) {
            smartTransferModel.getFeeByCoin(null, coinSymbol);
            return;
        }
        getMvpView().resetFeeBarProgress();
        smartTransferModel.getPrivateFeeCoin(coinSymbol);
    }


    @Override
    public void showDialog() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().showLoadDialog();
    }

    @Override
    public void dismissDialog() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().disMissLoadDialog();
    }

    @Override
    public void coinData(CoinModel coinModel) {
        this.coinModel = coinModel;
        if (isViewDetached()) {
            return;
        }
        if (coinModel == null) return;
        getMvpView().setCoins(coinModel);
    }

    @Override
    public void balanceData(BigDecimal balance) {
        if (isViewDetached()) {
            return;
        }
        getMvpView().setBalance(balance);
    }

    /**
     * 手续费获取之后如果当前选择币种是BTC则获取BTCUTXO
     *
     * @param fee
     */
    @Override
    public void feesData(BigDecimal fee) {
        if (isViewDetached()) {
            return;
        }

        if (isPrivateWallet) {
            mGasPrice = fee.toBigInteger();
            setFeesBySeekBarChange(50);
            return;
        }

        getMvpView().setFee(fee);

    }

    @Override
    public void btcfeesData(BtcFeesModel btcFee) {
        maxBtcFee = btcFee.getFastestFeeMax();
        minBtcFee = btcFee.getFastestFeeMin();
        setFeesBySeekBarChange(50);
    }

    @Override
    public void btcUTXOData(List<UTXOModel> utxo) {


        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());

        String fromAddress = walletDBModel.getBtcAddress();

        String toAddress = getAccountAddressBySymbol(WalletHelper.COIN_BTC);

        String privateKey = walletDBModel.getBtcPrivateKey();


//        获取btc交易签名
        try {

            BigDecimal amountBigDecimal = AmountUtil.bigDecimalFormat(new BigDecimal(amountString), WalletHelper.COIN_BTC);
            Long feeLong = WalletHelper.getBtcFee(utxo, amountBigDecimal.longValue(), transferGasPrice.intValue());//矿工费

            if (feeLong == -1) {
                getMvpView().showMessageDialog(MyApplication.getInstance().getString(R.string.no_balance));
                return;
            }

            String sign = WalletHelper.signBTCTransactionData(utxo,  //utxo列表
                    fromAddress,  //btc地址
                    toAddress,//btc转出地址
                    privateKey,//btc 私钥
                    amountBigDecimal.longValue(), feeLong);

            if (TextUtils.isEmpty(sign)) {
                getMvpView().transferFail(isPrivateWallet);
                return;
            }

            smartTransferModel.btcTransactionBroadcast(sign);

        } catch (Exception e) {
            LogUtil.E("BTC转账失败" + e);
            e.printStackTrace();
            transferFail();
        }
    }

    @Override
    public void usdtUTXOData(List<UTXOModel> utxo) {

        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());

        String fromAddress = walletDBModel.getBtcAddress();

        String toAddress = getAccountAddressBySymbol(WalletHelper.COIN_BTC);

        String privateKey = walletDBModel.getBtcPrivateKey();

        //        获取btc交易签名
        try {
            BigDecimal transAmount = AmountUtil.bigDecimalFormat(new BigDecimal(amountString), WalletHelper.COIN_BTC);

            Long feeLong = WalletHelper.getBtcFee(utxo, Transaction.MIN_NONDUST_OUTPUT.longValue(), transferGasPrice.intValue());//矿工费

            if (feeLong == -1) {
                getMvpView().showMessageDialog(MyApplication.getInstance().getString(R.string.no_btc_balance));
                return;
            }

            String sign = WalletHelper.signUSDTTransactionData(activity,
                    utxo,  //utxo列表
                    fromAddress,  //btc地址
                    toAddress,//btc转出地址
                    privateKey,//btc 私钥
                    transAmount.longValue(), transferGasPrice.intValue());

            if (TextUtils.isEmpty(sign)) {
                getMvpView().transferFail(isPrivateWallet);
                return;
            }

            smartTransferModel.btcTransactionBroadcast(sign);

        } catch (Exception e) {
            LogUtil.E("BTC转账失败" + e);
            e.printStackTrace();
            transferFail();
        }
    }


    @Override
    public void transferSuccess() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().transferSuccess(isPrivateWallet);
    }

    @Override
    public void transferFail() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().transferFail(isPrivateWallet);
    }

    @Override
    public void onStartGetUserInfo() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().showLoadDialog();
    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        if (isViewDetached()) {
            return;
        }
        getMvpView().disMissLoadDialog();
        getMvpView().showGoogleEdit(SPUtilHelper.getGoogleAuthFlag());
    }

    @Override
    public BaseMVPModel createBaseMVPModel() {
        smartTransferModel = new SmartTransferSource(this);
        return smartTransferModel;
    }


    @Override
    public void detachView() {
        userInfoPresenter.clear();
        super.detachView();
    }
}

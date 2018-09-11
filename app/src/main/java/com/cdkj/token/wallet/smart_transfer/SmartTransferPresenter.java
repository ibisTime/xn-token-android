package com.cdkj.token.wallet.smart_transfer;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.token.interfaces.UserInfoInterface;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.LocalCoinDBUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * TODO 这里有个问题 P层 和 M层如何划分？
 * Created by cdkj on 2018/9/10.
 */

public class SmartTransferPresenter extends Presenter<SmartTransferView> implements SmartTransferSource.SmartTransferModelCallBack, UserInfoInterface {


    public SmartTransferSource smartTransferModel;

    private boolean isPrivateWallet;//是否私钥钱包划转

    private CoinModel coinModel;
    private CoinModel.AccountListBean selectCoinData;
    private UserInfoPresenter userInfoPresenter;

    private BigInteger mGasPrice;//获取的燃料单位费用
    private BigInteger transferGasPrice;//计算后转账矿工费用


    public SmartTransferPresenter(Activity activity) {
        userInfoPresenter = new UserInfoPresenter(this, activity);
    }

    public void getUserInfo() {
        userInfoPresenter.getUserInfoRequest();
    }

    /**
     * 检测是否设置过资金密码
     */
    public void checkSetPassword() {

        if (isPrivateWallet) {        //私钥钱包直接显示密码框
            getMvpView().showPayPwdDialog(isPrivateWallet);
            return;
        }

        if (!userInfoPresenter.checkPayPwdAndStartSetPage()) { //中心化钱包是否设置过支付密码
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
        if (mGasPrice == null) return;
        BigDecimal minPrice = new BigDecimal(mGasPrice).multiply(new BigDecimal(0.85));//最小矿工费  最大最小是GasPrice上下浮动15%
        BigDecimal maxPrice = new BigDecimal(mGasPrice).multiply(new BigDecimal(1.15)); //最大矿工费

        float Progress = i / 100f;

        BigDecimal ProgressBigDecimal = new BigDecimal(Progress);

        BigDecimal lilmit = maxPrice.subtract(minPrice).multiply(ProgressBigDecimal);

        transferGasPrice = ((lilmit.add(minPrice)).toBigInteger());

        getMvpView().setFee(new BigDecimal(transferGasPrice));

    }


    /**
     * 划转交易（中心化到私用）
     */
    public void transfer(Context context, String password, String amount, String googleCode) {
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
        if (isViewDetached() || selectCoinData == null) {
            return;
        }

        String coinSymbol = selectCoinData.getCurrency();

        String toAddress = "";

        for (CoinModel.AccountListBean accountListBean : coinModel.getAccountList()) {
            if (TextUtils.equals(coinSymbol, accountListBean.getCurrency())) {
                toAddress = accountListBean.getCoinAddress();
                break;
            }
        }

        try {
            //BTC
            if (LocalCoinDBUtils.isBTC(selectCoinData.getCurrency())) {

                if (LocalCoinDBUtils.isBTC(coinSymbol)) {

                    //获取btc交易签名
//                    String sign = WalletHelper.signBTCTransactionData(unSpentBTCList,  //utxo列表
//                            "",  //btc地址
//                            "",//btc转出地址
//                            "",//btc 私钥
//                            12   //需要交易的金额
//                            , getBtcFee(unSpentBTCList, transactionAmount.longValue(), mfees.intValue())); //矿工费

                    return;
                }

                return;
            }
            smartTransferModel.transferPrivate(coinSymbol, toAddress, amount, transferGasPrice);
        } catch (Exception e) {
            e.printStackTrace();
            getMvpView().transferFail(isPrivateWallet);
        }

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
     * 获取手续费
     *
     * @param context
     * @param coinSymbol
     */

    public void getFeeByCoin(Context context, String coinSymbol) {
        if (isViewDetached()) {
            return;
        }
        smartTransferModel.getFeeByCoin(context, coinSymbol);
    }

    /**
     * 获取余额
     *
     * @param context
     * @param coinSymbol
     */

    public void getBalanceByCoin(Context context, String coinSymbol) {
        if (isViewDetached()) {
            return;
        }
        if (!isPrivateWallet) {
            smartTransferModel.getFeeByCoin(context, coinSymbol);
            return;
        }

    }

    /**
     * 私钥钱包划转
     *
     * @param isPrivate
     */
    public void setPrivateWallet(boolean isPrivate) {
        isPrivateWallet = isPrivate;
        if (isPrivate) {
            getMvpView().resetBarProgress();
            getMvpView().setPrivatePage();

        } else {
            getMvpView().setCenterPage();

        }
    }

    /**
     * 切换选择转账类型
     */
    public void togglePrivateStatus() {
        isPrivateWallet = !isPrivateWallet;
        setPrivateWallet(isPrivateWallet);
        getBalance();
        getFees();
    }

    /**
     * 切换余额
     */
    void getBalance() {
        if (isPrivateWallet) {
            String address = LocalCoinDBUtils.getAddressByCoin(selectCoinData.getCurrency(), SPUtilHelper.getUserId());
            smartTransferModel.getCoinBalanceBySymbol(selectCoinData.getCurrency(), address);
        } else {
            balanceData(selectCoinData.getAmount());
        }
    }


    /**
     * 切换手续费
     */
    void getFees() {
        if (!isPrivateWallet) {
            smartTransferModel.getFeeByCoin(null, selectCoinData.getCurrency());
            return;
        }
        getMvpView().resetBarProgress();
        smartTransferModel.getPrivateFeeCoin(selectCoinData.getCurrency());
    }


    /**
     * 点击币种
     *
     * @param position
     */
    public void selectCoin(int position) {
        if (coinModel == null) return;
        List<CoinModel.AccountListBean> accountList = coinModel.getAccountList();
        if (accountList == null || accountList.size() == 0 || position > accountList.size()) return;
        selectCoinData = accountList.get(position);
        if (selectCoinData == null) {
            return;
        }
        getMvpView().setSelectCoin(selectCoinData);
        getBalance();

        getFees();
    }


    @Override
    public void showDialog() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().onShowLoadingDialog();
    }

    @Override
    public void dismissDialog() {
        if (isViewDetached()) {
            return;
        }
        getMvpView().onDisMissLoadingDialog();
    }

    @Override
    public void coinData(CoinModel coinModel) {
        this.coinModel = coinModel;
        if (isViewDetached()) {
            return;
        }
        getMvpView().setCoins(coinModel);
    }

    @Override
    public void balanceData(BigDecimal balance) {
        if (isViewDetached()) {
            return;
        }
        getMvpView().setBalance(balance);
    }

    @Override
    public void feesData(BigDecimal fee) {
        if (isViewDetached()) {
            return;
        }
        getMvpView().setFee(fee);
    }

    @Override
    public void gasPrice(BigInteger gasprice) {
        mGasPrice = gasprice;
        setFeesBySeekBarChange(50);
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
        getMvpView().onShowLoadingDialog();
    }

    @Override
    public void onFinishedGetUserInfo(UserInfoModel userInfo, String errorMsg) {
        if (isViewDetached()) {
            return;
        }
        getMvpView().onDisMissLoadingDialog();
        if (userInfo != null && userInfo.isGoogleAuthFlag()) {
            getMvpView().showGoogleEdit();
        }
    }

    @Override
    BaseModel createBaseModel() {
        smartTransferModel = new SmartTransferSource(this);
        return smartTransferModel;
    }


    @Override
    public void detachView() {
        userInfoPresenter.clear();
        super.detachView();
    }
}

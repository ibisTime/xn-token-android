package com.cdkj.token.wallet;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.nets.rx.RxTransformerHelper;
import com.cdkj.baselibrary.nets.rx.RxTransformerListHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.CoinTypeAndAddress;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.WalletFragmentAllData;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.wallet.private_wallet.WalletFragmentView;

import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinWatermarkWithCurrency;
import static com.cdkj.token.utils.LocalCoinDBUtils.updateLocalCoinList;

/**
 * 首页
 * Created by cdkj on 2018/8/30.
 */

public class WalletFragmentPresenter {

    private WalletFragmentView walletFragmentView;

    private Activity activity;
    private WalletFragmentAllData walletFragmentAllData;

    private final String HIND_SIGN = "****";//隐藏金额


    public WalletFragmentPresenter(WalletFragmentView walletFragmentView, Activity activity) {
        this.walletFragmentView = walletFragmentView;
        SoftReference<Activity> mS = new SoftReference<>(activity);
        this.activity = mS.get();
        walletFragmentAllData = new WalletFragmentAllData();
    }


    /**
     * 界面刷新
     */
    public Disposable allRefresh() {

        if (isDetached()) {
            return null;
        }

        //币种列表
        Observable coinObservable = getCoinObservable();
        //去中心化钱包数据
        Observable walletObservable = getWalletObservable();
        //公告消息数据
        Observable msgObservable = getMsgObservable();

        return Observable.mergeDelayError(coinObservable, walletObservable, msgObservable)

                .subscribe(o -> {
                    if (o instanceof List) {
                        updateLocalCoinList((List<LocalCoinDbModel>) o);
                    } else if (o instanceof BalanceListModel) {
                        walletFragmentAllData.setPrivateWalletData((BalanceListModel) o);
                    } else if (o instanceof CoinModel) {
                        walletFragmentAllData.setWalletData((CoinModel) o);
                    } else if (o instanceof MsgListModel) {
                        walletFragmentAllData.setBulletinString(getFirstMsg((MsgListModel) o));
                    }

                }, throwable -> {

                }, () -> {
                    if (walletFragmentView != null) {
                        walletFragmentView.onAllDataRefresh(walletFragmentAllData);
                    }
                });
    }

    /**
     * 币种列表刷新
     *
     * @param isPrivateWallet 是否中心化钱包
     * @return
     */
    public void walletCoinsRefresh(boolean isPrivateWallet) {

        if (isDetached()) {
            return;
        }

        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();

        //去中心化钱包数据
        Observable walletObservable = getWalletObservable();

        walletObservable
                .map(o -> {
                    walletFragmentAllData.setWalletData((CoinModel) o);
                    return transformToAdapterData((CoinModel) o);
                })
                .subscribe(o -> {
                    walletBalanceModels.addAll((Collection<? extends WalletBalanceModel>) o);
                }, throwable -> {
                    LogUtil.E("测试" + throwable);
                    if (walletFragmentView != null) {
                        walletFragmentView.onCoinBalanceRefresh(null);
                    }
                }, () -> {
                    if (walletFragmentView != null) {
                        walletFragmentView.onCoinBalanceRefresh(walletBalanceModels);
                    }
                });
    }


    /**
     * 币种列表刷新
     *
     * @param isPrivateWallet 是否中心化钱包
     * @return
     */
    public void cardChangeCoinsRefresh(boolean isPrivateWallet) {

        if (walletFragmentView == null || walletFragmentAllData == null) return;

        if (isPrivateWallet) {
            walletFragmentView.onCardChangeCoinBalaceRefresh(transformToAdapterData(walletFragmentAllData.getWalletData()));
        } else {
            walletFragmentView.onCardChangeCoinBalaceRefresh(transformToAdapterData(walletFragmentAllData.getWalletData()));
        }

    }


    //币种数据
    @NonNull
    private Observable getCoinObservable() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "");
        map.put("ename", "");
        map.put("cname", "");
        map.put("symbol", "");
        map.put("status", "0"); // 0已发布，1已撤下
        map.put("contractAddress", "");

        return RetrofitUtils.createApi(MyApi.class)
                .getCoinListObservable("802267", StringUtils.getRequestJsonString(map))
                .compose(RxTransformerListHelper.applySchedulersResult(activity));
    }

    /**
     * 中心化钱包数据请求
     *
     * @return
     */
    @NonNull
    private Observable getWalletObservable() {
        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        return RetrofitUtils.createApi(MyApi.class).getAccountObservable("802503", StringUtils.getRequestJsonString(map))
                .compose(RxTransformerHelper.applySchedulersResult(activity));
    }

    /**
     * @return
     */
    @NonNull
    private Observable getMsgObservable() {
        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", "1");
        map.put("limit", "1");
        map.put("status", "1");
        map.put("fromSystemCode", AppConfig.SYSTEMCODE);
        map.put("toSystemCode", AppConfig.SYSTEMCODE);

        return RetrofitUtils.createApi(MyApi.class)
                .getMsgListObservable("804040", StringUtils.getRequestJsonString(map))
                .compose(RxTransformerHelper.applySchedulersResult(activity));

    }


    /**
     * 转换为adapter数据 （我的钱包） getUnit
     *
     * @param data
     * @return
     */
    public List<WalletBalanceModel> transformToAdapterData(CoinModel data) {
        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();

        for (CoinModel.AccountListBean accountListBean : data.getAccountList()) {

            WalletBalanceModel walletBalanceModel = new WalletBalanceModel();

            walletBalanceModel.setCoinSymbol(accountListBean.getCurrency());


            walletBalanceModel.setAmount(accountListBean.getAmount());

            walletBalanceModel.setFrozenAmount(accountListBean.getFrozenAmount());


            if (accountListBean.getAmount() != null && accountListBean.getFrozenAmount() != null) {

                BigDecimal amount = accountListBean.getAmount();

                BigDecimal frozenAmount = accountListBean.getFrozenAmount();

                //可用=总资产-冻结
                walletBalanceModel.setAvailableAmount(amount.subtract(frozenAmount));
            }

            walletBalanceModel.setCoinImgUrl(getCoinWatermarkWithCurrency(accountListBean.getCurrency(), 0));

            walletBalanceModel.setLocalMarketPrice(accountListBean.getMarketStringByLocalSymbol());

            walletBalanceModel.setLocalAmount(accountListBean.getAmountStringByLocalMarket());

            walletBalanceModel.setAddress(accountListBean.getCoinAddress());

            walletBalanceModel.setAccountNumber(accountListBean.getAccountNumber());

            walletBalanceModel.setCoinBalance(accountListBean.getCoinBalance());

            walletBalanceModel.setCoinType(accountListBean.getType());

            walletBalanceModels.add(walletBalanceModel);
        }

        return walletBalanceModels;
    }

    /**
     * 转换为adapter数据 （秘钥钱包）
     *
     * @param data
     * @return
     */
    @NonNull
    public List<WalletBalanceModel> transformToPrivateAdapterData(BalanceListModel data) {
        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();

        for (BalanceListModel.AccountListBean accountListBean : data.getAccountList()) {

            WalletBalanceModel walletBalanceModel = new WalletBalanceModel();

            walletBalanceModel.setCoinSymbol(accountListBean.getSymbol());

            walletBalanceModel.setAmount(new BigDecimal(accountListBean.getBalance()));
            walletBalanceModel.setAvailableAmount(new BigDecimal(accountListBean.getBalance()));

            walletBalanceModel.setCoinImgUrl(getCoinWatermarkWithCurrency(accountListBean.getSymbol(), 0));

            walletBalanceModel.setLocalMarketPrice(accountListBean.getMarketStringByLocalSymbol());

            walletBalanceModel.setAddress(accountListBean.getAddress());

            if (accountListBean.getBalance() != null) {
                walletBalanceModel.setCoinBalance(accountListBean.getBalance().toString());
            }


            walletBalanceModels.add(walletBalanceModel);
        }
        return walletBalanceModels;
    }


    /**
     * 获取第一条公告数据
     *
     * @param msgListModel
     * @return
     */
    private String getFirstMsg(MsgListModel msgListModel) {
        if (msgListModel == null || msgListModel.getList() == null || msgListModel.getList().size() < 1) {
            return "";
        }
        return msgListModel.getList().get(0).getSmsTitle();

    }

    /**
     * 销毁关联
     */
    public void detachView() {
        activity = null;
        walletFragmentView = null;
    }

    /**
     * 上下文是否存在
     *
     * @return
     */
    public boolean isDetached() {
        return activity == null || activity.isFinishing();
    }


    /**
     * 是否显示资产
     *
     * @param isShow
     */
//    private void toggleAssectsByEyeState(boolean isShow) {
//        if (isShow) {
//            mBinding.tvAssetsShow.setImageResource(R.drawable.eye_open);
//            setWalletAssetsText(mWalletData);
//            shePrivateWalletAssectText(mPrivateWalletData);
//            countAllWalletAmount();
//
//        } else {
//
//            mBinding.tvAllWalletAmount.setText(AppConfig.getSymbolByType(SPUtilHelper.getLocalMarketSymbol()) + HIND_SIGN);
//            mBinding.cardChangeLayout.tvWalletAmount.setText(HIND_SIGN);
//            mBinding.cardChangeLayout.tvPriWalletAmount.setText(HIND_SIGN);
//            mBinding.tvAssetsShow.setImageResource(R.drawable.eye_close);
//        }
//    }

//    /**
//     * 计算所有钱包资产(个人 + 私有)
//     */
//    private void countAllWalletAmount() {
//
//        BigDecimal wallAmount = BigDecimal.ZERO;
//
//        BigDecimal priWallAmount = BigDecimal.ZERO;
//
//        if (mWalletData != null) {
//            wallAmount = new BigDecimal(mWalletData.getAmountStringByLocalMarket());
//        }
//        if (mPrivateWalletData != null) {
//            priWallAmount = new BigDecimal(mPrivateWalletData.getAmountStringByLocalMarket());
//        }
//
//        mBinding.tvAllWalletAmount.setText(AppConfig.getSymbolByType(SPUtilHelper.getLocalMarketSymbol()) + BigDecimalUtils.add(wallAmount, priWallAmount).toPlainString());
//
//    }

//
//    /**
//     * 异步获取本地货币并请求钱包数据
//     *
//     * @param isSetRecyclerData 用户调用 getPriWalletAssetsData方法
//     * @param isShowDialog
//     */
//    public void getLocalCoinAndRequestWalletDdata(boolean isSetRecyclerData, boolean isShowDialog) {
//        Disposable disposable = WalletHelper.getLocalCoinListAsync(localCoinDbModels -> {
//            if (localCoinDbModels != null) {
//                mChooseCoinList = getChooseCoinList(localCoinDbModels);
//            }
//        });
//        mSubscription.add(disposable); //用于结束异步
//    }


    /**
     * 获取用户选择的币种
     */
    private List<CoinTypeAndAddress> getChooseCoinList(List<LocalCoinDbModel> localCoinDbModels) {

        List<CoinTypeAndAddress> chooseCoinList = new ArrayList<>();

        String chooseSymbol = WalletHelper.getUserChooseCoinSymbolString(SPUtilHelper.getUserId()); //获取用户选择币种

        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());//获取钱包信息

        boolean isFirstChoose = WalletHelper.userIsCoinChoosed(SPUtilHelper.getUserId());


        for (LocalCoinDbModel localCoinDbModel : localCoinDbModels) {           //获取本地缓存的币种

            if (localCoinDbModel == null) {
                continue;
            }
            //如果用户没有添加过自选 则不进行自选币种判断
            if (isFirstChoose && TextUtils.indexOf(chooseSymbol, localCoinDbModel.getSymbol()) == -1) {
                continue;
            }

            CoinTypeAndAddress coinTypeAndAddress = new CoinTypeAndAddress();    //0 公链币（ETH BTC WAN） 1 ethtoken（ETH） 2 wantoken（WAN）        通过币种和type 添加地址

            if (LocalCoinDBUtils.isCommonChainCoinByType(localCoinDbModel.getType())) {

                if (TextUtils.equals(WalletHelper.COIN_BTC, localCoinDbModel.getSymbol())) {
                    coinTypeAndAddress.setAddress(walletDBModel.getBtcAddress());
                } else if (TextUtils.equals(WalletHelper.COIN_ETH, localCoinDbModel.getSymbol())) {
                    coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());
                } else if (TextUtils.equals(WalletHelper.COIN_WAN, localCoinDbModel.getSymbol())) {
                    coinTypeAndAddress.setAddress(walletDBModel.getWanAddress());
                }

            } else if (LocalCoinDBUtils.isEthTokenCoin(localCoinDbModel.getType())) {

                coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());

            } else if (LocalCoinDBUtils.isWanTokenCoin(localCoinDbModel.getType())) {

                coinTypeAndAddress.setAddress(walletDBModel.getWanAddress());

            }
            coinTypeAndAddress.setSymbol(localCoinDbModel.getSymbol());

            if (!TextUtils.isEmpty(coinTypeAndAddress.getAddress())) {
                chooseCoinList.add(coinTypeAndAddress);
            }

        }

        return chooseCoinList;
    }


}

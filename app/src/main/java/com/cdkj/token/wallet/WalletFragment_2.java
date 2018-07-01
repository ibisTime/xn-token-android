package com.cdkj.token.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.WalletBalanceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentWallet2Binding;
import com.cdkj.token.model.AddCoinChangeEvent;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.CoinTypeAndAddress;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.CardChangeLayout;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.AccountUtil.ALLSCALE;
import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;
import static com.cdkj.token.views.CardChangeLayout.BOTTOMVIEW;
import static com.cdkj.token.views.CardChangeLayout.TOPVIEW;

/**
 * 钱包资产
 * Created by cdkj on 2018/6/28.
 */

public class WalletFragment_2 extends BaseLazyFragment {

    private FragmentWallet2Binding mBinding;

    private RefreshHelper mRefreshHelper;
    private List<CoinTypeAndAddress> mChooseCoinList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_2, null, false);
        mChooseCoinList = new ArrayList<>();
        initRefresh();

        initCardChangeListener();

        initClickListener();

        getMsgRequest();
        getWalletAssetsData(true);
        return mBinding.getRoot();
    }

    public static WalletFragment_2 getInstance() {
        WalletFragment_2 fragment = new WalletFragment_2();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initClickListener() {

        //公告关闭
        mBinding.imgBulletinClose.setOnClickListener(view -> {
            mBinding.linLayoutBulletin.setVisibility(View.GONE);
        });

        //添加自选
        mBinding.imgAddCoin.setOnClickListener(view -> {
            AddChoiceCoinActivity.open(mActivity);
        });

    }

    /**
     * 顶部卡片布局改变监听
     */
    void initCardChangeListener() {
        mBinding.cardChangeLayout.setChangeCallBack(new CardChangeLayout.ChangeCallBack() {
            @Override
            public boolean onChangeBefor(int index) {

                boolean isHasInfo = WalletHelper.isUserAddedWallet(SPUtilHelper.getUserId());

                if (!isHasInfo) {
                    showDoubleWarnListen(getString(R.string.please_create_or_import_wallet), view -> {
                        IntoWalletBeforeActivity.open(mActivity);
                    });
                }

                return isHasInfo;
            }

            @Override
            public void onChange(int index) {
                changeLayoutByIndex(index);
            }
        });
    }

    /**
     * 改变布局
     *
     * @param index 在Layout中的索引
     */
    private void changeLayoutByIndex(int index) {
        LogUtil.E("布局改变" + index);
        switch (index) {
            case BOTTOMVIEW:                                         //私密钱包
                mBinding.imgAddCoin.setVisibility(View.VISIBLE);
                mBinding.imgChange.setImageResource(R.drawable.change_red);
                mBinding.imgTransfer.setImageResource(R.drawable.transfer_red);
                getPriWalletAssetsData(true);
                break;
            case TOPVIEW:                                         //个人钱包
                mBinding.imgAddCoin.setVisibility(View.GONE);
                mBinding.imgChange.setImageResource(R.drawable.change_blue);
                mBinding.imgTransfer.setImageResource(R.drawable.transfer_blue);
                getWalletAssetsData(false);
                break;
        }
    }

    private void initRefresh() {
        mRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack(mActivity) {
            @Override
            public View getRefreshLayout() {
                mBinding.refreshLayout.setEnableLoadmore(false);
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rvWallet;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return new WalletBalanceAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
            }
        });

        setRecyclerViewLayoutManager(mBinding.rvWallet);

        mRefreshHelper.init(10);
    }

    private void setRecyclerViewLayoutManager(RecyclerView rv) {
        rv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        rv.setNestedScrollingEnabled(false);
    }

    @Override
    protected void lazyLoad() {

        if (mBinding == null) {
            return;
        }

        getMsgRequest();
    }

    @Override
    protected void onInvisible() {

    }

    /**
     * 获取钱包资产数据
     *
     * @param isRequstPrivateWallet 是否同时请求私钥钱包数据
     */
    private void getWalletAssetsData(boolean isRequstPrivateWallet) {

        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(mActivity) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                mBinding.tvWalletAmount.setText(data.getTotalAmountCNY());
                mRefreshHelper.setPageIndex(1);
                mRefreshHelper.setData(transformToAdapterData(data), getString(R.string.no_assets), R.mipmap.order_none);

                if (isRequstPrivateWallet && WalletHelper.isUserAddedWallet(SPUtilHelper.getUserId())) {  //没有添加钱包不用请求私钥钱包数据
                    getPriWalletAssetsData(false);
                }

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 获取私有钱包数据
     *
     * @param isSetRecyclerData 是否设置recyclerData
     */
    private void getPriWalletAssetsData(boolean isSetRecyclerData) {

        if (mChooseCoinList.isEmpty()) {
            mChooseCoinList = getChooseCoinList();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("accountList", mChooseCoinList);

        Call<BaseResponseModel<BalanceListModel>> call = RetrofitUtils.createApi(MyApi.class).getBalanceList("802270", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<BalanceListModel>(mActivity) {
            @Override
            protected void onSuccess(BalanceListModel data, String SucMessage) {
                mBinding.tvPriWalletAmount.setText(data.getTotalAmountCNY());
                countAllWalletAmount();
                if (isSetRecyclerData) {
                    List<WalletBalanceModel> walletBalanceModels = transformToAdapterData(data);
                    mRefreshHelper.setPageIndex(1);
                    mRefreshHelper.setData(walletBalanceModels, getString(R.string.no_assets), R.mipmap.order_none);

                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 获取用户选择的币种
     */
    private List<CoinTypeAndAddress> getChooseCoinList() {

        List<CoinTypeAndAddress> chooseCoinList = new ArrayList<>();

        String chooseSymbol = WalletHelper.getUserChooseCoinSymbolString(SPUtilHelper.getUserId()); //获取用户选择币种

        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUsreId(SPUtilHelper.getUserId());//获取钱包信息

        boolean isFirstChoose = WalletHelper.userIsCoinChoosed(SPUtilHelper.getUserId());

        for (LocalCoinDbModel localCoinDbModel : WalletHelper.getLocalCoinList()) {           //获取本地缓存的币种

            if (localCoinDbModel == null) {
                continue;
            }
            //如果用户没有添加过自选 则不进行自选币种判断
            if (isFirstChoose && TextUtils.indexOf(chooseSymbol, localCoinDbModel.getSymbol()) == -1) {
                continue;
            }

            CoinTypeAndAddress coinTypeAndAddress = new CoinTypeAndAddress();    //0 ETH 1BTC 2WAN         通过币种和type 添加地址

            if (TextUtils.equals(localCoinDbModel.getType(), "0")) {
                coinTypeAndAddress.setAddress(walletDBModel.getEthAddress());
            } else if (TextUtils.equals(localCoinDbModel.getType(), "1")) {
                coinTypeAndAddress.setAddress(walletDBModel.getBtcAddress());
            } else if (TextUtils.equals(localCoinDbModel.getType(), "2")) {
                coinTypeAndAddress.setAddress(walletDBModel.getWanAddress());
            }
            coinTypeAndAddress.setSymbol(localCoinDbModel.getSymbol());
            chooseCoinList.add(coinTypeAndAddress);
        }

        return chooseCoinList;
    }

    /**
     * 计算所有钱包资产(个人 + 私有)
     */
    private void countAllWalletAmount() {
        if (TextUtils.isEmpty(mBinding.tvWalletAmount.getText().toString()) || TextUtils.isEmpty(mBinding.tvPriWalletAmount.getText().toString())) {
            return;
        }
        BigDecimal wallAmount = new BigDecimal(mBinding.tvWalletAmount.getText().toString());
        BigDecimal priWallAmount = new BigDecimal(mBinding.tvPriWalletAmount.getText().toString());
        mBinding.tvAllWalletAmount.setText(getString(R.string.money_sing) + BigDecimalUtils.add(wallAmount, priWallAmount).toPlainString());
    }

    /**
     * 转换为adapter数据
     *
     * @param data
     * @return
     */
    private List<WalletBalanceModel> transformToAdapterData(CoinModel data) {
        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();
        for (CoinModel.AccountListBean accountListBean : data.getAccountList()) {

            WalletBalanceModel walletBalanceModel = new WalletBalanceModel();

            walletBalanceModel.setCoinName(accountListBean.getCurrency());

            if (!TextUtils.isEmpty(accountListBean.getAmountString()) && !TextUtils.isEmpty(accountListBean.getFrozenAmountString())) {
                BigDecimal amount = new BigDecimal(accountListBean.getAmountString());
                BigDecimal frozenAmount = new BigDecimal(accountListBean.getFrozenAmountString());
                walletBalanceModel.setAmount(AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), accountListBean.getCurrency(), ALLSCALE));
            }

            walletBalanceModel.setCoinImgUrl(getCoinWatermarkWithCurrency(accountListBean.getCurrency(), 1));

            walletBalanceModel.setMarketPrice(accountListBean.getPriceCNY());

            walletBalanceModel.setAmountCny(accountListBean.getAmountCNY());
            walletBalanceModels.add(walletBalanceModel);
        }

        return walletBalanceModels;
    }

    @NonNull
    private List<WalletBalanceModel> transformToAdapterData(BalanceListModel data) {
        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();

        for (BalanceListModel.AccountListBean accountListBean : data.getAccountList()) {

            WalletBalanceModel walletBalanceModel = new WalletBalanceModel();

            walletBalanceModel.setCoinName(accountListBean.getSymbol());

            walletBalanceModel.setAmount(AccountUtil.amountFormatUnitForShow(new BigDecimal(accountListBean.getBalance()), 8));

            walletBalanceModel.setCoinImgUrl(getCoinWatermarkWithCurrency(accountListBean.getSymbol(), 1));

            walletBalanceModel.setMarketPrice(accountListBean.getPriceCNY());

            walletBalanceModel.setAmountCny(accountListBean.getAmountCNY());

            walletBalanceModels.add(walletBalanceModel);
        }
        return walletBalanceModels;
    }

    /**
     * 获取消息列表
     */
    public void getMsgRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", "1");
        map.put("limit", "1");
        map.put("status", "1");
        map.put("fromSystemCode", MyConfig.SYSTEMCODE);
        map.put("toSystemCode", MyConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getMsgList("804040", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(mActivity) {
            @Override
            protected void onSuccess(MsgListModel data, String SucMessage) {
                if (data.getList() == null || data.getList().size() < 1) {
                    mBinding.tvBulletin.setVisibility(View.GONE);
                    return;
                }
                mBinding.tvBulletin.setVisibility(View.VISIBLE);
                mBinding.tvBulletin.setText(data.getList().get(0).getSmsTitle());
            }


            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 显示确认取消dialog
     *
     * @param str
     * @param onPositiveListener
     */
    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
                .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg(str)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);

        commonDialog.show();
    }

    /**
     * 自选币种改变
     *
     * @param ad
     */
    @Subscribe
    public void addCoinChangeEvent(AddCoinChangeEvent ad) {
        mChooseCoinList.clear();
        getPriWalletAssetsData(true);
    }

}

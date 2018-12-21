package com.cdkj.token.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.WalletBalanceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentWallet2Binding;
import com.cdkj.token.find.FutureImageShowActivity;
import com.cdkj.token.find.MsgListActivity;
import com.cdkj.token.find.NoneActivity;
import com.cdkj.token.interfaces.LocalCoinCacheInterface;
import com.cdkj.token.interfaces.LocalCoinCachePresenter;
import com.cdkj.token.model.AddCoinChangeEvent;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.CoinTypeAndAddress;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.dialogs.InfoSureDialog;
import com.cdkj.token.wallet.account_wallet.BillListActivity;
import com.cdkj.token.wallet.create_guide.CreateWalletStartActivity;
import com.cdkj.token.wallet.private_wallet.WalletCoinDetailsActivity;
import com.cdkj.token.wallet.smart_transfer.SmartTransferActivity;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinIconByCoinSymbol;

/**
 * 钱包资产
 * Created by cdkj on 2018/6/28.
 */
//TODO 代码分离优化  请求嵌套优化
public class WalletFragment extends BaseLazyFragment {

    public static final String HIND_SIGN = "****"; // 隐藏金额

    private FragmentWallet2Binding mBinding;

    private RefreshHelper mRefreshHelper;

    private List<CoinTypeAndAddress> mChooseCoinList; // 用户自选的币种

    private boolean isPrivateWallet = false; // 当前是否是私密钱包 默认我的钱包

    private LocalCoinCachePresenter mlLocalCoinCachePresenter;

    private CoinModel mWalletData;


    public static WalletFragment getInstance() {
        WalletFragment fragment = new WalletFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_2, null, false);

        initViewState();

        initRefresh();

        initClickListener();

        getMsgRequest();

        initLocalCoinPresenter();

        initAssetsByEyeState(SPUtilHelper.isAssetsShow());

        mlLocalCoinCachePresenter.getCoinList(mActivity);  //开始时请求币种缓存

        return mBinding.getRoot();
    }

    /**
     * 本地币种缓存Presenter回调
     * 获取缓存后再获取对应钱包数据
     */
    void initLocalCoinPresenter() {
        mlLocalCoinCachePresenter = new LocalCoinCachePresenter(new LocalCoinCacheInterface() {
            @Override
            public void cacheEnd(List<LocalCoinDbModel> data) {
                getWalletAssetsData(true, false);
            }

        });
    }

    /**
     * 初始化View 状态 本地货币类型
     */
    void initViewState() {
        mBinding.tvAllAssets.setText(getString(R.string.wallet_assets, SPUtilHelper.getLocalMarketSymbol()));
        mBinding.tvMyWallet.setText(getString(R.string.my_wallet, SPUtilHelper.getLocalMarketSymbol()));

        mBinding.tvWalletSymbol.setText(AppConfig.getSymbolByType(SPUtilHelper.getLocalMarketSymbol()));
    }


    /**
     * 点击事件
     */
    private void initClickListener() {

        //资产显示隐藏
        mBinding.fralayoutAssetsShow.setOnClickListener(view -> {

            boolean isShow = !SPUtilHelper.isAssetsShow();
            toggleAssetsByEyeState(isShow);
            SPUtilHelper.saveIsAssetsShow(isShow);

        });

        //公告信息
        mBinding.tvBulletin.setOnClickListener(view -> {
            MsgListActivity.open(mActivity);
        });


        //闪兑
        mBinding.linLayoutTransferChange.setOnClickListener(view -> {
            FutureImageShowActivity.open(mActivity, NoneActivity.FLASH);
        });

        //公告关闭
        mBinding.imgBulletinClose.setOnClickListener(view -> {
            mBinding.linLayoutBulletin.setVisibility(View.GONE);
        });

        //添加自选
        mBinding.imgAddCoin.setOnClickListener(view -> {
            if (isPrivateWallet){
                AddPriChoiceCoinActivity.open(mActivity);
            } else {
                AddChoiceCoinActivity.open(mActivity);
            }

        });

        //我的钱包说明
        mBinding.imgMyWalletInfo.setOnClickListener(view -> {
            new InfoSureDialog(mActivity).setInfoTitle(getString(R.string.my_account_wallet)).setInfoContent(getString(R.string.my_wallet_introduction)).show();
        });

        //一键划转
        mBinding.linLayoutSmartTransfer.setOnClickListener(view -> {
            boolean isHasInfo = WalletHelper.isUserAddedWallet(SPUtilHelper.getUserId());
            if (!isHasInfo) {
                CreateWalletStartActivity.open(mActivity);
                return;
            }
            SmartTransferActivity.open(mActivity, isPrivateWallet);
        });

    }

    /**
     * 是否显示资产
     *
     * @param isShow
     */
    private void toggleAssetsByEyeState(boolean isShow) {
        if (isShow) {
            mBinding.tvAssetsShow.setImageResource(R.drawable.eye_open);
            setWalletAssetsText(mWalletData);
            countAllWalletAmount();
        } else {
            mBinding.tvAllWalletAmount.setText(AppConfig.getSymbolByType(SPUtilHelper.getLocalMarketSymbol()) + HIND_SIGN);
            mBinding.tvWalletAmount.setText(HIND_SIGN);
            mBinding.tvAssetsShow.setImageResource(R.drawable.eye_close);
        }
    }

    /**
     * 是否显示资产
     *
     * @param isShow
     */
    private void initAssetsByEyeState(boolean isShow) {
        if (isShow) {
            mBinding.tvAssetsShow.setImageResource(R.drawable.eye_open);
            mBinding.tvWalletAmount.setText("0.00");
            mBinding.tvAllWalletAmount.setText("0.00");
        } else {
            mBinding.tvAllWalletAmount.setText(AppConfig.getSymbolByType(SPUtilHelper.getLocalMarketSymbol()) + HIND_SIGN);
            mBinding.tvWalletAmount.setText(HIND_SIGN);
            mBinding.tvAssetsShow.setImageResource(R.drawable.eye_close);
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

                WalletBalanceAdapter walletBalanceAdapter = new WalletBalanceAdapter(listData);

                walletBalanceAdapter.setOnItemClickListener((adapter, view, position) -> {

                    if (isPrivateWallet) {

                        WalletBalanceModel localCoinModel = walletBalanceAdapter.getItem(position);

                        String address = SPUtilHelper.getPastBtcInfo().split("\\+")[0];

                        WalletCoinDetailsActivity.open(mActivity, localCoinModel,TextUtils.equals(address, localCoinModel.getAddress()));
                    } else {
                        BillListActivity.open(mActivity, walletBalanceAdapter.getItem(position));
                    }

                });

                return walletBalanceAdapter;
            }

            @Override
            public void onRefresh(int pageindex, int limit) {
                mlLocalCoinCachePresenter.getCoinList(mActivity);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {

            }
        });

        mBinding.rvWallet.setLayoutManager(getRecyclerViewLayoutManager());

        mBinding.rvWallet.setNestedScrollingEnabled(false);


        mRefreshHelper.init(10);
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
    private void getWalletAssetsData(boolean isRequstPrivateWallet, boolean isShowDialog) {

        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802504", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog) {
            showLoadingDialog();
        }

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(mActivity) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {

                mWalletData = data;
                toggleAssetsByEyeState(SPUtilHelper.isAssetsShow());
                mRefreshHelper.setPageIndex(1);
                mRefreshHelper.setData(transformToAdapterData(data), getString(R.string.no_assets), R.mipmap.order_none);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
                disMissLoading();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setWalletAssetsText(CoinModel data) {
        if (data == null) {
            mBinding.tvWalletAmount.setText("0.00");
            return;
        }

        mBinding.tvWalletAmount.setText(data.getAmountStringByLocalMarket());

    }



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
                } else if (TextUtils.equals(WalletHelper.COIN_USDT, localCoinDbModel.getSymbol())) {
                    coinTypeAndAddress.setAddress(walletDBModel.getBtcAddress());
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

    /**
     * 计算所有钱包资产
     */
    private void countAllWalletAmount() {

        BigDecimal wallAmount = BigDecimal.ZERO;

        BigDecimal priWallAmount = BigDecimal.ZERO;

        if (mWalletData != null) {
            wallAmount = new BigDecimal(mWalletData.getAmountStringByLocalMarket());
        }

        mBinding.tvAllWalletAmount.setText(AppConfig.getSymbolByType(SPUtilHelper.getLocalMarketSymbol()) + BigDecimalUtils.add(wallAmount, priWallAmount).toPlainString());

    }

    /**
     * 转换为adapter数据 （我的钱包） getUnit
     *
     * @param data
     * @return
     */
    private List<WalletBalanceModel> transformToAdapterData(CoinModel data) {
        List<WalletBalanceModel> walletBalanceModels = new ArrayList<>();

        if (data == null) {
            return walletBalanceModels;
        }

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

            walletBalanceModel.setCoinImgUrl(getCoinIconByCoinSymbol(accountListBean.getCurrency()));

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
     * 获取消息列表
     */
    public void getMsgRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", "1");
        map.put("limit", "1");
        map.put("status", "1");
        map.put("fromSystemCode", AppConfig.SYSTEMCODE);
        map.put("toSystemCode", AppConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getMsgList("804040", StringUtils.getRequestJsonString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(mActivity) {
            @Override
            protected void onSuccess(MsgListModel data, String SucMessage) {
                if (data.getList() == null || data.getList().size() < 1) {
                    mBinding.linLayoutBulletin.setVisibility(View.GONE);
                    return;
                }
                mBinding.linLayoutBulletin.setVisibility(View.VISIBLE);
                mBinding.tvBulletin.setText(data.getList().get(0).getSmsTitle());
            }


            @Override
            protected void onFinish() {
            }
        });
    }


    /**
     * 获取 LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    private LinearLayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

    }

    @Override
    public void onDestroy() {
        if (mlLocalCoinCachePresenter != null) {
            mlLocalCoinCachePresenter.clear();
        }
        super.onDestroy();
    }

    /**
     * 自选币种改变
     *
     * @param ad
     */
    @Subscribe
    public void addCoinChangeEventPri(AddCoinChangeEvent ad) {
        if (ad.getTag().equals(AddCoinChangeEvent.NOT_PRI)){
            getWalletAssetsData(false, true);
        }


    }


}

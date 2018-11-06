package com.cdkj.token.wallet.private_wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.NetUtils;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.BTCBillListAdapter;
import com.cdkj.token.adapter.CoinBillListAdapter;
import com.cdkj.token.adapter.EthTokenCoinBillListAdapter;
import com.cdkj.token.adapter.USDTBillListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWalletBillBinding;
import com.cdkj.token.model.BTCBillModel;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.CoinAddressShowModel;
import com.cdkj.token.model.CoinTypeAndAddress;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.model.LocalEthTokenCoinBill;
import com.cdkj.token.model.LocalUSDTCoinBill;
import com.cdkj.token.model.TransferSuccessEvent;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

import static com.cdkj.token.utils.AmountUtil.ALLSCALE;

/**
 * 去中心化钱包币种详情
 * Created by cdkj on 2018/6/8.
 */
//TODO 本地币种适配器请求抽象抽取
public class WalletCoinDetailsActivity extends AbsLoadActivity {

    private ActivityWalletBillBinding mBinding;

    private RefreshHelper mRefreshHelper;
    private WalletBalanceModel accountListBean;


    /**
     * @param context
     * @param localCoinModel
     */
    public static void open(Context context, WalletBalanceModel localCoinModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletCoinDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, localCoinModel);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_bill, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        accountListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (accountListBean != null) {
            mBaseBinding.titleView.setMidTitle(accountListBean.getCoinSymbol());
            ImgUtils.loadCircleImg(WalletCoinDetailsActivity.this, accountListBean.getCoinImgUrl(), mBinding.ivIcon);
            setAmountInfo();
        }

        initRefreshHelper();

        initClickListener();

        if (mBinding != null && mRefreshHelper != null) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }

    }

    /**
     * 设置总额显示
     */
    void setAmountInfo() {
        if (!TextUtils.isEmpty(accountListBean.getCoinBalance())) {
            mBinding.tvAmount.setText(AmountUtil.transformFormatToString(new BigDecimal(accountListBean.getCoinBalance()), accountListBean.getCoinSymbol(), ALLSCALE) + accountListBean.getCoinSymbol());

        }

        mBinding.tvAmountCny.setText("≈" + accountListBean.getLocalAmount() + " " + SPUtilHelper.getLocalMarketSymbol());
    }


    private void initClickListener() {

        //收款
        mBinding.linLayoutInCoin.setOnClickListener(view -> {
            if (accountListBean != null) {
                CoinAddressShowModel coinAddressShowModel = new CoinAddressShowModel();
                coinAddressShowModel.setAddress(accountListBean.getAddress());
                coinAddressShowModel.setCoinSymbol(accountListBean.getCoinSymbol());
                WalletAddressShowActivity.open(this, coinAddressShowModel);
            }
        });

        //转账
        mBinding.linLayoutOutCoin.setOnClickListener(view -> {

            if (!NetUtils.isNetworkConnected(this)) {
                UITipDialog.showInfo(this, getString(R.string.please_open_the_net));
                return;
            }
            //BTC转账
            if (isBTC()) {
                WalletBTCTransferActivity.open(this, accountListBean);
                return;
            }

            if (isUSDT()){
                WalletUSDTTransferActivity.open(this, accountListBean);
                return;
            }
            WalletTransferActivity.open(this, accountListBean);
        });

    }

    private void initRefreshHelper() {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerView;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                if (isBTC()) {
                    return getBTCBillListAdapter(listData);
                }

                if (accountListBean != null && LocalCoinDBUtils.isEthTokenCoinByName(accountListBean.getCoinSymbol())) { //ETH token币
                    return getEthTokenCoinDetailsListAdapter(listData);
                }

                if (isUSDT()){ // USDT
                    return getUSDTBillListAdapter(listData);
                }

                return getCoinDetailsListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                if (accountListBean == null){
                    return;
                }

                if (isBTC()) {             //比特币

                    getBTCBillRequest(pageindex, limit, isShowDialog);

                } else if (LocalCoinDBUtils.isEthTokenCoinByName(accountListBean.getCoinSymbol())) { //ETH token币

                    getEthTokenCoinBillList(pageindex, limit, isShowDialog);

                } else if (isUSDT()){ // USDT

                    getUSDTBillRequest(pageindex, limit, isShowDialog);

                } else {                                                                //其它币种
                    getBillRequest(pageindex, limit, isShowDialog);
                }
            }

        });

        mRefreshHelper.init(RefreshHelper.LIMITE);
    }

    /**
     * 获取比特币流水列表
     *
     * @param listData
     * @return
     */
    private RecyclerView.Adapter getBTCBillListAdapter(List listData) {

        BTCBillListAdapter coinDetailsListAdapter = new BTCBillListAdapter(listData);

        coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
            BTCBillModel btcBillModel = coinDetailsListAdapter.getItem(position);
            if (accountListBean != null) {
                btcBillModel.setAddress(accountListBean.getAddress());
            }
            BTCTransactionDetailsActivity.open(this, btcBillModel);
        });

        return coinDetailsListAdapter;
    }


    /**
     * 获取BTC流水
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    private void getBTCBillRequest(int pageindex, int limit, boolean isShowDialog) {

        if (accountListBean == null) return;

        Map<String, String> map = new HashMap<>();
        map.put("address", accountListBean.getAddress());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<BTCBillModel>>> btcBillCall = RetrofitUtils.createApi(MyApi.class).getBTCBillList("802221", StringUtils.getRequestJsonString(map));

        addCall(btcBillCall);

        if (isShowDialog) showLoadingDialog();

        btcBillCall.enqueue(new BaseResponseModelCallBack<ResponseInListModel<BTCBillModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<BTCBillModel> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_record), R.mipmap.order_none);
            }

            @Override
            protected void onFinish() {
                getCoinBalance(accountListBean.getCoinSymbol());
            }
        });

    }

    /**
     * 获取数据适配器
     *
     * @param listData
     * @return
     */
    @NonNull
    CoinBillListAdapter getCoinDetailsListAdapter(List listData) {
        String coinSymbol = "";
        if (accountListBean != null) {
            coinSymbol = accountListBean.getCoinSymbol();
        }
        CoinBillListAdapter coinDetailsListAdapter = new CoinBillListAdapter(listData, coinSymbol);
        coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
            String coinType = "";
            if (accountListBean != null) {
                coinType = accountListBean.getCoinSymbol();
            }
            TransactionDetailsActivity.open(WalletCoinDetailsActivity.this, coinDetailsListAdapter.getItem(position), coinType);
        });
        return coinDetailsListAdapter;
    }

    /**
     * 获取ETH token币种数据适配器
     *
     * @param listData
     * @return
     */
    @NonNull
    EthTokenCoinBillListAdapter getEthTokenCoinDetailsListAdapter(List listData) {
        String coinSymbol = "";
        if (accountListBean != null) {
            coinSymbol = accountListBean.getCoinSymbol();
        }
        EthTokenCoinBillListAdapter coinDetailsListAdapter = new EthTokenCoinBillListAdapter(listData, coinSymbol);
        coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
            String coinType = "";
            if (accountListBean != null) {
                coinType = accountListBean.getCoinSymbol();
            }
            EthTokenCoinTransactionDetailsActivity.open(WalletCoinDetailsActivity.this, coinDetailsListAdapter.getItem(position), coinType);

        });
        return coinDetailsListAdapter;
    }

    /**
     * 获取流水
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getBillRequest(int pageindex, int limit, boolean isShowDialog) {

        if (accountListBean == null) return;

        Map<String, String> map = new HashMap<>();
        map.put("symbol", accountListBean.getCoinSymbol());
        map.put("address", accountListBean.getAddress());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<LocalCoinBill>>> call = RetrofitUtils.createApi(MyApi.class).getLocalCoinBillList("802271", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<LocalCoinBill>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<LocalCoinBill> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_record), R.mipmap.order_none);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
            }

            @Override
            protected void onFinish() {
                getCoinBalance(accountListBean.getCoinSymbol());
            }
        });
    }

    /**
     * 获取USDT流水列表
     *
     * @param listData
     * @return
     */
    USDTBillListAdapter getUSDTBillListAdapter(List listData) {

        USDTBillListAdapter coinDetailsListAdapter = new USDTBillListAdapter(listData, accountListBean.getAddress());

        coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
            LocalUSDTCoinBill usdtCoinBill = coinDetailsListAdapter.getItem(position);
//            if (accountListBean != null) {
//                usdtCoinBill.setAddress(accountListBean.getAddress());
//            }
            USDTTransactionDetailsActivity.open(this, usdtCoinBill, accountListBean.getAddress());
        });

        return coinDetailsListAdapter;
    }


    /**
     * 获取流水
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getUSDTBillRequest(int pageindex, int limit, boolean isShowDialog) {

        if (accountListBean == null) return;

        Map<String, String> map = new HashMap<>();
        map.put("symbol", accountListBean.getCoinSymbol());
//        map.put("address", accountListBean.getAddress());
        map.put("address", "1EQZmWtK1t6yFYi8zJd7gTx2yUHTZa1ePH");
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<LocalUSDTCoinBill>>> call = RetrofitUtils.createApi(MyApi.class).getUSDTCoinBillList("802505", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<LocalUSDTCoinBill>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<LocalUSDTCoinBill> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_record), R.mipmap.order_none);
            }

            @Override
            protected void onFinish() {
                getCoinBalance(accountListBean.getCoinSymbol());
            }

            @Override
            public void onResponse(Call<BaseResponseModel<ResponseInListModel<LocalUSDTCoinBill>>> call, Response<BaseResponseModel<ResponseInListModel<LocalUSDTCoinBill>>> response) {
                super.onResponse(call, response);
            }
        });
    }

    /**
     * 获取流水
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getEthTokenCoinBillList(int pageindex, int limit, boolean isShowDialog) {

        if (accountListBean == null) return;

        Map<String, String> map = new HashMap<>();
        map.put("contractAddress", LocalCoinDBUtils.getLocalCoinContractAddress(accountListBean.getCoinSymbol()));
        map.put("address", accountListBean.getAddress());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<LocalEthTokenCoinBill>>> call = RetrofitUtils.createApi(MyApi.class).getEthTokenCoinBillList("802308", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<LocalEthTokenCoinBill>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<LocalEthTokenCoinBill> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_record), R.mipmap.order_none);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
            }

            @Override
            protected void onFinish() {
                getCoinBalance(accountListBean.getCoinSymbol());
            }
        });
    }

    /**
     * 判断当前所属是否比特币
     *
     * @return
     */
    public boolean isBTC() {
        if (accountListBean == null) {
            return false;
        }
        return TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_BTC);
    }

    /**
     * 判断当前所属是否比特币
     *
     * @return
     */
    public boolean isUSDT() {
        if (accountListBean == null) {
            return false;
        }
        return TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_USDT);
    }

    /**
     * 获取币种余额
     */
    private void getCoinBalance(String coin) {

        if (accountListBean == null) return;

        List<CoinTypeAndAddress> coinList = new ArrayList<>();

        CoinTypeAndAddress coinTypeAndAddress = new CoinTypeAndAddress();

        coinTypeAndAddress.setSymbol(coin);
        coinTypeAndAddress.setAddress(accountListBean.getAddress());

        coinList.add(coinTypeAndAddress);

        Map<String, Object> map = new HashMap<>();
        map.put("accountList", coinList);

        Call<BaseResponseModel<BalanceListModel>> call = RetrofitUtils.createApi(MyApi.class).getBalanceList("802270", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<BalanceListModel>(this) {
            @Override
            protected void onSuccess(BalanceListModel data, String SucMessage) {

                if (data == null || data.getAccountList() == null
                        || data.getAccountList().size() == 0 || data.getAccountList().get(0) == null) {
                    return;
                }

                accountListBean.setCoinBalance(data.getAccountList().get(0).getBalance().toString());
                setAmountInfo();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    /**
     * 广播成功 刷新数据
     *
     * @param transferSuccessEvent
     */
    @Subscribe
    public void EventRefreshBIllList(TransferSuccessEvent transferSuccessEvent) {
        if (mBinding != null && mRefreshHelper != null) {
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }


}

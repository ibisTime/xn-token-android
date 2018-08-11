package com.cdkj.token.wallet.coin_detail;

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
import com.cdkj.token.adapter.CoinDetailsListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWalletBillBinding;
import com.cdkj.token.model.LocalCoinBill;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.AccountUtil.ETHSCALE;

/**
 * 钱包币种 详情
 * Created by cdkj on 2018/6/8.
 */

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
            mBaseBinding.titleView.setMidTitle(accountListBean.getCoinName());
            ImgUtils.loadCircleImg(WalletCoinDetailsActivity.this, accountListBean.getCoinImgUrl(), mBinding.ivIcon);
            setAmountInfo();
        }

        initRefreshHelper();

        initClickListener();

    }

    /**
     * 设置总额显示
     */
    void setAmountInfo() {
        if (!TextUtils.isEmpty(accountListBean.getCoinBalance())) {
            mBinding.tvAmount.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(accountListBean.getCoinBalance()),accountListBean.getCoinName(), ETHSCALE) + accountListBean.getCoinName());

        }

        if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_CNY)) {
            if (TextUtils.isEmpty(accountListBean.getAmountCny())) {
                mBinding.tvAmountCny.setText("≈0" + "" + WalletHelper.getShowLocalCoinType());
            } else {
                mBinding.tvAmountCny.setText("≈" + accountListBean.getAmountCny() + " " + WalletHelper.getShowLocalCoinType());
            }
        } else if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_USD)) {
            if (TextUtils.isEmpty(accountListBean.getAmountUSD())) {
                mBinding.tvAmountCny.setText("≈0" + " " + WalletHelper.getShowLocalCoinType());
            } else {
                mBinding.tvAmountCny.setText("≈" + accountListBean.getAmountUSD() + " " + WalletHelper.getShowLocalCoinType());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBinding != null && mRefreshHelper != null) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    private void initClickListener() {

        //收款
        mBinding.linLayoutInCoin.setOnClickListener(view -> {
            if (accountListBean != null) {
                WalletAddressShowActivity.open(this, accountListBean.getAddress());
            }
        });

        //转账
        mBinding.linLayoutOutCoin.setOnClickListener(view -> {

            if (!NetUtils.isNetworkConnected(this)) {
                UITipDialog.showInfo(this, getString(R.string.please_open_the_net));
                return;
            }

            if (accountListBean == null) return;

            //BTC转账
            if (TextUtils.equals(accountListBean.getCoinName(), WalletHelper.COIN_BTC)) {

                WalletBTCTransferActivity.open(this, accountListBean);

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
                CoinDetailsListAdapter coinDetailsListAdapter = getCoinDetailsListAdapter(listData);
                return coinDetailsListAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getBillRequest(pageindex, limit, isShowDialog);
            }

        });

        mRefreshHelper.init(RefreshHelper.LIMITE);
    }

    /**
     * 获取数据适配器
     *
     * @param listData
     * @return
     */
    @NonNull
    CoinDetailsListAdapter getCoinDetailsListAdapter(List listData) {
        String coinSymbol = "";
        if (accountListBean != null) {
            coinSymbol = accountListBean.getCoinName();
        }

        CoinDetailsListAdapter coinDetailsListAdapter = new CoinDetailsListAdapter(listData, coinSymbol);

        coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
            String coinType = "";
            if (accountListBean != null) {
                coinType = accountListBean.getCoinName();
            }
            TransactionDetailsActivity.open(WalletCoinDetailsActivity.this, coinDetailsListAdapter.getItem(position), coinType);
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
        map.put("symbol", accountListBean.getCoinName());
        map.put("address", accountListBean.getAddress());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<LocalCoinBill>>> call = RetrofitUtils.createApi(MyApi.class).getLocalCoinBillList("802271", StringUtils.getJsonToString(map));

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
                disMissLoading();
            }
        });


    }

}

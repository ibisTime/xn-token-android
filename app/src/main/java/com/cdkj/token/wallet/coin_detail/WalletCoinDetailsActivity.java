package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.NetUtils;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinDetailsListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWallteBillBinding;
import com.cdkj.token.databinding.HeaderBillListBinding;
import com.cdkj.token.model.BalanceListModel;
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
import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * 钱包币种 详情
 * Created by cdkj on 2018/6/8.
 */

public class WalletCoinDetailsActivity extends AbsBaseLoadActivity {

    private ActivityWallteBillBinding mBinding;

    private RefreshHelper mRefreshHelper;
    private HeaderBillListBinding mHeaderBinding;
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
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallte_bill, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_bill_list, null, false);

        accountListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (accountListBean != null) {

            ImgUtils.loadAvatar(WalletCoinDetailsActivity.this,accountListBean.getCoinImgUrl(), mHeaderBinding.ivIcon);

            mHeaderBinding.tvSymbol.setText(accountListBean.getCoinName());
            mBaseBinding.titleView.setMidTitle(accountListBean.getCoinName());

            if (!TextUtils.isEmpty(accountListBean.getCoinBalance())) {
                mHeaderBinding.tvAmount.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(accountListBean.getCoinBalance()), ETHSCALE));

            }

            if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_CNY)) {
                if (TextUtils.isEmpty(accountListBean.getAmountCny())) {
                    mHeaderBinding.tvAmountCny.setText("≈0" + "" + WalletHelper.getShowLocalCoinType());
                } else {
                    mHeaderBinding.tvAmountCny.setText("≈" + accountListBean.getAmountCny() + " " + WalletHelper.getShowLocalCoinType());
                }
            } else if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_USD)) {
                if (TextUtils.isEmpty(accountListBean.getAmountUSD())) {
                    mHeaderBinding.tvAmountCny.setText("≈0" + " " + WalletHelper.getShowLocalCoinType());
                } else {
                    mHeaderBinding.tvAmountCny.setText("≈" + accountListBean.getAmountUSD() + " " + WalletHelper.getShowLocalCoinType());
                }
            }

        }
        initRefreshHelper();

        initClickListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBinding != null && mRefreshHelper != null) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    private void initClickListener() {

        mBinding.linLayoutGetmoney.setOnClickListener(view -> {
            if (accountListBean != null) {
                WalletAddressShowActivity.open(this, accountListBean.getCoinName());
            }
        });

        mBinding.linLayoutSendMoney.setOnClickListener(view -> {

            if (!NetUtils.isNetworkConnected(this)) {
                UITipDialog.showInfo(this, getString(R.string.please_open_the_net));
                return;
            }

            if (accountListBean == null) return;

            WalletTransferActivity.open(this, accountListBean);
            ;
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
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {

                String coinSymbol = "";
                if (accountListBean != null) {
                    coinSymbol = accountListBean.getCoinName();
                }

                CoinDetailsListAdapter coinDetailsListAdapter = new CoinDetailsListAdapter(listData, coinSymbol);

                coinDetailsListAdapter.setHeaderAndEmpty(true);

                coinDetailsListAdapter.addHeaderView(mHeaderBinding.getRoot());

                coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
                    String coinType = "";
                    if (accountListBean != null) {
                        coinType = accountListBean.getCoinName();
                    }
                    TransactionDetailsActivity.open(WalletCoinDetailsActivity.this, coinDetailsListAdapter.getItem(position), coinType);
                });

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
                LogUtil.E("详情错误" + errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}

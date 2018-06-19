package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.WalletHelper;

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

public class WalletCoinDetailsActivity extends AbsBaseLoadActivity {

    private ActivityWallteBillBinding mBinding;

    private RefreshHelper mRefreshHelper;
    private HeaderBillListBinding mHeaderBinding;
    private BalanceListModel.AccountListBean accountListBean;


    /**
     * @param context
     * @param localCoinModel
     */
    public static void open(Context context, BalanceListModel.AccountListBean localCoinModel) {
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

        initRefreshHelper();

        accountListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (accountListBean != null) {
            mHeaderBinding.ivIcon.setImageResource(WalletHelper.getCoinIconByType(accountListBean.getSymbol()));
            mHeaderBinding.tvSymbol.setText(accountListBean.getSymbol());
            mBaseBinding.titleView.setMidTitle(accountListBean.getSymbol());
            mHeaderBinding.tvAmount.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(accountListBean.getBalance()), ETHSCALE));
            mHeaderBinding.tvAmountCny.setText("≈" + accountListBean.getAmountCNY() + " CNY");

        }


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
            WalletAddressShowActivity.open(this);
        });

        mBinding.linLayoutSendMoney.setOnClickListener(view -> {

            if (!NetUtils.isNetworkConnected(this)) {
                UITipDialog.showInfo(this, getString(R.string.please_open_the_net));
                return;
            }

            WalletTransferActivity.open(this, getIntent().getParcelableExtra(CdRouteHelper.DATASIGN));
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

                CoinDetailsListAdapter coinDetailsListAdapter = new CoinDetailsListAdapter(listData);

                coinDetailsListAdapter.setHeaderAndEmpty(true);

                coinDetailsListAdapter.addHeaderView(mHeaderBinding.getRoot());

                coinDetailsListAdapter.setOnItemClickListener((adapter, view, position) -> {
                    String coinType = "";
                    if (accountListBean != null) {
                        coinType = accountListBean.getSymbol();
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
        map.put("symbol", accountListBean.getSymbol());
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

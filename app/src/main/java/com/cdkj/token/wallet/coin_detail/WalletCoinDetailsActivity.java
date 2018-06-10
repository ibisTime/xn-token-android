package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinDetailsListAdapter;
import com.cdkj.token.databinding.ActivityWallteBillBinding;
import com.cdkj.token.databinding.HeaderBillListBinding;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.WalletHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * 钱包币种 详情
 * Created by cdkj on 2018/6/8.
 */

public class WalletCoinDetailsActivity extends AbsBaseLoadActivity {

    private ActivityWallteBillBinding mBinding;

    private RefreshHelper mRefreshHelper;
    private HeaderBillListBinding mHeaderBinding;


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


        mRefreshHelper.setData(null, "暂无记录", R.mipmap.order_none);

        BalanceListModel.AccountListBean localCoinModel = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (localCoinModel != null) {
            mHeaderBinding.ivIcon.setImageResource(WalletHelper.getCoinIconByType(localCoinModel.getSymbol()));
            mHeaderBinding.tvSymbol.setText(localCoinModel.getSymbol());
            mBaseBinding.titleView.setMidTitle(localCoinModel.getSymbol());
            mHeaderBinding.tvAmount.setText(AccountUtil.amountFormatUnitForShow(new BigDecimal(localCoinModel.getBalance()), 8));

        }


        initClickListener();

    }

    private void initClickListener() {

        mBinding.linLayoutGetmoney.setOnClickListener(view -> {
            WalletAddressShowActivity.open(this);
        });

        mBinding.linLayoutSendMoney.setOnClickListener(view -> WalletTransferActivity.open(this, getIntent().getParcelableExtra(CdRouteHelper.DATASIGN)));

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

                return coinDetailsListAdapter;
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {

            }

        });

        mRefreshHelper.init(RefreshHelper.LIMITE);
    }
}

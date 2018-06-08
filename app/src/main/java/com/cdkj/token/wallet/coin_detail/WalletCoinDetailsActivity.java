package com.cdkj.token.wallet.coin_detail;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinDetailsListAdapter;
import com.cdkj.token.databinding.ActivityWallteBillBinding;
import com.cdkj.token.databinding.HeaderBillListBinding;

import java.util.List;

/**
 * 钱包币种 详情
 * Created by cdkj on 2018/6/8.
 */

public class WalletCoinDetailsActivity extends AbsBaseLoadActivity {

    private ActivityWallteBillBinding mBinding;

    private RefreshHelper mRefreshHelper;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletCoinDetailsActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallte_bill, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        initRefreshHelper();

        mRefreshHelper.setData(null, "暂无记录", R.mipmap.order_none);

        initClickListener();

    }

    private void initClickListener() {

        mBinding.linLayoutGetmoney.setOnClickListener(view -> {
            WalletAddressShowActivity.open(this);
        });

        mBinding.linLayoutSendMoney.setOnClickListener(view -> WalletTransferActivity.open(this));

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
                HeaderBillListBinding mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_bill_list, null, false);
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

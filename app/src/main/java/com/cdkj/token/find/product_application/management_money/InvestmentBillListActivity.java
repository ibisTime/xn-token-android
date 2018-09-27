package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.InvestmentBillListAdapter;

import java.util.List;

/**
 * 投资账单
 * Created by cdkj on 2018/9/27.
 */

public class InvestmentBillListActivity extends AbsLoadActivity {

    protected LayoutCommonRecyclerRefreshBinding mBinding;

    private RefreshHelper refreshHelper;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InvestmentBillListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_common_recycler_refresh, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.investment_bill);
        mBaseBinding.titleView.setRightImg(R.drawable.ic_calendar);

        inRefreshHelper();

    }

    void inRefreshHelper() {
        refreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
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
                return new InvestmentBillListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {

            }
        });

        refreshHelper.init(RefreshHelper.LIMITE);
    }


}

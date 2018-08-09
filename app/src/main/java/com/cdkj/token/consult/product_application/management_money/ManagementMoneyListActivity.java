package com.cdkj.token.consult.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.token.R;
import com.cdkj.token.adapter.ManagementMoneyListAdapter;
import com.cdkj.token.common.AbsRefreshClipListActivity;

import java.util.List;

/**
 * 量化理财
 * Created by cdkj on 2018/8/9.
 */

public class ManagementMoneyListActivity extends AbsRefreshClipListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ManagementMoneyListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void topTitleViewRightClick() {
        MyManagementMoneyListActivity.open(this);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setStatusBarBlue();
        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(R.string.lianghualicai);
        mBaseBinding.titleView.setRightTitle(getString(R.string.my_management_money));
        initRefreshHelper(10);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        listData.add("");
        listData.add("");
        listData.add("");
        listData.add("");
        listData.add("");
        listData.add("");
        listData.add("");
        listData.add("");
        ManagementMoneyListAdapter managementMoneyListAdapter = new ManagementMoneyListAdapter(listData);

        managementMoneyListAdapter.setOnItemClickListener((adapter, view, position) -> {

        });

        return managementMoneyListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }

}

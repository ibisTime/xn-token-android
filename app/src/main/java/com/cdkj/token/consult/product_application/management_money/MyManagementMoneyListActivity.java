package com.cdkj.token.consult.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.token.R;
import com.cdkj.token.adapter.ManagementMoneyListAdapter;
import com.cdkj.token.adapter.MyManagementMoneyAdapter;
import com.cdkj.token.common.AbsRefreshClipListActivity;

import java.util.List;

/**
 * 我的量化理财
 * Created by cdkj on 2018/8/9.
 */

public class MyManagementMoneyListActivity extends AbsRefreshClipListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyManagementMoneyListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setStatusBarBlue();
        setTitleBgBlue();
        mBaseBinding.titleView.setMidTitle(getString(R.string.my_management_money));
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
        MyManagementMoneyAdapter adapter = new MyManagementMoneyAdapter(listData);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MyManagementMoneyDetailsActivity.open(this);
        });

        return adapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }

}

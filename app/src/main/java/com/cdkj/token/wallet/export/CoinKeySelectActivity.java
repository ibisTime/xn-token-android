package com.cdkj.token.wallet.export;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinKeySelectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 币种选择
 * Created by cdkj on 2018/6/7.
 */

public class CoinKeySelectActivity extends AbsRefreshListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinKeySelectActivity.class);
        context.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        CoinKeySelectAdapter selectAdapter = new CoinKeySelectAdapter(listData);
        selectAdapter.setOnItemClickListener((adapter, view, position) -> {
            CoinPrivateKeyShowActivity.open(CoinKeySelectActivity.this,selectAdapter.getItem(position));
        });
        return selectAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.export_private_key);
        initRefreshHelper(10);
        List<String> strings = new ArrayList<>();
        strings.add("ETH私钥");
        strings.add("WAN私钥");
        mRefreshHelper.setData(strings, "", 0);
    }
}

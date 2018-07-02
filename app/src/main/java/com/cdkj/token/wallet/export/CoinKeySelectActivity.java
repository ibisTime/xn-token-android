package com.cdkj.token.wallet.export;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinKeySelectAdapter;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
//            CoinPrivateKeyShowActivity.open(CoinKeySelectActivity.this, selectAdapter.getItem(position).getCoinType());
        });
        return selectAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        showLoadingDialog();
        mBaseBinding.titleView.setMidTitle(R.string.export_private_key);
        initRefreshHelper(10);
        mRefreshBinding.refreshLayout.setEnableRefresh(false);
        mRefreshBinding.refreshLayout.setEnableLoadmore(false);
        mSubscription.add(
                Observable.just("")
                        .subscribeOn(Schedulers.newThread())
                        .map(s -> WalletHelper.getLocalCoinList())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(() -> disMissLoading())
                        .subscribe(s -> {
                            mRefreshHelper.setData(s, "", 0);
                        }, throwable -> {

                        })
        );
    }
}

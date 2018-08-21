package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.ManagementMoneyListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.AbsRefreshClipListActivity;
import com.cdkj.token.model.ManageMoneyBuySuccessEvent;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.StringUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

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
        initRefreshHelper();

        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        ManagementMoneyListAdapter managementMoneyListAdapter = new ManagementMoneyListAdapter(listData);

        managementMoneyListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ManagementMoney managementMoney = managementMoneyListAdapter.getItem(position);
            if (managementMoney == null) return;
            ManagementMoneyDetailsActivity.open(ManagementMoneyListActivity.this, managementMoney.getCode());
        });

        return managementMoneyListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();
        map.put("status", "appDisplay");
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) {
            showLoadingDialog();
        }

        Call<BaseResponseModel<ResponseInListModel<ManagementMoney>>> call = RetrofitUtils.createApi(MyApi.class).getMoneyManageProductList("625510", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<ManagementMoney>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<ManagementMoney> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_product), 0);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                mRefreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    //购买成功刷新数据
    @Subscribe
    public void EventRefreshList(ManageMoneyBuySuccessEvent manageMoneyBuySuccessEvent) {
        if (mRefreshHelper != null) {
            mRefreshHelper.onDefaluteMRefresh(false);
        }
    }


}

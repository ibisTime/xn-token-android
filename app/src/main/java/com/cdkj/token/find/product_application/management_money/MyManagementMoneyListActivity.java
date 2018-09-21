package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.MyManagementMoneyAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.AbsRefreshClipListActivity;
import com.cdkj.token.model.MyManamentMoneyProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

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
        initRefreshHelper();
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {

        MyManagementMoneyAdapter adapter = new MyManagementMoneyAdapter(listData);

        adapter.setOnItemClickListener((adapter1, view, position) -> {
            MyManagementMoneyDetailsActivity.open(this, adapter.getItem(position));
        });

        return adapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (isShowDialog) showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("language", SPUtilHelper.getLanguage());

        Call<BaseResponseModel<ResponseInListModel<MyManamentMoneyProduct>>> call = RetrofitUtils.createApi(MyApi.class).getMyMoneyManageProductList("625526", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MyManamentMoneyProduct>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<MyManamentMoneyProduct> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_product), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

}

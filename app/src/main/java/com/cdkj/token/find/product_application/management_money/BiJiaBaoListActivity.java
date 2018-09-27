package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.ManagementMoneyListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityRefreshMoneyManagerBinding;
import com.cdkj.token.model.ManagementMoney;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币加宝投资列表
 * Created by cdkj on 2018/9/26.
 */

public class BiJiaBaoListActivity extends BaseActivity {

    private ActivityRefreshMoneyManagerBinding mBinding;

    private RefreshHelper mRefreshHelper;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BiJiaBaoListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_refresh_money_manager);

        UIStatusBarHelper.setStatusBarLightMode(this);
        UIStatusBarHelper.translucent(this);

        initClickListener();

        initRefreshHelper();
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private void initClickListener() {
        mBinding.imgFinish.setOnClickListener(view -> finish());
        mBinding.tvMyInvesment.setOnClickListener(view -> MyInvestmentDetails.open(this));
    }

    void initRefreshHelper() {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerView;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });

        mRefreshHelper.init(RefreshHelper.LIMITE);
    }

    /**
     * 获取数据适配器
     *
     * @param listData
     * @return
     */
    public RecyclerView.Adapter getListAdapter(List listData) {

        ManagementMoneyListAdapter managementMoneyListAdapter = new ManagementMoneyListAdapter(listData);

        managementMoneyListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ManagementMoney managementMoney = managementMoneyListAdapter.getItem(position);
            if (managementMoney == null) return;
            BijiaBaoDetailsActivity.open(BiJiaBaoListActivity.this, managementMoney.getCode());
        });

        return managementMoneyListAdapter;
    }


    /**
     * 获取数据
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();
        map.put("status", "appDisplay");
        map.put("start", pageindex + "");
        map.put("limit", limit + "");
        map.put("language", SPUtilHelper.getLanguage());
        if (isShowDialog) {
            showLoadingDialog();
        }

        Call<BaseResponseModel<ResponseInListModel<ManagementMoney>>> call = RetrofitUtils.createApi(MyApi.class).getMoneyManageProductList("625510", StringUtils.getRequestJsonString(map));

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
                disMissLoadingDialog();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDestroy();
        }
    }

}

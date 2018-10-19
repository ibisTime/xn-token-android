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
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.databinding.LayoutCommonRecyclerRefreshBinding;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.MyIncomeBillListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.InvestBillModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 邀请收益账单
 * Created by cdkj on 2018/9/27.
 */

public class MyIncomeBillListActivity extends AbsLoadActivity {

    protected LayoutCommonRecyclerRefreshBinding mBinding;

    private RefreshHelper refreshHelper;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyIncomeBillListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_common_recycler_refresh, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.income_invite);

        inRefreshHelper();

        refreshHelper.onDefaluteMRefresh(true);

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
                return new MyIncomeBillListAdapter(listData, MyIncomeBillListActivity.this);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getBillListRequest(pageindex, limit, isShowDialog);
            }
        });

        refreshHelper.init(RefreshHelper.LIMITE);
    }


    /**
     * 分页查询我的邀请收益
     *
     * @param pageindex
     * @param limit
     * @param isShowDialog
     */
    public void getBillListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("start", pageindex + "");
        map.put("limit", limit + "");

        if (isShowDialog) showLoadingDialog();

        Call<BaseResponseModel<ResponseInListModel<InvestBillModel>>> call = RetrofitUtils.createApi(MyApi.class).getInvestBillList("625802", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<InvestBillModel>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<InvestBillModel> data, String SucMessage) {
                refreshHelper.setData(data.getList(), getString(R.string.no_bill), 0);
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);
                refreshHelper.loadError(errorMessage, 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }



}

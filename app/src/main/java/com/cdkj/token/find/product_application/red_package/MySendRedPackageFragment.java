package com.cdkj.token.find.product_application.red_package;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListFragment;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.MySendRedPackageAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.MySendRedPackageBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class MySendRedPackageFragment extends AbsRefreshListFragment<MySendRedPackageBean.ListBean> {

    private Boolean isFirstRequest;

    public static MySendRedPackageFragment getInstance(boolean isFirstRequest) {
        MySendRedPackageFragment fragment = new MySendRedPackageFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CdRouteHelper.DATASIGN, isFirstRequest);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad() {
        if (mRefreshBinding == null || isFirstRequest) return;
        isFirstRequest = true;
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    protected void onInvisible() {

    }

    @Override
    protected void afterCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            isFirstRequest = getArguments().getBoolean(CdRouteHelper.DATASIGN);
        }
        initRefreshHelper(10);

        if (isFirstRequest) {
            mRefreshHelper.onDefaluteMRefresh(true);
        }
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List<MySendRedPackageBean.ListBean> listData) {

        MySendRedPackageAdapter mAdapter = new MySendRedPackageAdapter(listData);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            RedPackageShearActivity.open(mActivity, mAdapter.getItem(position).getCode());
        });
        return mAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        initData(pageindex, limit, isShowDialog);
    }

    private void initData(int pageindex, int limit, boolean isShowDialog) {


        if (isShowDialog) {
            showLoadingDialog();
        }

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
//        map.put("symbol", "");//币种
//        map.put("statusList", "");//红包状态
//        map.put("type", "");//红包类型
        map.put("start", pageindex + "");//红包类型
        map.put("limit", limit + "");

        Call<BaseResponseModel<ResponseInListModel<MySendRedPackageBean.ListBean>>> call = RetrofitUtils.createApi(MyApi.class).getSendRedPackage("623005", StringUtils.getJsonToString(map));
        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<MySendRedPackageBean.ListBean>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<MySendRedPackageBean.ListBean> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.red_package_send_empty), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();

            }
        });
    }
}

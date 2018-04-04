package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.ConsultListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.ConsultListModel;
import com.cdkj.token.model.ConsultModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2018/3/27.
 */

public class ConsultListActivity extends BaseRefreshActivity<ConsultModel> {

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, ConsultListActivity.class);
        activity.startActivity(intent);

    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setSubLeftImgState(true);
        setTopTitle(getString(R.string.consult_list_title));
        setTopLineState(true);

        initAdapter();

        getListData(pageIndex, 15, true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        getConsultListRequest(pageIndex, limit, canShowDialog);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            ConsultModel model = (ConsultModel) mAdapter.getItem(position);

            if (model == null) return;

            ConsultActivity.open(this, model.getCode());

        });
    }

    /**
     * 获取列表数据
     *
     * @param pageIndex
     * @param limit
     * @param canShowDialog
     */
    private void getConsultListRequest(int pageIndex, int limit, boolean canShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("limit", limit + "");
        map.put("start", pageIndex + "");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);
        map.put("orderColumn", "ui_order");
        map.put("orderDir", "asc");


        if (canShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApi.class).getConsultList("625327", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ConsultListModel>(this) {
            @Override
            protected void onSuccess(ConsultListModel data, String SucMessage) {
                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<ConsultModel> mDataList) {
        return new ConsultListAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.consult_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}

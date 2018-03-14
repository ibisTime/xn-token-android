package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.StatisticsAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.StatisticsListModel;
import com.cdkj.token.model.StatisticsModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * 交易流水列表
 * Created by lei on 2018/3/7.
 */

public class StatisticsActivity extends BaseRefreshActivity<StatisticsModel> {

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, StatisticsActivity.class);
        activity.startActivity(intent);

    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {
        setTopTitle(getString(R.string.consult_statistics_title));
        setTopLineState(true);
        setSubLeftImgState(true);

        getListData(pageIndex, limit, true);
    }



    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {

        HashMap map = new HashMap<>();

        map.put("limit",limit+"");
        map.put("start",pageIndex+"");

        Call call = RetrofitUtils.createApi(MyApi.class).getKtBillList("802107", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<StatisticsListModel>(this) {
            @Override
            protected void onSuccess(StatisticsListModel data, String SucMessage) {
                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<StatisticsModel> mDataList) {
        return new StatisticsAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.consult_statistics_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}

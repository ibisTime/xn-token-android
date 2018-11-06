package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AddChoiceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.AddCoinChangeEvent;
import com.cdkj.token.model.ChoiceCoinModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * //TODO 资产配置数据存取优化
 * 资产配置
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceCoinActivity extends AbsRefreshListActivity {

    private AddChoiceAdapter addChoiceAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, AddChoiceCoinActivity.class));
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.add_assets));

        initRefreshHelper(RefreshHelper.LIMITE);

        mRefreshBinding.refreshLayout.setEnableLoadmore(false);
    }

    @Override
    public void topTitleViewleftClick() {
        finish();
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        addChoiceAdapter = new AddChoiceAdapter(listData);
        addChoiceAdapter.setOnItemClickListener((adapter, view, position) -> {

            ChoiceCoinModel.CoinBean coinBean = addChoiceAdapter.getItem(position).getCoin();
            if (null != coinBean)
                doChoice(coinBean.getSymbol(), coinBean.getOrderNo());
        });
        return addChoiceAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        if (isShowDialog)
            showLoadingDialog();

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("type", "1"); // 1 中心化，0 去中心化

        Call call = RetrofitUtils.createApi(MyApi.class).getCoinChoiceList("802283", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseListCallBack<ChoiceCoinModel>(this) {
            @Override
            protected void onSuccess(List<ChoiceCoinModel> data, String SucMessage) {
                mRefreshHelper.setData(data, "", 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    public void doChoice(String symbol, int orderNo) {

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("symbol", symbol);
        map.put("orderNo", orderNo+"");
        map.put("type", "1"); // 1 中心化，0 去中心化

        Call call = RetrofitUtils.createApi(MyApi.class).doChoice("802280", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                mRefreshHelper.onDefaluteMRefresh(true);
                EventBus.getDefault().postSticky(new AddCoinChangeEvent().setTag(AddCoinChangeEvent.NOT_PRI));   //通知上级界面刷新
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

}

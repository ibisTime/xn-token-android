package com.cdkj.token.find.product_application.red_package;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinSelectListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CoinModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 币种选择
 * Created by cdkj on 2018/9/12.
 */

public class CoinSelectListActivity extends AbsRefreshListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CoinSelectListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.coin_type_select);
        initRefreshHelper(RefreshHelper.LIMITE);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        CoinSelectListAdapter coinSelectListAdapter = new CoinSelectListAdapter(listData);

        coinSelectListAdapter.setOnItemClickListener((adapter, view, position) -> {
            EventBus.getDefault().post(coinSelectListAdapter.getItem(position));
            finish();
        });

        return coinSelectListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.objectToJsonString(map));

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(null) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                mRefreshHelper.setData(data.getAccountList(), "", 0);
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


}

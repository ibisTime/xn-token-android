package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AddChoiceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CoinModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceActivity2 extends BaseRefreshActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, AddChoiceActivity2.class));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {

        setTopTitle("添加资产");
        setTopLineState(true);
        setSubLeftImgState(true);

//        init();

        getListData(pageIndex, limit, false);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {

            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {

                if (data == null)
                    return;


                setData(data.getAccountList());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List mDataList) {
        return new AddChoiceAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getStrRes(R.string.bill_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }
}

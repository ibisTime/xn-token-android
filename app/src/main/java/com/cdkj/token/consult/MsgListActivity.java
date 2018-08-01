package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.MsgListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.MsgListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 消息中心
 * Created by cdkj on 2017/8/9.
 */

public class MsgListActivity extends BaseRefreshActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MsgListActivity.class));
    }

    @Override
    protected void onInit(Bundle savedInstanceState, int pageIndex, int limit) {

        setTopTitle(getString(R.string.system_message));
        setTopLineState(true);
        setSubLeftImgState(true);

        getListData(pageIndex,limit,true);
    }

    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", pageIndex+"");
        map.put("limit", limit+"");
        map.put("status", "1");
        map.put("fromSystemCode", MyConfig.SYSTEMCODE);
        map.put("toSystemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.SYSTEMCODE);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getMsgList("804040", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(this) {
            @Override
            protected void onSuccess(MsgListModel data, String SucMessage) {

                setData(data.getList());

            }


            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List mDataList) {
        return new MsgListAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.no_system_message);
    }

    @Override
    public int getEmptyImg() {
        return 0;
    }
}

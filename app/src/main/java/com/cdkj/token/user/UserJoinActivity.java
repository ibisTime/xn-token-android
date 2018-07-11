package com.cdkj.token.user;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CommunityListAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/5/26.
 */

public class UserJoinActivity extends AbsRefreshListActivity {


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserJoinActivity.class));
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        CommunityListAdapter communityListAdapter = new CommunityListAdapter(listData);
        communityListAdapter.setOnItemClickListener((adapter, view, position) -> {
            copyText(communityListAdapter.getItem(position).getCvalue());
        });
        return communityListAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        getCommnityList();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.user_title_join));
        initRefreshHelper(10);
        mRefreshHelper.setEnbleRefreshAndLoad();
        mRefreshHelper.onDefaluteMRefresh(false);
    }


    private void copyText(String text) {
        if (!TextUtils.isEmpty(text)) {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, text);
            cmb.setPrimaryClip(clipData);
            ToastUtil.show(UserJoinActivity.this, getString(R.string.copy_success));
        }
    }

    /**
     * 获取社区列表
     *
     * @return
     */
    private void getCommnityList() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "followUs");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call<BaseResponseListModel<IntroductionInfoModel>> call = RetrofitUtils.getBaseAPiService().getKeySystemInfoList("660919", StringUtils.getJsonToString(map));

        showLoadingDialog();

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(List<IntroductionInfoModel> data, String SucMessage) {
                mRefreshHelper.setData(data, "", 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }


}

package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CountryCodeListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CountryCodeMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 国家区号列表
 * Created by cdkj on 2018/7/2.
 */

public class CountryCodeListActivity extends AbsRefreshListActivity {

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CountryCodeListActivity.class);
        context.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        CountryCodeListAdapter countryCodeListAdapter = new CountryCodeListAdapter(listData);
        countryCodeListAdapter.setOnItemClickListener((adapter, view, position) -> {
            SPUtilHelper.saveCountry(countryCodeListAdapter.getSelectCountryName(position));
            SPUtilHelper.saveCountryCode(countryCodeListAdapter.getSelectCountryCode(position));
            finish();
        });
        return countryCodeListAdapter;
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("status", "1");//status

        Call<BaseResponseListModel<CountryCodeMode>> call = RetrofitUtils.createApi(MyApi.class).getCountryList("801120", StringUtils.getJsonToString(map));

        if (isShowDialog) {
            showLoadingDialog();
        }
        call.enqueue(new BaseResponseListCallBack<CountryCodeMode>(this) {
            @Override
            protected void onSuccess(List<CountryCodeMode> data, String SucMessage) {
                mRefreshHelper.setData(data, getString(R.string.no_country), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.choose_country);
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mBaseBinding.titleView.setLeftImg(R.drawable.back_black_new);
        mBaseBinding.titleView.setLeftTitle("");
        initRefreshHelper(10);
        mRefreshHelper.onDefaluteMRefresh(true);
        mRefreshBinding.refreshLayout.setEnableLoadmore(false);
        mRefreshBinding.refreshLayout.setEnableRefresh(false);

    }
}

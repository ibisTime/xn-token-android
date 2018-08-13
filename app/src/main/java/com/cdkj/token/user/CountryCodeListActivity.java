package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.CountrySelectEvent;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CountryCodeListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.user.login.SignInActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 国家区号列表
 * Created by cdkj on 2018/7/2.
 */
@Route(path = CdRouteHelper.APP_COUNTRY_SELECT)
public class CountryCodeListActivity extends AbsRefreshListActivity {

    private boolean isSave;

    /**
     * @param context
     * @param isSaveSelect 是否把用户的选择保存在本地
     */
    public static void open(Context context, boolean isSaveSelect) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CountryCodeListActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isSaveSelect);
        context.startActivity(intent);
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        CountryCodeListAdapter countryCodeListAdapter = new CountryCodeListAdapter(listData);
        countryCodeListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (isSave) {
                SPUtilHelper.saveCountryInterCode(countryCodeListAdapter.getSelectInterCode(position));
                SPUtilHelper.saveCountryCode(countryCodeListAdapter.getSelectCountryCode(position));
                SPUtilHelper.saveCountryFlag(countryCodeListAdapter.getSelectPic(position));

            } else {
                CountrySelectEvent countrySelectEvent = new CountrySelectEvent();

                countrySelectEvent.setCountryCode(countryCodeListAdapter.getSelectInterCode(position));
                countrySelectEvent.setCountryName(countryCodeListAdapter.getSelectCountryName(position));
                countrySelectEvent.setCountryFlag(countryCodeListAdapter.getSelectPic(position));

                EventBus.getDefault().post(countrySelectEvent);     //发送国家选择通知
            }

            MyConfig.changeLanguageForCountry(this, countryCodeListAdapter.getSelectInterCode(position));
            EventBus.getDefault().post(new AllFinishEvent());
            SignInActivity.open(this, true);
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

        addCall(call);
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

        isSave = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, true);

        mBaseBinding.titleView.setMidTitle(R.string.choose_country);
        mBaseBinding.titleView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        mBaseBinding.titleView.setLeftImg(R.drawable.back_black);
        mBaseBinding.titleView.setLeftTitle("");
        initRefreshHelper(10);
        mRefreshHelper.onDefaluteMRefresh(true);
        mRefreshBinding.refreshLayout.setEnableLoadmore(false);
        mRefreshBinding.refreshLayout.setEnableRefresh(false);

    }
}

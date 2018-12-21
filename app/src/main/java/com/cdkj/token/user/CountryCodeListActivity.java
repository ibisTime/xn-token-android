package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.model.CountrySelectEvent;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CountryCodeListAdapter;
import com.cdkj.token.adapter.CountryListSortAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.comparator.PinyinComparator;
import com.cdkj.token.databinding.ActivityCountryListBinding;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.user.guide.GuideActivity;
import com.cdkj.token.user.login.SignInActivity2;
import com.cdkj.token.utils.HanyuPinyinHelper;
import com.zendesk.util.CollectionUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 国家区号列表
 * Created by cdkj on 2018/7/2.
 */
@Route(path = CdRouteHelper.APP_COUNTRY_SELECT)
public class CountryCodeListActivity extends AbsActivity {

    private ActivityCountryListBinding mBinding;

    protected RefreshHelper mRefreshHelper;
    private CountryListSortAdapter sortAdapter;

    private List<CountryCodeMode> list;
    private CountryCodeListAdapter countryCodeListAdapter;

    public static String[] sorts = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

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
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_country_list, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        isSave = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, true);

        setTopTitle(getStrRes(R.string.choose_country));

        init();
        initListener();

        initRefreshHelper(10);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private void init() {
        sortAdapter = new CountryListSortAdapter(this, sorts);
        mBinding.lvSort.setAdapter(sortAdapter);

        mBinding.refreshLayout.setEnableLoadmore(false);
        mBinding.refreshLayout.setEnableRefresh(false);

    }

    private void initListener() {
        mBinding.edtSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mBinding.edtSearch.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mBinding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEND
                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                search(mBinding.edtSearch.getText().toString().trim());

            return true;
        }
            return false;
        });

        mBinding.lvSort.setOnItemClickListener((adapterView, view, i, l) -> {
            //该字母首次出现的位置
            int position = countryCodeListAdapter.getPositionBySort(sorts[i]);
            if(position != -1){
                mBinding.rv.scrollToPosition(position);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) mBinding.rv.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(position, 0);
            }
        });
    }

    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                ((DefaultItemAnimator) mBinding.rv.getItemAnimator()).setSupportsChangeAnimations(false);
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });

        mBinding.rv.setNestedScrollingEnabled(false);

        mRefreshHelper.init(limit);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    public RecyclerView.Adapter getListAdapter(List listData) {
        countryCodeListAdapter = new CountryCodeListAdapter(listData);
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

            AppConfig.changeLanguageForCountry(this, countryCodeListAdapter.getSelectInterCode(position));
            AppConfig.changeLocalCoinTypeForCountry(countryCodeListAdapter.getSelectInterCode(position));
            EventBus.getDefault().post(new AllFinishEvent());
            SignInActivity2.open(this, MainActivity.class, GuideActivity.class);
            finish();
        });

        return countryCodeListAdapter;
    }

    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("status", "1");//status

        Call<BaseResponseListModel<CountryCodeMode>> call = RetrofitUtils.createApi(MyApi.class).getCountryList("801120", StringUtils.getRequestJsonString(map));

        addCall(call);
        if (isShowDialog) {
            showLoadingDialog();
        }
        call.enqueue(new BaseResponseListCallBack<CountryCodeMode>(this) {
            @Override
            protected void onSuccess(List<CountryCodeMode> data, String SucMessage) {

                if (!CollectionUtils.isEmpty(data)){
                    list = addSort(data);
                    Collections.sort(list, new PinyinComparator());
                    mRefreshHelper.setData(list, getString(R.string.no_country), 0);
                }

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    private List<CountryCodeMode> addSort(List<CountryCodeMode> data){

        for (CountryCodeMode mode : data){

            if (TextUtils.equals(AppConfig.SIMPLIFIED, SPUtilHelper.getLanguage())){

                String countryName = HanyuPinyinHelper.getFirstLetter(mode.getChineseName());
                mode.setSort(countryName.toUpperCase());

            } else {
                mode.setSort(mode.getInterName().substring(0,1).toUpperCase());
            }

        }

        return data;
    }

    private void search(String searchWords){
        if (TextUtils.isEmpty(searchWords.trim())){
            mRefreshHelper.setData(list, getString(R.string.no_country), 0);
            return;
        }

        List<CountryCodeMode> searchData = new ArrayList<>();
        for (CountryCodeMode mode : list) {

            String countryName;
            if (TextUtils.equals(AppConfig.SIMPLIFIED, SPUtilHelper.getLanguage())){
                countryName = mode.getChineseName();
            } else {
                countryName = mode.getInterName();
            }

            if (countryName.contains(searchWords)){
                searchData.add(mode);
            }

        }
        mRefreshHelper.setData(searchData, getString(R.string.no_country), 0);
    }

}

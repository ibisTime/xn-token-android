package com.cdkj.token.find;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AppListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.loader.BannerImageLoader;
import com.cdkj.token.databinding.FragmentFind2Binding;
import com.cdkj.token.find.dapp.DAppActivity;
import com.cdkj.token.find.product_application.management_money.BiJiaBaoListActivity;
import com.cdkj.token.find.product_application.red_package.SendRedPacketActivity;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.DAppModel;
import com.cdkj.token.model.RecommendAppModel;
import com.cdkj.token.user.invite.InviteActivity;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 应用 发现
 * Created by lei on 2018/3/6.
 */

public class FindFragment extends BaseLazyFragment {

    private FragmentFind2Binding mBinding;
    private List<BannerModel> bannerData = new ArrayList<>();

    private AppListAdapter appListAdapter;
    public static String RED_PACKET = "red_packet";

    public String CATEGORY_GAME = "0";
    public String CATEGORY_TOOL = "1";
    public String CATEGORY_INFO = "2";

    private String category = CATEGORY_GAME;

    // 发红包AppCode
    private String appCode = "";
    /**
     * 获得fragment实例
     *
     * @return
     */
    public static FindFragment getInstance() {
        FindFragment fragment = new FindFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_find_2, null, false);

        initRecyclerViewAndAdapter();

        initListener();

        initBanner();

        initRefresh();

        return mBinding.getRoot();

    }

    void initRefresh() {
        mBinding.refreshLayout.setEnableLoadmore(false);
        mBinding.refreshLayout.setOnRefreshListener(refreshlayout -> {
            getBanner(false);
        });
    }

    /**
     * 初始化recyclerView适配器
     */
    void initRecyclerViewAndAdapter() {
        mBinding.recyclerViewApp.setLayoutManager(new GridLayoutManager(mActivity, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        appListAdapter = new AppListAdapter(null);

        appListAdapter.setOnItemClickListener((adapter, view, position) -> {

            if (position < 0 || position > adapter.getData().size()) {
                return;
            }

            DAppActivity.open(mActivity, appListAdapter.getItem(position).getId());

        });

        appListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            WebViewActivity.openURL(mActivity, appListAdapter.getItem(position).getName(), appListAdapter.getItem(position).getUrl());
        });

        mBinding.recyclerViewApp.setAdapter(appListAdapter);
    }

    private void initListener() {
        //消息
//        mBinding.flRight.setOnClickListener(view -> {
//            MsgListActivity.open(mActivity);
//        });

        mBinding.llRecommend.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(appCode))
                SendRedPacketActivity.open(mActivity, appCode);
        });

        mBinding.llRecommend2.setOnClickListener(view -> {
            BiJiaBaoListActivity.open(mActivity);
        });

        mBinding.llRecommend3.setOnClickListener(view -> {
            InviteActivity.open(mActivity);
        });

        mBinding.llMore.setOnClickListener(view -> {
//            WebViewImgBgActivity.openContent(mActivity, recommendAppModel.getName(), recommendAppModel.getDescription());
        });

        mBinding.btnTypeGame.setOnClickListener(view -> {
            initCategoryView();

            mBinding.btnTypeGame.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.btnTypeGame.setBackgroundResource(R.mipmap.find_category);

            category = CATEGORY_GAME;
            getDAPPList();
        });

        mBinding.btnTypeInfo.setOnClickListener(view -> {
            initCategoryView();

            mBinding.btnTypeInfo.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.btnTypeInfo.setBackgroundResource(R.mipmap.find_category);

            category = CATEGORY_INFO;
            getDAPPList();
        });

        mBinding.btnTypeTool.setOnClickListener(view -> {
            initCategoryView();

            mBinding.btnTypeTool.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
            mBinding.btnTypeTool.setBackgroundResource(R.mipmap.find_category);

            category = CATEGORY_TOOL;
            getDAPPList();
        });
    }

    private void initCategoryView(){
        mBinding.btnTypeGame.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_acacac));
        mBinding.btnTypeGame.setBackgroundResource(R.mipmap.find_category_dark);

        mBinding.btnTypeInfo.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_acacac));
        mBinding.btnTypeInfo.setBackgroundResource(R.mipmap.find_category_dark);

        mBinding.btnTypeTool.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_acacac));
        mBinding.btnTypeTool.setBackgroundResource(R.mipmap.find_category_dark);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.banner.stopAutoPlay();
    }


    /**
     * 获取banner
     */
    private void getBanner(boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("location", "app_home"); // 交易位置轮播
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBanner("805806", StringUtils.getRequestJsonString(map));

        addCall(call);

        if (isShowDialog) {
            showLoadingDialog();
        }

        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {

            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                bannerData = data;

                if (bannerData == null || bannerData.isEmpty()) return;
                //设置图片集合
                mBinding.banner.setImages(bannerData);
                //banner设置方法全部调用完毕时最后调用
                mBinding.banner.start();

                mBinding.banner.startAutoPlay();

            }

            @Override
            protected void onFinish() {
                getLocalAppList();
            }
        });
    }

    private void initBanner() {

        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.CENTER);
        //设置图片加载器
        mBinding.banner.setImageLoader(new BannerImageLoader());

        //设置banner动画效果
//        mBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mBinding.banner.setOnBannerListener(position -> {

            if (bannerData == null || position > bannerData.size()) return;

            if (bannerData.get(position) != null) {

                switch (bannerData.get(position).getAction()){

                    case "0":
                        // do nothing
                        break;

                    case "1":
                        if (ImgUtils.isHaveHttp(bannerData.get(position).getUrl())) {
                            WebViewActivity.openURL(mActivity, bannerData.get(position).getName(), bannerData.get(position).getUrl());
                        }
                        break;

                    case "2":
                        DAppActivity.open(mActivity, Integer.parseInt(bannerData.get(position).getUrl()));
                        break;

                }

            }

        });

        // 设置在操作Banner时listView事件不触发
//        mBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 获取应用列表
     */
    public void getLocalAppList() {

        Map<String, String> map = new HashMap<>();

        map.put("language", SPUtilHelper.getLanguage());
        map.put("location", "0");
        map.put("status", "1");

        Call<BaseResponseListModel<RecommendAppModel>> call = RetrofitUtils.createApi(MyApi.class).getAppList("625412", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseListCallBack<RecommendAppModel>(mActivity) {
            @Override
            protected void onSuccess(List<RecommendAppModel> data, String SucMessage) {
                for(RecommendAppModel model : data){
                    if (model.getAction().equals("red_packet")){
                        appCode = model.getCode();
                    }
                }
            }

            @Override
            protected void onFinish() {
                getDAPPList();
            }
        });

    }

    public void getDAPPList(){
        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("category", category);
        map.put("name", "");
        map.put("start", "1");
        map.put("limit", "20");

        Call<BaseResponseModel<ResponseInListModel<DAppModel>>> call = RetrofitUtils.createApi(MyApi.class).getDAppList("625456", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<DAppModel>>(mActivity) {
            @Override
            protected void onSuccess(ResponseInListModel<DAppModel> data, String SucMessage) {
                appListAdapter.replaceData(data.getList());
                appListAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFinish() {
                if (mBinding.refreshLayout.isRefreshing()) {
                    mBinding.refreshLayout.finishRefresh();
                }
                disMissLoading();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (mBinding == null) {
            return;
        }

        // 刷新轮播图
        if (bannerData != null && !bannerData.isEmpty()) {
            mBinding.banner.startAutoPlay();
            return;
        }

        getBanner(true);
    }

    @Override
    protected void onInvisible() {
        if (mBinding == null) {
            return;
        }
        mBinding.banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        if (mBinding == null) {
            return;
        }
        mBinding.banner.stopAutoPlay();
        super.onDestroyView();
    }
}

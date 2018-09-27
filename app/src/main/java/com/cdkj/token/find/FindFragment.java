package com.cdkj.token.find;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AppListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentFindBinding;
import com.cdkj.token.common.loader.BannerImageLoader;
import com.cdkj.token.find.product_application.management_money.BiJiaBaoListActivity;
import com.cdkj.token.find.product_application.management_money.ManagementMoneyListActivity;
import com.cdkj.token.find.product_application.red_package.SendRedPacketActivity;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.RecommendAppModel;
import com.cdkj.token.user.WebViewImgBgActivity;
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

    private FragmentFindBinding mBinding;
    private List<BannerModel> bannerData = new ArrayList<>();

    private AppListAdapter appListAdapter;
    public static String RED_PACKET = "red_packet";

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
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_find, null, false);

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
        mBinding.recyclerViewApp.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false) {
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

            RecommendAppModel recommendAppModel = appListAdapter.getItem(position);

            if (recommendAppModel == null || TextUtils.isEmpty(recommendAppModel.getAction())) {
                return;
            }

            if (true) {
//                ManagementMoneyListActivity.open(mActivity);
                BiJiaBaoListActivity.open(mActivity);
                return;
            }
            switch (recommendAppModel.getAction()) {

                case "red_packet"://跳到红包
//                    SendRedPackageActivity.open(mActivity);
                    SendRedPacketActivity.open(mActivity, recommendAppModel.getCode());
                    break;
                case "money_manager"://跳到量化理财
                    ManagementMoneyListActivity.open(mActivity);
                    break;
                case "invitation"://跳到邀请有礼
                    InviteActivity.open(mActivity);
                    break;
                case "none":
                    WebViewImgBgActivity.openContent(mActivity, recommendAppModel.getName(), recommendAppModel.getDescription());
                    break;
            }


        });

        mBinding.recyclerViewApp.setAdapter(appListAdapter);
    }

    private void initListener() {
        //消息
        mBinding.flRight.setOnClickListener(view -> {
            MsgListActivity.open(mActivity);
        });

//        //首创玩法
//        mBinding.linLayoutFirstPlay.setOnClickListener(view -> {
////            NoneActivity.open(mActivity, NoneActivity.FIRST_CREATE);
//            WebViewImgBgActivity.openkey(mActivity, getString(R.string.consult_1), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_GLOBAL_MASTER));
//        });
//
//        mBinding.linLayoutYbb.setOnClickListener(view -> {
////            NoneActivity.open(mActivity, NoneActivity.YBB);
//            WebViewImgBgActivity.openkey(mActivity, getString(R.string.yibibao), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_YUBIBAO));
//        });
//
//        mBinding.linLayoutLhlc.setOnClickListener(view -> {
////            NoneActivity.open(mActivity, NoneActivity.LHLC);
////            WebViewImgBgActivity.openkey(mActivity, getString(R.string.lianghualicai), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_QUANTITATIVE_FINANCE));
//            ManagementMoneyListActivity.open(mActivity);
//        });
//
//        mBinding.linLayoutRedPacket.setOnClickListener(view -> {
//            //跳转到红包
//            SendRedPackageActivity.open(mActivity);
//        });

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
                getAppList();
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
                if (ImgUtils.isHaveHttp(bannerData.get(position).getUrl())) {
                    WebViewImgBgActivity.openURL(mActivity, "", bannerData.get(position).getUrl());
                }
            }

        });

        // 设置在操作Banner时listView事件不触发
//        mBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 获取应用列表
     */
    public void getAppList() {

        Map<String, String> map = new HashMap<>();

        map.put("language", SPUtilHelper.getLanguage());
        map.put("location", "0");
        map.put("status", "1");
//        map.put("orderColumn", "order_no");
//        map.put("orderDir", "desc");

        Call<BaseResponseListModel<RecommendAppModel>> call = RetrofitUtils.createApi(MyApi.class).getAppList("625412", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseListCallBack<RecommendAppModel>(mActivity) {
            @Override
            protected void onSuccess(List<RecommendAppModel> data, String SucMessage) {
                appListAdapter.replaceData(data);
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

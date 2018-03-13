package com.cdkj.token.consult;

import android.databinding.DataBindingUtil;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.ConsultAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentConsultBinding;
import com.cdkj.token.loader.BannerImageLoader;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.ConsultModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 首页
 * Created by lei on 2018/3/6.
 */

public class ConsultFragment extends BaseRefreshFragment<ConsultModel> {

    private FragmentConsultBinding mHeadBinding;
    private List<BannerModel> bannerData = new ArrayList<>();

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static ConsultFragment getInstance() {
        ConsultFragment fragment = new ConsultFragment();
        return fragment;
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    protected void afterCreate(int pageIndex, int limit) {
        mBinding.refreshLayout.setEnableAutoLoadmore(false);//不能自动加载
        setTopTitle(getString(R.string.consult_title));
        setTopTitleLine(true);

        initAdapter();

        initListener();

        initBanner();

        getListData(pageIndex, 15, true);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mHeadBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_consult, null, false);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mHeadBinding.getRoot());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            ConsultModel model = (ConsultModel) mAdapter.getItem(position);

            ConsultActivity.open(mActivity, model.getName(), model.getName(), model.getDate());

        });
    }

    private void initListener() {
        mHeadBinding.llStatistics.setOnClickListener(view -> {
            StatisticsActivity.open(mActivity);
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHeadBinding.banner.stopAutoPlay();
    }


    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {

        List<ConsultModel> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ConsultModel model = new ConsultModel();
            model.setName("资讯标题" + i);
            model.setDate((i + 1) + "分钟牵");
            list.add(model);
        }

        setData(list);


        // 刷新轮播图
        getBanner();
    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<ConsultModel> mDataList) {
        return new ConsultAdapter(mDataList);
    }

    @Override
    public String getEmptyInfo() {
        return getString(R.string.consult_none);
    }

    @Override
    public int getEmptyImg() {
        return R.mipmap.order_none;
    }


    /**
     * 获取banner
     */
    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("location", "trade"); // 交易位置轮播
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBanner("805806", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {

            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                bannerData = data;
                //设置图片集合
                mHeadBinding.banner.setImages(bannerData);
                //banner设置方法全部调用完毕时最后调用
                mHeadBinding.banner.start();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initBanner() {

        //设置banner样式
        mHeadBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        mHeadBinding.banner.setImageLoader(new BannerImageLoader());

        //设置banner动画效果
        mHeadBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mHeadBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mHeadBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mHeadBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mHeadBinding.banner.setOnBannerListener(position -> {

            if (bannerData.get(position).getUrl() != null) {
                if (bannerData.get(position).getUrl().indexOf("http") != -1) {
                    WebViewActivity.openURL(mActivity, bannerData.get(position - 1).getName(), bannerData.get(position - 1).getUrl());
                }
            }

        });

        // 设置在操作Banner时listView事件不触发
//        mHeadBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }
}

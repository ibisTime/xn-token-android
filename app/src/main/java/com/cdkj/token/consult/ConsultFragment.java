package com.cdkj.token.consult;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.util.AccountUtil;
import com.cdkj.token.adapter.ConsultAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentConsultBinding;
import com.cdkj.token.loader.BannerImageLoader;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.ConsultModel;
import com.cdkj.token.model.KtInfoModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.util.AccountUtil.OGCSCALE;
import static com.cdkj.token.util.CoinUtil.getFirstTokenCoin;

/**
 * 首页
 * Created by lei on 2018/3/6.
 */

public class ConsultFragment extends BaseRefreshFragment<String> {

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
        return false;
    }

    @Override
    protected void afterCreate(int pageIndex, int limit) {
        mBinding.refreshLayout.setEnableRefresh(false);
        mBinding.refreshLayout.setEnableAutoLoadmore(false);//不能自动加载

        initAdapter();

        initListener();

        initBanner();

        // 刷新轮播图
        getBanner();
        getListData(pageIndex, 15, true);
        getKtInfo(true);

        // 取消上啦加载
        setEnableLoadmore(false);
    }

    @Override
    public void after() {
        // 重新初始化RecyclerView的LayoutManager
        mBinding.rv.setLayoutManager( new GridLayoutManager(mActivity, 3));
        mAdapter.onAttachedToRecyclerView(mBinding.rv);
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

            if (model == null) return;

            ConsultActivity.open(mActivity, model.getCode());

        });
    }

    private void initListener() {
        mHeadBinding.flRight.setOnClickListener(view -> {
            MsgListActivity.open(mActivity);
        });

        mHeadBinding.llStatistics.setOnClickListener(view -> {
            StatisticsActivity.open(mActivity);
        });

        mHeadBinding.llMerchant.setOnClickListener(view -> {
            NoneActivity.open(mActivity,"merchant");
        });

        mHeadBinding.llMall.setOnClickListener(view -> {
            NoneActivity.open(mActivity,"mall");
        });

        mHeadBinding.llDig.setOnClickListener(view -> {
            NoneActivity.open(mActivity,"dig");
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHeadBinding.banner.stopAutoPlay();
    }


    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {
        getConsultListRequest(pageIndex, limit, canShowDialog);

    }

    @Override
    protected void onMRefresh(int pageIndex, int limit) {
        getKtInfo(false);
        // 刷新轮播图
        getBanner();
        getListData(pageIndex, limit, false);
    }


    /**
     * 获取空投数据信息
     */
    private void getKtInfo(boolean canShowDialog) {

        Map<String, String> map = new HashMap<>();
        map.put("currency", getFirstTokenCoin());

        Call call = RetrofitUtils.createApi(MyApi.class).getTkInfo("802906", StringUtils.getJsonToString(map));

        addCall(call);

        if (canShowDialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<KtInfoModel>(mActivity) {
            @Override
            protected void onSuccess(KtInfoModel data, String SucMessage) {

                mHeadBinding.tvKtInfo.setText(getTkText(data));
                mHeadBinding.progress.setMax(100);
                mHeadBinding.progress.setProgress(BigDecimalUtils.intValue(data.getUseRate()));
            }


            @Override
            protected void onFinish() {
                if (mHeadBinding.llStatistics.getVisibility() == View.INVISIBLE) {  //请求完成后才显示布局
                    mHeadBinding.llStatistics.setVisibility(View.VISIBLE);
                }
                disMissLoading();
            }
        });


    }

    /**
     * 获取空投数据显示文本
     *
     * @param data
     * @return
     */
    @NonNull
    private String getTkText(KtInfoModel data) {
        DecimalFormat df = new DecimalFormat("#######0.########");

        StringBuffer sb = new StringBuffer();

        sb.append(getString(R.string.kt_total));
        sb.append(AccountUtil.amountFormatUnit(data.getTotalCount(), getFirstTokenCoin(), OGCSCALE));
        sb.append("  ");
        sb.append(getString(R.string.kt_statistical_2));
        sb.append(AccountUtil.amountFormatUnit(data.getUseCount(), getFirstTokenCoin(), OGCSCALE));
        sb.append("  ");
        sb.append(getString(R.string.kt_ratio));
        sb.append(data.getUseRate()+"%");

        return sb.toString();

    }


    /**
     * 获取可变列表数据
     *
     * @param pageIndex
     * @param limit
     * @param canShowDialog
     */
    private void getConsultListRequest(int pageIndex, int limit, boolean canShowDialog) {

        List<String> list = new ArrayList<>();
        list.add("");
        setData(list);

    }

    @Override
    protected BaseQuickAdapter onCreateAdapter(List<String> mDataList) {
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
        map.put("location", "app_home"); // 交易位置轮播
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBanner("805806", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<BannerModel>(mActivity) {

            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                bannerData = data;

                if (bannerData == null || bannerData.isEmpty()) return;
                //设置图片集合
                mHeadBinding.banner.setImages(bannerData);
                //banner设置方法全部调用完毕时最后调用
                mHeadBinding.banner.start();

                mHeadBinding.banner.startAutoPlay();

            }

            @Override
            protected void onFinish() {
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

            if (bannerData == null || position > bannerData.size()) return;

            if (bannerData.get(position).getUrl() != null) {
                if (bannerData.get(position).getUrl().indexOf("http") != -1) {
                    WebViewActivity.openURL(mActivity, "", bannerData.get(position).getUrl());
                }
            }

        });

        // 设置在操作Banner时listView事件不触发
//        mHeadBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }
}

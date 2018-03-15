package com.cdkj.token.consult;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseRefreshFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.MoneyUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.Util.AccountUtil;
import com.cdkj.token.adapter.ConsultAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentConsultBinding;
import com.cdkj.token.loader.BannerImageLoader;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.model.ConsultListModel;
import com.cdkj.token.model.ConsultModel;
import com.cdkj.token.model.KtInfoModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.Util.AccountUtil.OGCSCALE;

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

        // 刷新轮播图
        getBanner();
        getListData(pageIndex, 15, true);
        getKtInfo(true);
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

        Call call = RetrofitUtils.createApi(MyApi.class).getTkInfo("802108", StringUtils.getJsonToString(new HashMap<>()));

        addCall(call);

        if (canShowDialog) showLoadingDialog();


        call.enqueue(new BaseResponseModelCallBack<KtInfoModel>(mActivity) {
            @Override
            protected void onSuccess(KtInfoModel data, String SucMessage) {
                mHeadBinding.tvKtInfo.setText(getTkText(data));
                mHeadBinding.progress.setMax(100);
                mHeadBinding.progress.setProgress(BigDecimalUtils.intValue(data.getUseRate()) * 100);
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
        DecimalFormat df = new DecimalFormat("#######0.########%");

        StringBuffer sb = new StringBuffer();

        sb.append(getString(R.string.kt_total));
        sb.append(AccountUtil.amountFormatUnit(data.getInitialBalance(), AccountUtil.OGC, OGCSCALE));
        sb.append("  ");
        sb.append(getString(R.string.kt_statistical_2));
        sb.append(AccountUtil.amountFormatUnit(data.getUseBalance(), AccountUtil.OGC, OGCSCALE));
        sb.append("  ");
        sb.append(getString(R.string.kt_ratio));
        sb.append(df.format(BigDecimalUtils.multiply(data.getUseRate(), new BigDecimal(100)).doubleValue()));

        return sb.toString();

    }


    /**
     * 获取列表数据
     *
     * @param pageIndex
     * @param limit
     * @param canShowDialog
     */
    private void getConsultListRequest(int pageIndex, int limit, boolean canShowDialog) {

        Map<String, String> map = new HashMap<>();

        map.put("limit", limit + "");
        map.put("start", pageIndex + "");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);
        map.put("orderColumn", "ui_order");
        map.put("orderDir", "asc");


        if (canShowDialog) showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApi.class).getConsultList("625327", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ConsultListModel>(mActivity) {
            @Override
            protected void onSuccess(ConsultListModel data, String SucMessage) {
                setData(data.getList());
            }

            @Override
            protected void onFinish() {
                if (mHeadBinding.llListTitle.getVisibility() == View.INVISIBLE) {
                    mHeadBinding.llListTitle.setVisibility(View.VISIBLE);
                }
                disMissLoading();
            }
        });


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
                    WebViewActivity.openURL(mActivity, bannerData.get(position - 1).getName(), bannerData.get(position - 1).getUrl());
                }
            }

        });

        // 设置在操作Banner时listView事件不触发
//        mHeadBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }
}

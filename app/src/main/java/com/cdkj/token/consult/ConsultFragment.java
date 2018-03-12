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
 * Created by lei on 2018/3/6.
 */

public class ConsultFragment extends BaseRefreshFragment<ConsultModel> {

    private FragmentConsultBinding mBinding;

    private List<String> banner = new ArrayList<>();
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
        mBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_consult, null, false);
        mAdapter.setHeaderAndEmpty(true);
        mAdapter.addHeaderView(mBinding.getRoot());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {

            ConsultModel model = (ConsultModel) mAdapter.getItem(position);

            ConsultActivity.open(mActivity,model.getName(),model.getName(),model.getDate());

        });

        inits();
        initListener();

        getListData(pageIndex,limit,true);
    }

    private void initListener() {
        mBinding.llStatistics.setOnClickListener(view -> {
            StatisticsActivity.open(mActivity);
        });
    }

    private void inits() {
        setTopTitle(getString(R.string.consult_title));
        setTopTitleLine(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding.banner.stopAutoPlay();
    }


    @Override
    protected void getListData(int pageIndex, int limit, boolean canShowDialog) {

        List<ConsultModel> list = new ArrayList<>();

        for (int i=0; i<3; i++){
            ConsultModel model = new ConsultModel();
            model.setName("资讯标题"+i);
            model.setDate((i+1)+"分钟牵");
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
                if (data != null){
                    bannerData = data;
                    banner.clear();
                    for (BannerModel model : data) {
                        banner.add(model.getPic());
                    }
                }

                initBanner();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initBanner() {
        if (banner == null) return;

        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBinding.banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        mBinding.banner.setImages(banner);
        //设置banner动画效果
        mBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mBinding.banner.setOnBannerClickListener(position -> {

            if (bannerData.get(position-1).getUrl()!=null){
                if (bannerData.get(position-1).getUrl().indexOf("http") != -1){
                    WebViewActivity.openURL(mActivity,bannerData.get(position-1).getName(),bannerData.get(position-1).getUrl());
                }
            }

        });
        //banner设置方法全部调用完毕时最后调用
        mBinding.banner.start();

        // 设置在操作Banner时listView事件不触发
//        mBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }
}

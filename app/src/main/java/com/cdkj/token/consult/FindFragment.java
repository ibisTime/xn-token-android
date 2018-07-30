package com.cdkj.token.consult;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentFindBinding;
import com.cdkj.token.loader.BannerImageLoader;
import com.cdkj.token.model.BannerModel;
import com.cdkj.token.user.WebViewImgBgActivity;
import com.cdkj.token.utils.ThaAppConstant;
import com.cdkj.token.wallet.red_package.SendRedPackageActivity;
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

    private FragmentFindBinding mHeadBinding;
    private List<BannerModel> bannerData = new ArrayList<>();

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
        mHeadBinding = DataBindingUtil.inflate(mActivity.getLayoutInflater(), R.layout.fragment_find, null, false);

        initListener();

        initBanner();

        return mHeadBinding.getRoot();

    }

    private void initListener() {
        //消息
        mHeadBinding.flRight.setOnClickListener(view -> {
            MsgListActivity.open(mActivity);
        });

        //首创玩法
        mHeadBinding.linLayoutFirstPlay.setOnClickListener(view -> {
//            NoneActivity.open(mActivity, NoneActivity.FIRST_CREATE);
            WebViewImgBgActivity.openkey(mActivity, getString(R.string.consult_1), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_GLOBAL_MASTER));
        });

        mHeadBinding.linLayoutYbb.setOnClickListener(view -> {
//            NoneActivity.open(mActivity, NoneActivity.YBB);
            WebViewImgBgActivity.openkey(mActivity, getString(R.string.yibibao), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_YUBIBAO));
        });

        mHeadBinding.linLayoutLhlc.setOnClickListener(view -> {
//            NoneActivity.open(mActivity, NoneActivity.LHLC);
            WebViewImgBgActivity.openkey(mActivity, getString(R.string.lianghualicai), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_QUANTITATIVE_FINANCE));
        });

        mHeadBinding.linLayoutRedPacket.setOnClickListener(view -> {
            //跳转到红包
            SendRedPackageActivity.open(mActivity);
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHeadBinding.banner.stopAutoPlay();
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

        showLoadingDialog();

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
                disMissLoading();
            }
        });
    }

    private void initBanner() {

        //设置banner样式
        mHeadBinding.banner.setBannerStyle(BannerConfig.CENTER);
        //设置图片加载器
        mHeadBinding.banner.setImageLoader(new BannerImageLoader());

        //设置banner动画效果
//        mHeadBinding.banner.setBannerAnimation(Transformer.DepthPage);
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
                if (bannerData.get(position).getUrl().indexOf("http://") != -1 || bannerData.get(position).getUrl().indexOf("https://") != -1) {
                    WebViewActivity.openURL(mActivity, "", bannerData.get(position).getUrl());
                }
            }

        });

        // 设置在操作Banner时listView事件不触发
//        mHeadBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }

    @Override
    protected void lazyLoad() {
        if (mHeadBinding == null) {
            return;
        }
        // 刷新轮播图
        if (bannerData != null && !bannerData.isEmpty()) {
            mHeadBinding.banner.startAutoPlay();
        } else {
            getBanner();
        }

    }

    @Override
    protected void onInvisible() {
        if (mHeadBinding == null) {
            return;
        }
        mHeadBinding.banner.stopAutoPlay();
    }

    @Override
    public void onDestroyView() {
        if (mHeadBinding == null) {
            return;
        }
        mHeadBinding.banner.stopAutoPlay();
        super.onDestroyView();
    }
}

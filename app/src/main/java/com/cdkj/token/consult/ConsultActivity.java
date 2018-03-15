package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityConsultBinding;
import com.cdkj.token.loader.BannerImageLoader;
import com.cdkj.token.model.ConsultListModel;
import com.cdkj.token.model.ConsultModel;
import com.cdkj.token.user.CallPhoneActivity;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zzhoujay.richtext.RichText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 资讯详情
 * Created by lei on 2018/3/7.
 */

public class ConsultActivity extends AbsBaseActivity {

    private ActivityConsultBinding mBinding;

    private String mPhoneNum;//电话号

    private String mStoreCode;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.banner.stopAutoPlay();
    }

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity, String code) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, ConsultActivity.class);
        intent.putExtra("code", code);
        activity.startActivity(intent);

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_consult, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getString(R.string.consult_activity_title));

        setSubLeftImgState(true);
        setTopLineState(true);

        initListener();

        initBanner();

        initData();

    }

    private void initListener() {

        mBinding.btnToPhone.setOnClickListener(v -> {
            CallPhoneActivity.open(this, mPhoneNum);
        });

        mBinding.btnToPay.setOnClickListener(v -> {

            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }

            StorePayActivity.open(this, mStoreCode);
        });

    }

    private void initData() {

        TextPaint paint = mBinding.tvTitle.getPaint();
        paint.setFakeBoldText(true);

        if (getIntent() == null) {
            return;
        }

        mStoreCode = getIntent().getStringExtra("code");

        getConsultDetails(mStoreCode);

    }

    private void setShowData(ConsultModel model) {

        mPhoneNum = model.getBookMobile();

        mBinding.tvTitle.setText(model.getName());
        mBinding.tvSlogan.setText(model.getSlogan());

        mBinding.tvPhone.setText(model.getBookMobile());

        mBinding.tvAddress.setText(getAddress(model));

        RichText.from(model.getDescription()).into(mBinding.tvRichText);


        setBannerData(model.getPic());

    }

    private String getAddress(ConsultModel model) {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append(model.getProvince());
        stringBuffer.append(model.getCity());
        stringBuffer.append(model.getArea());
        stringBuffer.append(model.getAddress());

        return stringBuffer.toString();

    }


    private void initBanner() {

        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
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

        // 设置在操作Banner时listView事件不触发
//        mHeadBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }


    /**
     * 获取详情
     *
     * @param code
     */
    private void getConsultDetails(String code) {

        Map<String, String> map = new HashMap<>();

        map.put("code", code);


        showLoadingDialog();

        Call call = RetrofitUtils.createApi(MyApi.class).getConsultDetail("625328", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ConsultModel>(this) {
            @Override
            protected void onSuccess(ConsultModel data, String SucMessage) {

                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 设置banner显示数据
     *
     * @param bannerData
     */
    public void setBannerData(String bannerData) {

        List<String> bannners = StringUtils.splitBannerList(bannerData);

        if (bannners.isEmpty()) return;

        mBinding.banner.setImages(bannners);
        mBinding.banner.start();
        mBinding.banner.startAutoPlay();
    }

}

package com.cdkj.token.find.dapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.api.ResponseInListModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.DAppGuideAdapter;
import com.cdkj.token.adapter.DAppIntroAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityDappBinding;
import com.cdkj.token.model.DAppGuideModel;
import com.cdkj.token.model.DAppModel;
import com.cdkj.token.model.DappIntroModel;
import com.zendesk.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/12/4.
 */

public class DAppActivity extends AbsActivity {

    private ActivityDappBinding mBinding;

    private boolean isLeftSide = true;

    private RefreshHelper mRefreshHelper;

    private int appId;
    private DAppModel dAppModel;

    private String h5Url;

    public static void open(Context context, int appId) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, DAppActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, appId);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_dapp, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        
        init();
        initListener();
    }

    private void init(){
        appId = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);

        getDAPP();
    }

    public void getDAPP(){
        showLoadingDialog();

        Map<String, Object> map = new HashMap<>();

        map.put("id", appId);

        Call<BaseResponseModel<DAppModel>> call = RetrofitUtils.createApi(MyApi.class).getDApp("625457", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<DAppModel>(this) {
            @Override
            protected void onSuccess(DAppModel data, String SucMessage) {
                dAppModel = data;
                initView();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private void initView(){
        if (dAppModel == null)
            return;

        ImgUtils.loadImage(this, dAppModel.getPicIcon(), mBinding.ivIcon);
        mBinding.tvName.setText(dAppModel.getName());
        mBinding.tvCompany.setText(dAppModel.getCompany());

        if (!CollectionUtils.isEmpty(dAppModel.getLabelList())){
            if (dAppModel.getLabelList().size() == 1){
                mBinding.tvLabel1.setText(dAppModel.getLabelList().get(0));
                mBinding.tvLabel1.setVisibility(View.VISIBLE);
            }else if(dAppModel.getLabelList().size() > 1){
                mBinding.tvLabel1.setVisibility(View.VISIBLE);
                mBinding.tvLabel2.setVisibility(View.VISIBLE);

                mBinding.tvLabel1.setText(dAppModel.getLabelList().get(0));
                mBinding.tvLabel2.setText(dAppModel.getLabelList().get(1));
            }
        }

        mBinding.tvStars.setText("("+dAppModel.getGrade()+")");
        setStars(dAppModel.getGrade());

        mBinding.tvDownloadNum.setText(dAppModel.getDownload()+"");
        mBinding.tvVol.setText(dAppModel.getVolume()+"");


        DappIntroModel dappIntroModel = new DappIntroModel();
        dappIntroModel.setContent(dAppModel.getDesc());
        List<String> pic = StringUtils.splitBannerList(dAppModel.getPicScreenshot());
        dappIntroModel.setPics(pic);
        initIntro(dappIntroModel);

        initGuide();
    }
    
    private void setStars(int grade){
        switch (grade){

            case 0:
                break;

            case 1:
                mBinding.ivStar1.setBackgroundResource(R.mipmap.dapp_stars_light);
                break;

            case 2:
                mBinding.ivStar1.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar2.setBackgroundResource(R.mipmap.dapp_stars_light);
                break;

            case 3:
                mBinding.ivStar1.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar2.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar3.setBackgroundResource(R.mipmap.dapp_stars_light);
                break;

            case 4:
                mBinding.ivStar1.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar2.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar3.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar4.setBackgroundResource(R.mipmap.dapp_stars_light);
                break;

            case 5:
            default:
                mBinding.ivStar1.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar2.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar3.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar4.setBackgroundResource(R.mipmap.dapp_stars_light);
                mBinding.ivStar5.setBackgroundResource(R.mipmap.dapp_stars_light);
                break;

        }
    }

    private void initListener() {
        mBinding.btnStart.setOnClickListener(view -> {
            WebViewActivity.openURL(this, dAppModel.getName(), dAppModel.getUrl());
        });

        mBinding.btnIntro.setOnClickListener(view -> {
            isLeftSide = true;
            initBtnView();
        });

        mBinding.btnGuide.setOnClickListener(view -> {
            isLeftSide = false;
            initBtnView();
        });

    }

    private void initBtnView() {

        if (isLeftSide){
            mBinding.btnIntro.setTextColor(ContextCompat.getColor(this, R.color.white));
            mBinding.btnIntro.setBackgroundResource(R.drawable.selector_blue);

            mBinding.btnGuide.setTextColor(ContextCompat.getColor(this, R.color.gray_acacac));
            mBinding.btnGuide.setBackgroundResource(R.drawable.btn_gray_bg_line);

            mBinding.llIntro.setVisibility(View.VISIBLE);
            mBinding.llGuide.setVisibility(View.GONE);
        } else {
            mBinding.btnGuide.setTextColor(ContextCompat.getColor(this, R.color.white));
            mBinding.btnGuide.setBackgroundResource(R.drawable.selector_blue);

            mBinding.btnIntro.setTextColor(ContextCompat.getColor(this, R.color.gray_acacac));
            mBinding.btnIntro.setBackgroundResource(R.drawable.btn_gray_bg_line);

            mBinding.llGuide.setVisibility(View.VISIBLE);
            mBinding.llIntro.setVisibility(View.GONE);
        }

    }

    public void initIntro(DappIntroModel dappIntroModel){

        int width = DisplayHelper.getScreenWidth(this) - DisplayHelper.dp2px(this, 15 * 2);
        mBinding.etvIntro.initWidth(width);
        mBinding.etvIntro.setMaxLines(4);
        mBinding.etvIntro.setCloseText(dappIntroModel.getContent());

        mBinding.rvInfo.setLayoutManager(getHorizontalManager());
        mBinding.rvInfo.setAdapter(new DAppIntroAdapter(dappIntroModel.getPics()));
    }

    /**
     * 获取 LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    private LinearLayoutManager getHorizontalManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };

    }

    //
    public void initGuide(){

        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack(this) {
            @Override
            public View getRefreshLayout() {
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getDAPPGuideList();
            }
        });

        mRefreshHelper.init(RefreshHelper.LIMITE);
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    public void getDAPPGuideList(){
        Map<String, Object> map = new HashMap<>();

        map.put("dappId", dAppModel.getId());
        map.put("start", 1);
        map.put("limit", 20);

        Call<BaseResponseModel<ResponseInListModel<DAppGuideModel.DAppGuide>>> call = RetrofitUtils.createApi(MyApi.class).getDAppGuideList("625466", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<ResponseInListModel<DAppGuideModel.DAppGuide>>(this) {
            @Override
            protected void onSuccess(ResponseInListModel<DAppGuideModel.DAppGuide> data, String SucMessage) {
                mRefreshHelper.setData(data.getList(), getString(R.string.no_dapp_guide), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    private RecyclerView.Adapter getListAdapter(List listData) {
        DAppGuideAdapter adapter = new DAppGuideAdapter(listData);
        adapter.setOnItemClickListener((adapter1, view, position) -> {

            DAppGuideModel.DAppGuide dAppGuide = adapter.getItem(position);

            if (TextUtils.isEmpty(h5Url)){
                geShareUrlRequest(dAppGuide);
                return;
            }

            DAppGuideActivity.open(DAppActivity.this, h5Url,
                    dAppGuide.getId(),
                    dAppGuide.getTitle(),
                    dAppGuide.getContent());

        });
        return adapter;
    }

    public void geShareUrlRequest(DAppGuideModel.DAppGuide dAppGuide) {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                h5Url = data.getCvalue();
                DAppGuideActivity.open(DAppActivity.this, h5Url,
                        dAppGuide.getId(),
                        dAppGuide.getTitle(),
                        dAppGuide.getContent());

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

}

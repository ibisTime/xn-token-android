package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.IncomeRankAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityIncomeRankBinding;
import com.cdkj.token.model.IncomeRankTopModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/10/14.
 */

public class IncomeRankActivity extends AbsLoadActivity {

    private ActivityIncomeRankBinding mBinding;

    private RefreshHelper mRefreshHelper;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, IncomeRankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_income_rank, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setShowTitle(false);
        setStatusBarBlue();

        initRefreshHelper();
        mRefreshHelper.onDefaluteMRefresh(true);

        initListener();
    }

    private void initListener() {
        mBinding.imgFinish.setOnClickListener(view -> {
            finish();
        });
    }

    private void initRefreshHelper() {
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
                return new IncomeRankAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                if (isShowDialog) showLoadingDialog();

                Map<String, String> map = new HashMap<>();

                map.put("userId", SPUtilHelper.getUserId());

                Call<BaseResponseModel<IncomeRankTopModel>> call = RetrofitUtils.createApi(MyApi.class).getIncomeRankTop("625801", StringUtils.getRequestJsonString(map));

                call.enqueue(new BaseResponseModelCallBack<IncomeRankTopModel>(IncomeRankActivity.this) {
                    @Override
                    protected void onSuccess(IncomeRankTopModel data, String SucMessage) {
                        setData(data);
                    }

                    @Override
                    protected void onFinish() {
                        disMissLoadingDialog();
                    }
                });
            }
        });

        mBinding.rv.setLayoutManager(getRecyclerViewLayoutManager());
        mBinding.rv.setNestedScrollingEnabled(false);

        mRefreshHelper.init(RefreshHelper.LIMITE);
        mBinding.refreshLayout.setEnableLoadmore(false);

    }


    private void setData(IncomeRankTopModel data) {


        ImgUtils.loadLogo(this, SPUtilHelper.getUserPhoto(), mBinding.ivAvatar);

        mBinding.tvMobile.setText(data.getMobile());
        mBinding.tvIncomeTotal.setText(AmountUtil.transformFormatToString2(data.getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
        if (data.getRank() > 100){
            mBinding.tvRank.setText("");
            mBinding.tvRankText.setText(R.string.rank_100);
        }else {
            mBinding.tvRank.setText(data.getRank()+"");
            mBinding.tvRankText.setText(R.string.income_rank);
        }

        if (data.getTop100().size() > 0){
            ImgUtils.loadLogo(this, data.getTop100().get(0).getPhoto(), mBinding.ivAvatarRank1);
            mBinding.tvMobileRank1.setText(data.getTop100().get(0).getMobile()+"");
            mBinding.tvIncomeRank1.setText(AmountUtil.transformFormatToString2(data.getTop100().get(0).getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
        }

        if (data.getTop100().size() > 1){
            ImgUtils.loadLogo(this, data.getTop100().get(1).getPhoto(), mBinding.ivAvatarRank2);
            mBinding.tvMobileRank2.setText(data.getTop100().get(1).getMobile()+"");
            mBinding.tvIncomeRank2.setText(AmountUtil.transformFormatToString2(data.getTop100().get(1).getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
        }

        if (data.getTop100().size() > 2){
            ImgUtils.loadLogo(this, data.getTop100().get(2).getPhoto(), mBinding.ivAvatarRank3);
            mBinding.tvMobileRank3.setText(data.getTop100().get(2).getMobile()+"");
            mBinding.tvIncomeRank3.setText(AmountUtil.transformFormatToString2(data.getTop100().get(2).getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
        }

        if (data.getTop100().size() > 3){

            List<IncomeRankTopModel.Top100Bean> top100List = data.getTop100();
            for (int i = 0; i < 3; i++) {
                top100List.remove(0);
            }
            mRefreshHelper.setData(top100List, getString(R.string.no_income_rank), 0);

        }
    }

    /**
     * 获取 LinearLayoutManager
     *
     * @return LinearLayoutManager
     */
    private LinearLayoutManager getRecyclerViewLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

    }

}

package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.MyIncomeAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityMyIncomeBinding;
import com.cdkj.token.model.BjbMyIncome;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/10/12.
 */

public class MyIncomeActivity extends BaseActivity {

    private ActivityMyIncomeBinding mBinding;

    private RefreshHelper mRefreshHelper;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, MyIncomeActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_income);

        initView();
        init();
        initListener();
    }

    private void init() {
        initRefreshHelper();
        mRefreshHelper.onDefaluteMRefresh(true);
    }

    private void initListener() {
        mBinding.imgFinish.setOnClickListener(view -> {
            finish();
        });

        mBinding.llIncomeInvite.setOnClickListener(view -> {
            MyIncomeBillListActivity.open(this);
        });

        mBinding.llIncomePop.setOnClickListener(view -> {
            InvestmentBillListActivity.open(this);
        });

        mBinding.llMoreRank.setOnClickListener(view -> {
            IncomeRankActivity.open(this);
        });
    }

    /**
     * 初始化图表
     */
    private void initView() {
        mBinding.rv.setHasFixedSize(true);

        //饼状图
        mBinding.mPieChart.setUsePercentValues(true);
        mBinding.mPieChart.getDescription().setEnabled(false);
        mBinding.mPieChart.setExtraOffsets(0, 0, 0, 0);

        mBinding.mPieChart.setDragDecelerationFrictionCoef(0.95f);
        //设置中间文件
//        mBinding.mPieChart.setCenterText(generateCenterSpannableText());

        mBinding.mPieChart.setDrawHoleEnabled(true);
        mBinding.mPieChart.setHoleColor(Color.WHITE);

        mBinding.mPieChart.setTransparentCircleColor(Color.WHITE);

        mBinding.mPieChart.setHoleRadius(83f);
        mBinding.mPieChart.setTransparentCircleRadius(83f);

        mBinding.mPieChart.setDrawCenterText(true);

        mBinding.mPieChart.setRotationAngle(270);
        // 触摸旋转
        mBinding.mPieChart.setRotationEnabled(true);
        mBinding.mPieChart.setHighlightPerTapEnabled(true);

        mBinding.mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // 饼图图例
        Legend l = mBinding.mPieChart.getLegend();
        l.setEnabled(false);
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(0f);
//        l.setYOffset(0f);

        // 输入标签样式
        mBinding.mPieChart.setEntryLabelColor(ContextCompat.getColor(this, R.color.blue_0064ff));
        mBinding.mPieChart.setEntryLabelTextSize(0f);

        mBinding.mPieChart.setTransparentCircleColor(ContextCompat.getColor(this, R.color.white));
    }

    /**
     * 刷新
     */
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
                return new MyIncomeAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                if (isShowDialog) showLoadingDialog();

                Map<String, String> map = new HashMap<>();

                map.put("userId", SPUtilHelper.getUserId());

                Call<BaseResponseModel<BjbMyIncome>> call = RetrofitUtils.createApi(MyApi.class).getMyIncome("625800", StringUtils.getRequestJsonString(map));

                call.enqueue(new BaseResponseModelCallBack<BjbMyIncome>(MyIncomeActivity.this) {
                    @Override
                    protected void onSuccess(BjbMyIncome data, String SucMessage) {
                        setData(data);
                        mRefreshHelper.setData(data.getTop5(), getString(R.string.no_income_rank), 0);
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

    private void setData(BjbMyIncome data){
        mBinding.mPieChart.setVisibility(View.VISIBLE);

        mBinding.tvIncomeYesterday.setText("≈ " + AmountUtil.transformFormatToString2(data.getIncomeYesterday(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
        mBinding.tvIncomeTotal.setText("≈ " + AmountUtil.transformFormatToString2(data.getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));
        mBinding.tvIncomeAmount.setText(AmountUtil.transformFormatToString2(data.getIncomeTotal(), WalletHelper.COIN_BTC, AmountUtil.SCALE_4));

        float ratioPop = new BigDecimal(data.getIncomeRatioPop()).multiply(new BigDecimal("100")).floatValue();
        float ratioInvite = new BigDecimal(data.getIncomeRatioInvite()).multiply(new BigDecimal("100")).floatValue();

        mBinding.tvFloatValue1.setText(ratioPop+"%");
        mBinding.tvFloatValue2.setText(ratioInvite+"%");

        if (ratioPop == 0f && ratioInvite == 0f){
            ratioPop = 50f;
            ratioInvite = 50f;
        }

        //模拟数据
        // 数据一次是:黄，橙，墨绿绿，
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(ratioPop));
        entries.add(new PieEntry(ratioInvite));

        //设置数据
        initPieData(entries);
    }

    //设置数据
    private void initPieData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(1f);
        dataSet.setSelectionShift(5f);

        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ContextCompat.getColor(this, R.color.pie_yellow_FFC832));
        colors.add(ContextCompat.getColor(this, R.color.pie_orange_FF6400));
        colors.add(ContextCompat.getColor(this, R.color.pie_green_006C6E));
        colors.add(ContextCompat.getColor(this, R.color.pie_green_46AAAF));
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(0f);
        data.setValueTextColor(ContextCompat.getColor(this, R.color.transparent));
        mBinding.mPieChart.setData(data);
        mBinding.mPieChart.highlightValues(null);
        //刷新
        mBinding.mPieChart.invalidate();
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

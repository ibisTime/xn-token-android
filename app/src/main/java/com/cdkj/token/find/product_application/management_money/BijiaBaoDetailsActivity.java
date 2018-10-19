package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityMoneyManagerDetails2Binding;
import com.cdkj.token.model.BiJiaBaoBuyModel;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.LocalCoinDBUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_YMD;

/**
 * Created by cdkj on 2018/9/27.
 */

public class BijiaBaoDetailsActivity extends AbsStatusBarTranslucentActivity {

    private ActivityMoneyManagerDetails2Binding mbinding;

    private String mProductCode;//产品编号

    private ManagementMoney mProductModel;

    public static void open(Context context, String productCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BijiaBaoDetailsActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, productCode);
        context.startActivity(intent);
    }


    @Override
    public View addContentView() {
        mbinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_money_manager_details_2, null, false);
        return mbinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setWhiteTitle();
        setPageBgImage(R.drawable.money_manager_bg2);
        if (getIntent() != null) {
            mProductCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }

        initClickListener();

        getProductDetailsRequest();
    }

    private void initClickListener() {

        //购买属性
        mbinding.linLayoutBuyInfo.setOnClickListener(view -> {
            setViewToggleShow(mbinding.webview1);
        });

        //赎回属性
        mbinding.linLayoutBackInfo.setOnClickListener(view -> {
            setViewToggleShow(mbinding.webview2);

        });

        //说明书
        mbinding.linLayoutProductInfo.setOnClickListener(view -> {
            setViewToggleShow(mbinding.webview3);
        });

        //购买

        mbinding.btnToBuy.setOnClickListener(view -> {

            if (mProductModel == null) return;

            BiJiaBaoBuyModel biJiaBaoBuyModel = new BiJiaBaoBuyModel();

            biJiaBaoBuyModel.setAvilAmount(mProductModel.getAvilAmount());
            biJiaBaoBuyModel.setExpectYield(mProductModel.getExpectYield());
            biJiaBaoBuyModel.setCoinSymbol(mProductModel.getSymbol());
            biJiaBaoBuyModel.setProductCode(mProductModel.getCode());
            biJiaBaoBuyModel.setProductName(mProductModel.getName());
            biJiaBaoBuyModel.setIncreAmount(mProductModel.getIncreAmount());

            BiJiaBaoBuyActivity.open(this, mProductModel);
        });
    }

    /**
     * view显示隐藏
     *
     * @param view
     */
    private void setViewToggleShow(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }


    /**
     * 获取产品详情请求
     */
    private void getProductDetailsRequest() {

        if (TextUtils.isEmpty(mProductCode)) {
            return;
        }

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("code", mProductCode);
        map.put("language", SPUtilHelper.getLanguage());

        Call<BaseResponseModel<ManagementMoney>> call = RetrofitUtils.createApi(MyApi.class).getMoneyManageProductDetails("625514", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ManagementMoney>(this) {
            @Override
            protected void onSuccess(ManagementMoney data, String SucMessage) {
                mProductModel = data;
                sheShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }

    /**
     * @param managementMoney
     */
    private void sheShowData(ManagementMoney managementMoney) {
        if (managementMoney == null) {
            return;
        }

        BigDecimal coinUnit = LocalCoinDBUtils.getLocalCoinUnit(managementMoney.getSymbol());//币种最小单位

        mBaseBinding.tvTitle.setText(managementMoney.getName());

        String[] totlaRatio = StringUtils.showformatPercentage(managementMoney.getExpectYield()).split("\\.");
        mbinding.tvBuyTotlaRatio.setText(totlaRatio[0]);//年化率
        if (totlaRatio.length > 1){
            mbinding.tvBuyTotlaRatioDecimal.setText("."+totlaRatio[1]);//年化率
        }

        mbinding.tvEndDay.setText(getString(R.string.product_days, managementMoney.getLimitDays() + ""));//产品期限
        mbinding.tvAvilAmount.setText(getCoinAmountText(managementMoney, coinUnit, managementMoney.getAvilAmount()));//剩余
        mbinding.tvMinAmount.setText(getCoinAmountText(managementMoney, coinUnit, managementMoney.getMinAmount()));//起购
        mbinding.tvTotalAmount.setText(getCoinAmountText(managementMoney, coinUnit, managementMoney.getAmount()));//产品总额
        mbinding.tvCoinName.setText(managementMoney.getSymbol());

        mbinding.tvStartTime.setText(DateUtil.formatStringData(managementMoney.getStartDatetime(), DATE_YMD));
        mbinding.tvIncomeTime.setText(DateUtil.formatStringData(managementMoney.getIncomeDatetime(), DATE_YMD));
        mbinding.tvEndTime.setText(DateUtil.formatStringData(managementMoney.getArriveDatetime(), DATE_YMD));

        //购买属性
        mbinding.webview1.loadData(getBuyDescByLanguage(managementMoney), "text/html;charset=UTF-8", "UTF-8");
        //赎回属性
        mbinding.webview2.loadData(getRedeemDescByLanguage(managementMoney), "text/html;charset=UTF-8", "UTF-8");
        //说明书
        mbinding.webview3.loadData(getDirectionsDescByLanguage(managementMoney), "text/html;charset=UTF-8", "UTF-8");

        mbinding.webview1.setVisibility(View.GONE);
        mbinding.webview2.setVisibility(View.GONE);
        mbinding.webview3.setVisibility(View.GONE);

        if (managementMoney.getStatus().equals("5")){ // 5-募集期
            mbinding.btnToBuy.setVisibility(View.VISIBLE);
        }else{
            mbinding.btnToBuy.setVisibility(View.GONE);
        }

        // 0 不能购买，1 起购，2 起息，3 结期
        if (TextUtils.isEmpty(managementMoney.getTimeStatus())){
            return;
        }
        int timeStatus = Integer.parseInt(managementMoney.getTimeStatus());
        if (timeStatus > 0){
            mbinding.tvProgress1.setBackgroundResource(R.drawable.oval_blue);
            mbinding.tvProgress1.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
            mbinding.tvStartTime.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
            mbinding.tvProgressStart.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));

            mbinding.ivSchedule1.setImageResource(R.mipmap.schedule_blue);
        }

        if (timeStatus > 1){
            mbinding.tvProgress2.setBackgroundResource(R.drawable.oval_blue);
            mbinding.tvProgress2.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
            mbinding.tvIncomeTime.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
            mbinding.tvProgressIncome.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));

            mbinding.ivSchedule2.setImageResource(R.mipmap.schedule_blue);
        }

        if (timeStatus > 2){
            mbinding.tvProgress3.setBackgroundResource(R.drawable.oval_blue);
            mbinding.tvProgress3.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
            mbinding.tvEndTime.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
            mbinding.tvProgressEnd.setTextColor(ContextCompat.getColor(this, R.color.blue_0064ff));
        }
    }

    /**
     * 获取购买属性
     * @param managementMoney
     * @return
     */
    public String getBuyDescByLanguage(ManagementMoney managementMoney) {

        if (managementMoney == null) {
            return "";
        }
        switch (SPUtilHelper.getLanguage()) {
            case AppConfig.ENGLISH:
                return managementMoney.getBuyDescEn();
            case AppConfig.KOREA:
                return managementMoney.getBuyDescKo();
            default:
                return managementMoney.getBuyDescZhCn();
        }
    }


    /**
     * 获取赎回属性
     *
     * @param managementMoney
     * @return
     */
    public String getRedeemDescByLanguage(ManagementMoney managementMoney) {

        if (managementMoney == null) {
            return "";
        }
        switch (SPUtilHelper.getLanguage()) {
            case AppConfig.ENGLISH:
                return managementMoney.getRedeemDescEn();
            case AppConfig.KOREA:
                return managementMoney.getRedeemDescKo();
            default:
                return managementMoney.getRedeemDescZhCn();
        }

    }


    /**
     * 获取说明书
     *
     * @param managementMoney
     * @return
     */
    public String getDirectionsDescByLanguage(ManagementMoney managementMoney) {

        if (managementMoney == null) {
            return "";
        }
        switch (SPUtilHelper.getLanguage()) {
            case AppConfig.ENGLISH:
                return managementMoney.getDirectionsEn();
            case AppConfig.KOREA:
                return managementMoney.getDirectionsKo();
            default:
                return managementMoney.getDirectionsZhCn();
        }
    }


    /**
     * 获取币种金额显示+币种名称显示文本
     *
     * @param data
     * @param coinUnit
     * @param amount
     * @return
     */
    private String getCoinAmountText(ManagementMoney data, BigDecimal coinUnit, BigDecimal amount) {
        return AmountUtil.transformFormatToString(amount, coinUnit, AmountUtil.ALLSCALE) + data.getSymbol();
    }


    @Override
    protected void onDestroy() {

        try {
            webViewClear(mbinding.webview1);
            webViewClear(mbinding.webview2);
            webViewClear(mbinding.webview3);

        } catch (Exception e) {

        }

        super.onDestroy();
    }

    private void webViewClear(WebView webView) {
        webView.clearHistory();
        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
    }
}

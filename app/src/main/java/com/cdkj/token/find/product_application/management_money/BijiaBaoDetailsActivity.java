package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.cdkj.baselibrary.api.BaseResponseModel;
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

        Call<BaseResponseModel<ManagementMoney>> call = RetrofitUtils.createApi(MyApi.class).getMoneyManageProductDetails("625511", StringUtils.getRequestJsonString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<ManagementMoney>(this) {
            @Override
            protected void onSuccess(ManagementMoney data, String SucMessage) {
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

        mbinding.tvBuyTotlaRatio.setText(StringUtils.showformatPercentage(managementMoney.getExpectYield()));//年化率

        mbinding.tvEndDay.setText(getString(R.string.product_days, managementMoney.getLimitDays() + ""));//产品期限

        mbinding.tvAvilAmount.setText(getCoinAmountText(managementMoney, coinUnit, managementMoney.getAvilAmount()));//剩余

        mbinding.tvMinAmount.setText(getCoinAmountText(managementMoney, coinUnit, managementMoney.getMinAmount()));//起购

        mbinding.tvTotalAmount.setText(getCoinAmountText(managementMoney, coinUnit, managementMoney.getAmount()));//产品总额

        mbinding.tvCoinName.setText(managementMoney.getSymbol());


        mbinding.tvStartTime.setText(DateUtil.formatStringData(managementMoney.getStartDatetime(),DATE_YMD));
        mbinding.tvIncomeTime.setText(DateUtil.formatStringData(managementMoney.getIncomeDatetime(),DATE_YMD));
        mbinding.tvEndTime.setText(DateUtil.formatStringData(managementMoney.getEndDatetime(),DATE_YMD));


        //产品介绍
        mbinding.webview3.loadData(managementMoney.getDescription(), "text/html;charset=UTF-8", "UTF-8");

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

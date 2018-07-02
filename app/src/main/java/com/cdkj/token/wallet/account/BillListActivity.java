package com.cdkj.token.wallet.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.views.MyPickerPopupWindow;
import com.cdkj.token.R;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.adapter.BillListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityBillListBinding;
import com.cdkj.token.databinding.HeaderBillListBinding;
import com.cdkj.token.model.BillModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by cdkj on 2018/5/25.
 */

public class BillListActivity extends AbsBaseActivity {

    private ActivityBillListBinding mBinding;
    private HeaderBillListBinding mHeaderBinding;

    private WalletBalanceModel mAccountBean;
    private BillListAdapter mBillAdapter;

    private BaseRefreshCallBack back;
    private RefreshHelper refreshHelper;

    private String type = "";
    private String kind = "0";
    private String[] types;
    private String[] bizType = {"", "charge", "withdraw", "withdrawfee"};

    public static void open(Context context, WalletBalanceModel mAccountBean) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, BillListActivity.class)
                .putExtra("mAccountBean", mAccountBean));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_bill_list, null, false);
        mHeaderBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.header_bill_list, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        types = new String[]{getStrRes(R.string.bill_type_all), getStrRes(R.string.bill_type_charge), getStrRes(R.string.bill_type_withdraw),
                getStrRes(R.string.bill_type_withdrawfee)};

        if (getIntent() == null)
            return;

        mAccountBean =  getIntent().getParcelableExtra("mAccountBean");

        initCallBack();

        init();
        initView();
        initListener();
    }

    private void init() {
        if (mAccountBean != null) {
            setTopTitle(mAccountBean.getCoinName());
        }
        setTopLineState(true);
        setSubLeftImgState(true);
        setSubRightImgAndClick(R.mipmap.bill_filter, v -> {
            initPopup(v);
        });

        refreshHelper = new RefreshHelper(this, back);
        refreshHelper.init(10);
        // 刷新
        refreshHelper.onDefaluteMRefresh(true);
    }

    private void initView() {
        mHeaderBinding.tvSymbol.setText(mAccountBean.getCoinName());
        ImgUtils.loadImage(this, getCoinWatermarkWithCurrency(mAccountBean.getCoinName(), 1), mHeaderBinding.ivIcon);


        BigDecimal amount = new BigDecimal(mAccountBean.getAmountString());
        BigDecimal  frozenAmount = new BigDecimal(mAccountBean.getFrozenAmountString());
        mHeaderBinding.tvAmount.setText(AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), mAccountBean.getCoinName(), 8));


        if(TextUtils.equals(WalletHelper.getShowLocalCoinType(),WalletHelper.LOCAL_COIN_CNY)){
            if (mAccountBean.getAmountCny() == null) {
                mHeaderBinding.tvAmountCny.setText("≈ 0"+WalletHelper.getShowLocalCoinType());
            } else {
                mHeaderBinding.tvAmountCny.setText("≈ " + mAccountBean.getAmountCny() + WalletHelper.getShowLocalCoinType());
            }

        }else{
            if (mAccountBean.getAmountUSD() == null) {
                mHeaderBinding.tvAmountCny.setText("≈ 0"+WalletHelper.getShowLocalCoinType());
            } else {
                mHeaderBinding.tvAmountCny.setText("≈ " + mAccountBean.getAmountUSD() + WalletHelper.getShowLocalCoinType());
            }
        }



    }

    private void initListener() {
        mBinding.llCharge.setOnClickListener(view -> {
            if (mAccountBean == null)
                return;
            RechargeActivity.open(this, mAccountBean);
        });

        mBinding.llWithdraw.setOnClickListener(view -> {
            if (mAccountBean == null)
                return;
            WithdrawActivity.open(this, mAccountBean);
        });
    }

    private void initCallBack() {

        back = new BaseRefreshCallBack(this) {
            @Override
            public SmartRefreshLayout getRefreshLayout() {
                mBinding.refreshLayout.setEnableLoadmore(false);
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                mBillAdapter = new BillListAdapter(listData);
                mBillAdapter.setHeaderAndEmpty(true);
                mBillAdapter.addHeaderView(mHeaderBinding.getRoot());
                mBillAdapter.setOnItemClickListener((adapter, view, position) -> {
                    BillDetailActivity.open(BillListActivity.this, (BillModel.ListBean) adapter.getItem(position));
                });

                return mBillAdapter;
            }

            @Override
            public void onRefresh(int pageindex, int limit) {
                EventBusModel model = new EventBusModel();
                model.setTag(BASE_COIN_LIST);
                // 是否需要通知所有需要的地方刷新CoinList配置
                model.setEvBoolean(false);
                // 不是的话需要告知其需要更新的位置
                model.setEvInfo("wallet");
                EventBus.getDefault().post(model);
            }

            @Override
            public void getListDataRequest(int pageIndex, int limit, boolean isShowDialog) {
                getListData(pageIndex, limit, isShowDialog);
            }
        };

    }

    private void getListData(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("limit", limit + "");
        map.put("start", pageIndex + "");
        map.put("bizType", type);
        map.put("kind", kind);
        map.put("accountNumber", mAccountBean.getAccountNumber());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("systemCode", MyConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBillListData("802524", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowDialog)
            showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<BillModel>(this) {

            @Override
            protected void onSuccess(BillModel data, String SucMessage) {
                if (data == null)
                    return;


                refreshHelper.setData(data.getList(), getStrRes(R.string.bill_none), 0);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * @param view
     */
    private void initPopup(View view) {
        MyPickerPopupWindow popupWindow = new MyPickerPopupWindow(this, R.layout.popup_picker);
        popupWindow.setNumberPicker(R.id.np_type, types);

        popupWindow.setOnClickListener(R.id.tv_cancel, v -> {
            popupWindow.dismiss();
        });

        popupWindow.setOnClickListener(R.id.tv_confirm, v -> {

            type = popupWindow.getNumberPicker(R.id.np_type, bizType);
            getListData(1, 10, true);

            popupWindow.dismiss();

        });

        popupWindow.show(view);
    }
}

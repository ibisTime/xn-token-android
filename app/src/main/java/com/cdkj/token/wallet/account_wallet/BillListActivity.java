package com.cdkj.token.wallet.account_wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.BillListAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWalletBillBinding;
import com.cdkj.token.model.BillFilterModel;
import com.cdkj.token.model.BillModel;
import com.cdkj.token.model.CoinAddressShowModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.views.ScrollPicker;
import com.cdkj.token.views.pop.PickerPop;
import com.cdkj.token.wallet.private_wallet.WalletAddressShowActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinIconByCoinSymbol;

/**
 * 中心化钱包流水
 * Created by cdkj on 2018/5/25.
 */

public class BillListActivity extends AbsLoadActivity {

    private ActivityWalletBillBinding mBinding;

    private WalletBalanceModel mAccountBean;
    private BillListAdapter mBillAdapter;

    private BaseRefreshCallBack refreshCallBackback;
    private RefreshHelper refreshHelper;

    private String filterType = "";
    private String kind = "0";

    private List<ScrollPicker.ScrollPickerData> filterTypeList; //筛选pop数据
    private PickerPop filterPickerPop;


    public static void open(Context context, WalletBalanceModel mAccountBean) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, BillListActivity.class)
                .putExtra(CdRouteHelper.DATASIGN, mAccountBean));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_bill, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        initFilterTypeList();

        if (getIntent() == null)
            return;

        mAccountBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        initCallBack();
        initData();
        initView();
        initListener();
    }

    void initFilterTypeList() {

        filterTypeList = new ArrayList<>();

        String[] bizType = new String[]{"", "charge", "withdraw", "withdrawfee", "redpacket_back", "sendredpacket_in", "sendredpacket_out"};

        String[] types = new String[]{getStrRes(R.string.bill_type_all), getStrRes(R.string.bill_type_charge), getStrRes(R.string.bill_type_withdraw),
                getStrRes(R.string.bill_type_withdrawfee), getString(R.string.redpacket_back), getString(R.string.redpacket_get), getString(R.string.send_red_package)};

        for (int i = 0; i < types.length; i++) {
            BillFilterModel billFilterModel = new BillFilterModel();
            billFilterModel.setItemText(types[i]);
            billFilterModel.setType(bizType[i]);
            filterTypeList.add(billFilterModel);
        }
    }

    private void initView() {
        ImgUtils.loadCircleImg(this, getCoinIconByCoinSymbol(mAccountBean.getCoinSymbol()), mBinding.ivIcon);
        mBinding.tvFilter.setVisibility(View.VISIBLE);

        mBinding.tvInMoney.setText(R.string.wallet_bill_list_charge);
        mBinding.tvOutMoney.setText(R.string.wallet_bill_list_withdraw);

        mBinding.tvAmount.setText(AmountUtil.transformFormatToString(mAccountBean.getAvailableAmount(), mAccountBean.getCoinSymbol(), 8) + " " + mAccountBean.getCoinSymbol());

        mBinding.tvAmountCny.setText("≈ " + mAccountBean.getLocalAmount() + SPUtilHelper.getLocalMarketSymbol());

    }

    private void initData() {
        if (mAccountBean != null) {
            mBaseBinding.titleView.setMidTitle(mAccountBean.getCoinSymbol());
        }
        refreshHelper = new RefreshHelper(this, refreshCallBackback);
        refreshHelper.init(10);
        // 刷新
        refreshHelper.onDefaluteMRefresh(true);
    }


    private void initListener() {

        //筛选
        mBinding.tvFilter.setOnClickListener(view -> {
            if (filterPickerPop == null) {
                filterPickerPop = new PickerPop(this);
                filterPickerPop.setPickerViewData(filterTypeList);
                filterPickerPop.setOnSelectListener(selectPosition -> {
                    if (selectPosition == null) {
                        return;
                    }
                    filterType = selectPosition.getSelectType();
                    refreshHelper.onDefaluteMRefresh(true);
                });
            }

            filterPickerPop.showPopupWindow();
        });

        //充币
        mBinding.linLayoutInCoin.setOnClickListener(view -> {
            if (mAccountBean == null)
                return;
            CoinAddressShowModel coinAddressShowModel = new CoinAddressShowModel();
            coinAddressShowModel.setAddress(mAccountBean.getAddress());
            coinAddressShowModel.setCoinSymbol(mAccountBean.getCoinSymbol());
            WalletAddressShowActivity.open(this, coinAddressShowModel);
        });

        //提币
        mBinding.linLayoutOutCoin.setOnClickListener(view -> {
            if (mAccountBean == null)
                return;
            WithdrawActivity.open(this, mAccountBean);
        });
    }

    private void initCallBack() {

        refreshCallBackback = new BaseRefreshCallBack(this) {
            @Override
            public SmartRefreshLayout getRefreshLayout() {

                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.recyclerView;
            }

            @Override
            public BaseQuickAdapter getAdapter(List listData) {
                mBillAdapter = new BillListAdapter(listData);
                mBillAdapter.setOnItemClickListener((adapter, view, position) -> {
                    BillDetailActivity.open(BillListActivity.this, (BillModel.ListBean) adapter.getItem(position));
                });
                return mBillAdapter;
            }

            @Override
            public void onRefresh(int pageindex, int limit) {

            }

            @Override
            public void getListDataRequest(int pageIndex, int limit, boolean isShowDialog) {
                getBillListData(pageIndex, limit, isShowDialog);
            }
        };

    }

    /**
     * 获取流水
     *
     * @param pageIndex
     * @param limit
     * @param isShowDialog
     */
    private void getBillListData(int pageIndex, int limit, boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("limit", limit + "");
        map.put("start", pageIndex + "");
        map.put("bizType", filterType);
        map.put("kind", kind);
        map.put("accountNumber", mAccountBean.getAccountNumber());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("systemCode", AppConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBillListData("802524", StringUtils.objectToJsonString(map));

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
                disMissLoadingDialog();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filterPickerPop != null) {
            filterPickerPop.dismiss();
        }
    }
}

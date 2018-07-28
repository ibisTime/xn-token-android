package com.cdkj.token.wallet.account;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.EventBusModel;
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
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.ScrollPicker;
import com.cdkj.token.views.pop.PickerPop;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.token.utils.CoinUtil.getCoinWatermarkWithCurrency;

/**
 * Created by cdkj on 2018/5/25.
 */
//TODO POP 优化
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
        ImgUtils.loadCircleImg(this, getCoinWatermarkWithCurrency(mAccountBean.getCoinName(), 1), mBinding.ivIcon);
        mBinding.tvFilter.setVisibility(View.VISIBLE);

        mBinding.tvInMoney.setText(R.string.wallet_bill_list_charge);
        mBinding.tvOutMoney.setText(R.string.wallet_bill_list_withdraw);

        if (!TextUtils.isEmpty(mAccountBean.getAmountString()) || !TextUtils.isEmpty(mAccountBean.getFrozenAmountString())) {
            BigDecimal amount = new BigDecimal(mAccountBean.getAmountString());
            BigDecimal frozenAmount = new BigDecimal(mAccountBean.getFrozenAmountString());
            mBinding.tvAmount.setText(AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), mAccountBean.getCoinName(), 8));
        }


        if (TextUtils.equals(WalletHelper.getShowLocalCoinType(), WalletHelper.LOCAL_COIN_CNY)) {
            if (mAccountBean.getAmountCny() == null) {
                mBinding.tvAmountCny.setText("≈ 0" + WalletHelper.getShowLocalCoinType());
            } else {
                mBinding.tvAmountCny.setText("≈ " + mAccountBean.getAmountCny() + WalletHelper.getShowLocalCoinType());
            }

        } else {
            if (mAccountBean.getAmountUSD() == null) {
                mBinding.tvAmountCny.setText("≈ 0" + WalletHelper.getShowLocalCoinType());
            } else {
                mBinding.tvAmountCny.setText("≈ " + mAccountBean.getAmountUSD() + WalletHelper.getShowLocalCoinType());
            }
        }


    }

    private void initData() {
        if (mAccountBean != null) {
            mBaseBinding.titleView.setMidTitle(mAccountBean.getCoinName());
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
            RechargeActivity.open(this, mAccountBean);
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
        map.put("bizType", filterType);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (filterPickerPop != null) {
            filterPickerPop.dismiss();
        }
    }
}

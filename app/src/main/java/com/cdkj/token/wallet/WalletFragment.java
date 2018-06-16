package com.cdkj.token.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.consult.MsgListActivity;
import com.cdkj.token.databinding.FragmentWalletBinding;
import com.cdkj.token.model.BalanceListModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.CoinTypeAndAddress;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.model.WalletDBModel;
import com.cdkj.token.utils.WalletHelper;
import com.cdkj.token.wallet.coin_detail.WalletCoinDetailsActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_ALL;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_SINGEL;


/**
 * 钱包币种界面
 * Created by lei on 2017/8/21.
 */

public class WalletFragment extends BaseLazyFragment {

    private CoinAdapter adapter;

    private FragmentWalletBinding mBinding;

    private BaseRefreshCallBack back;
    private RefreshHelper refreshHelper;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static WalletFragment getInstance() {
        WalletFragment fragment = new WalletFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, null, false);

        initCallBack();

        init();
        initListener();

        getMsgRequest();

        return mBinding.getRoot();
    }

    private void init() {
        refreshHelper = new RefreshHelper(mActivity, back);

        refreshHelper.init(10);

        loadConfigCoinData();

        // 刷新
//        refreshHelper.onDefaluteMRefresh(true);
    }

    private void initCallBack() {

        back = new BaseRefreshCallBack(mActivity) {
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
                adapter = new CoinAdapter(listData);
                adapter.setOnItemClickListener((adapter1, view, position) -> {

                    WalletCoinDetailsActivity.open(mActivity, adapter.getItem(position));

//                    CoinModel.AccountListBean bean = adapter.getItem(position);
//                    BillListActivity.open(mActivity, bean);
                });
                return adapter;
            }

            @Override
            public void onRefresh(int pageindex, int limit) {
                getMsgRequest();

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

                loadConfigCoinData();

//                getListData(pageIndex, limit, isShowDialog);
            }
        };

    }

    /**
     * 加载配置币种
     */
    private void loadConfigCoinData() {

        List<CoinTypeAndAddress> list = new ArrayList<>();

        WalletDBModel walletDBModel = WalletHelper.getPrivateKeyAndAddressByCoinType(WalletHelper.COIN_ETH);

        for (LocalCoinModel localCoinModel : WalletHelper.getConfigLocalCoinList()) {
            CoinTypeAndAddress coinTypeAndAddress = new CoinTypeAndAddress();
            coinTypeAndAddress.setAddress(walletDBModel.getAddress());
            coinTypeAndAddress.setSymbol(localCoinModel.getCoinType());
            list.add(coinTypeAndAddress);
        }

        getWalletBalanceByAccountList(list);

//        refreshHelper.setData(WalletHelper.getConfigLocalCoinList(), getStrRes(R.string.bill_none), R.mipmap.order_none);
    }

    private void initListener() {
        mBinding.llExchange.setOnClickListener(view -> {
            MsgListActivity.open(mActivity);
        });

        mBinding.llAdd.setOnClickListener(view -> {
            AddChoiceActivity.open(mActivity);
        });
    }


    @Override
    protected void lazyLoad() {
        if (mBinding != null) {
            getMsgRequest();
            refreshHelper.onMRefresh(1, 10, true);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && mBinding != null) {
            getMsgRequest();
            refreshHelper.onMRefresh(1, 10, true);
        }
    }

    @Override
    protected void onInvisible() {

    }


    /**
     * 根据账户类型获取钱包余额
     *
     * @param accountList <symbol,address>   币种 地址
     */
    private void getWalletBalanceByAccountList(List<CoinTypeAndAddress> accountList) {

        Map<String, Object> map = new HashMap<>();
        map.put("accountList", accountList);

        Call<BaseResponseModel<BalanceListModel>> call = RetrofitUtils.createApi(MyApi.class).getBalanceList("802270", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<BalanceListModel>(mActivity) {
            @Override
            protected void onSuccess(BalanceListModel data, String SucMessage) {

                mBinding.tvCny.setText(data.getTotalAmountCNY());

                List<BalanceListModel.AccountListBean> accountListBeans = new ArrayList<>();

                for (CoinTypeAndAddress coinTypeAndAddress : accountList) {

                    BalanceListModel.AccountListBean accountListBean = isHaveCoin(data, coinTypeAndAddress.getSymbol()); //返回数据中含有请求的币种

                    if (accountListBean == null) {                              //没有币种 则列表只显示币种名字
                        accountListBean = new BalanceListModel.AccountListBean();
                        accountListBean.setSymbol(coinTypeAndAddress.getSymbol());
                    }
                    accountListBeans.add(accountListBean);
                }

                refreshHelper.setData(data.getAccountList(), getStrRes(R.string.bill_none), R.mipmap.order_none);

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 判断返回的数据里是否有请求的币种信息
     *
     * @param data
     * @param symbol
     * @return
     */
    private BalanceListModel.AccountListBean isHaveCoin(BalanceListModel data, String symbol) {

        for (BalanceListModel.AccountListBean accountListBean : data.getAccountList()) {
            if (TextUtils.equals(accountListBean.getSymbol(), symbol)) {
                return accountListBean;
            }
        }
        return null;
    }


    /**
     * 获取币种列表
     *
     * @param pageIndex
     * @param limit
     * @param isShowDialog
     */
    private void getListData(int pageIndex, int limit, boolean isShowDialog) {
        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(mActivity) {

            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {

                if (data == null)
                    return;

                setView(data);

                refreshHelper.setData(data.getAccountList(), getStrRes(R.string.bill_none), R.mipmap.order_none);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setView(CoinModel data) {
        mBinding.tvCny.setText(data.getTotalAmountCNY() + "");
    }

    /**
     * 获取消息列表
     */
    public void getMsgRequest() {


        Map<String, String> map = new HashMap<>();
        map.put("channelType", "4");
        map.put("start", "1");
        map.put("limit", "1");
        map.put("status", "1");
        map.put("fromSystemCode", MyConfig.SYSTEMCODE);
        map.put("toSystemCode", MyConfig.SYSTEMCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getMsgList("804040", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(mActivity) {
            @Override
            protected void onSuccess(MsgListModel data, String SucMessage) {
                if (data.getList() == null || data.getList().size() < 1) {
                    return;
                }

                mBinding.tvNotice.setText(data.getList().get(0).getSmsTitle());
            }


            @Override
            protected void onFinish() {
            }
        });
    }

    @Subscribe
    public void eventBusModel(EventBusModel model) {
        if (model == null || true)
            return;

        switch (model.getTag()) {

            // CoinList配置更新通知，单一通知需要验证是否是自己
            case BASE_COIN_LIST_NOTIFY_SINGEL:
                if (!model.getEvInfo().equals("wallet"))
                    return;

            case BASE_COIN_LIST_NOTIFY_ALL:
                refreshHelper.onMRefresh(1, 10, true);
                break;
        }

    }

}

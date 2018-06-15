package com.cdkj.token.wallet.trusteeship;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinAdapter2;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.consult.MsgListActivity;
import com.cdkj.token.databinding.FragmentWalletBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.wallet.AddChoiceActivity2;
import com.cdkj.token.wallet.account.BillListActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_ALL;
import static com.cdkj.baselibrary.appmanager.EventTags.BASE_COIN_LIST_NOTIFY_SINGEL;


/**
 * Created by lei on 2017/8/21.
 */

public class WalletUserActivity extends AbsBaseLoadActivity {

    private CoinAdapter2 adapter;

    private FragmentWalletBinding mBinding;

    private BaseRefreshCallBack back;
    private RefreshHelper refreshHelper;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletUserActivity.class);
        context.startActivity(intent);
    }


    private void init() {
        refreshHelper = new RefreshHelper(this, back);

        refreshHelper.init(10);
        // 刷新
        refreshHelper.onDefaluteMRefresh(true);
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
                adapter = new CoinAdapter2(listData);
                adapter.setOnItemClickListener((adapter1, view, position) -> {
                    CoinModel.AccountListBean bean = adapter.getItem(position);

                    BillListActivity.open(WalletUserActivity.this, bean);
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
                getListData(pageIndex, limit, isShowDialog);
            }
        };

    }

    private void initListener() {
        mBinding.llExchange.setOnClickListener(view -> {
            MsgListActivity.open(this);
        });

        mBinding.llAdd.setOnClickListener(view -> {
            AddChoiceActivity2.open(this);
        });

        mBinding.linLayoutBack.setOnClickListener(view -> finish());
    }

    @Override
    public void onResume() {
        super.onResume();
        getMsgRequest();
        refreshHelper.onMRefresh(1, 10, true);
    }

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

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {

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
                mBinding.refreshLayout.finishRefresh();
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


        call.enqueue(new BaseResponseModelCallBack<MsgListModel>(this) {
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
        if (model == null)
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

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_wallet, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        UIStatusBarHelper.translucent(this);
        initCallBack();

        init();
        initListener();

        mBinding.llExchange.setVisibility(View.GONE);
        mBinding.llAdd.setVisibility(View.GONE);

//        getMsgRequest();

        getMsgRequest();
        refreshHelper.onMRefresh(1, 10, true);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }
}

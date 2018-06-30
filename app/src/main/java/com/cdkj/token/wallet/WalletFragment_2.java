package com.cdkj.token.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.CoinAdapter2;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.FragmentWallet2Binding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.MsgListModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.CardChangeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by cdkj on 2018/6/28.
 */

public class WalletFragment_2 extends BaseLazyFragment {

    private FragmentWallet2Binding mBinding;

    private RefreshHelper mRefreshHelper;


    public static WalletFragment_2 getInstance() {
        WalletFragment_2 fragment = new WalletFragment_2();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_2, null, false);

        initRefresh();

        initCardChangeListener();

        initClickListener();

        mRefreshHelper.onDefaluteMRefresh(true);
        getMsgRequest();
        return mBinding.getRoot();
    }

    private void initClickListener() {

        mBinding.imgClose.setOnClickListener(view -> {
            mBinding.linLayoutBulletin.setVisibility(View.GONE);
        });

    }

    /**
     * 布局改变监听
     */
    void initCardChangeListener() {
        mBinding.cardChangeLayout.setChangeCallBack(new CardChangeLayout.ChangeCallBack() {
            @Override
            public boolean onChangeBefor(int index) {

                boolean isHasInfo = WalletHelper.checkHasUserInfo(SPUtilHelper.getUserId());

                if (!isHasInfo) {
                    showDoubleWarnListen("请创建或导入钱包", view -> {
                        IntoWalletBeforeActivity.open(mActivity);
                    });
                }

                return isHasInfo;
            }

            @Override
            public void onChange(int index) {
                changeLayoutByIndex(index);
            }
        });
    }

    /**
     * 改变布局
     *
     * @param index 在Layout中的索引
     */
    private void changeLayoutByIndex(int index) {
        switch (index) {
            case 0:
                mBinding.imgChange.setImageResource(R.drawable.change_red);
                mBinding.imgTransfer.setImageResource(R.drawable.transfer_red);
                break;
            case 1:
                mBinding.imgChange.setImageResource(R.drawable.change_blue);
                mBinding.imgTransfer.setImageResource(R.drawable.transfer_blue);
                break;
        }
    }

    private void initRefresh() {
        mRefreshHelper = new RefreshHelper(mActivity, new BaseRefreshCallBack(mActivity) {
            @Override
            public View getRefreshLayout() {
                mBinding.refreshLayout.setEnableLoadmore(false);//禁止上拉
                return mBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                return mBinding.rv;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List listData) {
                return new CoinAdapter2(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListData(pageindex, limit, isShowDialog);
            }
        });

        mRefreshHelper.init(10);
    }

    @Override
    protected void lazyLoad() {

        if (mBinding == null) {
            return;
        }

        getMsgRequest();
    }

    @Override
    protected void onInvisible() {

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

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(mActivity) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
//                mRefreshHelper.reLoadAdapter();
                mRefreshHelper.setData(data.getAccountList(), getStrRes(R.string.bill_none), R.mipmap.order_none);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
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
                    mBinding.tvBulletin.setVisibility(View.GONE);
                    return;
                }
                mBinding.tvBulletin.setVisibility(View.VISIBLE);
                mBinding.tvBulletin.setText(data.getList().get(0).getSmsTitle());
            }


            @Override
            protected void onFinish() {
            }
        });
    }


    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(mActivity).builder()
                .setTitle(getString(com.cdkj.baselibrary.R.string.activity_base_tip)).setContentMsg(str)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), null, false);

        commonDialog.show();
    }
}

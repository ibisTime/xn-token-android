package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.base.BaseRefreshActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AddChoiceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.CoinCofigChange;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.LocalCoinModel;
import com.cdkj.token.utils.WalletHelper;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * 资产配置
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceActivity extends AbsRefreshListActivity {


    private AddChoiceAdapter addChoiceAdapter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, AddChoiceActivity.class));
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle("添加资产");

        initRefreshHelper(RefreshHelper.LIMITE);

        mRefreshHelper.onDefaluteMRefresh(true);

    }

    @Override
    public void topTitleViewleftClick() {
        saveChooseAndFinish();
    }

    @Override
    public void onBackPressed() {
        saveChooseAndFinish();
    }

    /**
     * 保存选择配置并退出
     */
    public void saveChooseAndFinish() {

        if (addChoiceAdapter != null && addChoiceAdapter.getData() != null && !addChoiceAdapter.getData().isEmpty()) {
            List<String> chooseTypes = new ArrayList<>();
            for (CoinModel.AccountListBean accountListBean : addChoiceAdapter.getData()) {
                if (accountListBean == null || !accountListBean.isChoose()) continue;
                chooseTypes.add(accountListBean.getLocalCoinType());
            }

            String configStr = StringUtils.listToString(chooseTypes, WalletHelper.HELPWORD_SIGN);

            if (!TextUtils.equals(WalletHelper.getWalletCoinConfig(), configStr)) {  //判断配置是否改变

                WalletHelper.saveWalletCoinConfig(configStr);

                EventBus.getDefault().post(new CoinCofigChange());  //通知上级界面配置改变
            }
        }

        finish();
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        addChoiceAdapter = new AddChoiceAdapter(listData);
        addChoiceAdapter.setOnItemClickListener((adapter, view, position) -> {

            CoinModel.AccountListBean accountListBean = addChoiceAdapter.getItem(position);

            accountListBean.setChoose(!accountListBean.isChoose());

            addChoiceAdapter.notifyItemChanged(position);

        });
        return addChoiceAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {

        List<CoinModel.AccountListBean> accountListBeans = new ArrayList<>();

        String configStr = WalletHelper.getWalletCoinConfig();

        for (LocalCoinModel localCoinModel : WalletHelper.getLocalCoinList()) {

            if (localCoinModel == null) continue;
            CoinModel.AccountListBean accountListBean = new CoinModel.AccountListBean();
            accountListBean.setCurrency(localCoinModel.getCoinShortName() + "-" + localCoinModel.getCoinEName());
            accountListBean.setLocalCoinType(localCoinModel.getCoinType());
            accountListBean.setChoose(configStr.indexOf(localCoinModel.getCoinType()) != -1);  //判断用户是否配置了币种
            accountListBeans.add(accountListBean);
        }

        mRefreshHelper.setData(accountListBeans, getStrRes(R.string.bill_none), R.mipmap.order_none);


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

                mRefreshHelper.setData(data.getAccountList(), getStrRes(R.string.bill_none), R.mipmap.order_none);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


}

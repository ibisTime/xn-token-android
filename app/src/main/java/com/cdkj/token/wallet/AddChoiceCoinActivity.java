package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsRefreshListActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.adapter.AddChoiceAdapter;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.model.AddCoinChangeEvent;
import com.cdkj.token.model.db.LocalCoinDbModel;
import com.cdkj.token.model.db.UserConfigDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.cdkj.token.utils.CoinUtil.COIN_SYMBOL_SPACE_SYMBOL;
import static com.cdkj.token.utils.CoinUtil.updateLocalCoinList;

/**
 * 资产配置
 * Created by cdkj on 2018/5/25.
 */

public class AddChoiceCoinActivity extends AbsRefreshListActivity {

    private AddChoiceAdapter addChoiceAdapter;
    private String chooseCoins;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, AddChoiceCoinActivity.class));
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.add_assets));

        initRefreshHelper(RefreshHelper.LIMITE);

        mRefreshBinding.refreshLayout.setEnableLoadmore(false);
        mRefreshBinding.refreshLayout.setEnableRefresh(false);

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

            StringBuffer chooseCoins = new StringBuffer();

            for (LocalCoinDbModel localCoinDbModel : addChoiceAdapter.getData()) {

                if (localCoinDbModel == null || !localCoinDbModel.isChoose()) continue;

                chooseCoins.append(localCoinDbModel.getSymbol());

                chooseCoins.append(COIN_SYMBOL_SPACE_SYMBOL);
            }

            String chooseCoinsStrings = StringUtils.subStringEnd(chooseCoins.toString(), 0);  //去掉最后一个分割符号

            if (!TextUtils.equals(this.chooseCoins, chooseCoinsStrings)) { //配置改变 保存用户选择
                WalletHelper.updateUserChooseCoinString(chooseCoinsStrings, SPUtilHelper.getUserId());
                EventBus.getDefault().postSticky(new AddCoinChangeEvent());   //通知上级界面刷新
            }
        }

        finish();
    }


    @Override
    public RecyclerView.Adapter getListAdapter(List listData) {
        addChoiceAdapter = new AddChoiceAdapter(listData);
        addChoiceAdapter.setOnItemClickListener((adapter, view, position) -> {

            LocalCoinDbModel localCoinDbModel = addChoiceAdapter.getItem(position);

            localCoinDbModel.setChoose(!localCoinDbModel.isChoose());

            addChoiceAdapter.notifyItemChanged(position);

        });
        return addChoiceAdapter;
    }

    @Override
    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
        getCoinList();
    }


    private void getCoinList() {

        Map<String, String> map = new HashMap<>();
        map.put("type", "");
        map.put("ename", "");
        map.put("cname", "");
        map.put("symbol", "");
        map.put("status", "0"); // 0已发布，1已撤下
        map.put("contractAddress", "");

        Call call = RetrofitUtils.createApi(MyApi.class).getCoinList("802267", StringUtils.getJsonToString(map));

        addCall(call);


        call.enqueue(new BaseResponseListCallBack<LocalCoinDbModel>(this) {

            @Override
            protected void onSuccess(List<LocalCoinDbModel> data, String SucMessage) {
                saveCoinAsync(data);

            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 验证用户选择的币种
     *
     * @param coinDbModels
     */
    private void checkUserChooseCoin(List<LocalCoinDbModel> coinDbModels) {

        List<LocalCoinDbModel> dbModels = new ArrayList<>();

        chooseCoins = WalletHelper.getUserChooseCoinSymbolString(SPUtilHelper.getUserId());

        StringBuffer chooseBuf = new StringBuffer();

        for (LocalCoinDbModel coinDbModel : coinDbModels) {                     //请求的币种和用户选择币种比对

            if (coinDbModel == null) continue;

            if (!WalletHelper.userIsCoinChoosed(SPUtilHelper.getUserId())) { //第一次配置全部选中

                coinDbModel.setChoose(true);

                chooseBuf.append(coinDbModel.getSymbol());

                chooseBuf.append(COIN_SYMBOL_SPACE_SYMBOL);


            } else {
                coinDbModel.setChoose(TextUtils.indexOf(chooseCoins, coinDbModel.getSymbol()) != -1);  //判断用户是否配置了币种
            }

            dbModels.add(coinDbModel);
        }

        mRefreshHelper.setData(dbModels, "", 0);

        checkFirstChooseAndSave(chooseBuf);

    }

    /**
     * 检测用户是否第一次配置
     *
     * @param chooseBuf
     */
    private void checkFirstChooseAndSave(StringBuffer chooseBuf) {
        if (!WalletHelper.userIsCoinChoosed(SPUtilHelper.getUserId())) { //第一次配置 保存
            chooseCoins = StringUtils.subStringEnd(chooseBuf.toString(), 0);  //去掉最后一个分割符号
            UserConfigDBModel userChooseCoinDBModel = new UserConfigDBModel();
            userChooseCoinDBModel.setChooseCoins(chooseCoins);
            userChooseCoinDBModel.setUserId(SPUtilHelper.getUserId());
            userChooseCoinDBModel.setIsChoosed(1);
            userChooseCoinDBModel.save();
        }
    }

    /**
     * 异步缓存币种
     *
     * @param data
     */
    private void saveCoinAsync(List<LocalCoinDbModel> data) {
        if (data == null) {
            return;
        }
        mSubscription.add(Observable.just(data)
                .subscribeOn(Schedulers.newThread())
                .map(localCoinDbModels -> {
                    updateLocalCoinList(localCoinDbModels);
                    return 0;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    checkUserChooseCoin(data);
                }, throwable -> {
                    checkUserChooseCoin(data);
                }));

    }


}

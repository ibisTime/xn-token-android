package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityMoneyManagerBuyBinding;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.BiJiaBaoAvliModel;
import com.cdkj.token.model.BiJiaBaoBuyModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.ManageMoneyBuySuccessEvent;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.views.dialogs.MoneyProductBuySuccessDialog;
import com.cdkj.token.views.dialogs.UserPayPasswordInputDialog;
import com.cdkj.token.wallet.account_wallet.RechargeAddressQRActivity;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 购买
 * Created by cdkj on 2018/9/27.
 */

public class BiJiaBaoBuyActivity extends AbsLoadActivity {

    private ActivityMoneyManagerBuyBinding mBinding;

    private BiJiaBaoBuyModel buyModel;

//    private BigDecimal avliAmount;//剩余额度

    private int buyAmount = 0;//当前购买份额

    private UserPayPasswordInputDialog passInputDialog;

    private UserInfoPresenter userInfoPresenter;

    public static void open(Context context, BiJiaBaoBuyModel buyModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BiJiaBaoBuyActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, buyModel);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_money_manager_buy, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            buyModel = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        }
        mBaseBinding.titleView.setMidTitle(R.string.buy);

        //获取用户信息
        userInfoPresenter = new UserInfoPresenter(null, this);
        userInfoPresenter.getUserInfoPayPwdStateRequest();

        initSeekBarChangeListener();
        initClickListener();

        getCoinBalance();
        setShowData();
        getBuyInterval();
    }


    private void initClickListener() {

        //转入资金
        mBinding.tvInMoney.setOnClickListener(view -> {

            if (buyModel == null) return;

            if (userInfoPresenter != null && !userInfoPresenter.checkPayPwdAndStartSetPage()) {  //判断用户是否设置过支付密码
                return;
            }

            RechargeAddressQRActivity.open(this, buyModel.getCoinSymbol());
        });

        //减少份额
        mBinding.tvReduce.setOnClickListener(view -> {
            if (buyAmount - 1 >= 0) {
                mBinding.seekBar.setProgress(buyAmount - 1);
            }
        });

        //增加份额
        mBinding.tvAdd.setOnClickListener(view -> {
            mBinding.seekBar.setProgress(buyAmount + 1);
        });

        //购买
        mBinding.btnBuy.setOnClickListener(view -> {
            if (!mBinding.checkboxRead.isChecked()) {
                UITipDialog.showInfo(this, getString(R.string.buy_condition_1));
                return;
            }
            if (buyAmount <= 0) {
                UITipDialog.showInfo(this, getString(R.string.buy_condition));
                return;
            }

            showPasswordInputDialog(buyAmount + "");

        });

    }

    private void initSeekBarChangeListener() {

        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                buyAmount = i;
                mBinding.tvBuyAmount.setText(i + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /**
     * 设置显示数据
     */
    private void setShowData() {

        if (buyModel == null) return;

//        avliAmount = BigDecimalUtils.div(buyModel.getAvilAmount(), buyModel.getIncreAmount(), 8);//剩余可购额度 / 起购 =剩余份数

//        String avliString = AmountUtil.formatCoinAmount(avliAmount);
//        mBinding.tvAvilAmount.setText(avliString + getString(R.string.fen));
        mBinding.tvExpectYield.setText(StringUtils.showformatPercentage(buyModel.getExpectYield()));

        mBinding.tvName.setText(buyModel.getProductName());
    }


    /**
     * 获取默认币种余额
     */
    public void getCoinBalance() {

        if (TextUtils.isEmpty(SPUtilHelper.getUserId()) || buyModel == null || TextUtils.isEmpty(buyModel.getCoinSymbol())) {
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("currency", buyModel.getCoinSymbol());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(null) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                if (data == null || StringUtils.checkPostionCrossingInList(data.getAccountList(), 0)) {
                    return;
                }

                CoinModel.AccountListBean accountListBean = data.getAccountList().get(0);

                if (accountListBean.getAmount() != null && accountListBean.getFrozenAmount() != null) {

                    BigDecimal amount = accountListBean.getAmount();

                    BigDecimal frozenAmount = accountListBean.getFrozenAmount();
                    //可用=总资产-冻结
                    String amountString = AmountUtil.transformFormatToString(amount.subtract(frozenAmount), accountListBean.getCurrency(), AmountUtil.ALLSCALE);
                    mBinding.tvCoinBalance.setText(Html.fromHtml(getString(R.string.can_use_balance, amountString, buyModel.getCoinSymbol())));
                }


            }


            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 获取可购买区间请求
     */
    public void getBuyInterval() {

        if (TextUtils.isEmpty(SPUtilHelper.getUserId()) || buyModel == null || TextUtils.isEmpty(buyModel.getProductCode())) {
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("productCode", buyModel.getProductCode());
        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<BiJiaBaoAvliModel>> call = RetrofitUtils.createApi(MyApi.class).getBuyInterval("625513", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<BiJiaBaoAvliModel>(this) {
            @Override
            protected void onSuccess(BiJiaBaoAvliModel data, String SucMessage) {
                mBinding.tvMin.setText(data.getMin() + getString(R.string.fen));
                mBinding.tvMax.setText(data.getMax() + getString(R.string.fen));
                mBinding.tvAvilAmount.setText(data.getMax() + getString(R.string.fen));
                mBinding.seekBar.setProgress(1);
                mBinding.seekBar.setMax(data.getMax());
            }

            @Override
            protected void onFinish() {

            }
        });

    }


    /**
     * 显示密码输入
     */
    private void showPasswordInputDialog(String investFen) {
        if (passInputDialog == null) {
            passInputDialog = new UserPayPasswordInputDialog(this);
        }

        passInputDialog.setPasswordInputEndListener(new UserPayPasswordInputDialog.PasswordInputEndListener() {
            @Override
            public void passEnd(String password) {
                passInputDialog.dismiss();

                if (TextUtils.isEmpty(password)) {
                    UITipDialog.showInfoNoIcon(BiJiaBaoBuyActivity.this, getString(R.string.please_input_transaction_pwd));
                    return;
                }

                buyRequest(investFen, password);
            }
        });
        passInputDialog.setPwdEmpty();
        passInputDialog.show();
    }


    /**
     * 购买请求
     *
     * @param investFen
     * @param pwd
     */
    public void buyRequest(String investFen, String pwd) {

        if (buyModel == null || TextUtils.isEmpty(buyModel.getProductCode()) || TextUtils.isEmpty(pwd)) {
            return;
        }

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("code", buyModel.getProductCode());
        map.put("investFen", investFen);
        map.put("tradePwd", pwd);
        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<IsSuccessModes>> call = RetrofitUtils.getBaseAPiService().successRequest("625520", StringUtils.getRequestJsonString(map));

        addCall(call);
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data != null && data.isSuccess()) {
                    buySuccess();
                } else {
                    buyFail();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }


    //购买成功
    public void buySuccess() {
        new MoneyProductBuySuccessDialog(this).show();
    }

    //购买失败
    public void buyFail() {
        UITipDialog.showFail(this, getString(R.string.buy_fail));
    }
}

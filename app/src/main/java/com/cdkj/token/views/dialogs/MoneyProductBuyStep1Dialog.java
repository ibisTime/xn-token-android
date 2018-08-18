package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogInfoSureBinding;
import com.cdkj.token.databinding.DialogMoneyProductBuyConfirmBinding;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.wallet.account.RechargeAddressQRActivity;

import java.math.BigDecimal;

/**
 * 理财产品购买1dialog
 * Created by cdkj on 2018/7/20.
 */

public class MoneyProductBuyStep1Dialog extends Dialog {

    private Activity mActivity;

    private DialogMoneyProductBuyConfirmBinding mBinding;

    private BigDecimal balanceBigDecimal;

    private String coinName;

    private BigDecimal coinuUnit;//货币最小单位

    private float inCome;//收益

    private ManagementMoney managementMoney;


    public toBuyListener toBuyListener;


    public MoneyProductBuyStep1Dialog setToBuyListener(MoneyProductBuyStep1Dialog.toBuyListener toBuyListener) {
        this.toBuyListener = toBuyListener;
        return this;
    }

    //购买监听
    public interface toBuyListener {
        void onBuy(String money);
    }

    public MoneyProductBuyStep1Dialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_money_product_buy_confirm, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
//        int screenHeight = DisplayHelper.getScreenHeight(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout((int) (screenWidth * 0.9f), LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        initClickListener();

        //输入框位数限制
        mBinding.editBuyCount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.editBuyCount, 15, 8));

        mBinding.editBuyCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence) || TextUtils.equals(".", charSequence.toString())) {
                    setIncomeForecast("0");
                    return;
                }

                float buyamount = Float.valueOf(charSequence.toString());
                setIncomeForecast(AmountUtil.formatCoinAmount(buyamount * inCome));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initClickListener() {

        mBinding.imgClose.setOnClickListener(view -> dismiss());

        //购买
        mBinding.btnBuy.setOnClickListener(view -> checkBuyState());

        //传入资金
        mBinding.tvInMoney.setOnClickListener(view -> {
            RechargeAddressQRActivity.open(mActivity, coinName);
            dismiss();
        });

    }

    /**
     * 检查是否具有购买条件
     * <p>
     * 1不能小于起购金额
     * 2不能大于余额
     * 3不能大于限购金额
     * 4不能大于总额（可售额度）
     */
    private void checkBuyState() {

        if (managementMoney == null) {
            return;
        }

        //购买金额小于起购余额
        if (TextUtils.isEmpty(mBinding.editBuyCount.getText().toString())) {
            UITipDialog.showInfoNoIcon(mActivity, "起购额度为" + AmountUtil.amountFormatUnitForShow(managementMoney.getMinAmount(), coinuUnit, AmountUtil.ALLSCALE));
            return;
        }


        //购买金额
        BigDecimal buyAmount = AmountUtil.bigDecimalFormat(new BigDecimal(mBinding.editBuyCount.getText().toString().trim()), coinuUnit);

        //购买金额小于起购余额
        if (!BigDecimalUtils.compareTo2(buyAmount, managementMoney.getMinAmount())) {
            UITipDialog.showInfoNoIcon(mActivity, "起购额度为" + AmountUtil.amountFormatUnitForShow(managementMoney.getMinAmount(), coinuUnit, AmountUtil.ALLSCALE));
            return;
        }

        //2购买金额大于余额
        if (BigDecimalUtils.compareTo(buyAmount, balanceBigDecimal)) {
            UITipDialog.showInfoNoIcon(mActivity, "可用余额不足");
            return;
        }

        //3不能大于限购金额
        if (BigDecimalUtils.compareTo(buyAmount, managementMoney.getLimitAmount())) {
            UITipDialog.showInfoNoIcon(mActivity, "限购额度为" + AmountUtil.amountFormatUnitForShow(managementMoney.getLimitAmount(), coinuUnit, AmountUtil.ALLSCALE));
            return;
        }


        //4购买金额大于总额（可售额度）
        if (BigDecimalUtils.compareTo(buyAmount, managementMoney.getAvilAmount())) {
            UITipDialog.showInfoNoIcon(mActivity, "可售额度不足");
            return;
        }

        if (toBuyListener != null) {
            toBuyListener.onBuy(mBinding.editBuyCount.getText().toString());
        }
    }


    /**
     * 显示的数据
     *
     * @return
     */
    public MoneyProductBuyStep1Dialog setShowData(BigDecimal balance, ManagementMoney managementMoney) {

        this.managementMoney = managementMoney;

        balanceBigDecimal = balance;

        this.coinName = managementMoney.getSymbol();

        inCome = managementMoney.getActualYield();

        mBinding.tvCoinName.setText(coinName);
        mBinding.tvCoinName2.setText(coinName);

        coinuUnit = LocalCoinDBUtils.getLocalCoinUnit(coinName);

        mBinding.tvBalance.setText(AmountUtil.amountFormatUnitForShow(balance, coinuUnit, AmountUtil.ALLSCALE) + " " + coinName);

        setIncomeForecast("0");

        return this;
    }


    void setIncomeForecast(String income) {
        Spanned s = Html.fromHtml(mActivity.getString(R.string.product_forecast_income, income, coinName));
        mBinding.tvIncomeForecast.setText(s);
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }


}

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
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogMoneyProductBuyConfirmBinding;
import com.cdkj.token.interfaces.ProductBuyListener;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.model.ProductBuyStep2Model;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.wallet.account_wallet.RechargeAddressQRActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_YMD;
import static java.math.BigDecimal.ROUND_DOWN;

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

    private float inComeRate;//收益

    private ManagementMoney managementMoney;


    public ProductBuyListener toBuyListener;

    private String expectInComeAmountString;//预期收益金额


    public MoneyProductBuyStep1Dialog setToBuyListener(ProductBuyListener toBuyListener) {
        this.toBuyListener = toBuyListener;
        return this;
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
        mBinding.editBuyCount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.editBuyCount, 15, 4));

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

                //预期收益计算=投资金额*年化收益率/360*投资期限（天）     计算结果先保留4位小数点 最后显示保留2位
                float buyamount = Float.valueOf(charSequence.toString());

                if (buyamount <= 0) {
                    setIncomeForecast("0");
                    return;
                }

                BigDecimal bigDecimal = new BigDecimal(buyamount * inComeRate / 360);

                buyamount = bigDecimal.setScale(4, ROUND_DOWN).floatValue();

                buyamount = buyamount * managementMoney.getLimitDays();

                DecimalFormat df = new DecimalFormat("#######0.##");

                String showMoney = df.format(buyamount);

                setIncomeForecast(showMoney);
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
     * <p>
     * 5递增金额
     */
    private void checkBuyState() {

        if (managementMoney == null) {
            return;
        }

        //购买金额小于起购余额
        if (TextUtils.isEmpty(mBinding.editBuyCount.getText().toString())) {
            UITipDialog.showInfoNoIcon(mActivity, mActivity.getString(R.string.product_buy_hint));
            return;
        }

        //购买金额
        BigDecimal buyAmount = AmountUtil.bigDecimalFormat(new BigDecimal(mBinding.editBuyCount.getText().toString().trim()), coinuUnit);

        //购买金额小于起购余额
        if (!BigDecimalUtils.compareTo2(buyAmount, managementMoney.getMinAmount())) {
            UITipDialog.showInfoNoIcon(mActivity, mActivity.getString(R.string.money_product_check_1) + AmountUtil.transformFormatToString(managementMoney.getMinAmount(), coinuUnit, AmountUtil.ALLSCALE));
            return;
        }

        if (managementMoney.getIncreAmount() == null || BigDecimalUtils.compareEqualsZERO(managementMoney.getIncreAmount())) {
            return;
        }

        BigDecimal seccondAmount = buyAmount.subtract(managementMoney.getMinAmount())
                .divideAndRemainder(managementMoney.getIncreAmount())[1];


        //5递增金额判断
        if (!BigDecimalUtils.compareEqualsZERO(seccondAmount)) {
            UITipDialog.showInfoNoIcon(mActivity, mActivity.getString(R.string.money_product_check_2));
            return;
        }

        //2购买金额大于余额
        if (BigDecimalUtils.compareTo(buyAmount, balanceBigDecimal)) {
            UITipDialog.showInfoNoIcon(mActivity, mActivity.getString(R.string.money_product_check_3));
            return;
        }

        //3不能大于限购金额
        if (BigDecimalUtils.compareTo(buyAmount, managementMoney.getLimitAmount())) {
            UITipDialog.showInfoNoIcon(mActivity, mActivity.getString(R.string.money_product_check_4) + AmountUtil.transformFormatToString(managementMoney.getLimitAmount(), coinuUnit, AmountUtil.ALLSCALE));
            return;
        }


        //4购买金额大于总额（可售额度）
        if (BigDecimalUtils.compareTo(buyAmount, managementMoney.getAvilAmount())) {
            UITipDialog.showInfoNoIcon(mActivity, mActivity.getString(R.string.money_product_check_5));
            return;
        }


        if (toBuyListener != null) {
            dismiss();
            ProductBuyStep2Model productBuyStep2Model = new ProductBuyStep2Model();

            productBuyStep2Model.setBuyAmount(AmountUtil.bigDecimalFormat(new BigDecimal(mBinding.editBuyCount.getText().toString()), coinuUnit));

            productBuyStep2Model.setBuyAmountString(mBinding.editBuyCount.getText().toString());

            productBuyStep2Model.setCoinSymbol(coinName);

            productBuyStep2Model.setProductName(managementMoney.getName());

            productBuyStep2Model.setEndTime(DateUtil.formatStringData(managementMoney.getArriveDatetime(), DATE_YMD));

            productBuyStep2Model.setExpectInComeAmount(expectInComeAmountString);

            toBuyListener.onBuyStep1(productBuyStep2Model);
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

        inComeRate = managementMoney.getActualYield();

        mBinding.tvCoinName.setText(coinName);
        mBinding.tvCoinName2.setText(coinName);

        coinuUnit = LocalCoinDBUtils.getLocalCoinUnit(coinName);

        mBinding.tvBalance.setText(AmountUtil.transformFormatToString(balance, coinuUnit, AmountUtil.ALLSCALE) + " " + coinName);

        setIncomeForecast("0");

        return this;
    }


    void setIncomeForecast(String income) {
        expectInComeAmountString = income;
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

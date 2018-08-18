package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogMoneyProductBuyConfirm2Binding;
import com.cdkj.token.interfaces.ProductBuyListener;
import com.cdkj.token.model.ProductBuyStep2Model;

/**
 * 购买信息确认第二步
 * Created by cdkj on 2018/7/20.
 */

public class MoneyProductBuyStep2Dialog extends Dialog {

    private Activity mActivity;

    private DialogMoneyProductBuyConfirm2Binding mBinding;

    public ProductBuyListener toBuyListener;

    private ProductBuyStep2Model productBuyStep2Model;

    public MoneyProductBuyStep2Dialog setToBuyListener(ProductBuyListener toBuyListener) {
        this.toBuyListener = toBuyListener;
        return this;
    }

    public MoneyProductBuyStep2Dialog setProductInfo(ProductBuyStep2Model productBuyStep2Model) {
        this.productBuyStep2Model = productBuyStep2Model;

        setShowInfo();

        return this;
    }


    public MoneyProductBuyStep2Dialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_money_product_buy_confirm_2, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout((int) (screenWidth * 0.9f), LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mBinding.imgClose.setOnClickListener(view -> {
            dismiss();
        });

        mBinding.tvToPay.setOnClickListener(view -> {
            if (toBuyListener != null) {
                toBuyListener.onBuyStep2(productBuyStep2Model);
            }
        });

    }

    /**
     * 设置数据显示
     */
    private void setShowInfo() {

        if (mBinding == null || mActivity == null || productBuyStep2Model == null) {
            return;
        }

        mBinding.tvName.setText(productBuyStep2Model.getProductName());
        mBinding.tvBuyAmount.setText(productBuyStep2Model.getBuyAmountString() + productBuyStep2Model.getCoinSymbol());
        mBinding.tvEndTime.setText(productBuyStep2Model.getEndTime());
        mBinding.tvIncome.setText(productBuyStep2Model.getExpectInComeAmount() + productBuyStep2Model.getCoinSymbol());

    }


    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

}

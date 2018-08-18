package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogMoneyProductBuyConfirm2Binding;
import com.cdkj.token.interfaces.ProductBuyListener;

/**
 * 购买信息确认第二步
 * Created by cdkj on 2018/7/20.
 */

public class MoneyProductBuyStep2Dialog extends Dialog {

    private Activity mActivity;

    private DialogMoneyProductBuyConfirm2Binding mBinding;

    public ProductBuyListener toBuyListener;


    public MoneyProductBuyStep2Dialog setToBuyListener(ProductBuyListener toBuyListener) {
        this.toBuyListener = toBuyListener;
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
        int screenHeight = DisplayHelper.getScreenHeight(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout((int) (screenWidth * 0.9f), (int) (screenHeight * 0.6));
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

}

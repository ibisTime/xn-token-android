package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.CdApplication;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogMoneyProductBuyConfirm2Binding;
import com.cdkj.token.databinding.DialogMoneyProductBuySuccessBinding;
import com.cdkj.token.find.product_application.management_money.MyInvestmentDetails;
import com.cdkj.token.find.product_application.management_money.MyManagementMoneyListActivity;
import com.cdkj.token.interfaces.ProductBuyListener;
import com.cdkj.token.model.ProductBuyStep2Model;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 理财产品购买成功
 * Created by cdkj on 2018/7/20.
 */

public class MoneyProductBuySuccessDialog extends Dialog {

    private Activity mActivity;

    private DialogMoneyProductBuySuccessBinding mBinding;

    public ProductBuyListener toBuyListener;

    protected CompositeDisposable mSubscription;

    public MoneyProductBuySuccessDialog setToBuyListener(ProductBuyListener toBuyListener) {
        this.toBuyListener = toBuyListener;
        return this;
    }


    public MoneyProductBuySuccessDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_money_product_buy_success, null, false);
        mSubscription = new CompositeDisposable();
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

        mBinding.tvLookBuyHistory.setOnClickListener(view -> {
            startToHistory();
        });

    }

    private void startToHistory() {
        dismiss();
        if (mActivity == null) return;
//        MyManagementMoneyListActivity.open(mActivity);
        MyInvestmentDetails.open(mActivity);
        mActivity.finish();
    }


    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        super.show();
    }

    @Override
    public void dismiss() {
        mSubscription.clear();
        super.dismiss();
    }

    @Override
    public void onDetachedFromWindow() {
        mSubscription.clear();
        super.onDetachedFromWindow();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mSubscription.add(startCodeDown(mActivity, mBinding.tvDelayToStart));
    }

    /**
     * 倒计时
     *
     * @return
     */
    public Disposable startCodeDown(final Activity activity, TextView tv) {
        return Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())    // 创建一个按照给定的时间间隔发射从0开始的整数序列
                .observeOn(AndroidSchedulers.mainThread())
                .take(6)//只发射开始的N项数据或者一定时间内的数据
                .subscribe(new Consumer<Long>() {
                               @Override
                               public void accept(Long aLong) throws Exception {
                                   tv.setText(activity.getString(R.string.delay_to_start, (5 - aLong) + ""));
                                   if (5 - aLong <= 0) {
                                       startToHistory();
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   startToHistory();
                               }
                           }
                );
    }


}

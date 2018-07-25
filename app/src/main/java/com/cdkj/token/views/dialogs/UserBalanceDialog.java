package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogInfoSureBinding;
import com.cdkj.token.databinding.DialogRedPacketBalanceBinding;

/**
 * 提示信息确认
 * Created by cdkj on 2018/7/20.
 */

public class UserBalanceDialog extends Dialog {

    private Activity mActivity;

    private DialogRedPacketBalanceBinding mBinding;

    private View.OnClickListener sureCLickListener;

    public UserBalanceDialog setSureCLickListener(View.OnClickListener sureCLickListener) {
        this.sureCLickListener = sureCLickListener;
        return this;
    }

    public UserBalanceDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_red_packet_balance, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout((int) (screenWidth * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        initListener();
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

    public UserBalanceDialog setInfoTitle(String title) {
        if (mBinding == null) return this;
        mBinding.tvTitle.setText(title);
        return this;
    }

    public void setShowBalance(String balanceStr) {
        mBinding.tvBalance.setText(balanceStr);
    }

    private void initListener() {
        mBinding.imgClose.setOnClickListener(view -> dismiss());
        mBinding.btnSure.setOnClickListener(sureCLickListener);
    }


}

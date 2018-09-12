package com.cdkj.token.find.product_application.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySendRedPacketBinding;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.views.dialogs.UserPayPasswordInputDialog;
import com.cdkj.token.wallet.account_wallet.RechargeAddressQRActivity;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;

/**
 * 发送红包
 * Created by cdkj on 2018/9/12.
 */

public class SendRedPacketActivity extends AbsLoadActivity implements SendRedPacketView {

    private ActivitySendRedPacketBinding mBinding;
    private UserPayPasswordInputDialog passInputDialog;
    private SendRedPacketPresenter sendRedPacketPresenter;
    private UserInfoPresenter userInfoPresenter;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SendRedPacketActivity.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_send_red_packet, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.theia_red_packet);
        sendRedPacketPresenter = new SendRedPacketPresenter();
        sendRedPacketPresenter.attachView(this);
        sendRedPacketPresenter.setLayoutByType(SendRedPacketPresenter.TYPE_LUCKY);

        userInfoPresenter = new UserInfoPresenter(null, this);
        sendRedPacketPresenter.getDefalutCoin();
        userInfoPresenter.getUserInfoRequest();//获取用户信息

        mBinding.tvInMoney.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        initClickListener();
        initEditWatcher();

    }

    /**
     * 普通红包总额计算
     */
    void initEditWatcher() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                calculateInputTotal();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mBinding.editAmount.addTextChangedListener(watcher);
        mBinding.editNumber.addTextChangedListener(watcher);
        mBinding.editAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.editAmount, 15, 8));
    }

    /**
     * 计算普通红包总额
     */
    void calculateInputTotal() {
        String amountString = mBinding.editAmount.getText().toString().trim();
        String numberString = mBinding.editNumber.getText().toString().trim();
        mBinding.tvTotalAmount.setText(sendRedPacketPresenter.calculateInputTotal(amountString, numberString) + getStrRes(R.string.red_package_unit));
    }


    private void initClickListener() {

        //币种选择

        mBinding.linLayoutCoinSelect.setOnClickListener(view -> {
            CoinSelectListActivity.open(this);
        });

        //转入资金
        mBinding.tvInMoney.setOnClickListener(view -> {
            String coinSymbol = mBinding.tvCoinName.getText().toString();
            RechargeAddressQRActivity.open(this, coinSymbol);
        });

        //拼手气红包
        mBinding.tvChangeLucky.setOnClickListener(view -> {
            sendRedPacketPresenter.setLayoutByType(SendRedPacketPresenter.TYPE_LUCKY);
            calculateInputTotal();
        });

        //普通红包
        mBinding.tvChangeOrdinary.setOnClickListener(view -> {
            sendRedPacketPresenter.setLayoutByType(SendRedPacketPresenter.TYPE_ORDINARY);
            calculateInputTotal();
        });


        //发送红包
        mBinding.btnSend.setOnClickListener(view -> {
            if (!userInfoPresenter.checkPayPwdAndStartSetPage()) {
                return;
            }
            String sendNumber = mBinding.editNumber.getText().toString();
            String sendAmount = mBinding.editAmount.getText().toString();
            String coinSymbol = mBinding.tvCoinName.getText().toString();
            if (!sendRedPacketPresenter.checkInputState(sendNumber, sendAmount, coinSymbol)) {
                return;
            }
            showPasswordInputDialog();
        });

    }

    @Override
    public void setOrdinaryStatus() {
        mBinding.linLayoutInputCount.setVisibility(View.VISIBLE);
        mBinding.tvTypeReadPacket.setText(R.string.ordinary_red_packet);
        mBinding.tvAmountType.setText(R.string.total);
        mBinding.tvRedPacketInfo.setText(R.string.red_packet_intro_ordinary);
    }

    @Override
    public void setluckyStatus() {
        mBinding.linLayoutInputCount.setVisibility(View.INVISIBLE);
        mBinding.tvTypeReadPacket.setText(R.string.lucky_red_packet);
        mBinding.tvAmountType.setText(R.string.single_amount);
        mBinding.tvRedPacketInfo.setText(R.string.red_packet_intro_lucky);
    }

    @Override
    public void setBalance(String balanceString) {
        mBinding.tvBalance.setText(balanceString);
    }

    @Subscribe
    @Override
    public void setDefaluteCoin(CoinModel.AccountListBean defaluteCoin) {
        if (defaluteCoin == null) return;

        mBinding.tvCoinName.setText(defaluteCoin.getCurrency());
        mBinding.tvCoinName2.setText(defaluteCoin.getCurrency());

        if (defaluteCoin.getAmount() != null && defaluteCoin.getFrozenAmount() != null) {

            BigDecimal amount = defaluteCoin.getAmount();

            BigDecimal frozenAmount = defaluteCoin.getFrozenAmount();
            //可用=总资产-冻结
            String amountString = AmountUtil.transformFormatToString(amount.subtract(frozenAmount), defaluteCoin.getCurrency(), AmountUtil.ALLSCALE);
            setBalance(amountString);
        }
    }


    @Override
    public void setSendSuccessStatus() {

    }

    @Override
    public void setSendFailStatus() {

    }

    @Override
    public void showLoadDialog() {
        showLoadingDialog();
    }

    @Override
    public void disMissLoadDialog() {
        disMissLoadingDialog();
    }

    @Override
    public void showInfoDialog(String msgSgring) {
        UITipDialog.showInfo(this, msgSgring);
    }


    /**
     * 显示密码输入
     */
    private void showPasswordInputDialog() {
        if (passInputDialog == null) {
            passInputDialog = new UserPayPasswordInputDialog(this);
            passInputDialog.setPasswordInputEndListener(new UserPayPasswordInputDialog.PasswordInputEndListener() {
                @Override
                public void passEnd(String password) {
                    passInputDialog.dismiss();

                    String sendNumber = mBinding.editNumber.getText().toString();
                    String sendAmount = mBinding.editAmount.getText().toString();
                    String sendGreeting = getGreeting();
                    String coinSymbol = mBinding.tvCoinName.getText().toString();

                    sendRedPacketPresenter.sendRedPacket(password, sendNumber, sendAmount, sendGreeting, coinSymbol);
                }
            });
        }
        passInputDialog.setPwdEmpty();
        passInputDialog.show();
    }

    /**
     * 获取祝福语言
     *
     * @return
     */
    private String getGreeting() {

        if (TextUtils.isEmpty(mBinding.etGreeting.getText().toString().trim())) {
            return mBinding.etGreeting.getHint().toString();
        }

        return mBinding.etGreeting.getText().toString().trim();
    }


    @Override
    public void onError(String msg, String code) {

    }

    @Override
    protected void onDestroy() {
        if (sendRedPacketPresenter != null) {
            sendRedPacketPresenter.detachView();
        }
        super.onDestroy();
    }


}

package com.cdkj.token.find.product_application.red_package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySendRedPacketBinding;
import com.cdkj.token.find.product_application.question.ApplicationQuestionListActivity;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.PickerViewModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.views.dialogs.UserPayPasswordInputDialog;
import com.cdkj.token.wallet.account_wallet.RechargeAddressQRActivity;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送红包
 * Created by cdkj on 2018/9/12.
 */

public class SendRedPacketActivity extends AbsLoadActivity implements SendRedPacketView {

    private ActivitySendRedPacketBinding mBinding;
    private UserPayPasswordInputDialog passInputDialog;
    private SendRedPacketPresenter sendRedPacketPresenter;
    private UserInfoPresenter userInfoPresenter;
    private OptionsPickerView menuPickerView;
    private List<PickerViewModel> pickerViewModels;
    private String appCode;

    public static void open(Context context, String appCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SendRedPacketActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, appCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_send_red_packet, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void topTitleViewRightClick() {
        showMenuPickView();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        appCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);

        mBaseBinding.titleView.setMidTitle(R.string.theia_red_packet);
        mBaseBinding.titleView.setRightImg(R.drawable.topbar_more);

        sendRedPacketPresenter = new SendRedPacketPresenter();
        sendRedPacketPresenter.attachView(this);
        sendRedPacketPresenter.setLayoutByType(SendRedPacketPresenter.TYPE_LUCKY);

        userInfoPresenter = new UserInfoPresenter(null, this);
        sendRedPacketPresenter.getDefalutCoin();
        userInfoPresenter.getUserInfoRequest();//获取用户信息

        mBinding.tvInMoney.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mBinding.tvTotalAmount.setText("0" + getStrRes(R.string.red_package_unit));

        initClickListener();
        initEditWatcher();

    }

    /**
     * 显示选择pickView
     */
    private void showMenuPickView() {

        if (menuPickerView == null) {
            //条件选择器
            menuPickerView = new OptionsPickerBuilder(SendRedPacketActivity.this, new OnOptionsSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {

                    menuPickerView.dismiss();

                    switch (options1) {
                        case 0:
                            RedPacketSendHistoryActivity.open(SendRedPacketActivity.this);
                            break;
                        case 1:
                            ApplicationQuestionListActivity.open(SendRedPacketActivity.this, appCode, getString(R.string.theia_redpacket_intro));
                            break;
                    }

                }
            })
                    .setSubmitColor(ContextCompat.getColor(this, R.color.text_black_cd))
                    .setCancelColor(ContextCompat.getColor(this, R.color.gray_999999))
                    .setCancelText(getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.confirm))//确认按钮文字
                    .build();
            pickerViewModels = new ArrayList<>();
            pickerViewModels.add(new PickerViewModel(getString(R.string.my_redpacket_history)));
            pickerViewModels.add(new PickerViewModel(getString(R.string.redpacket_question)));
            menuPickerView.setPicker(pickerViewModels);
        }

        menuPickerView.show();
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

        mBinding.tvTypeReadPacket.setText(R.string.ordinary_red_packet);
        mBinding.tvAmountType.setText(R.string.single_amount);
        mBinding.tvRedPacketInfo.setText(R.string.red_packet_intro_ordinary);

        mBinding.tvChangeLucky.setBackgroundResource(R.drawable.redpacket_btn_lucky_un_select);
        mBinding.tvChangeLucky.setTextColor(ContextCompat.getColor(this, R.color.red));

        mBinding.tvChangeOrdinary.setBackgroundResource(R.drawable.redpacket_btn_ordinary_select);
        mBinding.tvChangeOrdinary.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    @Override
    public void setluckyStatus() {

        mBinding.tvTypeReadPacket.setText(R.string.lucky_red_packet);

        mBinding.tvAmountType.setText(R.string.total);
        mBinding.tvRedPacketInfo.setText(R.string.red_packet_intro_lucky);

        mBinding.tvChangeLucky.setBackgroundResource(R.drawable.redpacket_btn_lucky_select);
        mBinding.tvChangeLucky.setTextColor(ContextCompat.getColor(this, R.color.white));


        mBinding.tvChangeOrdinary.setBackgroundResource(R.drawable.redpacket_btn_ordinary_un_select);
        mBinding.tvChangeOrdinary.setTextColor(ContextCompat.getColor(this, R.color.red));


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
    public void setSendSuccessStatus(String redPacketCode) {
        RedPacketShareQRActivity.open(this, redPacketCode, true);
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
        if (menuPickerView != null) {
            menuPickerView.dismiss();
        }
        super.onDestroy();
    }


}

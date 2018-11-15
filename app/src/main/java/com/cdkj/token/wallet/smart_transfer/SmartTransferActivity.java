package com.cdkj.token.wallet.smart_transfer;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.TextPwdInputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.adapter.SmartTrasfterCoinAdapter;
import com.cdkj.token.databinding.ActivitySmartTransferBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.RecyclerViewSpacesItemDecoration;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * TODO  一键划转 每个币种的操作都一样但是由于不同币种的判断条件不一致和数据源不一致导致结构混乱 需要重构
 * Created by cdkj on 2018/9/10.
 */

public class SmartTransferActivity extends AbsLoadActivity implements SmartTransferView {


    private ActivitySmartTransferBinding mBinding;
    private SmartTransferPresenter smartTransferPresenter;
    private SmartTrasfterCoinAdapter smartTrasfterCoinAdapter;
    private TextPwdInputDialog pwdInputDialog;
    private String selectCoinSymbol = "";
    private BigDecimal balanceBigDecimal = BigDecimal.ZERO;
    private BigDecimal feeBigDecimal = BigDecimal.ZERO;


    @Override
    protected void onDestroy() {
        if (smartTransferPresenter != null) {
            smartTransferPresenter.detachView();
            smartTransferPresenter = null;
        }
        super.onDestroy();
    }

    /**
     * @param context
     * @param isFromPrivate 是否来自私钥界面
     */
    public static void open(Context context, boolean isFromPrivate) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SmartTransferActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromPrivate);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_smart_transfer, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBaseBinding.titleView.setMidTitle(R.string.fast_transfer);
        mBaseBinding.contentView.setVisibility(View.GONE);
        initViews();
        initClickListener();
        smartTransferPresenter = new SmartTransferPresenter(this);
        smartTransferPresenter.attachView(this);
        smartTransferPresenter.setPrivateWallet(getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false));
        smartTransferPresenter.getUserInfo();
        smartTransferPresenter.getCoins(this);

    }


    private void initClickListener() {

        //切换
        mBinding.imgToggle.setOnClickListener(view -> {
            smartTransferPresenter.togglePrivateStatus();
        });

        //全部
        mBinding.tvAllCoin.setOnClickListener(view -> {
            setAllAmountText();
        });

        //确认划转
        mBinding.btnNext.setOnClickListener(view -> {

            if (TextUtils.isEmpty(selectCoinSymbol)) {
                UITipDialog.showInfo(this, getString(R.string.plealse_select_coin_type));
                return;
            }
            if (TextUtils.isEmpty(mBinding.editAmount.getText().toString())) {
                UITipDialog.showInfo(this, getString(R.string.input_smart_transfer_number));
                return;
            }

            BigDecimal amountBigDecimal = new BigDecimal(mBinding.editAmount.getText().toString().trim());

            if (amountBigDecimal.compareTo(BigDecimal.ZERO) == 0 || amountBigDecimal.compareTo(BigDecimal.ZERO) == -1) {
                UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
                return;
            }


            if (smartTransferPresenter.isPrivateWallet()) {

                BigDecimal amountBigInteger = AmountUtil.bigDecimalFormat(new BigDecimal(mBinding.editAmount.getText().toString().trim()),
                        selectCoinSymbol); //转账数量

                if (!LocalCoinDBUtils.isTokenCoinBySymbol(selectCoinSymbol) && !LocalCoinDBUtils.isBTC(selectCoinSymbol)) { //如果不是token币

                    BigDecimal getLimiteFee = new BigDecimal(WalletHelper.getDeflutGasLimit())                   //limite * gasPrice
                            .multiply(feeBigDecimal);

                    BigDecimal allBigInteger = amountBigInteger.add(getLimiteFee);//手续费+转账数量

                    int checkInt = allBigInteger.compareTo(balanceBigDecimal); //比较

                    if (checkInt == 1) {
                        UITipDialog.showInfo(this, getString(R.string.no_balance));
                        return;
                    }

                } else if (LocalCoinDBUtils.isBTC(selectCoinSymbol)) {

                    int checkInt = amountBigInteger.compareTo(balanceBigDecimal); //比较

                    if (checkInt == 1) {
                        UITipDialog.showInfo(this, getString(R.string.no_balance));
                        return;
                    }
                }else if (LocalCoinDBUtils.isUSDT(selectCoinSymbol)) {



                }

            }

            smartTransferPresenter.showPayPasswordDialog();
        });
    }

    /**
     * 显示所有余额 （除BTC、token币、中心化钱包外其他都需要减去手续费 ）
     */
    private void setAllAmountText() {

        String balanceString = "";

        //btc 或 token币
        if (!smartTransferPresenter.isPrivateWallet() || LocalCoinDBUtils.isBTC(selectCoinSymbol) || LocalCoinDBUtils.isTokenCoinBySymbol(selectCoinSymbol)) {
            balanceString = AmountUtil.transformFormatToString(balanceBigDecimal, selectCoinSymbol, AmountUtil.ALLSCALE);
        } else {
            BigDecimal limiteFee = new BigDecimal(WalletHelper.getDeflutGasLimit())                   //limite * gasPrice
                    .multiply(feeBigDecimal);
            //余额大于0 余额大于手续费
            if (BigDecimalUtils.compareTo(balanceBigDecimal, BigDecimal.ZERO) && BigDecimalUtils.compareTo(balanceBigDecimal, limiteFee)) {
                balanceString = AmountUtil.transformFormatToString(BigDecimalUtils.subtract(balanceBigDecimal, limiteFee), selectCoinSymbol, AmountUtil.ALLSCALE);
            }
        }

//        balanceString = AmountUtil.transformFormatToString(balanceBigDecimal, selectCoinSymbol, AmountUtil.ALLSCALE);

        setAmount(balanceString);
    }

    private void setAmount(String balanceString) {
        mBinding.editAmount.setText(balanceString);
        mBinding.editAmount.setSelection(balanceString.length());
    }

    private void initViews() {


        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, DisplayHelper.dp2px(this, 15));//底部间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, DisplayHelper.dp2px(this, 15));//右间距

        mBinding.recyclerViewCoin.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));

        mBinding.editAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.editAmount, 15, 8));

        mBinding.recyclerViewCoin.setLayoutManager(new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        //矿工费滑动设置显示
        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                smartTransferPresenter.setFeesBySeekBarChange(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    @Override
    public void showGoogleEdit(boolean isVisible) {
        if (isVisible) {
            mBinding.tvGoogle.setVisibility(View.VISIBLE);
            mBinding.eidtGoogle.setVisibility(View.VISIBLE);
            mBinding.linLayoutGoogle.setVisibility(View.VISIBLE);
        } else {
            mBinding.tvGoogle.setVisibility(View.GONE);
            mBinding.eidtGoogle.setVisibility(View.GONE);
            mBinding.linLayoutGoogle.setVisibility(View.GONE);
        }
    }

    @Override
    public void setCenterPage() {
        mBinding.seekBar.setVisibility(View.GONE);
        mBinding.tvAccount.setText(R.string.my_account_wallet);
        mBinding.tvAccountPrivate.setText(R.string.private_key_account);
        mBinding.viewPoint.setBackgroundResource(R.drawable.smart_transfer_blue_point);
        mBinding.viewPointPrivate.setBackgroundResource(R.drawable.smart_transfer_red_point);

    }

    @Override
    public void setPrivatePage() {
        mBinding.seekBar.setVisibility(View.VISIBLE);
        mBinding.tvAccountPrivate.setText(R.string.my_account_wallet);
        mBinding.tvAccount.setText(R.string.private_key_account);
        mBinding.viewPoint.setBackgroundResource(R.drawable.smart_transfer_red_point);
        mBinding.viewPointPrivate.setBackgroundResource(R.drawable.smart_transfer_blue_point);
    }

    @Override
    public void resetFeeBarProgress() {
        mBinding.seekBar.setProgress(50);
    }


    @Override
    public void setCoins(CoinModel coinModel) {
        if (smartTrasfterCoinAdapter == null) {
            smartTrasfterCoinAdapter = new SmartTrasfterCoinAdapter(coinModel.getAccountList());
            smartTrasfterCoinAdapter.setOnItemClickListener((adapter, view, position) -> {

                if (smartTrasfterCoinAdapter.getSelectPosition() == position) {
                    return;
                }

                smartTrasfterCoinAdapter.setSelectPosition(position);

                smartTransferPresenter.selectCoin(position);
            });
            mBinding.recyclerViewCoin.setAdapter(smartTrasfterCoinAdapter);

            selectDefaluteCoin();

        } else {
            smartTrasfterCoinAdapter.replaceData(coinModel.getAccountList());
        }

        if (mBaseBinding.contentView.getVisibility() == View.GONE) {
            mBaseBinding.contentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setSelectCoin(CoinModel.AccountListBean accountListBean) {
        selectCoinSymbol = accountListBean.getCurrency();
        mBinding.tvCoinSymbol.setText(selectCoinSymbol);
        mBinding.editAmount.setText("");
    }

    /**
     * 选择默认币种
     */
    void selectDefaluteCoin() {
        smartTransferPresenter.selectCoin(0);
        smartTrasfterCoinAdapter.setSelectPosition(0);
    }

    @Override
    public void setBalance(BigDecimal balance) {
        balanceBigDecimal = balance;
        String balanceString = AmountUtil.transformFormatToString(balance, selectCoinSymbol, AmountUtil.ALLSCALE);
        mBinding.tvBalance.setText(Html.fromHtml(getString(R.string.smart_transfer_balance, balanceString, selectCoinSymbol)));
    }

    @Override
    public void setFee(BigDecimal fee) {
        feeBigDecimal = fee;
        if (smartTransferPresenter.isPrivateWallet() && LocalCoinDBUtils.isBTCChain(selectCoinSymbol)) {
            DecimalFormat df = new DecimalFormat("#######0.#");
            mBinding.tvFee.setText(df.format(fee) + " " + "sat/b");
            return;
        }

        if (smartTransferPresenter.isPrivateWallet()) {
            BigDecimal getLimiteFee = new BigDecimal(WalletHelper.getDeflutGasLimit())                   //limite * gasPrice
                    .multiply(fee);
            Spanned fromSpanned = Html.fromHtml(getString(R.string.smart_transfer_fee, AmountUtil.transformFormatToString(getLimiteFee,
                    selectCoinSymbol, AmountUtil.ALLSCALE), LocalCoinDBUtils.getCoinUnitName(selectCoinSymbol)));
            mBinding.tvFee.setText(fromSpanned);
            return;
        }

        Spanned fromSpanned = Html.fromHtml(getString(R.string.smart_transfer_fee2, AmountUtil.transformFormatToString(fee, selectCoinSymbol, AmountUtil.ALLSCALE), LocalCoinDBUtils.getCoinUnitName(selectCoinSymbol)));
        mBinding.tvFee.setText(fromSpanned);
        return;

    }

    @Override
    public void seekBarChange() {
//        if (LocalCoinDBUtils.isETH(selectCoinSymbol) || LocalCoinDBUtils.isWAN(selectCoinSymbol)) {
//            mBinding.editAmount.setText("");
//        }
    }

    @Override
    public void showPayPwdDialog(boolean isPrivate) {
        if (isPrivate) {
            showPriPwdInputDialog();
        } else {
            showPwdInputDialog();
        }
    }

    @Override
    public void transferSuccess(boolean isPrivate) {
        UITipDialog.showSuccess(this, getString(R.string.transaction_success), dialogInterface -> finish());
    }

    @Override
    public void transferFail(boolean isPrivate) {
        showToast(getString(R.string.smart_transfer_ail));
    }

    @Override
    public void showMessageDialog(String msg) {
        UITipDialog.showInfo(this, msg);
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
    public void onError(String msg, String code) {

    }


    /**
     * 密码输入框
     */
    private void showPwdInputDialog() {
        if (pwdInputDialog != null) pwdInputDialog.dismiss();
        pwdInputDialog = new TextPwdInputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {
                    if (TextUtils.isEmpty(inputMsg)) {
                        UITipDialog.showInfoNoIcon(SmartTransferActivity.this, getStrRes(R.string.trade_code_hint));
                    }


                    String amount = mBinding.editAmount.getText().toString().trim();
                    String googleCode = mBinding.eidtGoogle.getText().toString().trim();

                    smartTransferPresenter.transfer(SmartTransferActivity.this, inputMsg, amount, googleCode);
                })
                .setNegativeBtn(getStrRes(R.string.cancel), null)
                .setContentMsg("");
        pwdInputDialog.getContentView().setText("");
        pwdInputDialog.getContentView().setHint(getStrRes(R.string.trade_code_hint));
        pwdInputDialog.getContentView().setText("");
        pwdInputDialog.show();
    }

    /**
     * 密码输入框
     */
    private void showPriPwdInputDialog() {
        if (pwdInputDialog != null) pwdInputDialog.dismiss();

        pwdInputDialog = new TextPwdInputDialog(this).builder().setTitle(getString(R.string.please_input_transaction_pwd))
                .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {
                    if (TextUtils.isEmpty(inputMsg)) {
                        UITipDialog.showInfoNoIcon(SmartTransferActivity.this, getString(R.string.please_input_transaction_pwd));
                        return;
                    }

                    if (!WalletHelper.checkPasswordByUserId(inputMsg, SPUtilHelper.getUserId())) {  //用户密码输入错误
                        UITipDialog.showInfoNoIcon(this, getString(R.string.transaction_password_error));
                        return;
                    }

                    smartTransferPresenter.transferPrivate(mBinding.editAmount.getText().toString().trim());

                })
                .setNegativeBtn(getStrRes(R.string.cancel), null)
                .setContentMsg("");
        pwdInputDialog.getContentView().setText("");
        pwdInputDialog.getContentView().setHint(getStrRes(R.string.please_input_transaction_pwd));
        pwdInputDialog.getContentView().setText("");
        pwdInputDialog.show();
    }


    /**
     * //TODO 余额+手续费检测
     *
     * @return
     */
    boolean transferInputCheck() {


        try {

            BigInteger amountBigInteger = AmountUtil.bigIntegerFormat(new BigDecimal(mBinding.editAmount.getText().toString().trim()), selectCoinSymbol); //转账数量

            if (amountBigInteger.compareTo(BigInteger.ZERO) == 0 || amountBigInteger.compareTo(BigInteger.ZERO) == -1) {
                UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
                return true;
            }


            BigDecimal allBigInteger = feeBigDecimal.add(new BigDecimal(amountBigInteger));//手续费+转账数量

            int checkInt = allBigInteger.compareTo(balanceBigDecimal); //比较

            if (checkInt == 1 || checkInt == 0) {
                UITipDialog.showInfo(this, getString(R.string.no_balance));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
            return true;
        }
        return false;
    }


}

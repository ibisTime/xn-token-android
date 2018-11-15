package com.cdkj.token.find.product_application.management_money;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.common.ThaAppConstant;
import com.cdkj.token.databinding.ActivityMoneyManagerBuyBinding;
import com.cdkj.token.interfaces.ProductBuyListener;
import com.cdkj.token.interfaces.UserInfoPresenter;
import com.cdkj.token.model.BiJiaBaoAvilModel;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.ManagementMoney;
import com.cdkj.token.model.ProductBuyStep2Model;
import com.cdkj.token.user.WebViewImgBgActivity;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.views.dialogs.MoneyProductBuyStep2Dialog;
import com.cdkj.token.views.dialogs.MoneyProductBuySuccessDialog;
import com.cdkj.token.views.dialogs.UserPayPasswordInputDialog;
import com.cdkj.token.wallet.account_wallet.RechargeAddressQRActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_YMD;
import static java.math.BigDecimal.ROUND_DOWN;

/**
 * 购买
 * Created by cdkj on 2018/9/27.
 */

public class BiJiaBaoBuyActivity extends AbsLoadActivity implements ProductBuyListener {

    private ActivityMoneyManagerBuyBinding mBinding;

    private ManagementMoney mProductModel;
    private BiJiaBaoAvilModel model;

//    private BigDecimal avliAmount;//剩余额度

    private String buyAmount = "";//当前购买份额
    private int buyShare = 1;//当前购买份额

    private int seekBarProgress;

    private UserPayPasswordInputDialog passInputDialog;
    private MoneyProductBuyStep2Dialog moneyProductBuyStep2Dialog;

    private UserInfoPresenter userInfoPresenter;

    // 是否是EditText发起的
    private boolean isSeekBarChange = false;

    public static void open(Context context, ManagementMoney mProductModel) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BiJiaBaoBuyActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, mProductModel);
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
            mProductModel = (ManagementMoney) getIntent().getSerializableExtra(CdRouteHelper.DATASIGN);
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

            if (mProductModel == null) return;

            if (userInfoPresenter != null && !userInfoPresenter.checkPayPwdAndStartSetPage()) {  //判断用户是否设置过支付密码
                return;
            }

            RechargeAddressQRActivity.open(this, mProductModel.getSymbol());
        });

        //减少份额
        mBinding.tvReduce.setOnClickListener(view -> {
            if (buyShare > 1) {
                mBinding.edtBuyAmount.setText((buyShare - 1) + "");
                mBinding.edtBuyAmount.setSelection(mBinding.edtBuyAmount.getText().length());
            }

        });

        //增加份额
        mBinding.tvAdd.setOnClickListener(view -> {
            if (buyShare < model.getMax()){
                mBinding.edtBuyAmount.setText((buyShare + 1) + "");
                mBinding.edtBuyAmount.setSelection(mBinding.edtBuyAmount.getText().length());
            }

        });

        //购买
        mBinding.btnBuy.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtBuyAmount.getText().toString().trim())){
                UITipDialog.showInfo(this, getString(R.string.buyamount_not_empty));
                return;
            }

            if (!mBinding.checkboxRead.isChecked()) {
                UITipDialog.showInfo(this, getString(R.string.user_protocol_hint));
                return;
            }

            if (userInfoPresenter != null && !userInfoPresenter.checkPayPwdAndStartSetPage()) {  //判断用户是否设置过支付密码
                return;
            }

            ProductBuyStep2Model buyStep2Model = new ProductBuyStep2Model();
            buyStep2Model.setProductName(mProductModel.getName());
            buyStep2Model.setBuyAmountString(buyAmount);
            buyStep2Model.setEndTime(DateUtil.formatStringData(mProductModel.getArriveDatetime(), DATE_YMD));
            buyStep2Model.setExpectInComeAmount(getExpectInCome(buyAmount));
            buyStep2Model.setCoinSymbol(mProductModel.getSymbol());

            moneyProductBuyStep2Dialog = new MoneyProductBuyStep2Dialog(this)
                    .setToBuyListener(this)
                    .setProductInfo(buyStep2Model);
            moneyProductBuyStep2Dialog.show();

        });

        mBinding.tvPopProtocol.setOnClickListener(view -> {
            WebViewImgBgActivity.openkey(this, getString(R.string.pop_protocol), ThaAppConstant.getH5UrlLangage(ThaAppConstant.H5_POP_PROTOCOL));
        });

    }

    private String getExpectInCome(String buyAmount){
        //预期收益计算=投资金额*年化收益率/365*投资期限（天）     计算结果先保留4位小数点 最后显示保留2位
        float buyamount = Float.valueOf(buyAmount);

        if (buyamount <= 0) {
            return "0";
        }

        BigDecimal bigDecimal = new BigDecimal(buyamount * mProductModel.getActualYield() / 365);

        buyamount = bigDecimal.setScale(8, ROUND_DOWN).floatValue();

        buyamount = buyamount * mProductModel.getLimitDays();

        DecimalFormat df = new DecimalFormat("#######0.####");

        String showMoney = df.format(buyamount);

        return showMoney;
    }

    private void initSeekBarChangeListener() {

        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                buyShare = i+1;
                setBuyShare(buyShare);

                if (isSeekBarChange){
                    mBinding.edtBuyAmount.setText(buyShare+"");
                    mBinding.edtBuyAmount.setSelection(mBinding.edtBuyAmount.getText().length());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mBinding.edtBuyAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (TextUtils.isEmpty(editable.toString().trim())){
                    return;
                }

                int progress = Integer.parseInt(editable.toString().trim());
                if (progress > model.getMax()){
                    mBinding.edtBuyAmount.setText(model.getMax()+"");
                    return;
                }

                if (progress < model.getMin()){
                    mBinding.edtBuyAmount.setText(model.getMin()+"");
                    return;
                }

                mBinding.seekBar.setProgress(progress-1);
                isSeekBarChange = false;

            }
        });

        mBinding.seekBar.setOnTouchListener((view, motionEvent) -> {
            isSeekBarChange = true;
            return false;
        });

    }

    private void setBuyShare(int quantity){
        buyAmount = AmountUtil.transformFormatToString(new BigDecimal(quantity).multiply(mProductModel.getIncreAmount()), mProductModel.getSymbol(),8);
        mBinding.tvAmount.setText("(" + buyAmount + mProductModel.getSymbol() +")");
    }

    /**
     * 设置显示数据
     */
    private void setShowData() {

        if (mProductModel == null) return;

        BigDecimal avilAmount = BigDecimalUtils.div(mProductModel.getAvilAmount(), mProductModel.getIncreAmount(), 8);//剩余可购额度 / 起购 =剩余份数

        String avilString = AmountUtil.formatCoinAmount(avilAmount);
        mBinding.tvAvilAmount.setText(avilString + getString(R.string.fen));
        mBinding.tvExpectYield.setText(StringUtils.showformatPercentage(mProductModel.getExpectYield()));

        mBinding.tvName.setText(mProductModel.getName());

        mBinding.tvPopProtocol.setText(Html.fromHtml(getString(R.string.read_buy_pop_protocol)));
    }


    /**
     * 获取默认币种余额
     */
    public void getCoinBalance() {

        if (TextUtils.isEmpty(SPUtilHelper.getUserId()) || mProductModel == null || TextUtils.isEmpty(mProductModel.getSymbol())) {
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("currency", mProductModel.getSymbol());
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
                    mBinding.tvCoinBalance.setText(Html.fromHtml(getString(R.string.can_use_balance, amountString, mProductModel.getSymbol())));
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

        if (TextUtils.isEmpty(SPUtilHelper.getUserId()) || mProductModel == null || TextUtils.isEmpty(mProductModel.getCode())) {
            return;
        }

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("productCode", mProductModel.getCode());
        map.put("userId", SPUtilHelper.getUserId());

        Call<BaseResponseModel<BiJiaBaoAvilModel>> call = RetrofitUtils.createApi(MyApi.class).getBuyInterval("625513", StringUtils.getRequestJsonString(map));

        call.enqueue(new BaseResponseModelCallBack<BiJiaBaoAvilModel>(this) {
            @Override
            protected void onSuccess(BiJiaBaoAvilModel data, String SucMessage) {
                model = data;

                // 初始最大最小份数
                mBinding.tvMin.setText(data.getMin() + getString(R.string.fen));
                mBinding.tvMax.setText(data.getMax() + getString(R.string.fen));
                // 初始进度条
                mBinding.seekBar.setProgress(0);
                mBinding.seekBar.setMax(data.getMax()-1);
                // 初始购买金额
                mBinding.edtBuyAmount.setText(model.getMin()+"");
                setBuyShare(buyShare);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }


    @Override
    public void onBuyStep1(ProductBuyStep2Model buyStep2Model) {

    }

    @Override
    public void onBuyStep2(ProductBuyStep2Model buyStep2Model) {
        showPasswordInputDialog(buyShare +"");
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

        if (mProductModel == null || TextUtils.isEmpty(mProductModel.getCode()) || TextUtils.isEmpty(pwd)) {
            return;
        }

        showLoadingDialog();

        Map<String, String> map = new HashMap<>();

        map.put("code", mProductModel.getCode());
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

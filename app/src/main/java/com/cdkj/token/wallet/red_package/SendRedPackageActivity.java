package com.cdkj.token.wallet.red_package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivitySendRedPackageBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.RedPackageEventBusBean;
import com.cdkj.token.model.RedPackageHistoryBean;
import com.cdkj.token.utils.AccountUtil;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * 发送红包
 */
public class SendRedPackageActivity extends AbsLoadActivity {
    ActivitySendRedPackageBinding mBinding;
    private double moneyNumber;//发币数量  最少0.001
    private int sendNumber;//发红包数量
    private int redType = 1;//红包类型 0=定额红包 1=拼手气红包  默认是拼手气红包
    private String currencyType;//币种类型名称
    private InputDialog inputDialog;
    private String mobile;
    private String tradePwd; //用户输入的密码

    private List<CoinModel.AccountListBean> cAccountListBeans;//币种列表
    private OptionsPickerView coinPickerView;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SendRedPackageActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_send_red_package, null, false);

        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.oragne));

        mBinding.etNumber.setEnabled(false);
        mBinding.etSendNumber.setEnabled(false);
        showUIState();
        initOnClick();
        getUserInfoRequest();
        getWalletAssetsData(false);
    }

    private void initOnClick() {
        mBinding.ivBack.setOnClickListener(view -> finish());
        mBinding.tvTopRight.setOnClickListener(view -> RedPackageHistoryActivity.open(this));
        mBinding.btnSubmit.setOnClickListener(v -> {
            checkData();
        });
        mBinding.llType.setOnClickListener(view -> {
        });
        mBinding.tvRedType.setOnClickListener(view -> {

            if (redType == 1) {        //拼手气红包变为普通红包
                redType = 0;
            } else if (redType == 0) {  //普通红包变为拼手气红包
                redType = 1;
            }
            showUIState();

            setTotalNumber();
        });
        mBinding.llType.setOnClickListener(view -> {
            if (cAccountListBeans != null && !cAccountListBeans.isEmpty()) {
                getWalletAssetsData(true);
            } else {
                showCoinSelectPickView();
            }
        });
        //
        mBinding.etSendNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                setTotalNumber();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTotalNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBinding.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                setTotalNumber();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTotalNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 普通红包和拼手气红包状态修改
     */
    private void showUIState() {
        if (redType == 1) {
            mBinding.tvRedType.setText(R.string.red_package_set_ordinary);
            mBinding.etNumber.setHint(R.string.red_package_please_total_number);
            mBinding.tvSendNumTip.setText(R.string.red_package_sub_total);
        } else if (redType == 0) {
            mBinding.tvRedType.setText(R.string.red_package_set_hand);
            mBinding.etNumber.setHint(R.string.red_package_please_sing_number);
            mBinding.tvSendNumTip.setText(R.string.red_package_sub_single);
        }
    }

    /**
     * 设置币种
     */
    private void setTotalNumber() {
        String etNumber = mBinding.etNumber.getText().toString().trim();
        String etSendNumber = mBinding.etSendNumber.getText().toString().trim();

        mBinding.tvSumType.setText(getString(R.string.red_package_unit) + currencyType);

        //拼手气红包  就是输入的币的数量
        mBinding.tvSumNumber.setText(TextUtils.isEmpty(etNumber) ? "0.000" : etNumber);

        if (TextUtils.isEmpty(etNumber) || TextUtils.isEmpty(etSendNumber)) {
            return;
        }

        if (TextUtils.isEmpty(currencyType)) {
            UITipDialog.showFail(SendRedPackageActivity.this, getString(R.string.red_package_please_type));
            return;
        }

        if (redType == 0) {    //普通红包就是输入的币的数量乘以发的包数 （拼手气红包不乘）

            double v1 = Double.parseDouble(etNumber);
            double v2 = Double.parseDouble(etSendNumber);
            if (v1 <= 0 || v2 <= 0) {
                return;
            }
            double total = v1 * v2;
            DecimalFormat df = new DecimalFormat("#######0.###");
            String showMoney = df.format(total);
            mBinding.tvSumNumber.setText(showMoney);

        }
    }

    private void checkData() {
        if (TextUtils.isEmpty(currencyType)) {
            ToastUtil.show(this, getString(R.string.red_package_please_type));
            return;
        }

        String money_et = mBinding.etNumber.getText().toString();
        if (TextUtils.isEmpty(money_et)) {
            ToastUtil.show(this, getString(R.string.red_package_piease_send_number));
            return;
        }
        moneyNumber = Double.parseDouble(money_et);
        if (moneyNumber < 0.001) {
            ToastUtil.show(this, getString(R.string.red_package_min_numer));
            return;
        }

        if (moneyNumber > 1000000) {
            ToastUtil.show(this, getString(R.string.red_package_max_numer));
            return;
        }

        String number_et = mBinding.etSendNumber.getText().toString();
        if (TextUtils.isEmpty(number_et)) {
            ToastUtil.show(this, getString(R.string.red_package_pleas_number));
            return;
        }

        sendNumber = Integer.parseInt(number_et);

        if (sendNumber < 1) {
            ToastUtil.show(this, getString(R.string.red_package_min_number));
            return;
        }

        if (sendNumber > 500) {
            ToastUtil.show(this, getString(R.string.red_package_mxn_number));
            return;
        }

        //去请求数据
        showInputDialog();


    }

    /**
     * 发红包接口
     */
    private void sendDatas() {
        showLoadingDialog();
        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("symbol", currencyType);//币种
        map.put("type", redType + "");
        map.put("count", moneyNumber + "");
        map.put("sendNum", sendNumber + "");
        map.put("greeting", getGreeting());
        map.put("tradePwd", tradePwd);//支付密码
        Call<BaseResponseModel<RedPackageHistoryBean>> baseResponseModelCall = RetrofitUtils.createApi(MyApi.class).sendRedPackage("623000", StringUtils.getJsonToString(map));
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<RedPackageHistoryBean>(this) {
            @Override
            protected void onSuccess(RedPackageHistoryBean data, String SucMessage) {
                //发送成功
                RedPackageShearActivity.open(SendRedPackageActivity.this, data.getCode());
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
//                super.onReqFailure(errorCode, errorMessage);
                UITipDialog.showFail(SendRedPackageActivity.this, errorMessage);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取发送红包参数 （祝福语）
     *
     * @return
     */
    private String getGreeting() {

        if (TextUtils.isEmpty(mBinding.etGreeting.getText().toString().trim())) {
            return mBinding.etGreeting.getHint().toString();
        }

        return mBinding.etGreeting.getText().toString().trim();
    }

    private void showInputDialog() {
        //判断有没有设置过支付密码
        if (!SPUtilHelper.getTradePwdFlag()) {
            showDoubleWarnListen(getString(R.string.red_package_please_set_type), view -> {
                //跳转设置支付界面
                PayPwdModifyActivity.open(this, false, mobile);
            });
            return;
        }
        if (inputDialog == null) {
            inputDialog = new InputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {
                        tradePwd = inputDialog.getContentView().getText().toString().trim();
                        if (TextUtils.isEmpty(tradePwd)) {
                            showToast(getStrRes(R.string.trade_code_hint));
                        } else {
                            //  payRequest(inputDialog.getContentView().getText().toString().trim());
                            //判断密码
                            sendDatas();
                            inputDialog.dismiss();
                        }
                    })
                    .setNegativeBtn(getStrRes(R.string.cancel), null)
                    .setContentMsg("");
            inputDialog.getContentView().setText("");
            inputDialog.getContentView().setHint(getStrRes(R.string.trade_code_hint));
        }
        inputDialog.getContentView().setText("");
        inputDialog.show();
    }

    /**
     * 获取币种
     */
    private void getWalletAssetsData(boolean isShowPickerView) {

        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", "");
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));
        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                cAccountListBeans = data.getAccountList();
                if (isShowPickerView) {
                    showCoinSelectPickView();
                } else {
                    setShowSelectCoinInfo(0); //默认显示第一个币种信息
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 显示选择pickView
     */
    private void showCoinSelectPickView() {
        if (cAccountListBeans == null || cAccountListBeans.size() == 0) {
            UITipDialog.showInfo(SendRedPackageActivity.this, getString(R.string.red_package_empty_type));
            return;
        }
        if (coinPickerView == null) {
            //条件选择器
            coinPickerView = new OptionsPickerBuilder(SendRedPackageActivity.this, new OnOptionsSelectListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    setShowSelectCoinInfo(options1);
                }
            }).setCancelText(getString(R.string.cancel))//取消按钮文字
                    .setSubmitText(getString(R.string.sure))//确认按钮文字

                    .build();
        }


        coinPickerView.setPicker(cAccountListBeans);
        coinPickerView.show();
    }

    void setShowSelectCoinInfo(int options1) {

        if (options1 < 0 || cAccountListBeans == null || options1 > cAccountListBeans.size()) {
            return;
        }

        CoinModel.AccountListBean accountListBean = cAccountListBeans.get(options1);
        String name = accountListBean.getCurrency();
        currencyType = name;
        mBinding.etNumber.setEnabled(true);
        mBinding.etSendNumber.setEnabled(true);
        setTotalNumber();

        mBinding.tvType.setText(name);
        if (!TextUtils.isEmpty(accountListBean.getAmountString()) && !TextUtils.isEmpty(accountListBean.getFrozenAmountString())) {
            BigDecimal amount = new BigDecimal(accountListBean.getAmountString());
            BigDecimal frozenAmount = new BigDecimal(accountListBean.getFrozenAmountString());
            String yue = AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), accountListBean.getCurrency(), 8);
            mBinding.tvBalance.setText(getString(R.string.red_package_have) + ": " + yue);
        }
    }


    /**
     * 获取用户信息
     */
    public void getUserInfoRequest() {

        if (!SPUtilHelper.isLoginNoStart()) {
            setShowData(null);
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {

            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                if (data == null)
                    return;
                SPUtilHelper.saveSecretUserId(data.getSecretUserId());
                SPUtilHelper.saveUserPhoto(data.getPhoto());
                SPUtilHelper.saveUserEmail(data.getEmail());
                SPUtilHelper.saveUserName(data.getNickname());
                SPUtilHelper.saveRealName(data.getRealName());
                SPUtilHelper.saveUserPhoneNum(data.getMobile());
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());
                mobile = data.getMobile();
                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    private void setShowData(UserInfoModel data) {
        if (data == null) {

            mBinding.imgLogo.setImageResource(R.mipmap.default_photo);
            return;
        }
        if (data.getNickname() == null)
            return;
        ImgUtils.loadLogo(this, data.getPhoto(), mBinding.imgLogo);

    }

    @Subscribe()
    public void onMessageEvent(RedPackageEventBusBean event) {
        finish();
    }
}

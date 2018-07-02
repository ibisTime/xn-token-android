package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivitySendRedPackageBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.RedPackageHistoryBean;
import com.cdkj.token.utils.AccountUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 发送红包
 */
public class SendRedPackageActivity extends AbsBaseLoadActivity {
    ActivitySendRedPackageBinding mBinding;
    private double moneyNumber;//发币数量  最少0.001
    private int sendNumber;//发红包数量
    private int redType = 1;//红包类型 0=定额红包 1=拼手气红包  默认是拼手气红包
    private String currencyType;//币种类型名称
    private InputDialog inputDialog;
    boolean isTradepwdFlag = false;//是否设置过支付密码
    private String mobile;

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
        initOnClick();
        getUserInfoRequest();
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
            if (redType == 1) {
                redType = 0;
                mBinding.tvRedType.setText("改为拼手气红包");
            } else if (redType == 0) {
                redType = 1;
                mBinding.tvRedType.setText("改为普通红包");
            }
        });
        mBinding.llType.setOnClickListener(view -> {
            getWalletAssetsData();
        });
    }

    private void checkData() {
        if (TextUtils.isEmpty(currencyType)) {
            ToastUtil.show(this, "请选择代币");
            return;
        }

        String money_et = mBinding.etNumber.getText().toString();
        if (TextUtils.isEmpty(money_et)) {
            ToastUtil.show(this, "请输入要发的数量");
            return;
        }
        moneyNumber = Double.parseDouble(money_et);
        if (moneyNumber < 0.001) {
            ToastUtil.show(this, "数量最小为0.001个");
            return;
        }

        String number_et = mBinding.etSendNumber.getText().toString();
        if (TextUtils.isEmpty(number_et)) {
            ToastUtil.show(this, "请输入要发红包数量");
            return;
        }
        sendNumber = Integer.parseInt(number_et);
        if (sendNumber < 1) {
            ToastUtil.show(this, "红包个数最少为1个");
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
        map.put("greeting", mBinding.etGreeting.getText().toString().trim());
        Call<BaseResponseModel<RedPackageHistoryBean>> baseResponseModelCall = RetrofitUtils.createApi(MyApi.class).sendRedPackage("623000", StringUtils.getJsonToString(map));
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<RedPackageHistoryBean>(this) {
            @Override
            protected void onSuccess(RedPackageHistoryBean data, String SucMessage) {
                //发送成功
                RedPackageShearActivity.open(SendRedPackageActivity.this);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void showInputDialog() {
        //判断有没有设置过支付密码
        if (!isTradepwdFlag) {

            showDoubleWarnListen("请先设置支付密码", view -> {
                //跳转设置支付界面
                PayPwdModifyActivity.open(this, false, mobile);
            });
            return;
        }

        if (inputDialog == null) {
            inputDialog = new InputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                        if (TextUtils.isEmpty(inputDialog.getContentView().getText().toString().trim())) {
                            showToast(getStrRes(R.string.trade_code_hint));
                        } else {
                            //  payRequest(inputDialog.getContentView().getText().toString().trim());
                            //判断密码
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
    private void getWalletAssetsData() {

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

                if (data.getAccountList() == null || data.getAccountList().size() == 0) {
                    UITipDialog.showInfo(SendRedPackageActivity.this, "暂无可用币种");
                    return;
                }

                //条件选择器
                OptionsPickerView pvOptions = new OptionsPickerBuilder(SendRedPackageActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        CoinModel.AccountListBean accountListBean = data.getAccountList().get(options1);
                        String name = accountListBean.getCurrency();
                        currencyType = name;
//                        accountListBean.get
                        mBinding.tvType.setText(name);
                        if (!TextUtils.isEmpty(accountListBean.getAmountString()) && !TextUtils.isEmpty(accountListBean.getFrozenAmountString())) {
                            BigDecimal amount = new BigDecimal(accountListBean.getAmountString());
                            BigDecimal frozenAmount = new BigDecimal(accountListBean.getFrozenAmountString());
                            String yue = AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), accountListBean.getCurrency(), 8);
                            mBinding.tvBalance.setText("持有" + name + "总量" + yue);
                        }
                    }
                }).build();

                pvOptions.setPicker(data.getAccountList());
                pvOptions.show();

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
                isTradepwdFlag = data.isTradepwdFlag();
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
        ImgUtils.loadAvatar(this, data.getPhoto(), mBinding.imgLogo);

    }
}

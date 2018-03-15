package com.cdkj.token.consult;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.dialog.InputDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.SystemUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.baselibrary.views.MyPickerPopupWindow;
import com.cdkj.token.R;
import com.cdkj.token.Util.AccountUtil;
import com.cdkj.token.Util.EditTextJudgeNumberWatcher;
import com.cdkj.token.Util.ResponseUtil;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityStorePayBinding;
import com.cdkj.token.databinding.ActivityWithdrawBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.wallet.account.WithdrawActivity;
import com.cdkj.token.wallet.account.WithdrawOrderActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.SystemUtils.paste;
import static com.cdkj.token.Util.AccountUtil.OGCSCALE;
import static com.cdkj.token.Util.AccountUtil.getUnit;

/**
 * 店鋪支付
 * Created by lei on 2017/10/18.
 */

public class StorePayActivity extends AbsBaseActivity {


    private ActivityStorePayBinding mBinding;

    private boolean mTradepwdFlag; //用户是否设置了支付密码

    private InputDialog inputDialog;

    private String mStoreCode;

    /**
     * @param context
     * @param storeCode 店铺编号
     */
    public static void open(Context context, String storeCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, StorePayActivity.class);
        intent.putExtra("storeCode", storeCode);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_store_pay, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.pay_confirm));

        setSubLeftImgState(true);

        if (getIntent() != null) {
            mStoreCode = getIntent().getStringExtra("storeCode");
        }

        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount, 8, 8));

        initListener();

        getAmount();

    }

    private void initListener() {

        mBinding.btnWithdraw.setOnClickListener(v -> {

            if (TextUtils.isEmpty(mBinding.edtAmount.getText().toString().trim())) {
                ToastUtil.show(this, getStrRes(R.string.wallet_withdraw_amount_hint));
                return;
            }

            if (StringUtils.parseDouble(mBinding.edtAmount.getText().toString().trim()) <= 0) {
                ToastUtil.show(this, getStrRes(R.string.wallet_withdraw_amount_hint));
                return;
            }

            getUserInfoRequest();

        });
    }


    /**
     * 获取用户信息 判断用户是否设置过支付密码
     */
    public void getUserInfoRequest() {

        if (mTradepwdFlag) {
            showInputDialog();
            return;
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                if (data == null) return;

                mTradepwdFlag = data.isTradepwdFlag();

                if (mTradepwdFlag) {  //如果设置了支付密码
                    showInputDialog();
                } else {
                    showDoubleWarnListen("您还未设置资金密码，请先设置资金密码！", view -> {
                        PayPwdModifyActivity.open(StorePayActivity.this, false, data.getMobile());
                    });
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void showInputDialog() {
        if (inputDialog == null) {
            inputDialog = new InputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                        if (TextUtils.isEmpty(inputDialog.getContentView().getText().toString().trim())) {
                            showToast(getStrRes(R.string.trade_code_hint));
                        } else {
                            payReuest(inputDialog.getContentView().getText().toString().trim());
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
     * 获取金额
     */
    public void getAmount() {

        if (TextUtils.isEmpty(SPUtilHelper.getUserToken()))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", AccountUtil.OGC);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {

            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {

                if (data == null || data.getAccountList() == null || data.getAccountList().isEmpty())
                    return;
                CoinModel.AccountListBean accountListBean = data.getAccountList().get(0);

                if (accountListBean == null || TextUtils.isEmpty(accountListBean.getAmountString()) || TextUtils.isEmpty(accountListBean.getFrozenAmountString()))
                    return;

                //总金额 - 冻结金额 =可用金额
                mBinding.tvBalance.setText(AccountUtil.amountFormatUnitForShow(BigDecimalUtils.subtract(new BigDecimal(accountListBean.getAmountString()), new BigDecimal(accountListBean.getFrozenAmountString())), AccountUtil.OGC, OGCSCALE));

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    public void payReuest(String pwd) {

        if (TextUtils.isEmpty(mStoreCode)) return;

        Map<String, String> map = new HashMap<>();
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtAmount.getText().toString().trim());
        map.put("toStore", mStoreCode);
        map.put("token", SPUtilHelper.getUserToken());
        map.put("tradePwd", pwd);
        map.put("transAmount", bigDecimal.multiply(getUnit(AccountUtil.OGC)).toPlainString().split("\\.")[0]);

        Call call = RetrofitUtils.getBaseAPiService().stringRequest("625340", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<String>(this) {
            @Override
            protected void onSuccess(String data, String SucMessage) {
                showToast(getString(R.string.pay_succ));
                finish();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

};



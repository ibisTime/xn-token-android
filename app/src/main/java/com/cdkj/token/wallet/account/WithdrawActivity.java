package com.cdkj.token.wallet.account;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.NumberPwdInputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityWithdrawBinding;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.AccountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.AccountUtil.getLocalCoinUnit;

/**
 * 提币
 * <p>
 * Created by lei on 2017/10/18.
 */

public class WithdrawActivity extends AbsLoadActivity {

    private WalletBalanceModel model;
    private PermissionHelper permissionHelper;

    private NumberPwdInputDialog inputDialog;
    private ActivityWithdrawBinding mBinding;


    // 是否需要交易密码和谷歌验证 认证账户不需要交易密码和谷歌验证
    private boolean isCerti = true;

    public static void open(Context context, WalletBalanceModel model) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, WithdrawActivity.class).putExtra(CdRouteHelper.DATASIGN, model));
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_withdraw, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void topTitleViewRightClick() {
        if (model == null) return;
        WithdrawOrderActivity.open(this, model.getCoinName());
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.wallet_title_withdraw));

        mBaseBinding.titleView.setRightTitle(getString(R.string.wallet_charge_recode));


        init();

        initListener();

        getUserInfoRequest();

//        getWithdrawFee();
    }

    private void init() {

        if (getIntent() == null) {
            return;
        }

        model = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);

        if (model == null) {
            return;
        }
        mBinding.tvBalance.setText(AccountUtil.sub(Double.parseDouble(model.getAmountString()),
                Double.parseDouble(model.getFrozenAmountString()), model.getCoinName()));
        mBinding.tvFee.setText(model.getCoinName());

        if (model.getCoinBalance() != null)
            mBinding.edtAmount.setHint(getString(R.string.wallet_withdraw_amount_hint2) + AccountUtil.sub(Double.parseDouble(model.getCoinBalance()),
                    Double.parseDouble(model.getFrozenAmountString()), model.getCoinName()));

        // 设置提现手续费
        mBinding.edtCommission.setText(AccountUtil.getWithdrawFee(model.getCoinName()));

    }

    private void initListener() {

        //二维码
        mBinding.fraLayoutQRcode.setOnClickListener(view -> {
            QRscan();
        });

        mBinding.btnWithdraw.setOnClickListener(view -> {

            if (!check()) {
                return;
            }

//            if (isCerti) {
//                withdrawal("");
//                return;
//            }

            if (SPUtilHelper.getTradePwdFlag()) {
                showInputDialog();
            } else {
                UITipDialog.showInfo(WithdrawActivity.this, getString(R.string.please_set_account_money_password), new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        PayPwdModifyActivity.open(WithdrawActivity.this, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
                    }
                });
            }


        });


        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount, 8, 8));
    }

    //权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.editToAddress.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(WithdrawActivity.this, getStrRes(R.string.user_address_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtAmount.getText().toString().trim())) {
            UITipDialog.showInfoNoIcon(WithdrawActivity.this, getStrRes(R.string.wallet_withdraw_amount_hint));
            return false;
        }

        if (isCerti) {
            if (SPUtilHelper.getGoogleAuthFlag() && TextUtils.isEmpty(mBinding.editGoogleCode.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getStrRes(R.string.google_code_hint));
                mBinding.linLayoutGoogle.setVisibility(View.VISIBLE);
                mBinding.viewGoogle.setVisibility(View.VISIBLE);
                return false;
            }
        }

        return true;
    }

    private void showInputDialog() {
        if (inputDialog == null) {
            inputDialog = new NumberPwdInputDialog(this).builder().setTitle(getStrRes(R.string.trade_code_hint))
                    .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                        if (inputDialog.getContentView().getText().toString().trim().equals("")) {
                            UITipDialog.showInfoNoIcon(WithdrawActivity.this, getStrRes(R.string.trade_code_hint));
                        } else {
                            withdrawal(inputDialog.getContentView().getText().toString().trim());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 100) {
            //处理扫描结果（在界面上显示）
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!TextUtils.isEmpty(result))
                        mBinding.editToAddress.setText(result);

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WithdrawActivity.this, getStrRes(R.string.qrcode_parsing_failure), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void getWithdrawFee() {
        Map<String, String> map = new HashMap<>();

        map.put("ckey", "withdraw_fee_" + model.getCoinName().toLowerCase());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.edtCommission.setText(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    /**
     * 提现
     *
     * @param tradePwd
     */
    private void withdrawal(String tradePwd) {
        BigDecimal bigDecimal = new BigDecimal(mBinding.edtAmount.getText().toString().trim());

        Map<String, String> map = new HashMap<>();

        map.put("googleCaptcha", mBinding.editGoogleCode.getText().toString());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("applyUser", SPUtilHelper.getUserId());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("accountNumber", model.getAccountNumber());
        map.put("amount", bigDecimal.multiply(getLocalCoinUnit(model.getCoinName())).toString().split("\\.")[0]);
        map.put("payCardNo", mBinding.editToAddress.getText().toString().trim());
        map.put("payCardInfo", model.getCoinName());
        map.put("applyNote", model.getCoinName() + getString(R.string.bill_type_withdraw));
        map.put("tradePwd", tradePwd);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("802750", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                UITipDialog.showSuccess(WithdrawActivity.this, getStrRes(R.string.wallet_withdraw_success), dialogInterface -> finish());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 二维码扫描
     */
    private void QRscan() {
        permissionHelper = new PermissionHelper(this);

        permissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(WithdrawActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 100);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getStrRes(R.string.camera_refused));
            }
        }, Manifest.permission.CAMERA);
    }


    /**
     * 获取用户信息 获取用户是否设置了支付密码
     */
    public void getUserInfoRequest() {

        if (!SPUtilHelper.isLoginNoStart()) {

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
                if (data == null)
                    return;
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveGoogleAuthFlag(data.isGoogleAuthFlag());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    @Subscribe
    public void setAddress(EventBusModel model) {

        if (model == null)
            return;

        if (model.getTag().equals(EventTags.ADDRESS_SELECT)) {
            if (model.getEvInt() == 0) { // 地址未认证，需要交易密码
                isCerti = true;
            } else { // 地址已认证，不需要交易密码
                isCerti = false;
            }
            mBinding.editToAddress.setText(model.getEvInfo());
        }

    }
}

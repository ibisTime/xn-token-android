package com.cdkj.token.wallet.private_wallet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.TextPwdInputDialog;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.BigDecimalUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityTransferBinding;
import com.cdkj.token.model.CionAddressType;
import com.cdkj.token.model.GasPrice;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.AmountUtil;
import com.cdkj.token.utils.EditTextJudgeNumberWatcher;
import com.cdkj.token.utils.LocalCoinDBUtils;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.web3j.crypto.WalletUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

import static com.cdkj.token.utils.AmountUtil.ALLSCALE;
import static com.cdkj.token.utils.LocalCoinDBUtils.getCoinUnitName;

/**
 * 钱包转账 (BTC WAN)
 * Created by cdkj on 2018/6/8.
 */

public class WalletTransferActivity extends AbsLoadActivity {

    private ActivityTransferBinding mBinding;

    private final int CODEPERSE = 101;


    private BigInteger mGasPrice;//获取的燃料单位费用
    private BigInteger transferGasPrice;//计算后转账矿工费用

    private WalletBalanceModel accountListBean;

    private PermissionHelper mPermissionHelper;

    private TextPwdInputDialog passWordInputDialog;

    private boolean isContractAddress = false;//是否是  合约地址 仅限于  wan 和 eth使用

    //需要的权限
    private String[] needLocationPermissions = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void open(Context context, WalletBalanceModel accountListBean) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletTransferActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, accountListBean);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_transfer, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setStatusBarBlue();
        setTitleBgBlue();

        accountListBean = getIntent().getParcelableExtra(CdRouteHelper.DATASIGN);
        mPermissionHelper = new PermissionHelper(this);
        if (accountListBean != null && !TextUtils.isEmpty(accountListBean.getCoinBalance())) {
            mBinding.tvCurrency.setText(AmountUtil.transformFormatToString(new BigDecimal(accountListBean.getCoinBalance()), accountListBean.getCoinSymbol(), ALLSCALE) + " " + accountListBean.getCoinSymbol());
            mBaseBinding.titleView.setMidTitle(accountListBean.getCoinSymbol());
        }

        getGasPriceValue();

        mBaseBinding.titleView.setMidTitle(R.string.transfer);
        mBinding.edtAmount.addTextChangedListener(new EditTextJudgeNumberWatcher(mBinding.edtAmount, 15, 8));

        initClickListener();

    }

    /**
     * 转账确认地址类型
     */
    private void transferInputCheckAddressType() {
        showLoadingDialog();
        HashMap<String, String> map = new HashMap<>();
        map.put("address", mBinding.editToAddress.getText().toString());
        map.put("symbol", accountListBean.getCoinSymbol());

        Call<BaseResponseModel<CionAddressType>> baseResponseModelCall = RetrofitUtils.createApi(MyApi.class).transferInputCheckAddressType("802108", StringUtils.getRequestJsonString(map));
        addCall(baseResponseModelCall);
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<CionAddressType>(this) {
            @Override
            protected void onSuccess(CionAddressType data, String SucMessage) {
                isContractAddress = data.isIsSuccess();
                showPasswordInputDialog();
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }

    /**
     * 转账输入状态检测
     *
     * @return
     */
    boolean transferInputCheck() {
        if (TextUtils.isEmpty(mBinding.editToAddress.getText().toString().trim())) {
            UITipDialog.showInfo(this, getString(R.string.please_to_address));
            return true;
        }

        if (!WalletUtils.isValidAddress(mBinding.editToAddress.getText().toString().trim())) {
            UITipDialog.showInfo(this, getStrRes(R.string.error_wallet_address));
            return true;
        }

        if (isSameAddressByCoin()) {

            return true;
        }

        if (TextUtils.isEmpty(mBinding.edtAmount.getText().toString().trim())) {
            UITipDialog.showInfo(this, getString(R.string.please_input_transaction_number));
            return true;
        }

        try {

            if (accountListBean == null || TextUtils.isEmpty(accountListBean.getCoinBalance())) {
                UITipDialog.showInfo(this, getStrRes(R.string.no_balance));
                return true;
            }

            BigInteger amountBigInteger = AmountUtil.bigIntegerFormat(new BigDecimal(mBinding.edtAmount.getText().toString().trim()), accountListBean.getCoinSymbol()); //转账数量

            if (amountBigInteger.compareTo(BigInteger.ZERO) == 0 || amountBigInteger.compareTo(BigInteger.ZERO) == -1) {
                UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
                return true;
            }

            if (transferGasPrice == null) return true;

            if (!LocalCoinDBUtils.isTokenCoinBySymbol(accountListBean.getCoinSymbol())) { //token币不进行手续费校验

                BigInteger allBigInteger = WalletHelper.getDeflutGasLimit().multiply(transferGasPrice).add(amountBigInteger);//手续费+转账数量

                int checkInt = allBigInteger.compareTo(new BigDecimal(accountListBean.getCoinBalance()).toBigInteger()); //比较

                if (checkInt == 1) {
                    UITipDialog.showInfo(this, getString(R.string.no_balance));
                    return true;
                }

            } else {

                int checkInt = amountBigInteger.compareTo(new BigDecimal(accountListBean.getCoinBalance()).toBigInteger());

                if (checkInt == 1) {
                    UITipDialog.showInfo(this, getString(R.string.no_balance));
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            UITipDialog.showInfo(this, getString(R.string.please_correct_transaction_number));
            return true;
        }
        return false;
    }

    /**
     * 根据币种判断用户to 地址 和from地址是否相同 相同不允许转账
     *
     * @return
     */
    private boolean isSameAddressByCoin() {

        if (accountListBean == null) return false;

        WalletDBModel walletDBModel = WalletHelper.getUserWalletInfoByUserId(WalletHelper.WALLET_USER);

        String toAddress = mBinding.editToAddress.getText().toString();

        //币种类型
        String coinType = LocalCoinDBUtils.getLocalCoinType(accountListBean.getCoinSymbol());

        //Wan及Wantoken币
        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_WAN)
                || LocalCoinDBUtils.isWanTokenCoin(coinType)) {

            return TextUtils.equals(walletDBModel.getWanAddress(), toAddress);
        }

        //ETH及ETHtoken币
        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_ETH)
                || LocalCoinDBUtils.isEthTokenCoin(coinType)) {
            return TextUtils.equals(walletDBModel.getEthAddress(), toAddress);
        }

        return false;
    }


    /**
     * 相机权限请求
     */
    private void permissionRequest() {
        mPermissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {
                Intent intent = new Intent(WalletTransferActivity.this, CaptureActivity.class);
                startActivityForResult(intent, CODEPERSE);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getStrRes(R.string.no_camera_permission));
            }
        }, needLocationPermissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 转账操作
     */
    private void transfer() {
        showLoadingDialog();
        mSubscription.add(Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .map(s -> transferByCoin())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> disMissLoadingDialog())
                .subscribe(s -> {

                    if (TextUtils.isEmpty(s)) {
                        UITipDialog.showFail(WalletTransferActivity.this, getString(R.string.transfer_fail));
                        return;
                    }

                    LogUtil.E("交易hash" + s);

                    UITipDialog.showSuccess(WalletTransferActivity.this, getString(R.string.transaction_success), dialogInterface -> finish());

                }, throwable -> {
                    UITipDialog.showFail(WalletTransferActivity.this, getString(R.string.transfer_fail));
                    LogUtil.E("has————" + throwable);
                }));
    }

    /**
     * 根据币种类型进行转账操作
     *
     * @return
     * @throws Exception
     */
    private String transferByCoin() throws Exception {

        WalletDBModel w = WalletHelper.getUserWalletInfoByUserId(WalletHelper.WALLET_USER);

        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_WAN)) {   //TODO 转账地址优化
            if (isContractAddress) {
                LogUtil.E("pppppp 合约wan单位: " + BigInteger.valueOf(210000) + " 手续费: " + transferGasPrice);
                return WalletHelper.transferForWan(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), BigInteger.valueOf(210000), transferGasPrice);
            } else {
                LogUtil.E("pppppp 普通wan单位: " + WalletHelper.getDeflutGasLimit() + " 手续费: " + transferGasPrice);
                return WalletHelper.transferForWan(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), WalletHelper.getDeflutGasLimit(), transferGasPrice);
            }
        }

        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_ETH)) {
            if (isContractAddress) {
                LogUtil.E("pppppp 合约ETH单位: " + BigInteger.valueOf(210000) + " 手续费: " + transferGasPrice);
                return WalletHelper.transferForEth(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), BigInteger.valueOf(210000), transferGasPrice);
            } else {
                LogUtil.E("pppppp 普通ETH单位: " + WalletHelper.getDeflutGasLimit() + " 手续费: " + transferGasPrice);
                return WalletHelper.transferForEth(w, mBinding.editToAddress.getText().toString(), mBinding.edtAmount.getText().toString().trim(), WalletHelper.getDeflutGasLimit(), transferGasPrice);
            }
        }

        //币种类型
        String coinType = LocalCoinDBUtils.getLocalCoinType(accountListBean.getCoinSymbol());
        //合约地址
        String contractAddress = LocalCoinDBUtils.getLocalCoinContractAddress(accountListBean.getCoinSymbol());

        if (LocalCoinDBUtils.isEthTokenCoin(coinType)) {
            return WalletHelper.transferForEthTokenCoin(w, mBinding.editToAddress.getText().toString(), WalletHelper.getUnitAmountValue(mBinding.edtAmount.getText().toString(), accountListBean.getCoinSymbol()), contractAddress, transferGasPrice);
        }

        if (LocalCoinDBUtils.isWanTokenCoin(coinType)) {
            return WalletHelper.transferForWanTokenCoin(w, mBinding.editToAddress.getText().toString(), WalletHelper.getUnitAmountValue(mBinding.edtAmount.getText().toString(), accountListBean.getCoinSymbol()), contractAddress, transferGasPrice);
        }

        return "";
    }

    /**
     * 获取燃料费用
     */
    private void getGasPriceValue() {
        if (accountListBean == null) {
            return;
        }
        showLoadingDialog();

        Call<BaseResponseModel<GasPrice>> call = null;

        if (canGetETHGasPrice()) {
            call = RetrofitUtils.createApi(MyApi.class).getGasPrice("802117", "{}");  //ETH gas
        } else {
            call = RetrofitUtils.createApi(MyApi.class).getGasPrice("802358", "{}");
        }
        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<GasPrice>(this) {
            @Override
            protected void onSuccess(GasPrice gasPrice, String SucMessage) {
                mGasPrice = gasPrice.getGasPrice();
                transferGasPrice = gasPrice.getGasPrice();
                setShowGasPrice(mGasPrice);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    /**
     * 能否获取ETH网络Gas
     *
     * @return
     */
    private boolean canGetETHGasPrice() {

        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_ETH)) {
            return true;
        }

        if (LocalCoinDBUtils.isEthTokenCoinByName(accountListBean.getCoinSymbol())) {
            return true;
        }

        return false;
    }


    /**
     * 显示密码输入框
     */
    private void showPasswordInputDialog() {

        passWordInputDialog = new TextPwdInputDialog(this).builder().setTitle(getStrRes(R.string.please_input_transaction_pwd))
                .setPositiveBtn(getStrRes(R.string.confirm), (view, inputMsg) -> {

                    String tradePwd = passWordInputDialog.getContentView().getText().toString().trim();

                    if (TextUtils.isEmpty(tradePwd)) {
                        UITipDialog.showInfoNoIcon(WalletTransferActivity.this, getString(R.string.please_input_transaction_pwd));
                        return;
                    }

                    if (!WalletHelper.checkPasswordByUserId(tradePwd, WalletHelper.WALLET_USER)) {
                        UITipDialog.showInfoNoIcon(this, getString(R.string.transaction_password_error));
                        return;
                    }

                    transfer();
                })
                .setNegativeBtn(getStrRes(R.string.cancel), null)
                .setContentMsg("");

        passWordInputDialog.getContentView().setText("");
        passWordInputDialog.getContentView().setHint(getStrRes(R.string.please_input_transaction_pwd));
        passWordInputDialog.getContentView().setText("");
        passWordInputDialog.show();
    }


    /**
     * 设置矿工费显示
     */
    private void setShowGasPrice(BigInteger gasPrice) {
        if (accountListBean == null || gasPrice == null) return;

        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_WAN) || LocalCoinDBUtils.isWanTokenCoin(LocalCoinDBUtils.getLocalCoinType(accountListBean.getCoinSymbol()))) {
            //如果是 wan或者wantoken币  显示的时候单位变化一下
            BigDecimal serverMoney = BigDecimalUtils.div(new BigDecimal(gasPrice), new BigDecimal(1000000000), 4);
            mBinding.tvGas.setText(serverMoney + " Gwin");

        } else if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_ETH) || LocalCoinDBUtils.isEthTokenCoin(LocalCoinDBUtils.getLocalCoinType(accountListBean.getCoinSymbol()))) {
            //如果是 eth或者ethtoken币  显示的时候单位变化一下
            BigDecimal serverMoney = BigDecimalUtils.div(new BigDecimal(gasPrice), new BigDecimal(1000000000), 4);
            mBinding.tvGas.setText(serverMoney + " Gwei");
        } else {
            mBinding.tvGas.setText(
                    AmountUtil.transformFormatToString(new BigDecimal(WalletHelper.getDeflutGasLimit())                   //limite * gasPrice
                            .multiply(new BigDecimal(gasPrice)), accountListBean.getCoinSymbol(), ALLSCALE) + " " + getCoinUnitName(accountListBean.getCoinSymbol()));

        }

    }


    private void initClickListener() {
        //扫码
        mBinding.fraLayoutQRcode.setOnClickListener(view -> {
            permissionRequest();
        });

        mBinding.btnNext.setOnClickListener(view -> {
            if (transferInputCheck()) return;

            if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_WAN) || TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_ETH)) {
                transferInputCheckAddressType();
            } else {
                showPasswordInputDialog();
            }

        });

        //矿工费滑动设置显示
        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setGaspriceBySeekBarChange(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    /**
     * 根据seekBar滑动计算gasPrice
     *
     * @param i
     */
    void setGaspriceBySeekBarChange(int i) {
        if (mGasPrice == null) return;
        BigDecimal minPrice;
        BigDecimal maxPrice;
        if (TextUtils.equals(accountListBean.getCoinSymbol(), WalletHelper.COIN_WAN)) {
            //如果是wan的话 最小值为181  最大值为  返回的值减去181+返回的值
            minPrice = new BigDecimal("181000000000");
            maxPrice = new BigDecimal(mGasPrice.subtract(new BigInteger("181000000000")).add(mGasPrice));
        } else {
            minPrice = new BigDecimal(mGasPrice).multiply(new BigDecimal(0.85));//最小矿工费  最大最小是GasPrice上下浮动15%
            maxPrice = new BigDecimal(mGasPrice).multiply(new BigDecimal(1.15)); //最大矿工费

        }
        float Progress = i / 100f;

        BigDecimal ProgressBigDecimal = new BigDecimal(Progress);

        BigDecimal lilmit = maxPrice.subtract(minPrice).multiply(ProgressBigDecimal);

        transferGasPrice = ((lilmit.add(minPrice)).toBigInteger());
        LogUtil.E("pppppp 滑动后的手续费值为: " + transferGasPrice);
        setShowGasPrice(transferGasPrice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /**
         * 处理二维码扫描结果
         */
        if (requestCode == CODEPERSE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (WalletUtils.isValidAddress(result)) {
                        mBinding.editToAddress.setText(result);
                    } else {
                        Toast.makeText(WalletTransferActivity.this, R.string.error_wallet_address, Toast.LENGTH_LONG).show();
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(WalletTransferActivity.this, R.string.resolve_wallet_address_fail, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

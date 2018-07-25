package com.cdkj.token.wallet.account;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityAddressQrimgShowBinding;
import com.cdkj.token.model.CoinModel;
import com.cdkj.token.model.WalletBalanceModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class RechargeAddressQRActivity extends AbsLoadActivity {

    private ActivityAddressQrimgShowBinding mBinding;

    /**
     * @param context
     * @param coinType 币种类型
     */
    public static void open(Context context, String coinType) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, RechargeAddressQRActivity.class)
                .putExtra(CdRouteHelper.DATASIGN, coinType));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_address_qrimg_show, null, false);
        return mBinding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(getStrRes(R.string.wallet_title_charge));

        if (getIntent() != null) {
            getAddressByType(getIntent().getStringExtra(CdRouteHelper.DATASIGN));
        }
        initListener();
    }


    private void initQRCodeAndAddress(String address) {
        Bitmap mBitmap = CodeUtils.createImage(address, 400, 400, null);
        mBinding.imgQRCode.setImageBitmap(mBitmap);
        mBinding.txtAddress.setText(address);

    }

    private void initListener() {

        mBinding.btnCopy.setOnClickListener(view -> {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(mBinding.txtAddress.getText().toString()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
            UITipDialog.showInfoNoIcon(RechargeAddressQRActivity.this, getStrRes(R.string.wallet_charge_address_copy_success));
        });
    }


    /**
     * 获取币种地址
     *
     * @param
     */
    private void getAddressByType(String type) {

        if (TextUtils.isEmpty(SPUtilHelper.getUserId()) || TextUtils.isEmpty(type))
            return;

        Map<String, Object> map = new HashMap<>();
        map.put("currency", type);
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getAccount("802503", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<CoinModel>(this) {
            @Override
            protected void onSuccess(CoinModel data, String SucMessage) {
                if (data.getAccountList() == null || data.getAccountList().size() == 0) {
                    return;
                }
                initQRCodeAndAddress(data.getAccountList().get(0).getCoinAddress());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

}

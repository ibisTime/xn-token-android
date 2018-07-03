package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityQrredPackageImgBinding;
import com.cdkj.token.model.RedPackageEventBusBean;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;
import org.spongycastle.asn1.esf.SPuri;

public class QRRedPackageImgActivity extends AbsBaseLoadActivity {

    ActivityQrredPackageImgBinding mBinding;
    private String redPackageCode;

    public static void open(Context context, String redPackageCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QRRedPackageImgActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, redPackageCode);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_qrred_package_img, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            redPackageCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }
        mBinding.llOther.setOnClickListener(view -> {
            EventBus.getDefault().post(new RedPackageEventBusBean());
            finish();
        });
//        http://m.thadev.hichengdai.com/redPacket/receive.html?code=RP201806292144024227306&inviteCode=11&lang=cn

        String uri = "http://m.thadev.hichengdai.com/redPacket/receive.html";
        uri += "?code=" + redPackageCode;//红包码
        uri += "&inviteCode=" + SPUtilHelper.getSecretUserId();//

        if (TextUtils.equals(SPUtilHelper.getLanguage(), ENGLISH)) {
            uri += "&lang=en";//国际化
        } else {
            uri += "&lang=cn";
        }

        Bitmap bitmap = CodeUtils.createImage(uri, 500, 500, null);
        mBinding.ivQrImg.setImageBitmap(bitmap);
        UITipDialog.showSuccess(this, getString(R.string.red_package_send_success));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //屏蔽按键
        return true;
    }
}

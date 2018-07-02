package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityQrredPackageImgBinding;
import com.cdkj.token.model.RedPackageEventBusBean;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

public class QRRedPackageImgActivity extends AbsBaseLoadActivity {

    ActivityQrredPackageImgBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QRRedPackageImgActivity.class);
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
        mBinding.llOther.setOnClickListener(view -> {
            EventBus.getDefault().post(new RedPackageEventBusBean());
            finish();
        });

        Bitmap bitmap = CodeUtils.createImage("adad", 500, 500, null);
        mBinding.ivQrImg.setImageBitmap(bitmap);
        UITipDialog.showSuccess(this, getString(R.string.red_package_send_success));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //屏蔽按键
        return true;
    }
}

package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityQrredPackageImgBinding;

/**
 * 谷歌设置修改
 * Created by cdkj on 2018/7/24.
 */

public class GoogleCodeSetActivity extends AbsLoadActivity {

    private ActivityQrredPackageImgBinding popupBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, GoogleCodeSetActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    @Override
    public View addMainView() {
        popupBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_qrred_package_img, null, false);
        return popupBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
//        popupBinding.tvCancel.setOnClickListener(v -> {
//            finish();
//        });
//
//        popupBinding.tvClose.setOnClickListener(v -> {
//            UserGoogleActivity.open(this, "close");
//            finish();
//        });
//
//        popupBinding.tvModify.setOnClickListener(v -> {
//            UserGoogleActivity.open(this, "modify");
//            finish();
//        });
    }
}

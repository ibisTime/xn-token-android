package com.cdkj.token.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.PopupGoogleBinding;

/**
 * 谷歌设置修改
 * Created by cdkj on 2018/7/24.
 */

public class GoogleCodeSetActivity extends BaseActivity {

    private PopupGoogleBinding popupBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, GoogleCodeSetActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIStatusBarHelper.translucent(this, ContextCompat.getColor(this, R.color.transparent));

        popupBinding = DataBindingUtil.setContentView(this, R.layout.popup_google);

        initListener();

    }

    private void initListener() {
        popupBinding.tvCancel.setOnClickListener(v -> {
            finish();
        });

        popupBinding.tvClose.setOnClickListener(v -> {
            UserGoogleActivity.open(this, "close");
            finish();
        });

        popupBinding.tvModify.setOnClickListener(v -> {
            UserGoogleActivity.open(this, "modify");
            finish();
        });
    }

}

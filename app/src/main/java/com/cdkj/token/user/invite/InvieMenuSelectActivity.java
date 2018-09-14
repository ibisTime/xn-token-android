package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySelectMenuBinding;

/**
 * Created by cdkj on 2018/9/14.
 */

public class InvieMenuSelectActivity extends BaseActivity {


    private ActivitySelectMenuBinding menuBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InvieMenuSelectActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_menu);

        menuBinding.tvNext.setOnClickListener(view -> {
            InviteRuleActivity.open(this);
            finish();
        });

        menuBinding.emptyView.setOnClickListener(view -> finish());

    }
}

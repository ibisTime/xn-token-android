package com.cdkj.token.user.invite;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteCopyDialogBinding;

/**
 * 复制dialog显示
 * Created by cdkj on 2018/9/17.
 */

public class CopyActivity extends BaseActivity {

    private ActivityInviteCopyDialogBinding dialogBinding;

    /**
     * @param context
     * @param url
     */
    public static void open(Context context, String url) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, CopyActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, url);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogBinding = DataBindingUtil.setContentView(this, R.layout.activity_invite_copy_dialog);

        dialogBinding.linLayoutRoot.setOnClickListener(view -> finish());

        dialogBinding.linLayoutBg.setOnClickListener(view -> {});

        dialogBinding.tvUrl.setText(getIntent().getStringExtra(CdRouteHelper.DATASIGN));

        dialogBinding.tvCopy.setOnClickListener(view -> {
            ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(null, dialogBinding.tvText.getText().toString() + dialogBinding.tvUrl.getText().toString());
            cmb.setPrimaryClip(clipData);
            UITipDialog.showInfoNoIcon(CopyActivity.this, getStrRes(R.string.copy_success));
        });


    }

}

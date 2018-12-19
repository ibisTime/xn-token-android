package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogBackupDontScreensBinding;

/**
 * 隐私服务协议及条款
 * Created by cdkj on 2018/7/20.
 */

public class PrivateAgreementDialog extends Dialog {

    private Activity mActivity;

    private DialogBackupDontScreensBinding mBinding;


    public PrivateAgreementDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_backup_dont_screens, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }

        setContentView(mBinding.getRoot());
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        mBinding.tvIKnow.setOnClickListener(view -> {
            dismiss();
        });
    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }


}

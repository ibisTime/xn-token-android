package com.cdkj.token.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.DialogPayPasswordInputBinding;
import com.cdkj.token.user.login.ForgetPwdActivity;
import com.cdkj.token.views.password.SixPassWordView;

/**
 * 支付 资金密码
 * Created by cdkj on 2018/7/20.
 */

public class UserPayPasswordInputDialog extends Dialog {

    private Activity mActivity;

    private DialogPayPasswordInputBinding mBinding;

    private PasswordInputEndListener passwordInputEndListener;

    public void setPasswordInputEndListener(PasswordInputEndListener passwordInputEndListener) {
        this.passwordInputEndListener = passwordInputEndListener;
    }

    public interface PasswordInputEndListener {  //密码输入完成
        void passEnd(String password);
    }

    public UserPayPasswordInputDialog(@NonNull Activity activity) {
        super(activity, R.style.TipsDialog);
        this.mActivity = activity;
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_pay_password_input, null, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        int screenWidth = DisplayHelper.getScreenWidth(mActivity);
        setContentView(mBinding.getRoot());
        getWindow().setLayout((int) (screenWidth * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        //关闭
        mBinding.imgClose.setOnClickListener(view -> dismiss());

        //忘记密码
        mBinding.tvForget.setOnClickListener(view -> {
            dismiss();

            ForgetPwdActivity.open(mActivity,
                    SPUtilHelper.getUserPhoneNum(),
                    SPUtilHelper.getUserEmail(),
                    ForgetPwdActivity.RC_PAY_PWD_MODIFY);
        });

        mBinding.passView.setPasswordInputEndListener(new SixPassWordView.PasswordInputEndListener() {
            @Override
            public void passEnd(String password) {
                if (passwordInputEndListener != null) {
                    passwordInputEndListener.passEnd(password);
                }
            }
        });

    }

    @Override
    public void show() {
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        super.show();
    }

    public UserPayPasswordInputDialog setInfoTitle(String title) {
        if (mBinding == null) return this;
        mBinding.tvTitle.setText(title);
        return this;
    }

    public void setPwdEmpty() {
        if (mBinding == null) return;
        mBinding.passView.clearPwd();
    }


}

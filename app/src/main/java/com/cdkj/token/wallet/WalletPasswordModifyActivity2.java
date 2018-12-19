package com.cdkj.token.wallet;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityWalletPwdModifyBinding;
import com.cdkj.token.utils.wallet.WalletHelper;

import static com.cdkj.baselibrary.utils.StringUtils.stringFilter;

/**
 * 修改钱包密码
 * Created by cdkj on 2018/6/7.
 */

public class WalletPasswordModifyActivity2 extends AbsLoadActivity {

    private ActivityWalletPwdModifyBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, WalletPasswordModifyActivity2.class);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_wallet_pwd_modify, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        mBaseBinding.titleView.setMidTitle(R.string.modify_wallet_pwd);


        mBinding.editOldPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mBinding.editOldPwd.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    mBinding.editOldPwd.setText(str);
                    //设置新的光标所在位置
                    mBinding.editOldPwd.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBinding.editNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mBinding.editNewPwd.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    mBinding.editNewPwd.setText(str);
                    //设置新的光标所在位置
                    mBinding.editNewPwd.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.editNewPwdRe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String editable = mBinding.editNewPwdRe.getText().toString();
                String str = stringFilter(editable.toString());
                if (!editable.equals(str)) {
                    mBinding.editNewPwdRe.setText(str);
                    //设置新的光标所在位置
                    mBinding.editNewPwdRe.setSelection(str.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.tvConfirm.setOnClickListener(view -> {

            if (TextUtils.isEmpty(mBinding.editOldPwd.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_password_hint));
                return;
            }
            if (TextUtils.isEmpty(mBinding.editNewPwd.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_password_pwdnew_hint));
                return;
            }
            if (mBinding.editNewPwd.getText().toString().length() < 6) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_password_format_hint));
                return;
            }

            if (TextUtils.isEmpty(mBinding.editNewPwdRe.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_repassword_hint));
                return;
            }

            if (!TextUtils.equals(mBinding.editNewPwd.getText().toString(), mBinding.editNewPwdRe.getText().toString())) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.user_password_repwd_format_hint));
                return;
            }

            if (!WalletHelper.checkPasswordByUserId(mBinding.editOldPwd.getText().toString(), WalletHelper.WALLET_USER)) {
                UITipDialog.showInfoNoIcon(this, getString(R.string.old_pwd_error));
                return;
            }

            updatePassword();
        });

    }


    /**
     * 修改密码
     *
     * @param
     */
    private void updatePassword() {
        try {
            WalletHelper.updateWalletPassWord(mBinding.editNewPwdRe.getText().toString(), WalletHelper.WALLET_USER);
            UITipDialog.showSuccess(WalletPasswordModifyActivity2.this, getString(R.string.update_password_success), dialogInterface -> {
                finish();
            });
        } catch (Exception e) {
            UITipDialog.showSuccess(WalletPasswordModifyActivity2.this, getString(R.string.update_password_fail), dialogInterface -> {
                finish();
            });
        }
    }

}

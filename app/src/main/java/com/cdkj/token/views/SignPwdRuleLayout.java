package com.cdkj.token.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cdkj.token.R;
import com.cdkj.token.databinding.LayoutSignPwdRuleBinding;

/**
 * 密码输入验证
 * Created by cdkj on 2018/7/1.
 */

public class SignPwdRuleLayout extends LinearLayout {

    public LayoutSignPwdRuleBinding mBinding;

    private String rule;
    private boolean isPass;

    public SignPwdRuleLayout(Context context) {
        this(context, null);
    }

    public SignPwdRuleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignPwdRuleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.sign_pwd_rule);

        rule = ta.getString(R.styleable.sign_pwd_rule_rule);
        init();
    }


    private void init() {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_sign_pwd_rule, this, true);

        mBinding.tvRule.setText(rule);
    }

    public void changeRuleState(boolean isPass) {
        this.isPass = isPass;

        if (isPass) {
            mBinding.ivIsPass.setBackgroundResource(R.mipmap.sign_pwd_rule_pass);
        } else {
            mBinding.ivIsPass.setBackgroundResource(R.mipmap.sign_pwd_rule_no_pass);
        }
    }

    boolean getRuleState(){
        return isPass;
    }

}

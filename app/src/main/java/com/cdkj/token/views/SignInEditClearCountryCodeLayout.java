package com.cdkj.token.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cdkj.token.R;
import com.cdkj.token.databinding.LayoutEditClearBinding;
import com.cdkj.token.databinding.LayoutEditClearCountryCodeBinding;

/**
 * 清除按钮  下划线 获取验证码 EditText
 * Created by cdkj on 2018/7/1.
 */

public class SignInEditClearCountryCodeLayout extends LinearLayout {

    public LayoutEditClearCountryCodeBinding mBinding;

    private String hintText;

    public SignInEditClearCountryCodeLayout(Context context) {
        this(context, null);
    }

    public SignInEditClearCountryCodeLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignInEditClearCountryCodeLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.sign_edit_clear_layout);

        hintText = ta.getString(R.styleable.sign_edit_clear_layout_hint_text);
        init();
    }


    private void init() {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_edit_clear_country_code, this, true);

        mBinding.edit.setHint(hintText);

        mBinding.edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeImgShowState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mBinding.imgEditClear.setOnClickListener(view -> {
            mBinding.edit.setText("");
            mBinding.imgEditClear.setVisibility(GONE);
        });

        mBinding.edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    changeImgShowState();
                    mBinding.viewLine.setBackgroundResource(R.drawable.line_blue_gradient);
                } else {
                    mBinding.imgEditClear.setVisibility(GONE);
                    mBinding.viewLine.setBackgroundResource(R.drawable.gray);
                }
            }
        });
    }

    void changeImgShowState() {
        if (!mBinding.edit.isEnabled()) {
            mBinding.imgEditClear.setVisibility(GONE);
            return;
        }
        if (TextUtils.isEmpty(getText())) {
            mBinding.imgEditClear.setVisibility(GONE);
        } else {
            mBinding.imgEditClear.setVisibility(VISIBLE);
        }
    }

    public String getText() {
        return mBinding.edit.getText().toString();
    }

    public EditText getEditText() {
        return mBinding.edit;
    }

    public TextView getLeftTextView() {
        return mBinding.countryCode;
    }

    public ImageView getLeftImage() {
        return mBinding.imgCountry;
    }

    public View getLeftRootView() {
        return mBinding.linLayoutCountry;
    }

    public void setDownImgVisibilityGone() {
        mBinding.imgDown.setVisibility(GONE);
    }

}

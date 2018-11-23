package com.cdkj.token.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cdkj.token.R;
import com.cdkj.token.databinding.LayoutUserTabBinding;

/**
 * Created by cdkj on 2018/11/23.
 */

public class UserTableLayout extends LinearLayout {

    private Context mContext;
    private LayoutUserTabBinding mBinding;

    String titleLeft;
    String titleRight;

    public UserTableLayout(Context context) {
        this(context, null);
        mContext = context;
    }

    public UserTableLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserTableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.user_tab_layout);

        titleLeft = ta.getString(R.styleable.user_tab_layout_title_left);
        titleRight = ta.getString(R.styleable.user_tab_layout_title_right);
        init();
    }

    private void init() {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_user_tab, this, true);

        mBinding.tvTitleLeft.setText(titleLeft);
        mBinding.tvTitleRight.setText(titleRight);

        mBinding.llLeft.setOnClickListener(view -> {
            setDarkView();

            mBinding.lineLeft.setVisibility(VISIBLE);
            mBinding.tvTitleLeft.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        });

        mBinding.llRight.setOnClickListener(view -> {
            setDarkView();

            mBinding.lineRight.setVisibility(VISIBLE);
            mBinding.tvTitleRight.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        });
    }

    private void setDarkView(){
        mBinding.tvTitleLeft.setTextColor(ContextCompat.getColor(mContext, R.color.gray_d6d5d5));
        mBinding.tvTitleRight.setTextColor(ContextCompat.getColor(mContext, R.color.gray_d6d5d5));

        mBinding.lineLeft.setVisibility(GONE);
        mBinding.lineRight.setVisibility(GONE);
    }

}

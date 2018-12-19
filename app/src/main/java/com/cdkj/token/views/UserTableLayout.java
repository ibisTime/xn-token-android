package com.cdkj.token.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cdkj.token.R;
import com.cdkj.token.databinding.LayoutUserTabBinding;
import com.cdkj.token.interfaces.UserTabLayoutInterface;

/**
 * Created by cdkj on 2018/11/23.
 */

public class UserTableLayout extends LinearLayout {

    public static int LEFT = 0;
    public static int RIGHT = 1;

    private int position = 0;
    private LayoutUserTabBinding mBinding;

    private UserTabLayoutInterface mInterface;

    int light = Color.parseColor("#ffffff");
    int dark = Color.parseColor("#D6D5D5");

    String titleLeft;
    String titleRight;

    public UserTableLayout(Context context) {
        this(context, null);
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

    public UserTableLayout setInterface(UserTabLayoutInterface mInterface) {
        this.mInterface = mInterface;
        return this;
    }

    private void init() {

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_user_tab, this, true);

        mBinding.tvTitleLeft.setText(titleLeft);
        mBinding.tvTitleRight.setText(titleRight);

        mBinding.llLeft.setOnClickListener(view -> {
            setDarkView();

            position = LEFT;

            mBinding.lineLeft.setVisibility(VISIBLE);
            mBinding.tvTitleLeft.setTextColor(light);

            if (null != mInterface)
                mInterface.onLeftTabListener();
        });

        mBinding.llRight.setOnClickListener(view -> {
            setDarkView();

            position = RIGHT;

            mBinding.lineRight.setVisibility(VISIBLE);
            mBinding.tvTitleRight.setTextColor(light);

            if (null != mInterface)
                mInterface.onRightTabListener();
        });
    }

    private void setDarkView(){
        mBinding.tvTitleLeft.setTextColor(dark);
        mBinding.tvTitleRight.setTextColor(dark);

        mBinding.lineLeft.setVisibility(GONE);
        mBinding.lineRight.setVisibility(GONE);
    }

    public int getPosition(){
        return position;
    }

    public int setPosition(int position){
        return this.position = position;
    }
}

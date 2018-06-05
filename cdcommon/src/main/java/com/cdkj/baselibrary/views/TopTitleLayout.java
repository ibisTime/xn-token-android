package com.cdkj.baselibrary.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.R;

/**
 * 顶部TitleView
 * Created by cdkj on 2017/10/10.
 */

public class TopTitleLayout extends FrameLayout {

    private TextView mTextTitle;
    private FrameLayout mLeftFra;
    private FrameLayout mRightFra;
    private ImageView mLeftImg;
    private TextView mLeftTv;
    private ImageView mRightImg;
    private TextView mRightTv;

    private Context mContext;

    public TopTitleLayout(@NonNull Context context) {
        this(context, null);
    }

    public TopTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.common_title_info, this, true);
        mTextTitle = (TextView) findViewById(R.id.tv_top_title_abs);
        mLeftTv = (TextView) findViewById(R.id.tv_back);
        mRightTv = (TextView) findViewById(R.id.tv_top_right);
        mLeftFra = (FrameLayout) findViewById(R.id.fram_img_back);
        mRightFra = (FrameLayout) findViewById(R.id.fllayout_right);
        mLeftImg = (ImageView) findViewById(R.id.img_back);
        mRightImg = (ImageView) findViewById(R.id.img_right);
    }

    public void setLeftTitle(String text) {
        mLeftTv.setText(text);
        mLeftTv.setVisibility(text != null ? VISIBLE : GONE);
        mLeftFra.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setLeftTitleColor(@ColorRes int color) {
        mLeftTv.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setRightTitleColor(@ColorRes int color) {
        mRightTv.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setMidTitleColor(@ColorRes int color) {
        mTextTitle.setTextColor(ContextCompat.getColor(mContext, color));
    }

    public void setRightTitle(String text) {
        mRightTv.setText(text);
        mRightTv.setVisibility(text != null ? VISIBLE : GONE);
        mRightFra.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setMidTitle(String text) {
        mTextTitle.setText(text);
        mTextTitle.setVisibility(text != null ? VISIBLE : GONE);
    }
    public void setMidTitle(@StringRes int textid) {
        mTextTitle.setText(textid);
        mTextTitle.setVisibility(textid != 0 ? VISIBLE : GONE);
    }

    public void setLeftImg(@DrawableRes int img) {
        mLeftImg.setVisibility(img != 0 ? VISIBLE : GONE);
        mLeftFra.setVisibility(img != 0 ? VISIBLE : GONE);
        if (img != 0) {
            mLeftImg.setImageResource(img);
        }
    }

    public void setRightImg(@DrawableRes int img) {
        mRightImg.setVisibility(img != 0 ? VISIBLE : GONE);
        mRightFra.setVisibility(img != 0 ? VISIBLE : GONE);
        if (img != 0) {
            mRightImg.setImageResource(img);
        }
    }

    public void setLeftFraClickListener(View.OnClickListener listener) {
        mLeftFra.setOnClickListener(listener);
    }

    public void setRightFraClickListener(View.OnClickListener listener) {
        mRightFra.setOnClickListener(listener);
    }

    public void setBackgroundColor(int color) {
        findViewById(R.id.fram_title).setBackgroundColor(color);
    }

}

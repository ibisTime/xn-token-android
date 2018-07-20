package com.cdkj.baselibrary.views;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.R;

/**
 * 加载中布局 可以设置空页面 错误页面 加载中页面 titleView
 * Created by cdkj on 2017/10/10.
 */

public class ViewLoadLayout extends FrameLayout {

    private TextView mEmptyTextView;
    private ImageView mEmptyImageView;
    private FrameLayout mEmptyfra;
    private LoadingView mEmptyLoadingView;

    private View mContentView;

    public ViewLoadLayout(@NonNull Context context) {
        this(context, null);
    }

    public ViewLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewLoadLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /*addMainView
    * addTopTitleView
    *
    * */

    /**
     * 初始化
     */
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, this, true);
        mEmptyTextView = (TextView) findViewById(R.id.tv_empty);
        mEmptyImageView = (ImageView) findViewById(R.id.img_empty);
        mEmptyLoadingView = (LoadingView) findViewById(R.id.load_empty);
        mEmptyfra = (FrameLayout) findViewById(R.id.fra_empty);
    }


    public void hindEmptyAll() {
        showEmptyFra(false);
        setShowText(null);
        setShowImage(0);
        setShowLoadingView(false);
    }

    private void showEmptyFra(boolean isShow) {
        mEmptyfra.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void hindAll() {
        hindEmptyAll();
        showContent(false);
    }

    public void showContent(boolean isShow) {
        if (mContentView != null) {
            mContentView.setVisibility(isShow ? VISIBLE : GONE);
        }
    }

    public void setShowText(String text) {
        showContent(text == null);
        showEmptyFra(text != null);
        mEmptyTextView.setText(text);
        mEmptyTextView.setVisibility(text != null ? VISIBLE : GONE);
    }

    public void setShowImage(@DrawableRes int img) {
        showEmptyFra(img != 0);
        showContent(img == 0);
        mEmptyImageView.setVisibility(img != 0 ? VISIBLE : GONE);
        if (img <= 0) {
            mEmptyImageView.setImageResource(R.drawable.default_pic);
        }else{
            mEmptyImageView.setImageResource(img);
        }
    }

    public void setShowLoadingView(boolean isSHow) {
        showContent(!isSHow);
        if (!isSHow) {
            setShowText(null);
            setShowImage(0);
        }
        showEmptyFra(isSHow);
        mEmptyLoadingView.setVisibility(isSHow ? VISIBLE : GONE);
    }

    /**
     * 添加要显示的View
     *
     * @param contentView
     */
    public void addContentView(View contentView) {
        if (contentView != null) {
            mContentView = contentView;
            addView(contentView);
        }
    }

}

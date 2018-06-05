package com.cdkj.baselibrary.interfaces;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;

/**
 * 刷新方法回调
 * Created by cdkj on 2017/10/17.
 */

public abstract class BaseRefreshCallBack<T> implements RefreshInterface<T> {

    private EmptyViewBinding emptyViewBinding;

    private Activity context;

    public BaseRefreshCallBack(Activity context) {
        this.context = context;
    }

    @Override
    public View getEmptyView() {
        if (context == null) {
            return null;
        }
        emptyViewBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.empty_view, null, false);
        return emptyViewBinding.getRoot();
    }

    @Override
    public void showErrorState(String errorMsg, int img) {
        if (emptyViewBinding == null) {
            return;
        }
        if (TextUtils.isEmpty(errorMsg) && img == 0) {
            emptyViewBinding.getRoot().setVisibility(View.GONE);
            return;
        }
        emptyViewBinding.getRoot().setVisibility(View.VISIBLE);
        emptyViewBinding.tv.setText(errorMsg);
        if (img <= 0) {
            emptyViewBinding.img.setVisibility(View.GONE);
        } else {
            emptyViewBinding.img.setImageResource(img);
            emptyViewBinding.img.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showEmptyState(String errorMsg, int errorImg) {
        if (emptyViewBinding == null) {
            return;
        }
        if (TextUtils.isEmpty(errorMsg) && errorImg == 0) {
            emptyViewBinding.getRoot().setVisibility(View.GONE);
            return;
        }
        emptyViewBinding.getRoot().setVisibility(View.VISIBLE);
        emptyViewBinding.tv.setText(errorMsg);
        if (errorImg <= 0) {
            emptyViewBinding.img.setVisibility(View.GONE);
        } else {
            emptyViewBinding.img.setImageResource(errorImg);
            emptyViewBinding.img.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }

    @Override
    public void onDestroy() {
        context = null;
    }
}

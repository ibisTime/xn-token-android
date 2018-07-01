package com.cdkj.baselibrary.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.interfaces.RefreshInterface;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2017/10/15.
 */
public class RefreshHelper<T> {

    public static int LIMITE = 15;

    private RefreshInterface mRefreshInterface;//刷新接口

    private BaseQuickAdapter mAdapter;//数据适配器

    private SmartRefreshLayout mRefreshLayout;

    private RecyclerView mRecyclerView;

    private int mPageIndex; //分页下标

    private int mLimit;//数据大小

    private List<T> mDataList;//数据

    private Context mContext;

    private View mEmptyView;

    public int getmPageIndex() {
        return mPageIndex;
    }

    public int getmLimit() {
        return mLimit;
    }

    public List<T> getmDataList() {
        return mDataList;
    }

    public BaseQuickAdapter getmAdapter() {
        return mAdapter;
    }

    public SmartRefreshLayout getmRefreshLayout() {
        return mRefreshLayout;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }


    public RefreshHelper(Context context, RefreshInterface mRefreshInterface) {
        this.mRefreshInterface = mRefreshInterface;
        this.mContext = context;
    }


    public void setPageIndex(int mPageIndex) {
        this.mPageIndex = mPageIndex;
    }

    /**
     * 初始化
     *
     * @param limit 分页个数
     */
    public void init(int limit) {

        mPageIndex = 1;//分页从1开始

        mLimit = limit;//分页数量

        mDataList = new ArrayList<T>();

        if (mRefreshInterface != null) {
            mRefreshLayout = (SmartRefreshLayout) mRefreshInterface.getRefreshLayout();
            mRecyclerView = mRefreshInterface.getRecyclerView();

            mAdapter = (BaseQuickAdapter) mRefreshInterface.getAdapter(mDataList);

            mEmptyView = mRefreshInterface.getEmptyView();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        }

        if (mAdapter != null) {
            View tv = new View(mContext); //先设置 不显示任何东西的 emptyView
            mAdapter.setEmptyView(tv);
            mRecyclerView.setAdapter(mAdapter);
        }
        initRefreshLayout();
    }


    /**
     * 初始化刷新加载
     */
    private void initRefreshLayout() {

        if (mRefreshLayout == null) return;

        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);//不满一行启动上啦加载

        mRefreshLayout.setEnableAutoLoadmore(false);//禁用惯性

        mRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) { //刷新
                onMRefresh(1, mLimit, false);
                if (mRefreshInterface != null) {
                    mRefreshInterface.onRefresh(1, mLimit);
                }

            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {//加载
                if (mDataList.size() > 0) {
                    mPageIndex++;
                }
                onMLoadMore(mPageIndex, mLimit);
                if (mRefreshInterface != null) {
                    mRefreshInterface.onLoadMore(mPageIndex, mLimit);
                }
            }
        });
    }

    //执行默认刷新 mPageIndex变为一
    public void onDefaluteMRefresh(boolean isShowDialog) {
        mPageIndex = 1;
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(mPageIndex, mLimit, isShowDialog);
        }
    }

    //执行默认刷新 mPageIndex++
    public void onDefaluteMLoadMore(boolean isShowDialog) {
        if (mDataList.size() > 0) {
            mPageIndex++;
        }
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(mPageIndex, mLimit, isShowDialog);
        }
    }

    //刷新
    public void onMRefresh(int pageindex, int limit, boolean isShowDialog) {
        mPageIndex = pageindex;
        mLimit = limit;
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(pageindex, limit, isShowDialog);
        }

    }

    //加载
    public void onMLoadMore(int pageIndex, int limit) {
        mPageIndex = pageIndex;
        mLimit = limit;
        if (mRefreshInterface != null) {
            mRefreshInterface.getListDataRequest(pageIndex, limit, false);
        }
    }


    //加载错误布局
    public void loadError(String str, int img) {

        refreshLayoutStop();

        if (mEmptyView != null && mDataList.isEmpty()) {
            if (mRefreshInterface != null) {
                mRefreshInterface.showErrorState(str, img);
            }
            if (mAdapter != null) mAdapter.setEmptyView(mEmptyView);
        }
    }

    //停止加载和刷新布局动画
    void refreshLayoutStop() {
        if (mRefreshLayout != null) {
            if (mRefreshLayout.isRefreshing()) { //停止刷新
                mRefreshLayout.finishRefresh();
            }
            if (mRefreshLayout.isLoading()) {//停止加载
                mRefreshLayout.finishLoadmore();
            }
        }
    }


    public void reLoadAdapter() {
        mDataList.clear();
        mAdapter = (BaseQuickAdapter) mRefreshInterface.getAdapter(mDataList);
    }

    /**
     * 设置加载数据 实现分页逻辑
     *
     * @param datas
     */
    public void setData(List<T> datas, String emp, int img) {

        refreshLayoutStop();

        if (mPageIndex == 1) {         //如果当前加载的是第一页数据
            if (datas != null && datas.size() > 0) {
                mDataList.clear();
                mDataList.addAll(datas);
            } else {
                mDataList.clear();
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        } else if (mPageIndex > 1) {
            if (datas == null || datas.size() <= 0) {
                mPageIndex--;
            } else {
                mDataList.addAll(datas);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        if (mEmptyView != null && mDataList.isEmpty()) {
            if (mRefreshInterface != null) {
                mRefreshInterface.showEmptyState(emp, img);
            }
            if (mAdapter != null) mAdapter.setEmptyView(mEmptyView);
        }
    }

    /**
     * 防止内存泄漏
     */
    public void onDestroy() {
        if (mRefreshInterface != null) {
            mRefreshInterface.onDestroy();
        }
        mContext = null;
    }

}

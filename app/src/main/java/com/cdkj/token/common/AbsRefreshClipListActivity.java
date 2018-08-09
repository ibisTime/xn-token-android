package com.cdkj.token.common;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.baselibrary.interfaces.BaseRefreshCallBack;
import com.cdkj.baselibrary.utils.RefreshHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityRefreshClipBinding;

import java.util.List;

/**
 * 公用刷新
 * Created by cdkj on 2018/2/28.
 */
public abstract class AbsRefreshClipListActivity<T> extends AbsLoadActivity {

    protected ActivityRefreshClipBinding mRefreshBinding;

    protected RefreshHelper mRefreshHelper;

    @Override
    public View addMainView() {
        mRefreshBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_refresh_clip, null, false);
        return mRefreshBinding.getRoot();
    }


    /**
     * 初始化刷新相关
     */
    protected void initRefreshHelper(int limit) {
        mRefreshHelper = new RefreshHelper(this, new BaseRefreshCallBack<T>(this) {
            @Override
            public View getRefreshLayout() {
                return mRefreshBinding.refreshLayout;
            }

            @Override
            public RecyclerView getRecyclerView() {
                ((DefaultItemAnimator) mRefreshBinding.recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                return mRefreshBinding.recyclerView;
            }

            @Override
            public RecyclerView.Adapter getAdapter(List<T> listData) {
                return getListAdapter(listData);
            }

            @Override
            public void getListDataRequest(int pageindex, int limit, boolean isShowDialog) {
                getListRequest(pageindex, limit, isShowDialog);
            }
        });
        mRefreshHelper.init(limit);

    }


    abstract public RecyclerView.Adapter getListAdapter(List<T> listData);

    abstract public void getListRequest(int pageindex, int limit, boolean isShowDialog);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshHelper != null) {
            mRefreshHelper.onDestroy();
        }
    }
}

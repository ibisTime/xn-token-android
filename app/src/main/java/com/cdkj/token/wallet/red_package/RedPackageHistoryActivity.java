package com.cdkj.token.wallet.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityRedPackageHistoryBinding;

import java.util.ArrayList;

/**
 * 收发红包记录
 */
public class RedPackageHistoryActivity extends AbsBaseLoadActivity {

    //    int loadDataType = 0;//加载的是  发红包的数据 还是抢红包的数据   0是抢到的红包  1是发出的红包
    ActivityRedPackageHistoryBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, RedPackageHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
//        activity_red_package_history
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_red_package_history, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        mBinding.ivBack.setOnClickListener(view -> {
            finish();
        });
        initTopTitle();
        initViewPager();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    private void initTopTitle() {
        mBinding.topLayout.radiogroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.radio_left:
                    mBinding.viewpager.setCurrentItem(0, true);
                    break;
                case R.id.radio_right:
                    mBinding.viewpager.setCurrentItem(1, true);
                    break;
            }
        });
    }

    private void initViewPager() {
        //设置fragment数据
        ArrayList fragments = new ArrayList<>();

        fragments.add(MyGetRedPackageFragment.getInstance(true));
        fragments.add(MySendRedPackageFragment.getInstance(false));

        mBinding.viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.viewpager.setOffscreenPageLimit(fragments.size());
        //默认选中第一个
        mBinding.viewpager.setCurrentItem(0, true);

    }

//    @Override
//    public void afterCreate(Bundle savedInstanceState) {
//        mBaseBinding.titleView.setMidTitle("我的糖包");
////        RedPackageHistoryAdapter
//        initRefreshHelper(10);
//        mRefreshHelper.onDefaluteMRefresh(true);
//        mRefreshBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                //刷新监听
//                Log.i("pppppp", "onRefresh: 刷新了");
//            }
//        });
//    }
//
//    @Override
//    public RecyclerView.Adapter getListAdapter(List<RedPackageHistoryBean> listData) {
//        RedPackageHistoryAdapter mAdapter = new RedPackageHistoryAdapter(listData);
//        View headView = View.inflate(this, R.layout.layout_red_package_title, null);
//        mAdapter.addHeaderView(headView);
//        RadioButton radio_left = headView.findViewById(R.id.radio_left);
//        RadioButton radio_right = headView.findViewById(R.id.radio_right);
//        radio_left.setOnCheckedChangeListener((compoundButton, b) -> {
//            loadDataType = 0;
//            mRefreshHelper.onDefaluteMRefresh(true);
//
//        });
//        radio_right.setOnCheckedChangeListener((compoundButton, b) -> {
//            loadDataType = 1;
//            mRefreshHelper.onDefaluteMRefresh(true);
//        });
//        mAdapter.setHeaderAndEmpty(true);
//        return mAdapter;
//    }
//
//    @Override
//    public void getListRequest(int pageindex, int limit, boolean isShowDialog) {
//        initDatas(pageindex, limit, isShowDialog);
//    }
//
//    private void initDatas(int pageindex, int limit, boolean isShowDialog) {
////        if (isShowDialog) {
////            showLoadingDialog();
////        }
//
//    }
}

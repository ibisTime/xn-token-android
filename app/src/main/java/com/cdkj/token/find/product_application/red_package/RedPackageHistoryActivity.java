package com.cdkj.token.find.product_application.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsLoadActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityRedPackageHistoryBinding;

import java.util.ArrayList;

/**
 * 收发红包记录
 */
public class RedPackageHistoryActivity extends AbsLoadActivity {

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
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_red_package_history, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setStatusBarColor(ContextCompat.getColor(this, R.color.red));
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
}

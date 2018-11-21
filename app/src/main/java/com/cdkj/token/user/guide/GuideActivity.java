package com.cdkj.token.user.guide;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityGuideBinding;
import com.cdkj.token.find.FindFragment;
import com.cdkj.token.user.UserFragment;
import com.cdkj.token.wallet.WalletFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2018/11/15.
 */

public class GuideActivity extends AbsStatusBarTranslucentActivity {

    private ActivityGuideBinding mBinding;

    private List<Fragment> fragments;

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_guide,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        sheShowTitle(false);
        setPageBgImage(R.mipmap.app_page_bg_new);

        init();
        initViewPager();
    }

    private void init(){


    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(WalletFragment.getInstance());
        fragments.add(FindFragment.getInstance());
        fragments.add(UserFragment.getInstance());

        mBinding.vpWallet.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.vpWallet.setOffscreenPageLimit(fragments.size());

        mBinding.vpWallet.setCurrentItem(0);

    }
}

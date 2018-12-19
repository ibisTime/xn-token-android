package com.cdkj.token.user.guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityGuideBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2018/11/15.
 */

public class GuideActivity extends AbsStatusBarTranslucentActivity {

    private ActivityGuideBinding mBinding;

    private List<Fragment> fragments;

    public static void open(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

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

        fragments.add(GuidePersonFragment.getInstance());
        fragments.add(GuidePrivateFragment.getInstance());

        mBinding.vpWallet.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.vpWallet.setOffscreenPageLimit(fragments.size());

        mBinding.vpWallet.setCurrentItem(0);

        mBinding.vpWallet.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //页面滚动时小白点移动的距离，并通过setLayoutParams(params)不断更新其位置
                float leftMargin = DisplayHelper.dp2px(GuideActivity.this, 25) * (position + positionOffset);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBinding.ivDots.getLayoutParams();
                params.leftMargin = (int) leftMargin;
                mBinding.ivDots.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
//                //页面跳转时，设置小圆点的margin
//                float leftMargin = DisplayHelper.dp2px(GuideActivity.this, 25) * position;
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBinding.ivDots.getLayoutParams();
//                params.leftMargin = (int) leftMargin;
//                mBinding.ivDots.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}

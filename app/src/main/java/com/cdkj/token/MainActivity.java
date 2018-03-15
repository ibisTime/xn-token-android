package com.cdkj.token;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.token.consult.ConsultFragment;
import com.cdkj.token.databinding.ActivityMainBinding;
import com.cdkj.token.user.UserFragment;
import com.cdkj.token.wallet.WalletFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/main/page")
public class MainActivity extends AbsBaseActivity {

    private ActivityMainBinding mBinding;

    public static final int CONSULT = 0;
    public static final int WALLET = 1;
    public static final int MY = 2;
    private List<Fragment> fragments;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViewPager();
        initListener();

        init();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }


    private void init() {
        setShowIndex(CONSULT);
    }


    /**
     * 初始化事件
     */
    private void initListener() {

        mBinding.layoutMainBottom.llConsult.setOnClickListener(v -> {
            setShowIndex(CONSULT);

        });

        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            setShowIndex(WALLET);

        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
            if (!SPUtilHelper.isLogin(this, false)) {
                return;
            }
            setShowIndex(MY);

        });


    }

    public void setTabIndex(int index) {
        setTabDark();
        switch (index) {
            case CONSULT:
                mBinding.layoutMainBottom.ivConsult.setImageResource(R.mipmap.main_consult_light);
                mBinding.layoutMainBottom.tvConsult.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case WALLET:
                mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_light);
                mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
            case MY:
                mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_light);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }

    }

    private void setTabDark(){
        mBinding.layoutMainBottom.ivConsult.setImageResource(R.mipmap.main_consult_dark);
        mBinding.layoutMainBottom.tvConsult.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivWallet.setImageResource(R.mipmap.main_wallet_dark);
        mBinding.layoutMainBottom.tvWallet.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));

        mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_dark);
        mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.gray_666666));
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mBinding.pagerMain.setPagingEnabled(false);//禁止左右切换

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(ConsultFragment.getInstance());
        fragments.add(WalletFragment.getInstance());
        fragments.add(UserFragment.getInstance());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());
    }


    /**
     * 设置要显示的界面
     *
     * @param index
     */
    private void setShowIndex(int index) {
        if (index < 0 && index >= fragments.size()) {
            return;
        }

        mBinding.pagerMain.setCurrentItem(index, false);
        setTabIndex(index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = fragments.get(fragments.size()-1);
        fragment.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onBackPressed() {
        showDoubleWarnListen(getStrRes(R.string.exit_confirm), view -> {
            EventBus.getDefault().post(EventTags.AllFINISH);
            finish();
        });
    }
}

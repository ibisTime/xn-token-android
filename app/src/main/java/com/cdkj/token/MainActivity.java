package com.cdkj.token;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.token.databinding.ActivityMainBinding;
import com.cdkj.token.find.FindFragment;
import com.cdkj.token.user.UserFragment;
import com.cdkj.token.wallet.WalletFragment;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Route(path = CdRouteHelper.APPMAIN)
public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;

    public static final int WALLET = 0;
    public static final int CONSULT = 1;
    public static final int ME = 2;
    private List<Fragment> fragments;

    public BigDecimal totalAsset = BigDecimal.ZERO;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initListener();
        init();
        initViewPager();
    }


    private void init() {
        setShowIndex(WALLET);
//        CoinListService.open(this);
    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            setShowIndex(WALLET);

        });

        mBinding.layoutMainBottom.llConsult.setOnClickListener(v -> {
            setShowIndex(CONSULT);

        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
            setShowIndex(ME);
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
            case ME:
                mBinding.layoutMainBottom.ivMy.setImageResource(R.mipmap.main_my_light);
                mBinding.layoutMainBottom.tvMy.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
                break;
        }

    }

    private void setTabDark() {
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

        fragments.add(WalletFragment.getInstance());
        fragments.add(FindFragment.getInstance());
        fragments.add(UserFragment.getInstance());

        mBinding.pagerMain.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.pagerMain.setOffscreenPageLimit(fragments.size());

        mBinding.pagerMain.setCurrentItem(0);

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
        Fragment fragment = fragments.get(fragments.size() - 1);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        showDoubleWarnListen(getStrRes(R.string.exit_confirm), view -> {
            EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
            finish();
        });
    }

    public void updateTotalAsset(BigDecimal totalAsset){
        if (null != totalAsset)
            this.totalAsset = totalAsset;
    }

    public BigDecimal getTotalAsset(){
        return totalAsset;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

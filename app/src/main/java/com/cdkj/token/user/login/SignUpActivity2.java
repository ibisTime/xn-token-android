package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.LoginFinishEvent;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivitySignUp2Binding;
import com.cdkj.token.model.SignUpInfoModel;
import com.cdkj.token.user.login.signup.SignUpStep1Fragment;
import com.cdkj.token.user.login.signup.SignUpStep2Fragment;
import com.cdkj.token.user.login.signup.SignUpStep3Fragment;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2018/11/21.
 */

public class SignUpActivity2 extends AbsStatusBarTranslucentActivity {

    private ActivitySignUp2Binding mBinding;

    private List<Fragment> fragments;
    private Fragment signUpStep1Fragment;

    private SignUpInfoModel mSignUpInfoModel;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, SignUpActivity2.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_up2,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setPageBgImage(R.mipmap.app_page_bg_new);

        setMidTitle(R.string.activity_sign_up_title);
        setWhiteTitle();

        init();
        initViewPager();
    }

    private void init() {
        mSignUpInfoModel = new SignUpInfoModel();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {

        //设置fragment数据
        fragments = new ArrayList<>();

        signUpStep1Fragment = SignUpStep1Fragment.getInstance();
        fragments.add(signUpStep1Fragment);
        fragments.add(SignUpStep2Fragment.getInstance());
        fragments.add(SignUpStep3Fragment.getInstance());

        mBinding.vpSignUp.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mBinding.vpSignUp.setOffscreenPageLimit(fragments.size());

        mBinding.vpSignUp.setCurrentItem(0);
        mBinding.vpSignUp.setPagingEnabled(false); // 按条件禁止滑动

        mBinding.vpSignUp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                int light = ContextCompat.getColor(SignUpActivity2.this,R.color.green_68c5ca);
                int dark = ContextCompat.getColor(SignUpActivity2.this,R.color.white);

                mBinding.vStep1.setBackgroundColor(dark);
                mBinding.vStep2.setBackgroundColor(dark);
                mBinding.vStep3.setBackgroundColor(dark);

                if (position == 0){
                    mBinding.vStep1.setBackgroundColor(light);
                }else if (position == 1) {
                    mBinding.vStep2.setBackgroundColor(light);
                }else {
                    mBinding.vStep3.setBackgroundColor(light);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setCurrentItem(int index){
        if (null != mBinding)
            mBinding.vpSignUp.setCurrentItem(index);
    }

    public SignUpInfoModel getSignUpInfo(){
        return mSignUpInfoModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        signUpStep1Fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe
    public void finishAll(LoginFinishEvent loginFinishEvent) {
        if (canEvenFinish()) {
            this.finish();
        }
    }

}

package com.cdkj.token.user.login.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentSignUpStep1Binding;

/**
 * Created by cdkj on 2018/11/21.
 */

public class SignUpStep1Fragment extends BaseLazyFragment {

    private FragmentSignUpStep1Binding mBinding;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static SignUpStep1Fragment getInstance() {
        SignUpStep1Fragment fragment = new SignUpStep1Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_sign_up_step1, null ,false);
        return mBinding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

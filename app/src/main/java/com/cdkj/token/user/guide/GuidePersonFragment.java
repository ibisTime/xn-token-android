package com.cdkj.token.user.guide;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentGuidePersonBinding;
import com.cdkj.token.user.login.SignInActivity2;
import com.cdkj.token.user.login.SignUpActivity2;

/**
 * Created by cdkj on 2018/11/21.
 */

public class GuidePersonFragment extends BaseLazyFragment {

    private FragmentGuidePersonBinding mBinding;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static GuidePersonFragment getInstance() {
        GuidePersonFragment fragment = new GuidePersonFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_person, null, false);

        initListener();

        return mBinding.getRoot();
    }

    private void initListener() {
        mBinding.btnSignIn.setOnClickListener(view -> {
            SignInActivity2.open(mActivity, MainActivity.class, null);
        });

        mBinding.btnSignUp.setOnClickListener(view -> {
            SignUpActivity2.open(mActivity);
        });
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

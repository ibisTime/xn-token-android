package com.cdkj.token.user.guide;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentGuidePrivateBinding;

/**
 * Created by cdkj on 2018/11/21.
 */

public class GuidePrivateFragment extends BaseLazyFragment {

    private FragmentGuidePrivateBinding mBinding;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static GuidePrivateFragment getInstance() {
        GuidePrivateFragment fragment = new GuidePrivateFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_guide_private, null, false);

        return mBinding.getRoot();
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

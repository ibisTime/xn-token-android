package com.cdkj.token.user;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentUser2Binding;

/**
 * Created by cdkj on 2018/6/28.
 */

public class UserFragment2 extends BaseLazyFragment {

    private FragmentUser2Binding mBinding;

    public static UserFragment2 getInstance() {
        UserFragment2 fragment = new UserFragment2();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user2, null, false);

        return mBinding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

package com.cdkj.token.wallet;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentWallet2Binding;

/**
 * Created by cdkj on 2018/6/28.
 */

public class WalletFragment_2 extends BaseLazyFragment {

    private FragmentWallet2Binding mBinding;


    public static WalletFragment_2 getInstance() {
        WalletFragment_2 fragment = new WalletFragment_2();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet_2, null, false);

        return mBinding.getRoot();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

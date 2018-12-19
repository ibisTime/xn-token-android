package com.cdkj.token.wallet.backup_guide;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.BaseLazyFragment;
import com.cdkj.token.R;
import com.cdkj.token.databinding.FragmentBackup3Binding;
import com.zendesk.util.CollectionUtils;

/**
 * Created by cdkj on 2018/11/27.
 */

public class BackupFragment3 extends BaseLazyFragment {

    private FragmentBackup3Binding mBinding;

    private String[] wordsList;

    /**
     * 获得fragment实例
     *
     * @return
     */
    public static BackupFragment3 getInstance(String[] wordsList) {
        BackupFragment3 fragment = new BackupFragment3();

        Bundle bundle = new Bundle();
        bundle.putStringArray(CdRouteHelper.DATASIGN, wordsList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fragment_backup3, null ,false);

        init();
        initListener();

        setView();

        return mBinding.getRoot();
    }

    private void init(){
        wordsList = getArguments().getStringArray(CdRouteHelper.DATASIGN);
    }

    private void initListener() {
        mBinding.btnSuccess.setOnClickListener(view -> {
            BackupWalletActivity activity = (BackupWalletActivity) getActivity();
            BackupWalletWordsCheckActivity.open(mActivity, activity.isFromWalletTool());
            activity.finish();
        });
    }

    private void setView() {

        if (CollectionUtils.isEmpty(wordsList) || wordsList.length != 4) {
            return;
        }

        mBinding.tvMemonic1.setText(wordsList[0]);
        mBinding.tvMemonic2.setText(wordsList[1]);
        mBinding.tvMemonic3.setText(wordsList[2]);
        mBinding.tvMemonic4.setText(wordsList[3]);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {

    }
}

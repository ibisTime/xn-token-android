package com.cdkj.token.wallet.backup_guide;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityBackupWalletBinding;
import com.cdkj.token.model.db.WalletDBModel;
import com.cdkj.token.utils.wallet.WalletHelper;
import com.cdkj.token.views.dialogs.BackupPromptDialog;
import com.zendesk.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cdkj on 2018/11/27.
 */

public class BackupWalletActivity extends AbsStatusBarTranslucentActivity {

    private ActivityBackupWalletBinding mBinding;

    private boolean isFromWalletTool;
    private List<Fragment> fragments;

    public static void open(Context context, boolean isFromWalletTool) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BackupWalletActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, isFromWalletTool);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_backup_wallet,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setMidTitle("备份钱包助记词");
        setWhiteTitle();
        setPageBgImage(R.mipmap.app_page_bg_new);

        //禁止截屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        //截图提示弹框
        new BackupPromptDialog(this).show();


        isFromWalletTool = getIntent().getBooleanExtra(CdRouteHelper.DATASIGN, false);

        initViewPager();
    }



    private void initViewPager() {

        //
        List<String> wordsList = getMemonic();
        if (CollectionUtils.isEmpty(wordsList) || wordsList.size() != 12){
            return;
        }

        //设置fragment数据
        fragments = new ArrayList<>();

        fragments.add(BackupFragment1.getInstance(wordsList.subList(0,4).toArray(new String[4])));
        fragments.add(BackupFragment2.getInstance(wordsList.subList(4,8).toArray(new String[4])));
        fragments.add(BackupFragment3.getInstance(wordsList.subList(8,12).toArray(new String[4])));

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

                mBinding.tvPageNum.setText(position+1+"");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 不同界面获取不同数据
     *
     * @return
     */
    public List<String> getMemonic() {
        if (isFromWalletTool) {
            return WalletHelper.getHelpWordsListByUserId(WalletHelper.WALLET_USER);
        }

        WalletDBModel walletDBModel = JSON.parseObject(SPUtilHelper.getWalletCache(), WalletDBModel.class);

        if (walletDBModel == null) return null;

        return StringUtils.splitAsList(walletDBModel.getHelpWordsEn(), WalletHelper.HELPWORD_SPACE_SYMBOL);
    }

    protected void setCurrentItem(int index){
        if (null != mBinding)
            mBinding.vpSignUp.setCurrentItem(index);
    }

    protected boolean isFromWalletTool(){
        return isFromWalletTool;
    }

    @Override
    public void onBackPressed() {
        showDoubleWarnListen(getStrRes(R.string.exit_confirm), view -> {
            finish();
        });
    }

}

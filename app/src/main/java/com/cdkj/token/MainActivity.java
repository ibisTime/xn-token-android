package com.cdkj.token;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.adapters.ViewPagerAdapter;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.consult.ConsultFragment;
import com.cdkj.token.databinding.ActivityMainBinding;
import com.cdkj.token.model.VersionModel;
import com.cdkj.token.user.UserFragment;
import com.cdkj.token.wallet.WalletFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.UpdateUtil.isForceUpload;
import static com.cdkj.token.utils.UpdateUtil.startWeb;

public class MainActivity extends AbsBaseActivity {

    private ActivityMainBinding mBinding;

    public static final int WALLET = 0;
    public static final int CONSULT = 1;
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
        initListener();
        init();
        initViewPager();
        getVersion();
    }


    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }


    private void init() {
        UIStatusBarHelper.translucent(this);
        UIStatusBarHelper.setStatusBarLightMode(this);
        setShowIndex(WALLET);

//        CoinListService.open(this);
    }

    /**
     * 初始化事件
     */
    private void initListener() {

        mBinding.layoutMainBottom.llConsult.setOnClickListener(v -> {
            setShowIndex(CONSULT);

        });

        mBinding.layoutMainBottom.llWallet.setOnClickListener(v -> {
            setShowIndex(WALLET);

        });

        mBinding.layoutMainBottom.llMy.setOnClickListener(v -> {
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
        fragments.add(ConsultFragment.getInstance());
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

    /**
     * 获取最新版本
     *
     * @return
     */
    private void getVersion() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "android-c");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("660918", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(this) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null)
                    return;

                if (data.getVersion() > AppUtils.getAppVersionCode(MainActivity.this)) {  //版本号不一致说明有更新
                    showUploadDialog(data);
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 显示更新dialog
     *
     * @param versionModel
     */
    private void showUploadDialog(VersionModel versionModel) {

        if (isFinishing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getStrRes(R.string.tip_update))
                .setMessage(versionModel.getNote())
                .setPositiveButton(getStrRes(R.string.confirm), (dialogInterface, i) -> {
                    startWeb(MainActivity.this, versionModel.getDownloadUrl());
                    EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                    finish();
                })
                .setCancelable(false);


        if (isForceUpload(versionModel.getForceUpdate())) { // 强制更新
            builder.show();
        } else {
            builder.setNegativeButton(getStrRes(R.string.cancel), null).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        CoinListService.close(this);
    }
}

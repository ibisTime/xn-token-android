package com.cdkj.token.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.MainActivity;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.interfaces.StartPagePresenter;
import com.cdkj.token.interfaces.StartPageView;
import com.cdkj.token.model.CountryCodeMode;
import com.cdkj.token.model.IpCountryInfo;
import com.cdkj.token.model.SystemParameterModel;
import com.cdkj.token.model.VersionModel;
import com.cdkj.token.utils.wallet.WalletDBAegisUtils;
import com.cdkj.token.wallet.create_guide.CreateWalletStartActivity;
import com.cdkj.token.wallet.import_guide.ImportWalletStartActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cdkj.token.utils.UpdateUtil.isForceUpload;
import static com.cdkj.token.utils.UpdateUtil.startWeb;
import static com.cdkj.token.utils.wallet.WalletHelper.HELPWORD_SPACE_SYMBOL;

/**
 * 启动页
 */
@Route(path = CdRouteHelper.APPSTART)
public class StartActivity extends BaseActivity implements StartPageView {


    public StartPagePresenter pagePresenter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity
        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_start);

        pagePresenter = new StartPagePresenter(this);
        pagePresenter.start();

    }

    @Override
    public void addNetCall(Call call) {
        addCall(call);
    }

    @Override
    public void versionUpdateDialog(VersionModel versionModel) {
        if (isForceUpload(versionModel.getForceUpdate())) { // 强制更新
            showSureDialog(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(StartActivity.this, versionModel.getDownloadUrl());
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                finish();
            });

        } else {
            showDoubleWarnListen(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(StartActivity.this, versionModel.getDownloadUrl());
            }, view -> {
                if (pagePresenter != null) {
                    pagePresenter.noUpdate();
                }
            });
        }
    }

    @Override
    public void onStartPageEnd() {
        finish();
    }


    @Override
    protected void onDestroy() {
        if (pagePresenter != null) {
            pagePresenter.clear();
        }
        super.onDestroy();
    }

    /**
     * 显示确认取消弹框
     *
     * @param title
     * @param content
     * @param onPositiveListener
     * @param onNegativeListener
     */
    protected void showDoubleWarnListen(String title, String content, CommonDialog.OnPositiveListener onPositiveListener, CommonDialog.OnNegativeListener onNegativeListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(title).setContentMsg(content)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(getString(com.cdkj.baselibrary.R.string.activity_base_cancel), onNegativeListener, false);

        commonDialog.show();
    }

    /**
     * 只显示确认弹框的按钮
     *
     * @param title
     * @param str
     * @param onPositiveListener
     */
    protected void showSureDialog(String title, String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (this == null || isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(title)
                .setContentMsg(str)
                .setPositiveBtn(getString(com.cdkj.baselibrary.R.string.activity_base_confirm), onPositiveListener);

        commonDialog.show();
    }

}

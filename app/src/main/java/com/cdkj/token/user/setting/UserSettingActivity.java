package com.cdkj.token.user.setting;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.api.MyApi;
import com.cdkj.token.databinding.ActivityUserSettingBinding;
import com.cdkj.token.model.VersionModel;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.token.utils.UpdateUtil.isForceUpload;
import static com.cdkj.token.utils.UpdateUtil.startWeb;

/**
 * 设置
 * Created by cdkj on 2018/8/8.
 */
public class UserSettingActivity extends AbsActivity {

    private ActivityUserSettingBinding mBinding;

    private VersionModel versionModel;
    private boolean isUpdate;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_setting, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.setting));
        initClickListener();
        getVersion();
    }

    void initClickListener() {
        //本地货币
        mBinding.linLayoutLocalCoin.setOnClickListener(view -> {
            LocalMarketTypeChooseActivity.open(this);
        });
        //语言
        mBinding.linLayoutLanguage.setOnClickListener(view -> UserLanguageActivity.open(this));

        //版本更新
        mBinding.versionUpdate.setOnClickListener(view -> {
            if (versionModel != null && isUpdate) {
                showUploadDialog(versionModel);
            } else {
                UserAboutActivity.open(this);
            }
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
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getVersion("660918", StringUtils.getRequestJsonString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<VersionModel>(this) {

            @Override
            protected void onSuccess(VersionModel data, String SucMessage) {
                if (data == null) {
                    mBinding.tvVersionInfo.setText(AppUtils.getAppVersionName(UserSettingActivity.this));
                    return;
                }
                versionModel = data;
                //版本号不一致说明有更新
                isUpdate = data.getVersion() > AppUtils.getAppVersionCode(UserSettingActivity.this);
                if (isUpdate) {
                    mBinding.tvVersionInfo.setText(R.string.version_update_info);
                } else {
                    mBinding.tvVersionInfo.setText(AppUtils.getAppVersionName(UserSettingActivity.this));
                }
            }

            @Override
            protected void onReqFailure(String errorCode, String errorMessage) {

            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });
    }


    /**
     * 显示更新dialog
     *
     * @param versionModel
     */
    private void showUploadDialog(VersionModel versionModel) {

        if (isForceUpload(versionModel.getForceUpdate())) { // 强制更新
            showSureDialog(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(UserSettingActivity.this, versionModel.getDownloadUrl());
                EventBus.getDefault().post(new AllFinishEvent()); //结束所有界面
                finish();
            });

        } else {
            showDoubleWarnListen(getStrRes(R.string.tip_update), versionModel.getNote(), view -> {
                startWeb(UserSettingActivity.this, versionModel.getDownloadUrl());
            });
        }
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

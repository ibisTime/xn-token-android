package com.cdkj.token.user;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.model.AllFinishEvent;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityUserAboutBinding;

import org.greenrobot.eventbus.EventBus;

import static com.cdkj.token.utils.UpdateUtil.startWeb;


/**
 * Created by lei on 2018/1/5.
 */

public class UserAboutActivity extends AbsStatusBarTranslucentActivity {

    private ActivityUserAboutBinding mBinding;


    public static void open(Context context) {
        if (context == null)
            return;

        context.startActivity(new Intent(context, UserAboutActivity.class));
    }


    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_about, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setMidTitle(getStrRes(R.string.user_about));
        mBinding.tvVersionName.setText(getString(R.string.version_num, AppUtils.getAppVersionName(this)));
        setPageBgImage(R.drawable.my_bg);
    }
}

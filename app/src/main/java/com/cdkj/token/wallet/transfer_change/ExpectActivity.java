package com.cdkj.token.wallet.transfer_change;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityExpectBinding;

import static com.cdkj.token.find.NoneActivity.FLASH;
import static com.cdkj.token.find.NoneActivity.FRIENDS;

/**
 * Created by cdkj on 2018/12/12.
 */

public class ExpectActivity extends AbsActivity {

    private ActivityExpectBinding mBinding;

    /**
     * @param context
     * @param showType
     */
    public static void open(Context context, int showType) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ExpectActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, showType);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_expect,null,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        int location = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);

        switch (location) {
            case FLASH:
                setTopTitle(getString(R.string.transfer_change));
                ImgUtils.loadActImgId(this, R.mipmap.expect_flash, mBinding.ivIcon);
                break;
            case FRIENDS:
                setTopTitle("通讯录");
                ImgUtils.loadActImgId(this, R.mipmap.expect_friends, mBinding.ivIcon);
                break;
        }

    }
}

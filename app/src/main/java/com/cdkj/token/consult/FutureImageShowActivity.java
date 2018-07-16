package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityFutureImgShowBinding;

import static com.cdkj.token.consult.NoneActivity.FLASH;
import static com.cdkj.token.consult.NoneActivity.ONE_CLICK;

/**
 * 功能说明图片展示
 * Created by cdkj on 2018/7/16.
 */

public class FutureImageShowActivity extends AbsBaseLoadActivity {

    private ActivityFutureImgShowBinding mBinding;

    /**
     * @param context
     * @param showType
     */
    public static void open(Context context, int showType) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FutureImageShowActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, showType);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_future_img_show, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        int location = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);

        switch (location) {
            case FLASH:
                mBaseBinding.titleView.setMidTitle(getString(R.string.transfer_change));
                ImgUtils.loadActImgId(this, R.drawable.flash_explanation, mBinding.img);
                break;
            case ONE_CLICK:
                mBaseBinding.titleView.setMidTitle(getString(R.string.fast_transfer));
                ImgUtils.loadActImgId(this, R.drawable.one_click_explanation, mBinding.img);
                break;
        }

    }


}

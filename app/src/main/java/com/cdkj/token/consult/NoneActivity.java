package com.cdkj.token.consult;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.base.AbsActivity;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityNoneBinding;

/**
 * Created by lei on 2018/3/29.
 */

public class NoneActivity extends AbsActivity {

    private ActivityNoneBinding mBinding;

    public static final int FIRST_CREATE = 1; //全球首创

    public static final int YBB = 2;//余币宝

    public static final int LHLC = 3;//量化理财

    public static final int FLASH = 4;//闪兑

    public static final int ONE_CLICK = 5;//一键划转

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity, int location) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, NoneActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, location);
        activity.startActivity(intent);

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_none, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

//        setTopTitle(getString(R.string.consult_none_title));

        setSubLeftImgState(true);
        setTopLineState(false);

        if (getIntent() == null) return;

        int location = getIntent().getIntExtra(CdRouteHelper.DATASIGN, 0);

        switch (location) {
            case 0:
                setTopTitle(getString(R.string.consult_none_title));
//                mBinding.tvContent.setText(R.string.please_wait);
                mBinding.tvContent.setVisibility(View.GONE);
                mBinding.tvTitle.setText(R.string.consult_none_title);
                mBinding.tvTitle.setGravity(Gravity.CENTER);
                return;

            case FIRST_CREATE:
                mBinding.tvContent.setText(R.string.waite_content_1);
                mBinding.tvTitle.setText(R.string.waite_title_1);
                setTopTitle(getString(R.string.consult_1));
                break;

            case YBB:
                mBinding.tvContent.setText(R.string.waite_content_2);
                mBinding.tvTitle.setText(R.string.wait_title_2);
                setTopTitle(getString(R.string.yibibao));
                break;

            case LHLC:
                mBinding.tvContent.setText(R.string.waite_content_3);
                mBinding.tvTitle.setText(R.string.waite_title_3);
                setTopTitle(getString(R.string.lianghualicai));
                break;

            case FLASH:

                mBinding.tvTitle.setVisibility(View.GONE);
                mBinding.tvContent.setVisibility(View.GONE);

                mBinding.tvContent2.setVisibility(View.VISIBLE);
                mBinding.tvContent2.setText(R.string.please_wait);
                setTopTitle(getString(R.string.transfer_change));
                break;
            case ONE_CLICK:
                mBinding.tvTitle.setVisibility(View.GONE);
                mBinding.tvContent.setVisibility(View.GONE);

                mBinding.tvContent2.setVisibility(View.VISIBLE);
                mBinding.tvContent2.setText(R.string.please_wait);
                setTopTitle(getString(R.string.fast_transfer));
                break;
        }

    }
}

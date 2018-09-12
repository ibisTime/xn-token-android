package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.base.AbsStatusBarTranslucentActivity;
import com.cdkj.baselibrary.dialog.UITipDialog;
import com.cdkj.baselibrary.utils.BitmapUtils;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteQrBinding;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 邀请有礼
 * Created by cdkj on 2018/8/8.
 */

public class InviteQrActivity extends AbsStatusBarTranslucentActivity {

    private ActivityInviteQrBinding mBinding;


    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteQrActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addContentView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_invite_qr, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setWhiteTitle();
        setStatusBarWhite();
        setMidTitle(getString(R.string.invite_gift));
        setPageBgImage(R.mipmap.invite_bg);

        setClickListener();

    }

    private void setClickListener() {

        // 本地保存
        mBinding.llSave.setOnClickListener(view -> {



        });

        // 微信
        mBinding.llWx.setOnClickListener(view -> {

            showLoadingDialog();
            mSubscription.add(Observable.just(screenshot())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.newThread())  //创建
                    .map(o -> BitmapUtils.WeChatBitmapToByteArray(o))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
//                        WxUtil.shareBitmapToWX(InviteQrActivity.this, path);
                        disMissLoadingDialog();

                        // 还原UI
                        mBinding.llDo.setVisibility(View.VISIBLE);
                    }, throwable -> {
                        disMissLoadingDialog();
                        UITipDialog.showFail(this, getString(R.string.info_share_fail));
                        LogUtil.E("微信分享错误" + throwable.toString());

                        // 还原UI
                        mBinding.llDo.setVisibility(View.VISIBLE);
                    }));

        });

        // 朋友圈
        mBinding.llPyq.setOnClickListener(view -> {

            showLoadingDialog();
            mSubscription.add(Observable.just(screenshot())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.newThread())  //创建
                    .map(o -> BitmapUtils.WeChatBitmapToByteArray(o))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
//                        WxUtil.shareBitmapToWXPYQ(InviteQrActivity.this, path);
                        disMissLoadingDialog();

                        // 还原UI
                        mBinding.llDo.setVisibility(View.VISIBLE);
                    }, throwable -> {
                        disMissLoadingDialog();
                        UITipDialog.showFail(this, getString(R.string.info_share_fail));
                        LogUtil.E("微信朋友圈分享错误" + throwable.toString());

                        // 还原UI
                        mBinding.llDo.setVisibility(View.VISIBLE);
                    }));

        });

        // 微博
        mBinding.llWb.setOnClickListener(view -> {

        });

    }

    private Bitmap screenshot(){
        mBinding.llDo.setVisibility(View.GONE);
        return BitmapUtils.getBitmapByView(mBinding.llRoot);
    }
}

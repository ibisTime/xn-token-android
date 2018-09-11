package com.cdkj.token.user.invite;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;

import com.cdkj.baselibrary.appmanager.AppConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.UIStatusBarHelper;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityInviteFriendQrcodeBinding;
import com.cdkj.token.common.ThaAppConstant;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 邀请好友二维码
 * Created by cdkj on 2018/8/9.
 */

public class InviteFriendQrImgShowActivity extends BaseActivity {

    private ActivityInviteFriendQrcodeBinding mBinding;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, InviteFriendQrImgShowActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friend_qrcode);
        UIStatusBarHelper.setStatusBarLightMode(this);
        UIStatusBarHelper.translucent(this);

        mBinding.imgBack.setOnClickListener(view -> finish());

        mBinding.tvName.setText(getString(R.string.invite_my_name, SPUtilHelper.getUserName()));

        getRedPacketShareUrlRequest();
    }


    public void getRedPacketShareUrlRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
        map.put("systemCode", AppConfig.SYSTEMCODE);
        map.put("companyCode", AppConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                Bitmap bitmap = CodeUtils.createImage(data.getCvalue() + ThaAppConstant.getInviteFriendUrl(SPUtilHelper.getSecretUserId()), 500, 500, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();

                ImgUtils.loadByte(InviteFriendQrImgShowActivity.this, datas, mBinding.ivQrImg);
            }

            @Override
            protected void onFinish() {
                disMissLoadingDialog();
            }
        });

    }


}

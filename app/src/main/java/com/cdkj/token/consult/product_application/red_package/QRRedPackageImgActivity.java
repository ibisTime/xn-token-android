package com.cdkj.token.consult.product_application.red_package;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.CdRouteHelper;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.token.R;
import com.cdkj.token.databinding.ActivityQrredPackageImgBinding;
import com.cdkj.token.model.RedPackageEventBusBean;
import com.cdkj.token.utils.ThaAppConstant;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


public class QRRedPackageImgActivity extends BaseActivity {

    ActivityQrredPackageImgBinding mBinding;
    private String redPackageCode;

    public static void open(Context context, String redPackageCode) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, QRRedPackageImgActivity.class);
        intent.putExtra(CdRouteHelper.DATASIGN, redPackageCode);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_qrred_package_img);
        if (getIntent() != null) {
            redPackageCode = getIntent().getStringExtra(CdRouteHelper.DATASIGN);
        }
        mBinding.llOther.setOnClickListener(view -> {
            EventBus.getDefault().post(new RedPackageEventBusBean());
            finish();
        });

        ImgUtils.loadLogo(QRRedPackageImgActivity.this, SPUtilHelper.getUserPhoto(), mBinding.imgLogo);

        mBinding.tvUserName.setText(SPUtilHelper.getUserName());

        getRedPacketShareUrlRequest();
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new RedPackageEventBusBean());
        finish();
    }


    public void getRedPacketShareUrlRequest() {

        Map<String, String> map = new HashMap<>();
        map.put("ckey", "redPacketShareUrl");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }

                Bitmap bitmap = CodeUtils.createImage(data.getCvalue() + ThaAppConstant.getRedPacketShareUrl(redPackageCode, SPUtilHelper.getSecretUserId()), 500, 500, null);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] datas = baos.toByteArray();

                ImgUtils.loadByte(QRRedPackageImgActivity.this, datas, mBinding.ivQrImg);

                mBinding.ivQrImg.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}

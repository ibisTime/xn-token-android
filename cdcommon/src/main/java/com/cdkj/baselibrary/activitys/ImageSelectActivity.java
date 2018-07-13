package com.cdkj.baselibrary.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.interfaces.CameraPhotoListener;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 打开相机 相册 图片裁剪 功能
 */
public class ImageSelectActivity extends Activity implements View.OnClickListener, CameraPhotoListener {

    private TextView tv_take_capture;// 拍照
    private TextView tv_alumb;// 相册选取
    private TextView tv_cancle;// 取消
    private View empty_view;// 取消

    private boolean isSplit = false;//执行相机或拍照后是否需要裁剪 默认不需要

    public static final int SHOWPIC = 1; //显示拍照按钮
    public static final int SHOWALBUM = 2;//显示相册

    private CameraHelper cameraHelper;
    public static final String IMAGE_URL = "/ogc/ocoin/";
    public  final static String staticPath="ogc_pic";
    /**
     * @param activity
     * @param photoid
     * @param showType 显示的按钮
     * @param isSplit  是否裁剪
     */
    public static void launch(Activity activity, int photoid, int showType, boolean isSplit) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageSelectActivity.class);
        intent.putExtra("showType", showType);
        intent.putExtra("isSplit", isSplit);
        activity.startActivityForResult(intent, photoid);
    }

    public static void launch(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageSelectActivity.class);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchFragment(Fragment fragment, int photoid) {
        if (fragment == null || fragment.getActivity() == null) {
            return;
        }
        Intent intent = new Intent(fragment.getActivity(), ImageSelectActivity.class);
        fragment.startActivityForResult(intent, photoid);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);
        initLayout();
        initVar();
    }

    private void initVar() {
        if (getIntent() != null) {
            isSplit = getIntent().getBooleanExtra("isSplit", isSplit); //获取是否裁剪
            switch (getIntent().getIntExtra("showType", 0)) {      //根据参数显示相册按钮还是显示拍照按钮 默认两个都显示
                case SHOWPIC:
                    tv_take_capture.setVisibility(View.VISIBLE);
                    tv_alumb.setVisibility(View.GONE);
                    break;
                case SHOWALBUM:
                    tv_take_capture.setVisibility(View.GONE);
                    tv_alumb.setVisibility(View.VISIBLE);
                    break;
                default:
                    tv_take_capture.setVisibility(View.VISIBLE);
                    tv_alumb.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            tv_take_capture.setVisibility(View.VISIBLE);
            tv_alumb.setVisibility(View.VISIBLE);
        }
        cameraHelper = new CameraHelper(this, this);
        cameraHelper.setSplit(isSplit);
    }

    protected void initLayout() {
        tv_take_capture = (TextView) findViewById(R.id.tv_take_capture);
        tv_alumb = (TextView) findViewById(R.id.tv_alumb);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        empty_view = findViewById(R.id.empty_view);

        tv_take_capture.setOnClickListener(this);
        tv_alumb.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        empty_view.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_take_capture) {
            cameraHelper.startCamera();
        } else if (i == R.id.tv_alumb) {
            cameraHelper.startAlbum();
        } else if (i == R.id.empty_view || i == R.id.tv_cancle) {
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //拍照回调
        cameraHelper.onActivityResult(requestCode, resultCode, data);
    }


    //权限申请回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraHelper != null) {
            cameraHelper.clear();
        }
    }

    @Override
    public void onPhotoSuccessful(int code, String path) {
        setResult(Activity.RESULT_OK, new Intent().putExtra(CameraHelper.staticPath, path));
        finish();
    }

    @Override
    public void onPhotoFailure(int code, String msg) {
        if(!TextUtils.isEmpty(msg)){
            ToastUtil.show(this, msg);
        }
        finish();
    }

    @Override
    public void noPermissions(int code) {
        ToastUtil.show(this, getString(R.string.no_camera_permission));
    }

    /**
     * 保存到系统相册
     *
     * @param context
     * @param bmp
     */
    public static void saveImageToGallery(final Context context, final Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), IMAGE_URL);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

}

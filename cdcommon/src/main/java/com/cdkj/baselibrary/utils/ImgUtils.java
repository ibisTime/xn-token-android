package com.cdkj.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

/**
 * 图片加载工具类
 * Created by Administrator on 2016-09-14.
 */
public class ImgUtils {

    public static void loadLogo(Activity context, String imgid, ImageView img) {
        if (!AppUtils.isActivityExist(context)) {
            return;
        }
        if (img == null) {
            return;
        }

        if (isHaveHttp(imgid)) {
            GlideApp.with(context).load(imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new CircleCrop()).into(img);
        } else {
            GlideApp.with(context).load(SPUtilHelper.getQiniuUrl() + imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new CircleCrop()).into(img);
        }

    }

    public static void loadCircleImg(Activity context, String imgid, ImageView img) {
        if (!AppUtils.isActivityExist(context)) {
            return;
        }
        if (img == null) {
            return;
        }

        if (isHaveHttp(imgid)) {
            GlideApp.with(context).load(imgid).transform(new CircleCrop()).into(img);
        } else {
            GlideApp.with(context).load(SPUtilHelper.getQiniuUrl() + imgid).transform(new CircleCrop()).into(img);
        }

    }


    public static void loadByte(Activity context, byte[] imgid, ImageView img) {
        if (!AppUtils.isActivityExist(context)) {
            return;
        }
        if (context == null || img == null) {
            return;
        }

        GlideApp.with(context).load(imgid).dontAnimate().into(img);
    }

    /**
     * 判断是否有http头
     *
     * @param url
     * @return
     */
    public static boolean isHaveHttp(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return TextUtils.indexOf(url, "http://") != -1 || TextUtils.indexOf(url, "https://") != -1;
    }

    public static void loadLogo(Context context, String imgid, ImageView img) {
        if (context == null || img == null) {
            return;
        }
        LogUtil.E("图片" + imgid);

        try {
            if (isHaveHttp(imgid)) {
                GlideApp.with(context).load(imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new CircleCrop()).into(img);
            } else {
                GlideApp.with(context).load(SPUtilHelper.getQiniuUrl() + imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new CircleCrop()).into(img);
            }
        } catch (Exception e) {
        }
    }


    public static void loadActImg(Activity context, String imgid, ImageView img) {

        if (!AppUtils.isActivityExist(context)) {
            return;
        }

        if (context == null || img == null) {
            return;
        }

        if (isHaveHttp(imgid)) {
            GlideApp.with(context).load(imgid).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(img);
        } else {
            GlideApp.with(context).load(SPUtilHelper.getQiniuUrl() + imgid).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(img);
        }


    }

    public static void loadActImgId(Activity context, int imgid, ImageView img) {

        if (!AppUtils.isActivityExist(context)) {

            return;
        }

        if (context == null || img == null) {
            return;
        }

        try {
            GlideApp.with(context).load(imgid).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(img);
        } catch (Exception e) {

        }

    }


    public static void loadFraImgId(Fragment context, int imgid, ImageView img) {

        if (context == null || img == null) {
            return;
        }


        try {
            GlideApp.with(context).load(imgid).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(img);
        } catch (Exception e) {
        }

    }

    public static void loadImage(Context context, String path, ImageView iv) {

        if (context == null)
            return;


        if (isHaveHttp(path)) {
            GlideApp.with(context)
                    .load(path)
                    .into(iv);
        } else {

            GlideApp.with(context)
                    .load(SPUtilHelper.getQiniuUrl() + path)
                    .into(iv);
        }

    }

    public static void loadLocalImage(Context context, String path, ImageView iv) {

        if (context == null)
            return;

        GlideApp.with(context)
                .load(path)
                .into(iv);

    }


}

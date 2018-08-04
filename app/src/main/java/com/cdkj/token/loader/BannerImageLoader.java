package com.cdkj.token.loader;

import android.content.Context;
import android.widget.ImageView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.GlideApp;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.token.R;
import com.cdkj.token.model.BannerModel;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        GlideApp.with(context)
                .load(getImgUrl(path))
                .placeholder(R.drawable.default_pic)
                .into(imageView);
    }


    public String getImgUrl(Object object) {
        if (object == null) return "";
        String url;
        if (object instanceof BannerModel) {
            BannerModel banner = (BannerModel) object;
            url = banner.getPic();
        } else {
            url = object.toString();
        }

        if (ImgUtils.isHaveHttp(url)) {
            return url;
        }

        return SPUtilHelper.getQiniuUrl() + url;


    }

}

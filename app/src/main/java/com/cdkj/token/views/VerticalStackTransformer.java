package com.cdkj.token.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.cdkj.baselibrary.utils.DisplayHelper;


/**
 * Created by Nate on 2016/7/22.
 */
public class VerticalStackTransformer implements ViewPager.PageTransformer {

    private Context context;
    private int spaceBetweenFirAndSecWith = 20 * 2;//第一张卡片和第二张卡片宽度差  dp单位
    private int spaceBetweenFirAndSecHeight = 10;//第一张卡片和第二张卡片高度差   dp单位

    public VerticalStackTransformer(Context context) {
        this.context = context;
    }

    public VerticalStackTransformer(Context context, int spaceBetweenFirAndSecWith, int spaceBetweenFirAndSecHeight) {
        this.context = context;
        this.spaceBetweenFirAndSecWith = spaceBetweenFirAndSecWith;
        this.spaceBetweenFirAndSecHeight = spaceBetweenFirAndSecHeight;
    }


    @Override
    public void transformPage(View page, float position) {

        final float width = page.getWidth();

        page.setRotationX(0);
        page.setRotationY(0);
        page.setRotation(0);
        page.setScaleX(1);
        page.setScaleY(1);
        page.setPivotX(0);
        page.setPivotY(0);
        page.setTranslationY(0);
        page.setTranslationX(true ? 0f : -width * position);


        if (position <= 0.0f) {
            page.setAlpha(1.0f);
            page.setTranslationX(0f);
            //控制停止滑动切换的时候，只有最上面的一张卡片可以点击
            page.setClickable(true);
        } else {
            float scale = (float) (page.getWidth() - DisplayHelper.dp2px(context, (int) (spaceBetweenFirAndSecWith * position))) / (float) (page.getWidth());
            //控制下面卡片的可见度
            page.setAlpha(1.0f);
            //控制停止滑动切换的时候，只有最上面的一张卡片可以点击
            page.setClickable(false);
            page.setPivotX(page.getWidth() / 2f);
            page.setPivotY(page.getHeight() / 2f);
            page.setScaleX(scale);
            page.setScaleY(scale);
            page.setTranslationX(-page.getWidth() * position + (page.getWidth() * 0.5f) * (1 - scale) + DisplayHelper.dp2px(context, 15) * position);
        }
    }
}

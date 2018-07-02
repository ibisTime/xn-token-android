package com.cdkj.token.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;

/**
 * 椭圆蓝点View 用于CardLayoutChangeLayout
 * Created by cdkj on 2018/7/3.
 */

public class OvalBlueView extends View {

    public OvalBlueView(Context context) {
        this(context, null);
    }

    public OvalBlueView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OvalBlueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 创建画笔

        RectF rectF2 = new RectF(0, 0, DisplayHelper.dp2px(getContext(), 20)+0.3f, DisplayHelper.dp2px(getContext(), 12)+0.3f);
        Paint p2 = new Paint();
        p2.setColor(Color.parseColor("#FFC7DFFB"));
        canvas.drawRoundRect(rectF2, 15, 15, p2);

        Paint p = new Paint();
        p.setColor(ContextCompat.getColor(getContext(), R.color.blue));
        RectF rectF = new RectF(DisplayHelper.dp2px(getContext(), 2), DisplayHelper.dp2px(getContext(), 2), DisplayHelper.dp2px(getContext(), 18), DisplayHelper.dp2px(getContext(), 10));
        canvas.drawRoundRect(rectF, 15, 15, p);

    }
}

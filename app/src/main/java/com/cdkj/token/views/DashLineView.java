package com.cdkj.token.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cdkj.token.R;

/**
 * 虚线
 * Created by cdkj on 2018/7/23.
 */
public class DashLineView extends View {
    private Paint mPaint;


    public DashLineView(Context context) {
        this(context, null);
    }

    public DashLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#BAC1C8"));
        mPaint.setStrokeWidth(3);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerY = getHeight() / 2;
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawLine(0, centerY, getWidth(), centerY, mPaint);
    }
}


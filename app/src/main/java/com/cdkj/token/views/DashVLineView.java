package com.cdkj.token.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cdkj.baselibrary.utils.DisplayHelper;
import com.cdkj.token.R;

/**
 * 虚线 水平方向
 * Created by cdkj on 2018/7/23.
 */
public class DashVLineView extends View {

    private Paint mPaint;

    public DashVLineView(Context context) {
        this(context, null);
    }

    public DashVLineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashVLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#BAC1C8"));
        mPaint.setStrokeWidth(3);
        mPaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), mPaint);
    }
}


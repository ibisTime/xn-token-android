package com.cdkj.token.views.pattern_lock;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by cdkj on 22/02/2018.
 */

public class DefaultLockerNormalCellView implements INormalCellView {

    private @ColorInt
    int normalColor;
    private @ColorInt
    int fillColor;

    private float lineWidth;

    private Paint paint;

    public DefaultLockerNormalCellView() {
        this.paint = PatternLockViewConfig.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
    }

    public int getNormalColor() {
        return normalColor;
    }

    public DefaultLockerNormalCellView setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    public int getFillColor() {
        return fillColor;
    }

    public DefaultLockerNormalCellView setFillColor(int fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public DefaultLockerNormalCellView setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        return this;
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull LockPointModel cellBean) {
        final int saveCount = canvas.save();

        // draw outer circle
        this.paint.setColor(this.getNormalColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint);

        // draw fill circle
        this.paint.setColor(this.getFillColor());
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint);

        canvas.restoreToCount(saveCount);
    }
}

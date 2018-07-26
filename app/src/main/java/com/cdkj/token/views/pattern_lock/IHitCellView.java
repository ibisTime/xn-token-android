package com.cdkj.token.views.pattern_lock;

import android.graphics.Canvas;
import android.support.annotation.NonNull;

/**
 * Created by cdkj on 22/02/2018.
 */

public interface IHitCellView {
    /**
     * 绘制已设置的每个图案的样式
     *
     * @param canvas
     * @param cellBean
     * @param isError
     */
    void draw(@NonNull Canvas canvas, @NonNull LockPointModel cellBean, boolean isError);
}

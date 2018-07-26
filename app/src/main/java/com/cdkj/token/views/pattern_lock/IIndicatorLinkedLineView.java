package com.cdkj.token.views.pattern_lock;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by cdkj on 22/02/2018.
 */

public interface IIndicatorLinkedLineView {
    /**
     * 绘制指示器连接线
     *
     * @param canvas
     * @param hitList
     * @param cellBeanList
     * @param isError
     */
    void draw(@NonNull Canvas canvas,
              @Nullable List<Integer> hitList,
              @NonNull List<LockPointModel> cellBeanList,
              boolean isError);
}
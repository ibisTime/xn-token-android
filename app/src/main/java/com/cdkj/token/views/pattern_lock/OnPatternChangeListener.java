package com.cdkj.token.views.pattern_lock;

import java.util.List;

/**
 * Created by cdkj on 14/10/2017.
 */

public interface OnPatternChangeListener {
    /**
     * 开始绘制图案时（即手指按下触碰到绘画区域时）会调用该方法
     *
     * @param view
     */
    void onStart(PatternLockView view);

    /**
     * 图案绘制改变时（即手指在绘画区域移动时）会调用该方法，请注意只有 @param hitList改变了才会触发此方法
     *
     * @param view
     * @param hitList
     */
    void onChange(PatternLockView view, List<Integer> hitList);

    /**
     * 图案绘制完成时（即手指抬起离开绘画区域时）会调用该方法
     *
     * @param view
     * @param hitList
     */
    void onComplete(PatternLockView view, List<Integer> hitList);

    /**
     * 已绘制的图案被清除时会调用该方法
     *
     * @param view
     */
    void onClear(PatternLockView view);
}
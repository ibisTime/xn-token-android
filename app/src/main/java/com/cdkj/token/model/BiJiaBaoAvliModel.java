package com.cdkj.token.model;

/**
 * 获取可购买区间
 * Created by cdkj on 2018/9/28.
 */

public class BiJiaBaoAvliModel {

    private int min;//最小可购买份数
    private int max;//最大可购买份数

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}

package com.cdkj.token.views.pattern_lock;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于创建密码点
 * Created by cdkj on 20/09/2017.
 */

class CellFactory {
    private int width;
    private int height;
    private List<LockPointModel> cellBeanList;

    public CellFactory(int width, int height) {
        this.width = width;
        this.height = height;
        this.cellBeanList = new ArrayList<>();
        this.create();
    }

    private void create() {
        final float pWidth = this.width / 8f;
        final float pHeight = this.height / 8f;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.cellBeanList.add(new LockPointModel(
                        (i * 3 + j),
                        (j * 3 + 1) * pWidth,
                        (i * 3 + 1) * pHeight,
                        pWidth));
            }
        }
    }

    public List<LockPointModel> getCellBeanList() {
        return this.cellBeanList;
    }
}
package com.cdkj.token.model;

/**自选币种改变
 * Created by cdkj on 2018/7/1.
 */

public class AddCoinChangeEvent {

    public static String PRI = "private";
    public static String NOT_PRI = "not_private";

    private String tag;

    public String getTag() {
        return tag;
    }

    public AddCoinChangeEvent setTag(String tag) {
        this.tag = tag;
        return this;
    }
}

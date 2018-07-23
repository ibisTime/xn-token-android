package com.cdkj.token.model;

/**
 * Created by cdkj on 2018/7/23.
 */

public class HelpWordsCheckModel {

    private String words;

    public boolean isChoose;

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}

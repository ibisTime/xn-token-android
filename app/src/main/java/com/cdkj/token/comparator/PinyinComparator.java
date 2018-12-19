package com.cdkj.token.comparator;

import com.cdkj.token.model.CountryCodeMode;

import java.util.Comparator;

/**
 * Created by cdkj on 2018/12/13.
 */


public class PinyinComparator implements Comparator<CountryCodeMode> {

    public int compare(CountryCodeMode sort1, CountryCodeMode sort2) {
        if (sort2.getSort().equals("#")) {
            return -1;
        } else if (sort1.getSort().equals("#")) {
            return 1;
        } else {
            return sort1.getSort().compareTo(sort2.getSort());
        }
    }

}

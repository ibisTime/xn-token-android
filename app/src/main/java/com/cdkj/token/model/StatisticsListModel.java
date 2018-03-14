package com.cdkj.token.model;

import java.util.List;

/**
 * Created by cdkj on 2018/3/14.
 */

public class StatisticsListModel {


    /**
     * list : [{"creates":"2018-03-12 17:00:16","hash":"0x37284535fc866ad808eafb0daa3ae70e774ce5c44521b30595f384d0eb125d4c","tokenFrom":"0x8e979a7621314e0ba506c54a2482f7f49a99ef44","tokenTo":"0xe6b167f1fbdf6363e509908e8594affa16410902","tokenValue":"1000000000"}]
     * pageNO : 1
     * pageSize : 1
     * start : 0
     * totalCount : 4
     * totalPage : 4
     */

    private List<StatisticsModel> list;

    public List<StatisticsModel> getList() {
        return list;
    }

    public void setList(List<StatisticsModel> list) {
        this.list = list;
    }

}

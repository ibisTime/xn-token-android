package com.cdkj.baselibrary.api;

import java.util.List;

/**
 * 包含对象里的list
 * Created by cdkj on 2017/6/8.
 */

public class ResponseInListModel<T> {

    private List<T> list;
    private int pageNo;
    private int totalCount;
    private int pageSize;
    private int totalPage;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}

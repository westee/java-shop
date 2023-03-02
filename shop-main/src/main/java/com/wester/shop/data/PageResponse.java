package com.wester.shop.data;

import java.util.List;

public class PageResponse<T> {
    int pageNum;
    int pageSize;
    long count;
    List<T> data;

    public PageResponse() {}

    public PageResponse(int pageNum, int pageSize, long count, List<T> data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.count = count;
        this.data = data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getCount() {
        return count;
    }

    public List<T> getData() {
        return data;
    }

    public static PageResponse pageData(Integer pageNum, Integer pageSize, long count, List data) {
        return new PageResponse<>(pageNum, pageSize, count, data);
    }
}

package com.wester.api.data;

import com.wester.api.generate.Order;

import java.io.Serializable;
import java.util.List;

public class PageOrderResponse<T> implements Serializable {
    int pageNum;
    int pageSize;
    long totalPage;
    List<T> data;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> orders) {
        this.data = orders;
    }
}

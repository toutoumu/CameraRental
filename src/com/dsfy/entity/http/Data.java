package com.dsfy.entity.http;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回数据用的
 *
 * @param <T>
 */
public class Data<T> {
    @Expose
    private int total;
    @Expose
    private List<T> rows = new ArrayList<T>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}

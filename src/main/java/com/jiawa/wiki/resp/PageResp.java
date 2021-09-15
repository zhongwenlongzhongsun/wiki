package com.jiawa.wiki.resp;

import java.util.List;

public class PageResp<T> {
    private long total;      //总行数

    private List<T> list;   //由于列表里面的数据是不确定的，所以使用泛型

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageResp{");
        sb.append("total=").append(total);
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}
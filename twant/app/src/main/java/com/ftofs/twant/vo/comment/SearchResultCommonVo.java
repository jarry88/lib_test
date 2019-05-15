package com.ftofs.twant.vo.comment;

import java.util.List;

/**
 * @author liusf
 * @create 2019/4/9 14:14
 * @description ES搜索結果
 * @params
 * @return
 */
public class SearchResultCommonVo<T> {
    private int total;
    private List<T> resultList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}

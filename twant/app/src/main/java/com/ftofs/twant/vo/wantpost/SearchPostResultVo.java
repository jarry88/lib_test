package com.ftofs.twant.vo.wantpost;

import java.util.List;

/**
 * @Description: ES搜索返回
 * @Auther: yangjian
 * @Date: 2019/3/23 14:54
 */
public class SearchPostResultVo {
    /**
     * 總記錄
     */
    private int total;
    /**
     * 數據集合
     */
    private List<SearchWantPostVo> resultList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SearchWantPostVo> getResultList() {
        return resultList;
    }

    public void setResultList(List<SearchWantPostVo> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "SearchPostResultVo{" +
                "total=" + total +
                ", resultList=" + resultList +
                '}';
    }
}

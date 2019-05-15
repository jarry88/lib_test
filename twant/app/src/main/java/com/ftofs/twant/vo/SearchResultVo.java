package com.ftofs.twant.vo;

import com.ftofs.twant.vo.search.ElasticAggVo;

import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品搜索返回结果Vo
 *
 * @author dqw
 * Created 2017/6/7 14:00
 */
public class SearchResultVo {
    private int total;
    private List<SearchVo> goodsList;
    private List<ElasticAggVo> aggs;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SearchVo> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<SearchVo> goodsList) {
        this.goodsList = goodsList;
    }

    public List<ElasticAggVo> getAggs() {
        return aggs;
    }

    public void setAggs(List<ElasticAggVo> aggs) {
        this.aggs = aggs;
    }

    @Override
    public String toString() {
        return "SearchPostResultVo{" +
                "total=" + total +
                ", goodsList=" + goodsList +
                ", aggs=" + aggs +
                '}';
    }
}


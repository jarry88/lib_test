package com.ftofs.twant.vo.orders;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 订单搜索结果
 *
 * @author hbj
 * Created 2017/7/7 11:52
 */
public class SearchOrdersResultVo {
    /**
     * 总数量
     */
    private int total;
    /**
     * 订单列表
     */
    private List<SearchOrdersVo> ordersList = new ArrayList<>();
    private List<SearchOrdersStateResultVo> aggs = new ArrayList<>();

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SearchOrdersVo> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(List<SearchOrdersVo> ordersList) {
        this.ordersList = ordersList;
    }

    public List<SearchOrdersStateResultVo> getAggs() {
        return aggs;
    }

    public void setAggs(List<SearchOrdersStateResultVo> aggs) {
        this.aggs = aggs;
    }

    @Override
    public String toString() {
        return "SearchOrdersResultVo{" +
                "total=" + total +
                ", ordersList=" + ordersList +
                ", aggs=" + aggs +
                '}';
    }
}

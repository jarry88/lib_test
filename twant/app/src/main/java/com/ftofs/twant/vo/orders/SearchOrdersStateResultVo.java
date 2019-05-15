package com.ftofs.twant.vo.orders;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 订单搜索结果各订单状态下的数量信息
 *
 * @author hbj
 * Created 2017/7/7 11:56
 */
public class SearchOrdersStateResultVo {
    /**
     * 订单状态
     */
    private int orderState;
    /**
     * 总数量
     */
    private Long count;

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int ordersState) {
        this.orderState = ordersState;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "SearchOrdersStateResultVo{" +
                "orderState=" + orderState +
                ", count=" + count +
                '}';
    }
}

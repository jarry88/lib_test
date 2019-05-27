package com.ftofs.twant.vo.statistical;

import com.ftofs.twant.domain.statistics.StatOrders;

import java.math.BigDecimal;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 统计中订单数据缓存
 * 
 * @author zxy
 * Created 2017/4/13 11:03
 */
public class StatOrdersVo {
    /**
     * 订单
     */
    private StatOrders statOrders = null;
    /**
     * 订单总金额
     */
    private BigDecimal ordersAmountSum = new BigDecimal(0);
    /**
     * 下单会员总数
     */
    private long ordersMemberCount = 0L;
    /**
     * 订单总数
     */
    private long ordersCount = 0L;
    /**
     * 订单客单价
     */
    private BigDecimal ordersAmountAverage  = new BigDecimal(0);
    /**
     * 订单创建时间的小时
     */
    private String createTimeHour = "";
    /**
     * 订单创建时间到天
     */
    private String statDate = "";
    /**
     * 会员ID
     */
    private int memberId = 0;
    /**
     * 一级地区ID
     */
    private int receiverAreaId1 = 0;

    public StatOrders getStatOrders() {
        return statOrders;
    }

    public void setStatOrders(StatOrders statOrders) {
        this.statOrders = statOrders;
    }

    public BigDecimal getOrdersAmountSum() {
        return ordersAmountSum;
    }

    public void setOrdersAmountSum(BigDecimal ordersAmountSum) {
        this.ordersAmountSum = ordersAmountSum;
    }

    public long getOrdersMemberCount() {
        return ordersMemberCount;
    }

    public void setOrdersMemberCount(long ordersMemberCount) {
        this.ordersMemberCount = ordersMemberCount;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public BigDecimal getOrdersAmountAverage() {
        return ordersAmountAverage;
    }

    public void setOrdersAmountAverage(BigDecimal ordersAmountAverage) {
        this.ordersAmountAverage = ordersAmountAverage;
    }

    public String getCreateTimeHour() {
        return createTimeHour;
    }

    public void setCreateTimeHour(String createTimeHour) {
        this.createTimeHour = createTimeHour;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getReceiverAreaId1() {
        return receiverAreaId1;
    }

    public void setReceiverAreaId1(int receiverAreaId1) {
        this.receiverAreaId1 = receiverAreaId1;
    }

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }
}

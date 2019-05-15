package com.ftofs.twant.vo.orders;

import java.math.BigDecimal;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 订单支付Vo
 *
 * @author hbj
 * Created 2017/4/13 14:47
 */
public class OrdersPayVo {
    private List<SearchOrdersVo> ordersVoList;
    /**
     * 支付单ID
     */
    private int payId;
    /**
     * 支付单号
     */
    private long paySn;
    /**
     * 还需要在线支付金额(选择在线支付的订单，使用站内余额方式支付后还在线支付的差额)
     */
    private BigDecimal ordersOnlineDiffAmount = new BigDecimal(0);
    /**
     * 是否是多人拼团订单
     */
    private int isGroup = 0;
    /**
     * 是否是门店订单
     */
    private int isChain = 0;
    /**
     * 是否是虚拟订单
     */
    private int isVirtual = 0;

    public OrdersPayVo() {
    }

    public OrdersPayVo(List<SearchOrdersVo> ordersVoList) {

    }

    public List<SearchOrdersVo> getOrdersVoList() {
        return ordersVoList;
    }

    public void setOrdersVoList(List<SearchOrdersVo> ordersVoList) {
        this.ordersVoList = ordersVoList;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
    }

    public BigDecimal getOrdersOnlineDiffAmount() {
        return ordersOnlineDiffAmount;
    }

    public void setOrdersOnlineDiffAmount(BigDecimal ordersOnlineDiffAmount) {
        this.ordersOnlineDiffAmount = ordersOnlineDiffAmount;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public int getIsChain() {
        return isChain;
    }

    public void setIsChain(int isChain) {
        this.isChain = isChain;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    @Override
    public String toString() {
        return "OrdersPayVo{" +
                "ordersVoList=" + ordersVoList +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", ordersOnlineDiffAmount=" + ordersOnlineDiffAmount +
                ", isGroup=" + isGroup +
                ", isChain=" + isChain +
                ", isVirtual=" + isVirtual +
                '}';
    }
}

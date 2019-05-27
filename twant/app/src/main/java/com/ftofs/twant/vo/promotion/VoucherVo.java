package com.ftofs.twant.vo.promotion;

import java.math.BigDecimal;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 店铺券Vo
 * 
 * @author hbj
 * Created 2017/4/13 14:48
 */
public class VoucherVo {
    /**
     * 自增编码
     */
    private int voucherId;
    /**
     * 店铺券名称
     */
    private String voucherTitle = "";
    /**
     * 店铺券有效期开始时间
     */
    private String startTime;
    /**
     * 店铺券有效期结束时间
     */
    private String endTime;
    /**
     * 面额
     */
    private BigDecimal price = new BigDecimal(0);
    /**
     * 店铺券使用时的订单限额
     */
    private BigDecimal limitAmount = new BigDecimal(0);
    /**
     * 店铺Id
     */
    private int storeId = 0;
    /**
     * 是否可用(目前只有下单时使用)
     */
    private int useEnable = 1;
    /**
     * 使用条件描述
     */
    private String limitText = "";

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getUseEnable() {
        return useEnable;
    }

    public void setUseEnable(int useEnable) {
        this.useEnable = useEnable;
    }

    public String getLimitText() {
        return limitText;
    }

    public void setLimitText(String limitText) {
        this.limitText = limitText;
    }

    @Override
    public String toString() {
        return "VoucherVo{" +
                "voucherId=" + voucherId +
                ", voucherTitle='" + voucherTitle + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", price=" + price +
                ", limitAmount=" + limitAmount +
                ", storeId=" + storeId +
                ", useEnable=" + useEnable +
                ", limitText='" + limitText + '\'' +
                '}';
    }
}

package com.ftofs.twant.entity;


public class StoreVoucherVo {
    /**
     * 自增编码
     */
    public int voucherId;
    /**
     * 店铺券名称
     */
    public String voucherTitle = "";
    /**
     * 店铺券有效期开始时间
     */
    public String startTime;
    /**
     * 店铺券有效期结束时间
     */
    public String endTime;
    /**
     * 面额
     */
    public float price;
    /**
     * 店铺券使用时的订单限额
     */
    public float limitAmount;
    /**
     * 店铺Id
     */
    public int storeId = 0;
    /**
     * 是否可用(目前只有下单时使用)
     */
    public int useEnable = 1;
    /**
     * 使用条件描述
     */
    public String limitText = "";

    

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

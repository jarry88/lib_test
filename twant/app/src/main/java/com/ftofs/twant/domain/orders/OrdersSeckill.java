package com.ftofs.twant.domain.orders;

public class OrdersSeckill {
    /**
     * 產品SKU编号
     */
    private int ordersSeckillId;

    /**
     * 秒杀活动產品 主键
     */
    private int seckillGoodsId;

    /**
     * 秒杀排期编号
     */
    private int buyNum;

    /**
     * 会员
     */
    private int memberId;

    public int getOrdersSeckillId() {
        return ordersSeckillId;
    }

    public void setOrdersSeckillId(int ordersSeckillId) {
        this.ordersSeckillId = ordersSeckillId;
    }

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "OrdersSeckill{" +
                "ordersSeckillId=" + ordersSeckillId +
                ", seckillGoodsId=" + seckillGoodsId +
                ", buyNum=" + buyNum +
                ", memberId=" + memberId +
                '}';
    }
}

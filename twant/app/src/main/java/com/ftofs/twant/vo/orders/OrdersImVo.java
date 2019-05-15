package com.ftofs.twant.vo.orders;

/**
 * @Description: IM訂單視圖對象
 * @Auther: yangjian
 * @Date: 2019/4/9 14:44
 */
public class OrdersImVo {
    /**
     * 订单主键ID
     */
    private int ordersId;
    /**
     * 订单编号
     */
    private long ordersSn;
    /**
     * 圖片
     */
    private String imageSrc;
    /**
     * 商品名稱
     */
    private String goodsName;

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    @Override
    public String toString() {
        return "OrdersImVo{" +
                "ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsName='" + goodsName + '\'' +
                '}';
    }
}

package com.ftofs.twant.seller.entity;

import java.math.BigDecimal;

/**
 * 卖家版订单详情商品列表Item
 * @author zwm
 */
public class SellerOrderDetailGoodsItem {
    public int commonId;
    public int goodsId;
    public String goodsName;
    public String goodsImage;
    public String goodsFullSpecs;
    public double goodsPrice;
    public double goodsPayAmount;
    public int buyNum;
    public int commissionRate;
    public double commissionAmount;
}

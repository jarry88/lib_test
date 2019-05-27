package com.ftofs.twant.vo.statistical;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.statistics.StatOrdersGoods;

import java.math.BigDecimal;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 统计中订单商品数据缓存
 * 
 * @author zxy
 * Created 2017/4/13 11:02
 */
public class StatOrdersGoodsVo {
    /**
     * 订单商品
     */
    private StatOrdersGoods statOrdersGoods = null;
    /**
     * 订单商品总金额
     */
    private BigDecimal goodsPayAmountSum = new BigDecimal(0);
    /**
     * 订单商品购买量
     */
    private long goodsBuyNumSum = 0L;
    /**
     * 下单量
     */
    private long ordersCount = 0L;
    /**
     * 订单商品一级分类ID
     */
    private int categoryId1 = 0;
    /**
     * 商品SPU实体
     */
    private GoodsCommon goodsCommon = null;

    public StatOrdersGoods getStatOrdersGoods() {
        return statOrdersGoods;
    }

    public void setStatOrdersGoods(StatOrdersGoods statOrdersGoods) {
        this.statOrdersGoods = statOrdersGoods;
    }

    public BigDecimal getGoodsPayAmountSum() {
        return goodsPayAmountSum;
    }

    public void setGoodsPayAmountSum(BigDecimal goodsPayAmountSum) {
        this.goodsPayAmountSum = goodsPayAmountSum;
    }

    public long getGoodsBuyNumSum() {
        return goodsBuyNumSum;
    }

    public void setGoodsBuyNumSum(long goodsBuyNumSum) {
        this.goodsBuyNumSum = goodsBuyNumSum;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public GoodsCommon getGoodsCommon() {
        return goodsCommon;
    }

    public void setGoodsCommon(GoodsCommon goodsCommon) {
        this.goodsCommon = goodsCommon;
    }
}

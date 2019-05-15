package com.ftofs.twant.vo.goods.goodsdetail;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 优惠券
 *
 * @author shopnc.feng
 * Created on 2017/11/6 14:55
 */
public class GoodsDetailCouponVo {
    /**
     * 券面额
     */
    private BigDecimal couponPrice = BigDecimal.ZERO;
    /**
     * 消费限额（为0表示不限额使用）
     */
    private BigDecimal limitAmount = BigDecimal.ZERO;
    /**
     * 券限制适用品类的范围说明
     */
    private String useGoodsRangeExplain = "";
    /**
     * 使用有效期时间描述
     */
    private String useTimeText = "";

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getUseGoodsRangeExplain() {
        return useGoodsRangeExplain;
    }

    public void setUseGoodsRangeExplain(String useGoodsRangeExplain) {
        this.useGoodsRangeExplain = useGoodsRangeExplain;
    }

    public String getUseTimeText() {
        return useTimeText;
    }

    public void setUseTimeText(String useTimeText) {
        this.useTimeText = useTimeText;
    }

    @Override
    public String toString() {
        return "GoodsDetailCouponVo{" +
                "couponPrice=" + couponPrice +
                ", limitAmount=" + limitAmount +
                ", useGoodsRangeExplain='" + useGoodsRangeExplain + '\'' +
                ", useTimeText='" + useTimeText + '\'' +
                '}';
    }
}

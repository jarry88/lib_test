package com.ftofs.twant.vo.promotion.platform.coupon;

import com.ftofs.twant.domain.promotion.platform.coupon.Coupon;
import com.ftofs.twant.vo.buy.BuyGoodsItemVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 平台券、產品Spu列表实体
 *
 * @author zxy
 * Created 2017/9/4 11:12
 */
public class CouponAndGoodsCommonVo {
    /**
     * 平台券详情
     */
    private Coupon coupon = null;
    /**
     * 下单时单个產品信息列表
     */
    private List<BuyGoodsItemVo> buyGoodsItemVoList = new ArrayList<>();
    /**
     * 是否可用
     */
    private boolean couponIsAble = false;
    /**
     * 还差多少钱可用该券(购买时显示使用)
     */
    private BigDecimal diffAmount = BigDecimal.ZERO;

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public List<BuyGoodsItemVo> getBuyGoodsItemVoList() {
        return buyGoodsItemVoList;
    }

    public void setBuyGoodsItemVoList(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        this.buyGoodsItemVoList = buyGoodsItemVoList;
    }

    public boolean isCouponIsAble() {
        return couponIsAble;
    }

    public void setCouponIsAble(boolean couponIsAble) {
        this.couponIsAble = couponIsAble;
    }

    public BigDecimal getDiffAmount() {
        return diffAmount;
    }

    public void setDiffAmount(BigDecimal diffAmount) {
        this.diffAmount = diffAmount;
    }

    @Override
    public String toString() {
        return "CouponAndGoodsCommonVo{" +
                "coupon=" + coupon +
                ", buyGoodsItemVoList=" + buyGoodsItemVoList +
                ", couponIsAble=" + couponIsAble +
                ", diffAmount=" + diffAmount +
                '}';
    }
}

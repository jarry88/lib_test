package com.ftofs.twant.vo.cart;

import java.math.BigDecimal;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 购物车整体统计信息
 *
 * @author hbj
 * Created 2017/4/13 14:45
 */
public class CartVo {
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 购物车总金额
     */
    private BigDecimal cartAmount;

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(BigDecimal cartAmount) {
        this.cartAmount = cartAmount;
    }

    @Override
    public String toString() {
        return "CartVo{" +
                "buyNum=" + buyNum +
                ", cartAmount=" + cartAmount +
                '}';
    }
}

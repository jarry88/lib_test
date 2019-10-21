package com.ftofs.twant.vo.member;

import com.ftofs.twant.domain.member.MemberHomeStat;

/**
 * @author liusf
 * @create 2019/4/15 18:35
 * @description 個人專頁數據視圖類
 * @params
 * @return
 */
public class MemberHomeStatVo extends MemberHomeStat {
    /**
     * 購物籃商品數量
     */
    private long cartList = 0;

    public long getCartList() {
        return cartList;
    }

    public void setCartList(long cartList) {
        this.cartList = cartList;
    }
}

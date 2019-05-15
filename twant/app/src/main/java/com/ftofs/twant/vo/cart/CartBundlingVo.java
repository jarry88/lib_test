package com.ftofs.twant.vo.cart;

import com.ftofs.twant.domain.orders.Cart;
import com.ftofs.twant.domain.promotion.Bundling;
import com.ftofs.twant.domain.store.Store;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 优惠套装购物车
 *
 * @author hbj
 * Created 2017/4/13 14:44
 */
public class CartBundlingVo {
    /**
     * 购物车Id
     */
    private int cartId;
    /**
     * 优惠套装id
     */
    private int bundlingId = 0;
    /**
     * 优惠套装名称
     */
    private String bundlingName;
    /**
     * 优惠套装单价(各商品单价之和)
     */
    private BigDecimal goodsPrice;
    /**
     * 商品图
     */
    private String imageName;
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 商品小计[单价 * 数量]
     */
    private BigDecimal itemAmount;
    /**
     * 商品库存
     */
    private int goodsStorage;
    /**
     * 商品状态
     */
    private int goodsStatus;
    /**
     * 店铺ID
     */
    private int storeId;
    /**
     * 店铺名
     */
    private String storeName;
    /**
     * 商品库存是否足够
     */
    private int storageStatus;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 优惠套装商品列表
     */
    private List<BuyBundlingItemVo> buyBundlingItemVoList = new ArrayList<>();
    /**
     * 开始时间
     */

    private Timestamp startTime;
    /**
     * 结束时间
     */

    private Timestamp endTime;

    /**
     * 取得商品信息[联查购物车]
     * @param cart
     * @param bundling
     * @param store
     */
    public CartBundlingVo(Cart cart, Bundling bundling, Store store) {
        this.cartId = cart.getCartId();
        this.buyNum = cart.getBuyNum();
        this.bundlingName = bundling.getBundlingTileFinal();
        this.memberId = cart.getMemberId();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.bundlingId = cart.getBundlingId();
        this.startTime = bundling.getStartTime();
        this.endTime = bundling.getEndTime();
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }

    public String getBundlingName() {
        return bundlingName;
    }

    public void setBundlingName(String bundlingName) {
        this.bundlingName = bundlingName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getImageName() {
        return buyBundlingItemVoList.get(0).getSpuImageSrc();
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }


    public List<BuyBundlingItemVo> getBuyBundlingItemVoList() {
        return buyBundlingItemVoList;
    }

    public void setBuyBundlingItemVoList(List<BuyBundlingItemVo> buyBundlingItemVoList) {
        this.buyBundlingItemVoList = buyBundlingItemVoList;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getStorageStatus() {
        return getGoodsStorage() >= buyNum ? 1 : 0;
    }

    public void setStorageStatus(int storageStatus) {
        this.storageStatus = storageStatus;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CartBundlingVo{" +
                "cartId=" + cartId +
                ", bundlingId=" + bundlingId +
                ", bundlingName='" + bundlingName + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", imageName='" + imageName + '\'' +
                ", buyNum=" + buyNum +
                ", itemAmount=" + itemAmount +
                ", goodsStorage=" + goodsStorage +
                ", goodsStatus=" + goodsStatus +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storageStatus=" + storageStatus +
                ", memberId=" + memberId +
                ", buyBundlingItemVoList=" + buyBundlingItemVoList +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

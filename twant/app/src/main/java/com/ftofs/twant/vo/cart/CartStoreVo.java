package com.ftofs.twant.vo.cart;

import com.ftofs.twant.domain.promotion.Conform;
import com.ftofs.twant.vo.promotion.VoucherTemplateVo;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 单个店铺购物车信息
 *
 * @author hbj
 * Created 2017/4/13 14:44
 */
public class CartStoreVo implements Cloneable {
    /**
     * sku
     */
    private List<CartItemVo> cartItemVoList = new ArrayList<>();
    /**
     * spu
     */
    private List<CartSpuVo> cartSpuVoList = new ArrayList<>();
    /**
     * 店铺券
     */
    private List<VoucherTemplateVo> voucherTemplateVoList = new ArrayList<>();
    /**
     * 满优惠活动
     */
    private List<Conform> conformList = new ArrayList<>();
    /**
     * 购物车总金额
     */
    private BigDecimal cartAmount;
    /**
     * 店铺名
     */
    private String storeName;
    /**
     * 店铺Id
     */
    private int storeId;
    /**
     * bycj 1606061130 添加一个卖家id
     */
    private int sellerId = 0;
    /**
     * bycj 1606061130 判断卖家是否在线
     */
    private int isOnline = 0;
    /**
     * 购物车商品种类(sku)数量
     */
    private int buyNum;
    /**
     * 优惠套装
     */
    private List<CartBundlingVo> cartBundlingVoList = new ArrayList<>();
    /**
     * 门店编号
     */
    private int chainId;
    /**
     * 门店名称
     */
    private String chainName;

    /**
     * 客服列表
     */
    private List<StoreServiceStaffVo> serviceStaffList;

    public List<CartItemVo> getCartItemVoList() {
        return cartItemVoList;
    }

    public void setCartItemVoList(List<CartItemVo> cartItemVoList) {
        this.cartItemVoList = cartItemVoList;
    }

    public List<CartSpuVo> getCartSpuVoList() {
        return cartSpuVoList;
    }

    public void setCartSpuVoList(List<CartSpuVo> cartSpuVoList) {
        this.cartSpuVoList = cartSpuVoList;
    }

    public List<VoucherTemplateVo> getVoucherTemplateVoList() {
        return voucherTemplateVoList;
    }

    public void setVoucherTemplateVoList(List<VoucherTemplateVo> voucherTemplateVoList) {
        this.voucherTemplateVoList = voucherTemplateVoList;
    }

    public BigDecimal getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(BigDecimal cartAmount) {
        this.cartAmount = cartAmount;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public List<Conform> getConformList() {
        return conformList;
    }

    public void setConformList(List<Conform> conformList) {
        this.conformList = conformList;
    }

    public List<CartBundlingVo> getCartBundlingVoList() {
        return cartBundlingVoList;
    }

    public void setCartBundlingVoList(List<CartBundlingVo> cartBundlingVoList) {
        this.cartBundlingVoList = cartBundlingVoList;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<StoreServiceStaffVo> getServiceStaffList() {
        return serviceStaffList;
    }

    public void setServiceStaffList(List<StoreServiceStaffVo> serviceStaffList) {
        this.serviceStaffList = serviceStaffList;
    }

    @Override
    public String toString() {
        return "CartStoreVo{" +
                "cartItemVoList=" + cartItemVoList +
                ", cartSpuVoList=" + cartSpuVoList +
                ", voucherTemplateVoList=" + voucherTemplateVoList +
                ", conformList=" + conformList +
                ", cartAmount=" + cartAmount +
                ", storeName='" + storeName + '\'' +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", isOnline=" + isOnline +
                ", buyNum=" + buyNum +
                ", cartBundlingVoList=" + cartBundlingVoList +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                '}';
    }
}

package com.ftofs.twant.vo.buy;

import com.ftofs.twant.domain.promotion.Conform;
import com.ftofs.twant.vo.cart.CartBundlingVo;
import com.ftofs.twant.vo.promotion.VoucherVo;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 存放下单时单个店铺的购买信息
 *
 * @author hbj
 * Created 2017/4/13 14:41
 */
public class BuyStoreVo {
    /**
     * 店铺下商品列表集合
     */
    private List<BuyGoodsItemVo> buyGoodsItemVoList = new ArrayList<>();
    /**
     * spu 集合
     */
    private List<BuyGoodsSpuVo> buyGoodsSpuVoList = new ArrayList<>();
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺Id
     */
    private int storeId;
    /**
     * 商家id
     */
    private int sellerId = 0;
    /**
     * im 是否在线
     */
    private int isOnline = 0;
    /**
     * 店铺可用的满减送
     */
    private Conform conform;
    /**
     * 店铺可用店铺券列表
     */
    private List<VoucherVo> voucherVoList = new ArrayList<>();
    /**
     * 店铺商品总价[不含运费、促销],在Vo内自动计算
     */
    private BigDecimal buyItemAmount = new BigDecimal(0);
    /**
     * 店铺商品总价[不参加促销的被排除在外][不含运费、促销],在Vo内自动计算
     */
    private BigDecimal buyItemExcludejoinBigSaleAmount = new BigDecimal(0);
    /**
     * 店铺商品减去满优惠后的总价[不参加促销的被排除在外][不含运费]
     */
    private BigDecimal buyStoreExcludejoinBigSaleAmount = new BigDecimal(0);
    /**
     * 店铺运费
     */
    private BigDecimal freightAmount = new BigDecimal(0);
    /**
     * 是否自营
     */
    private int isOwnShop;
    /**
     * 优惠套装列表
     */
    private List<CartBundlingVo> cartBundlingVoList = new ArrayList<>();
    /**
     * 海外购商品税费
     */
    private BigDecimal foreignTaxAmount = BigDecimal.ZERO;

    /**
     * 客服列表
     */
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    public BuyStoreVo() {
    }

    public BuyStoreVo(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        this.storeId = buyGoodsItemVoList.get(0).getStoreId();
        this.buyGoodsItemVoList = buyGoodsItemVoList;
        this.storeName = buyGoodsItemVoList.get(0).getStoreName();
        this.isOwnShop = buyGoodsItemVoList.get(0).getIsOwnShop();
    }

    public List<BuyGoodsItemVo> getBuyGoodsItemVoList() {
        return buyGoodsItemVoList;
    }

    public void setBuyGoodsItemVoList(List<BuyGoodsItemVo> buyGoodsItemVoList) {
        this.buyGoodsItemVoList = buyGoodsItemVoList;
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

    public BigDecimal getBuyItemAmount() {
        return buyItemAmount;
    }

    public void setBuyItemAmount(BigDecimal buyItemAmount) {
        this.buyItemAmount = buyItemAmount;
    }

    public BigDecimal getBuyItemExcludejoinBigSaleAmount() {
        return buyItemExcludejoinBigSaleAmount;
    }

    public void setBuyItemExcludejoinBigSaleAmount(BigDecimal buyItemExcludejoinBigSaleAmount) {
        this.buyItemExcludejoinBigSaleAmount = buyItemExcludejoinBigSaleAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public List<VoucherVo> getVoucherVoList() {
        return voucherVoList;
    }

    public void setVoucherVoList(List<VoucherVo> voucherVoList) {
        this.voucherVoList = voucherVoList;
    }

    public List<BuyGoodsSpuVo> getBuyGoodsSpuVoList() {
        return buyGoodsSpuVoList;
    }

    public void setBuyGoodsSpuVoList(List<BuyGoodsSpuVo> buyGoodsSpuVoList) {
        this.buyGoodsSpuVoList = buyGoodsSpuVoList;
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

    public List<CartBundlingVo> getCartBundlingVoList() {
        return cartBundlingVoList;
    }

    public void setCartBundlingVoList(List<CartBundlingVo> cartBundlingVoList) {
        this.cartBundlingVoList = cartBundlingVoList;
    }

    /**
     * 取得web端下单第二步时页面全并表格数量
     * @return
     */
    public int getItemCount() {
        int count = buyGoodsItemVoList.size() + cartBundlingVoList.size();
        for (CartBundlingVo cartBundlingVo : cartBundlingVoList) {
            count += cartBundlingVo.getBuyBundlingItemVoList().size();
        }
        return count;
    }

    public Conform getConform() {
        return conform;
    }

    public void setConform(Conform conform) {
        this.conform = conform;
    }

    public BigDecimal getBuyStoreExcludejoinBigSaleAmount() {
        return getBuyItemExcludejoinBigSaleAmount();
    }

    public void setBuyStoreExcludejoinBigSaleAmount(BigDecimal buyStoreExcludejoinBigSaleAmount) {
        this.buyStoreExcludejoinBigSaleAmount = buyStoreExcludejoinBigSaleAmount;
    }

    public BigDecimal getForeignTaxAmount() {
        return foreignTaxAmount;
    }

    public void setForeignTaxAmount(BigDecimal foreignTaxAmount) {
        this.foreignTaxAmount = foreignTaxAmount;
    }

    public List<StoreServiceStaffVo> getStoreServiceStaffVoList() {
        return storeServiceStaffVoList;
    }

    public void setStoreServiceStaffVoList(List<StoreServiceStaffVo> storeServiceStaffVoList) {
        this.storeServiceStaffVoList = storeServiceStaffVoList;
    }

    @Override
    public String toString() {
        return "BuyStoreVo{" +
                "buyGoodsItemVoList=" + buyGoodsItemVoList +
                ", buyGoodsSpuVoList=" + buyGoodsSpuVoList +
                ", storeName='" + storeName + '\'' +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", isOnline=" + isOnline +
                ", conform=" + conform +
                ", voucherVoList=" + voucherVoList +
                ", buyItemAmount=" + buyItemAmount +
                ", buyItemExcludejoinBigSaleAmount=" + buyItemExcludejoinBigSaleAmount +
                ", buyStoreExcludejoinBigSaleAmount=" + buyStoreExcludejoinBigSaleAmount +
                ", freightAmount=" + freightAmount +
                ", isOwnShop=" + isOwnShop +
                ", cartBundlingVoList=" + cartBundlingVoList +
                ", foreignTaxAmount=" + foreignTaxAmount +
                '}';
    }

}

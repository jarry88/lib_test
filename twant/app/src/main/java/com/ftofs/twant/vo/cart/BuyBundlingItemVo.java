package com.ftofs.twant.vo.cart;

import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.orders.Cart;
import com.ftofs.twant.domain.orders.CartBundling;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.vo.orders.GoodsContractVo;
import com.ftofs.twant.vo.promotion.GiftVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 优惠套装商品购物车
 *
 * @author hbj
 * Created 2017/4/13 14:44
 */
public class BuyBundlingItemVo {
    /**
     * 购物车Id
     */
    private int cartId;
    /**
     * 商品Id
     */
    private int goodsId;
    /**
     * SPU
     */
    private int commonId;
    /**
     * 商品名
     */
    private String goodsName;
    /**
     * 规格
     */
    private String goodsFullSpecs;
    /**
     * 单价[不分终端]
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
     * 会员ID
     */
    private int memberId;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * SPU 主图
     */
    private String spuImageSrc;
    /**
     * PC端起购价0
     */
    private BigDecimal webPrice0 = new BigDecimal(0);
    /**
     * APP端起购价0
     */
    private BigDecimal appPrice0 = new BigDecimal(0);
    /**
     * 微信端起购价0
     */
    private BigDecimal wechatPrice0 = new BigDecimal(0);
    /**
     * 是否有赠品
     */
    private int isGift = 0;
    /**
     * 赠品列表
     */
    private List<GiftVo> giftVoList = new ArrayList<>();
    /**
     * 消保编号1
     */
    private Integer contractItem1 = 0;
    /**
     * 消保编号2
     */
    private Integer contractItem2 = 0;
    /**
     * 消保编号3
     */
    private Integer contractItem3 = 0;
    /**
     * 消保编号4
     */
    private Integer contractItem4 = 0;
    /**
     * 消保编号5
     */
    private Integer contractItem5 = 0;
    /**
     * 消保编号6
     */
    private Integer contractItem6 = 0;
    /**
     * 消保编号7
     */
    private Integer contractItem7 = 0;
    /**
     * 消保编号8
     */
    private Integer contractItem8 = 0;
    /**
     * 消保编号9
     */
    private Integer contractItem9 = 0;
    /**
     * 消保编号10
     */
    private Integer contractItem10 = 0;
    /**
     * 消保
     */
    private List<GoodsContractVo> goodsContractVoList = new ArrayList<>();

    public BuyBundlingItemVo() {
    }

    /**
     * 取得商品信息
     * @param cartBundling
     * @param goods
     * @param goodsCommon
     * @param store
     */
    public BuyBundlingItemVo(CartBundling cartBundling, Goods goods, GoodsCommon goodsCommon, Store store, Cart cart) {
        this.cartId = cartBundling.getCartId();
        this.buyNum = cart.getBuyNum();
        this.goodsId = goods.getGoodsId();
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.imageName = goods.getImageName();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = goods.getGoodsStorage();
        this.goodsStatus = 1;
        this.memberId = cartBundling.getMemberId();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.unitName = goodsCommon.getUnitName();
        this.webPrice0 = goods.getWebPrice0();
        this.appPrice0 = goods.getAppPrice0();
        this.wechatPrice0 = goods.getWechatPrice0();
        this.spuImageSrc = goodsCommon.getImageSrc();
        this.isGift = goodsCommon.getIsGift();
        this.contractItem1 = goodsCommon.getContractItem1();
        this.contractItem2 = goodsCommon.getContractItem2();
        this.contractItem3 = goodsCommon.getContractItem3();
        this.contractItem4 = goodsCommon.getContractItem4();
        this.contractItem5 = goodsCommon.getContractItem5();
        this.contractItem6 = goodsCommon.getContractItem6();
        this.contractItem7 = goodsCommon.getContractItem7();
        this.contractItem8 = goodsCommon.getContractItem8();
        this.contractItem9 = goodsCommon.getContractItem9();
        this.contractItem10 = goodsCommon.getContractItem10();
    }


    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getImageName() {
        return imageName;
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

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
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

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSpuImageSrc() {
        return spuImageSrc;
    }

    public void setSpuImageSrc(String spuImageSrc) {
        this.spuImageSrc = spuImageSrc;
    }

    public BigDecimal getWebPrice0() {
        return webPrice0;
    }

    public void setWebPrice0(BigDecimal webPrice0) {
        this.webPrice0 = webPrice0;
    }

    public BigDecimal getAppPrice0() {
        return appPrice0;
    }

    public void setAppPrice0(BigDecimal appPrice0) {
        this.appPrice0 = appPrice0;
    }

    public BigDecimal getWechatPrice0() {
        return wechatPrice0;
    }

    public void setWechatPrice0(BigDecimal wechatPrice0) {
        this.wechatPrice0 = wechatPrice0;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public List<GiftVo> getGiftVoList() {
        return giftVoList;
    }

    public void setGiftVoList(List<GiftVo> giftVoList) {
        this.giftVoList = giftVoList;
    }

    public Integer getContractItem1() {
        return contractItem1;
    }

    public void setContractItem1(Integer contractItem1) {
        this.contractItem1 = contractItem1;
    }

    public Integer getContractItem2() {
        return contractItem2;
    }

    public void setContractItem2(Integer contractItem2) {
        this.contractItem2 = contractItem2;
    }

    public Integer getContractItem3() {
        return contractItem3;
    }

    public void setContractItem3(Integer contractItem3) {
        this.contractItem3 = contractItem3;
    }

    public Integer getContractItem4() {
        return contractItem4;
    }

    public void setContractItem4(Integer contractItem4) {
        this.contractItem4 = contractItem4;
    }

    public Integer getContractItem5() {
        return contractItem5;
    }

    public void setContractItem5(Integer contractItem5) {
        this.contractItem5 = contractItem5;
    }

    public Integer getContractItem6() {
        return contractItem6;
    }

    public void setContractItem6(Integer contractItem6) {
        this.contractItem6 = contractItem6;
    }

    public Integer getContractItem7() {
        return contractItem7;
    }

    public void setContractItem7(Integer contractItem7) {
        this.contractItem7 = contractItem7;
    }

    public Integer getContractItem8() {
        return contractItem8;
    }

    public void setContractItem8(Integer contractItem8) {
        this.contractItem8 = contractItem8;
    }

    public Integer getContractItem9() {
        return contractItem9;
    }

    public void setContractItem9(Integer contractItem9) {
        this.contractItem9 = contractItem9;
    }

    public Integer getContractItem10() {
        return contractItem10;
    }

    public void setContractItem10(Integer contractItem10) {
        this.contractItem10 = contractItem10;
    }

    public List<GoodsContractVo> getGoodsContractVoList() {
        return goodsContractVoList;
    }

    public void setGoodsContractVoList(List<GoodsContractVo> goodsContractVoList) {
        this.goodsContractVoList = goodsContractVoList;
    }

    @Override
    public String toString() {
        return "BuyBundlingItemVo{" +
                "cartId=" + cartId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", imageName='" + imageName + '\'' +
                ", buyNum=" + buyNum +
                ", itemAmount=" + itemAmount +
                ", goodsStorage=" + goodsStorage +
                ", goodsStatus=" + goodsStatus +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", imageSrc='" + imageSrc + '\'' +
                ", unitName='" + unitName + '\'' +
                ", spuImageSrc='" + spuImageSrc + '\'' +
                ", webPrice0=" + webPrice0 +
                ", appPrice0=" + appPrice0 +
                ", wechatPrice0=" + wechatPrice0 +
                ", isGift=" + isGift +
                ", giftVoList=" + giftVoList +
                ", contractItem1=" + contractItem1 +
                ", contractItem2=" + contractItem2 +
                ", contractItem3=" + contractItem3 +
                ", contractItem4=" + contractItem4 +
                ", contractItem5=" + contractItem5 +
                ", contractItem6=" + contractItem6 +
                ", contractItem7=" + contractItem7 +
                ", contractItem8=" + contractItem8 +
                ", contractItem9=" + contractItem9 +
                ", contractItem10=" + contractItem10 +
                ", goodsContractVoList=" + goodsContractVoList +
                '}';
    }
}

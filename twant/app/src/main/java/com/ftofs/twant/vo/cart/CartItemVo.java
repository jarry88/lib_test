package com.ftofs.twant.vo.cart;

import com.ftofs.twant.domain.chain.ChainCart;
import com.ftofs.twant.domain.chain.ChainGoods;
import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.orders.Cart;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.vo.orders.GoodsContractVo;
import com.ftofs.twant.vo.promotion.GiftVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 单条商品信息
 *
 * @author hbj
 * Created 2017/4/13 14:44
 */
public class CartItemVo {
    /**
     * 购物车Id[门店购物车Id<0,非门店购物车Id>0]
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
     * 单价[不同端的最后单价]
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
     * 图片路径
     */
    private String imageSrc;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 是否达到起购量
     */
    private int batchNumState;
    /**
     * 起购量0
     */
    private int batchNum0;
    /**
     * 起购量0  终点
     */
    private int batchNum0End = 0;
    /**
     * 起购量1
     */
    private int batchNum1;
    /**
     * 起购量1  终点
     */
    private int batchNum1End = 0;
    /**
     * 起购量2
     */
    private int batchNum2;
    /**
     * PC端起购价0
     */
    private BigDecimal webPrice0 = new BigDecimal(0);
    /**
     * PC端起购价1
     */
    private BigDecimal webPrice1 = new BigDecimal(0);
    /**
     * PC端起购价2
     */
    private BigDecimal webPrice2 = new BigDecimal(0);
    /**
     * PC端促销进行状态
     */
    private int webUsable = 0;
    /**
     * APP端起购价0
     */
    private BigDecimal appPrice0 = new BigDecimal(0);
    /**
     * APP端起购价1
     */
    private BigDecimal appPrice1 = new BigDecimal(0);
    /**
     * APP端起购价2
     */
    private BigDecimal appPrice2 = new BigDecimal(0);
    /**
     * APP端促销进行状态
     */
    private int appUsable;
    /**
     * 微信端起购价0
     */
    private BigDecimal wechatPrice0 = new BigDecimal(0);
    /**
     * 微信端起购价1
     */
    private BigDecimal wechatPrice1 = new BigDecimal(0);
    /**
     * 微信端起购价2
     */
    private BigDecimal wechatPrice2 = new BigDecimal(0);
    /**
     * 微信端促销进行状态
     */
    private int wechatUsable;
    /**
     * 促销开始时间
     */

    private Timestamp promotionBeginTime;
    /**
     * 促销结束时间
     */

    private Timestamp promotionEndTime;
    /**
     * 销售模式
     */
    private int goodsModal;
    /**
     * SPU 主图
     */
    private String spuImageSrc;
    /**
     * 所在spu下的sku购买总量
     */
    private int spuBuyNum;
    /**
     * 参加促销
     */
    private int joinBigSale = 0;
    /**
     * 活动类型文字
     */
    private String promotionTypeText;
    /**
     * 折扣标题
     */
    private String promotionTitle;
    /**
     * 商品价格0
     */
    private BigDecimal goodsPrice0;
    /**
     * 商品价格1
     */
    private BigDecimal goodsPrice1;
    /**
     * 商品价格2
     */
    private BigDecimal goodsPrice2;
    /**
     * 活动类型
     */
    private int promotionType;
    /**
     * 是否正处于预定商品状态（定金+尾款）
     */
    private int isBook = 0;
    /**
     * 是否有赠品
     */
    private int isGift = 0;
    /**
     * 赠品列表
     */
    private List<GiftVo> giftVoList = new ArrayList<>();
    /**
     * 优惠套装id
     */
    private int bundlingId = 0;
    /**
     * 优惠套装商品
     */
    private List<BuyBundlingItemVo> buyBundlingItemVoList;
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
     * 简化后的消保
     */
    private List<GoodsContractVo> goodsContractVoList = new ArrayList<>();
    /**
     * 限时折扣/秒杀 限购数量
     */
    private int limitAmount;
    /**
     * 门店编号
     */
    private int chainId;
    /**
     * 门店名称
     */
    private String chainName;
    /**
     * 商品ID，当商品为门店商品时，该值为真正的goodsId[仅购物车列表使用]
     */
    private int realGoodsId;
    /**
     * spu ID，当商品为门店商品时，该值为真正的commonId[仅购物车列表使用]
     */
    private int realCommonId;
    /**
     * 门店商品Id
     */
    private int chainGoodsId;
    /**
     * 是否是虚拟
     */
    private int isVirtual;
    /**
     * 是否是秒杀
     */
    private int isSecKill;
    /**
     * 移杀商品Id
     */
    private int seckillGoodsId;
    /**
     * 是否是海外购
     */
    private int isForeign;
    /**
     * 是否是门店商品
     */
    private int isChain;
    /**
     * 商品类型 3-海外购、2-虚拟、1-门店、0-除前3种之外的所有商品
     */
    private int goodsType;

    public CartItemVo() {
    }

    /**
     * 取得商品信息[联查购物车]
     * @param cart
     * @param goods
     * @param goodsCommon
     * @param store
     */
    public CartItemVo(Cart cart, Goods goods, GoodsCommon goodsCommon, Store store) {
        this.cartId = cart.getCartId();
        this.buyNum = cart.getBuyNum();
        this.goodsId = goods.getGoodsId();
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.imageName = goods.getImageName();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = goods.getGoodsStorage();
        this.goodsStatus = goodsCommon.getGoodsState();
        this.memberId = cart.getMemberId();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.unitName = goodsCommon.getUnitName();
        this.batchNum0 = goodsCommon.getBatchNum0();
        this.batchNum1 = goodsCommon.getBatchNum1();
        this.batchNum2 = goodsCommon.getBatchNum2();
        this.batchNum0End = goodsCommon.getBatchNum0End();
        this.batchNum1End = goodsCommon.getBatchNum1End();
        this.webPrice0 = goods.getWebPrice0();
        this.webPrice1 = goods.getWebPrice1();
        this.webPrice2 = goods.getWebPrice2();
        this.webUsable = goods.getWebUsable();
        this.appPrice0 = goods.getAppPrice0();
        this.appPrice1 = goods.getAppPrice1();
        this.appPrice2 = goods.getAppPrice2();
        this.appUsable = goods.getAppUsable();
        this.wechatPrice0 = goods.getWechatPrice0();
        this.wechatPrice1 = goods.getWechatPrice1();
        this.wechatPrice2 = goods.getWechatPrice2();
        this.wechatUsable = goods.getWechatUsable();
        this.goodsModal = goodsCommon.getGoodsModal();
        this.spuImageSrc = goodsCommon.getImageSrc();
        this.joinBigSale = goodsCommon.getJoinBigSale();
        this.promotionTitle = goods.getPromotionTitle();
        this.promotionTypeText = goods.getPromotionTypeText();
        this.goodsPrice0 = goods.getGoodsPrice0();
        this.goodsPrice1 = goods.getGoodsPrice1();
        this.goodsPrice2 = goods.getGoodsPrice2();
        this.promotionType = goods.getPromotionType();
        this.isBook = 0;
        this.isGift = goodsCommon.getIsGift();
        this.bundlingId = cart.getBundlingId();
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
        this.limitAmount = goods.getLimitAmount();
        this.isVirtual = 0;
        this.isForeign = 0;
    }

    /**
     * 到得商品信息[不联查购物车]
     * @param goods
     * @param goodsCommon
     * @param store
     */
    public CartItemVo(Goods goods, GoodsCommon goodsCommon, Store store) {
        this.cartId = goods.getGoodsId();
        this.goodsId = goods.getGoodsId();
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.imageName = goods.getImageName();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = goods.getGoodsStorage();
        this.goodsStatus = goodsCommon.getGoodsState();
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.unitName = goodsCommon.getUnitName();
        this.batchNum0 = goodsCommon.getBatchNum0();
        this.batchNum1 = goodsCommon.getBatchNum1();
        this.batchNum2 = goodsCommon.getBatchNum2();
        this.batchNum0End = goodsCommon.getBatchNum0End();
        this.batchNum1End = goodsCommon.getBatchNum1End();
        this.webPrice0 = goods.getWebPrice0();
        this.webPrice1 = goods.getWebPrice1();
        this.webPrice2 = goods.getWebPrice2();
        this.webUsable = goods.getWebUsable();
        this.appPrice0 = goods.getAppPrice0();
        this.appPrice1 = goods.getAppPrice1();
        this.appPrice2 = goods.getAppPrice2();
        this.appUsable = goods.getAppUsable();
        this.wechatPrice0 = goods.getWechatPrice0();
        this.wechatPrice1 = goods.getWechatPrice1();
        this.wechatPrice2 = goods.getWechatPrice2();
        this.wechatUsable = goods.getWechatUsable();
        this.goodsModal = goodsCommon.getGoodsModal();
        this.spuImageSrc = goodsCommon.getImageSrc();
        this.joinBigSale = goodsCommon.getJoinBigSale();
        this.promotionTitle = goods.getPromotionTitle();
        this.promotionTypeText = goods.getPromotionTypeText();
        this.goodsPrice0 = goods.getGoodsPrice0();
        this.goodsPrice1 = goods.getGoodsPrice1();
        this.goodsPrice2 = goods.getGoodsPrice2();
        this.promotionType = goodsCommon.getPromotionType();
        this.isBook = 0;
        this.isGift = 0;
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
        this.limitAmount = goods.getLimitAmount();
        this.isVirtual = 0;
        this.isForeign = 0;
    }

    /**
     * 取得商品信息[联查购物车][门店]
     * @param chainCart
     * @param goods
     * @param goodsCommon
     * @param chainGoods
     */
    public CartItemVo(ChainCart chainCart, ChainGoods chainGoods, Goods goods, GoodsCommon goodsCommon) {
        this.cartId = -chainCart.getCartId();
        this.buyNum = chainCart.getBuyNum();
        this.goodsId = -(chainGoods.getChainId()%100*1000000 + goods.getGoodsId()%1000000);
        this.commonId = -(chainGoods.getChainId()%100*1000000 + goodsCommon.getCommonId()%1000000);
        this.realGoodsId = goods.getGoodsId();
        this.realCommonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.imageName = goods.getImageName();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = chainGoods.getGoodsStorage();
        this.webPrice0 = chainGoods.getGoodsPrice();
        this.appPrice0 = chainGoods.getGoodsPrice();
        this.wechatPrice0 = chainGoods.getGoodsPrice();
        this.goodsPrice0 = chainGoods.getGoodsPrice();
        this.goodsPrice = chainGoods.getGoodsPrice();
        this.goodsStatus = goodsCommon.getGoodsState();
        this.memberId = chainCart.getMemberId();
        this.storeId = -chainGoods.getChainId();
        this.storeName = chainGoods.getChainName();
        this.unitName = goodsCommon.getUnitName();
        this.goodsModal = goodsCommon.getGoodsModal();
        this.spuImageSrc = goodsCommon.getImageSrc();
        this.joinBigSale = goodsCommon.getJoinBigSale();
        this.chainId = chainGoods.getChainId();
        this.chainName = chainGoods.getChainName();
        this.isChain = 1;
    }

    /**
     * 取得商品信息[不联查购物车][门店]
     * @param goods
     * @param goodsCommon
     * @param chainGoods
     */
    public CartItemVo(ChainGoods chainGoods, Goods goods, GoodsCommon goodsCommon) {
        this.cartId = chainGoods.getGoodsId();
        this.goodsId = goods.getGoodsId();
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.imageName = goods.getImageName();
        this.imageSrc = goods.getImageSrc();
        this.goodsStorage = chainGoods.getGoodsStorage();
        this.goodsPrice = chainGoods.getGoodsPrice();
        this.goodsStatus = goodsCommon.getGoodsState() ;
        this.storeId = goodsCommon.getStoreId();
        this.unitName = goodsCommon.getUnitName();
        this.goodsModal = goodsCommon.getGoodsModal();
        this.spuImageSrc = goodsCommon.getImageSrc();
        this.joinBigSale = goodsCommon.getJoinBigSale();
        this.chainId = chainGoods.getChainId();
        this.chainName = chainGoods.getChainName();
        this.chainGoodsId = chainGoods.getChainGoodsId();
    }

    public CartItemVo(CartBundlingVo cartBundlingVo) {
        this.cartId = cartBundlingVo.getCartId();
        this.commonId = cartBundlingVo.getBuyBundlingItemVoList().get(0).getCommonId();
        this.goodsPrice = cartBundlingVo.getGoodsPrice();
        this.goodsName = cartBundlingVo.getBundlingName();
        this.buyNum = cartBundlingVo.getBuyNum();
        this.imageName = cartBundlingVo.getImageName();
        this.imageSrc = cartBundlingVo.getBuyBundlingItemVoList().get(0).getSpuImageSrc();
        this.unitName = "件";
    }

    public CartItemVo(BuyBundlingItemVo buyBundlingItemVo, CartBundlingVo cartBundlingVo) {
        this.commonId = buyBundlingItemVo.getCommonId();
        this.goodsPrice = buyBundlingItemVo.getGoodsPrice();
        this.goodsName = buyBundlingItemVo.getGoodsName();
        this.goodsFullSpecs = buyBundlingItemVo.getGoodsFullSpecs();
        this.imageName = buyBundlingItemVo.getImageName();
        this.imageSrc = buyBundlingItemVo.getSpuImageSrc();
        this.unitName = buyBundlingItemVo.getUnitName();
        this.buyNum = cartBundlingVo.getBuyNum();
        this.goodsStorage = cartBundlingVo.getGoodsStorage();
        this.cartId = cartBundlingVo.getCartId();
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

    public int getStorageStatus() {
        return goodsStorage >= buyNum ? 1 : 0;
    }

    public void setStorageStatus(int storageStatus) {
        this.storageStatus = storageStatus;
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

    public int getBatchNumState() {
        return buyNum >= batchNum0 ? 1 : 0;
    }

    public void setBatchNumState(int batchNumState) {
        this.batchNumState = batchNumState;
    }

    public int getBatchNum0() {
        return batchNum0;
    }

    public void setBatchNum0(int batchNum0) {
        this.batchNum0 = batchNum0;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
    }

    public int getBatchNum2() {
        return batchNum2;
    }

    public void setBatchNum2(int batchNum2) {
        this.batchNum2 = batchNum2;
    }

    public BigDecimal getWebPrice0() {
        return webPrice0;
    }

    public void setWebPrice0(BigDecimal webPrice0) {
        this.webPrice0 = webPrice0;
    }

    public BigDecimal getWebPrice1() {
        return webPrice1;
    }

    public void setWebPrice1(BigDecimal webPrice1) {
        this.webPrice1 = webPrice1;
    }

    public BigDecimal getWebPrice2() {
        return webPrice2;
    }

    public void setWebPrice2(BigDecimal webPrice2) {
        this.webPrice2 = webPrice2;
    }

    public int getWebUsable() {
        return webUsable;
    }

    public void setWebUsable(int webUsable) {
        this.webUsable = webUsable;
    }

    public BigDecimal getAppPrice0() {
        return appPrice0;
    }

    public void setAppPrice0(BigDecimal appPrice0) {
        this.appPrice0 = appPrice0;
    }

    public BigDecimal getAppPrice1() {
        return appPrice1;
    }

    public void setAppPrice1(BigDecimal appPrice1) {
        this.appPrice1 = appPrice1;
    }

    public BigDecimal getAppPrice2() {
        return appPrice2;
    }

    public void setAppPrice2(BigDecimal appPrice2) {
        this.appPrice2 = appPrice2;
    }

    public int getAppUsable() {
        return appUsable;
    }

    public void setAppUsable(int appUsable) {
        this.appUsable = appUsable;
    }

    public BigDecimal getWechatPrice0() {
        return wechatPrice0;
    }

    public void setWechatPrice0(BigDecimal wechatPrice0) {
        this.wechatPrice0 = wechatPrice0;
    }

    public BigDecimal getWechatPrice1() {
        return wechatPrice1;
    }

    public void setWechatPrice1(BigDecimal wechatPrice1) {
        this.wechatPrice1 = wechatPrice1;
    }

    public BigDecimal getWechatPrice2() {
        return wechatPrice2;
    }

    public void setWechatPrice2(BigDecimal wechatPrice2) {
        this.wechatPrice2 = wechatPrice2;
    }

    public int getWechatUsable() {
        return wechatUsable;
    }

    public void setWechatUsable(int wechatUsable) {
        this.wechatUsable = wechatUsable;
    }

    public Timestamp getPromotionBeginTime() {
        return promotionBeginTime;
    }

    public void setPromotionBeginTime(Timestamp promotionBeginTime) {
        this.promotionBeginTime = promotionBeginTime;
    }

    public Timestamp getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(Timestamp promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
    }

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
    }

    public String getSpuImageSrc() {
        return spuImageSrc;
    }

    public void setSpuImageSrc(String spuImageSrc) {
        this.spuImageSrc = spuImageSrc;
    }

    public int getBatchNum0End() {
        return batchNum0End;
    }

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public int getBatchNum1End() {
        return batchNum1End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
    }

    public int getSpuBuyNum() {
        return spuBuyNum;
    }

    public void setSpuBuyNum(int spuBuyNum) {
        this.spuBuyNum = spuBuyNum;
    }

    public int getJoinBigSale() {
        return joinBigSale;
    }

    public void setJoinBigSale(int joinBigSale) {
        this.joinBigSale = joinBigSale;
    }

    public String getPromotionTypeText() {
        return promotionTypeText;
    }

    public void setPromotionTypeText(String promotionTypeText) {
        this.promotionTypeText = promotionTypeText;
    }

    public String getPromotionTitle() {
        return promotionTitle;
    }

    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }

    public BigDecimal getGoodsPrice0() {
        return goodsPrice0;
    }

    public void setGoodsPrice0(BigDecimal goodsPrice0) {
        this.goodsPrice0 = goodsPrice0;
    }

    public BigDecimal getGoodsPrice1() {
        return goodsPrice1;
    }

    public void setGoodsPrice1(BigDecimal goodsPrice1) {
        this.goodsPrice1 = goodsPrice1;
    }

    public BigDecimal getGoodsPrice2() {
        return goodsPrice2;
    }

    public void setGoodsPrice2(BigDecimal goodsPrice2) {
        this.goodsPrice2 = goodsPrice2;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public int getIsBook() {
        return isBook;
    }

    public void setIsBook(int isBook) {
        this.isBook = isBook;
    }

    public List<GiftVo> getGiftVoList() {
        return giftVoList;
    }

    public void setGiftVoList(List<GiftVo> giftVoList) {
        this.giftVoList = giftVoList;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public int getBundlingId() {
        return bundlingId;
    }

    public void setBundlingId(int bundlingId) {
        this.bundlingId = bundlingId;
    }


    public List<BuyBundlingItemVo> getBuyBundlingItemVoList() {
        return buyBundlingItemVoList;
    }

    public void setBuyBundlingItemVoList(List<BuyBundlingItemVo> buyBundlingItemVoList) {
        this.buyBundlingItemVoList = buyBundlingItemVoList;
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

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
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

    public int getRealGoodsId() {
        return realGoodsId;
    }

    public void setRealGoodsId(int realGoodsId) {
        this.realGoodsId = realGoodsId;
    }

    public int getRealCommonId() {
        return realCommonId;
    }

    public void setRealCommonId(int realCommonId) {
        this.realCommonId = realCommonId;
    }

    public int getChainGoodsId() {
        return chainGoodsId;
    }

    public void setChainGoodsId(int chainGoodsId) {
        this.chainGoodsId = chainGoodsId;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    public int getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(int isForeign) {
        this.isForeign = isForeign;
    }

    public int getIsSecKill() {
        return isSecKill;
    }

    public void setIsSecKill(int isSecKill) {
        this.isSecKill = isSecKill;
    }

    public int getIsChain() {
        return isChain;
    }

    public void setIsChain(int isChain) {
        this.isChain = isChain;
    }

    /**
     * 商品类型
     * @return
     */
    public int getGoodsType() {
        if (isForeign == 1) {
            goodsType = 3;
        } else if (isVirtual == 1) {
            goodsType = 2;
        } else if (isChain == 1) {
            goodsType = 1;
        } else {
            goodsType = 0;
        }
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    @Override
    public String toString() {
        return "CartItemVo{" +
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
                ", storageStatus=" + storageStatus +
                ", memberId=" + memberId +
                ", imageSrc='" + imageSrc + '\'' +
                ", unitName='" + unitName + '\'' +
                ", batchNumState=" + batchNumState +
                ", batchNum0=" + batchNum0 +
                ", batchNum0End=" + batchNum0End +
                ", batchNum1=" + batchNum1 +
                ", batchNum1End=" + batchNum1End +
                ", batchNum2=" + batchNum2 +
                ", webPrice0=" + webPrice0 +
                ", webPrice1=" + webPrice1 +
                ", webPrice2=" + webPrice2 +
                ", webUsable=" + webUsable +
                ", appPrice0=" + appPrice0 +
                ", appPrice1=" + appPrice1 +
                ", appPrice2=" + appPrice2 +
                ", appUsable=" + appUsable +
                ", wechatPrice0=" + wechatPrice0 +
                ", wechatPrice1=" + wechatPrice1 +
                ", wechatPrice2=" + wechatPrice2 +
                ", wechatUsable=" + wechatUsable +
                ", promotionBeginTime=" + promotionBeginTime +
                ", promotionEndTime=" + promotionEndTime +
                ", goodsModal=" + goodsModal +
                ", spuImageSrc='" + spuImageSrc + '\'' +
                ", spuBuyNum=" + spuBuyNum +
                ", joinBigSale=" + joinBigSale +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", promotionTitle='" + promotionTitle + '\'' +
                ", goodsPrice0=" + goodsPrice0 +
                ", goodsPrice1=" + goodsPrice1 +
                ", goodsPrice2=" + goodsPrice2 +
                ", promotionType=" + promotionType +
                ", isBook=" + isBook +
                ", isGift=" + isGift +
                ", giftVoList=" + giftVoList +
                ", bundlingId=" + bundlingId +
                ", buyBundlingItemVoList=" + buyBundlingItemVoList +
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
                ", limitAmount=" + limitAmount +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", realGoodsId=" + realGoodsId +
                ", realCommonId=" + realCommonId +
                ", chainGoodsId=" + chainGoodsId +
                ", isVirtual=" + isVirtual +
                ", isSecKill=" + isSecKill +
                ", seckillGoodsId=" + seckillGoodsId +
                ", isForeign=" + isForeign +
                ", isChain=" + isChain +
                ", goodsType=" + goodsType +
                '}';
    }
}

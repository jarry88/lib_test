package com.ftofs.twant.vo.cart;

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
 * 单条產品信息
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
     * 產品Id
     */
    private int goodsId;
    /**
     * SPU
     */
    private int commonId;
    /**
     * 產品名
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
     * 產品图
     */
    private String imageName;
    /**
     * 购买数量
     */
    private int buyNum;
    /**
     * 產品小计[单价 * 数量]
     */
    private BigDecimal itemAmount;
    /**
     * 產品库存
     */
    private int goodsStorage;
    /**
     * 產品状态
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
     * 產品库存是否足够
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

    private String promotionBeginTime;
    /**
     * 促销结束时间
     */

    private String promotionEndTime;
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
     * 產品价格0
     */
    private BigDecimal goodsPrice0;
    /**
     * 產品价格1
     */
    private BigDecimal goodsPrice1;
    /**
     * 產品价格2
     */
    private BigDecimal goodsPrice2;
    /**
     * 活动类型
     */
    private int promotionType;
    /**
     * 是否正处于预定產品状态（定金+尾款）
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
     * 优惠套装產品
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
     * 產品ID，当產品为门店產品时，该值为真正的goodsId[仅购物车列表使用]
     */
    private int realGoodsId;
    /**
     * spu ID，当產品为门店產品时，该值为真正的commonId[仅购物车列表使用]
     */
    private int realCommonId;
    /**
     * 门店產品Id
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
     * 移杀產品Id
     */
    private int seckillGoodsId;
    /**
     * 是否是海外购
     */
    private int isForeign;
    /**
     * 是否是门店產品
     */
    private int isChain;
    /**
     * 產品类型 3-海外购、2-虚拟、1-门店、0-除前3种之外的所有產品
     */
    private int goodsType;

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
        return storageStatus;
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
        return batchNumState;
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

    public int getBatchNum0End() {
        return batchNum0End;
    }

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
    }

    public int getBatchNum1End() {
        return batchNum1End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
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

    public String getPromotionBeginTime() {
        return promotionBeginTime;
    }

    public void setPromotionBeginTime(String promotionBeginTime) {
        this.promotionBeginTime = promotionBeginTime;
    }

    public String getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(String promotionEndTime) {
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

    public int getIsSecKill() {
        return isSecKill;
    }

    public void setIsSecKill(int isSecKill) {
        this.isSecKill = isSecKill;
    }

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public int getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(int isForeign) {
        this.isForeign = isForeign;
    }

    public int getIsChain() {
        return isChain;
    }

    public void setIsChain(int isChain) {
        this.isChain = isChain;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
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

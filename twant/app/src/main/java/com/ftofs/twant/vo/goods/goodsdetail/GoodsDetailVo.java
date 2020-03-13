package com.ftofs.twant.vo.goods.goodsdetail;

import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsImage;
import com.ftofs.twant.vo.goods.GoodsInfoVo;
import com.ftofs.twant.domain.promotion.Book;
import com.ftofs.twant.domain.promotion.Conform;
import com.ftofs.twant.domain.promotion.Discount;
import com.ftofs.twant.vo.contract.ContractVo;
import com.ftofs.twant.vo.goods.SpecJsonVo;
import com.ftofs.twant.vo.promotion.GiftVo;
import com.ftofs.twant.vo.store.StoreVo;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品详情
 *
 * @author shopnc.feng
 * Created 2017/4/13 14:06
 */
public class GoodsDetailVo {
    /**
     * 產品SKU编号
     */
    private int goodsId=0;
    /**
     * 產品SPU
     */
    private int commonId=0;
    /**
     * 產品名称
     */
    private String goodsName="";
    /**
     * 產品產地
     *  Modify By yangjian 2019/1/8 9:42
     */
    private Integer goodsCountry=0;
    /**
     * 產品买点
     */
    private String jingle="";
    /**
     * 產品分类编号
     */
    private int categoryId=0;
    /**
     * 二维码
     */
    private String goodsQRCode;
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 品牌编号
     */
    private int brandId;
    /**
     * 產品描述
     */

    private String goodsBody;
    /**
     * 移动端端描述
     */

    private String mobileBody;
    /**
     * 產品货号
     */
    private String goodsSerial;
    /**
     * 產品状态<br>
     * 可以购买1，不可购买0
     */
    private int goodsStatus;
    /**
     * 颜色规格值编号<br>
     * 编号为1的规格对应的规格值的编号
     */
    private int colorId;
    /**
     * 一级地区编号
     */
    private int areaId1;
    /**
     * 二级地区编号
     */
    private int areaId2;
    /**
     * 被關注数量
     */
    private int goodsFavorite;
    /**
     * 被点击数量
     */
    private int goodsClick;
    /**
     * 评价数量
     */
    private Integer evaluateNum;
    /**
     * 好评率
     */
    private Integer goodsRate;
    /**
     * 销售数量
     */
    private int goodsSaleNum;
    /**
     * 產品图片
     */
    private String imageSrc;
    /**
     * 是否参加促销
     * 1参加促销
     */
    private int joinBigSale;
    /**
     * 规格JSON
     */
    private List<SpecJsonVo> specJson = new ArrayList<>();
    /**
     * 產品规格名称JSON
     */
    private List<String> goodsSpecNameList = new ArrayList<>();
    /**
     * 產品编号和规格值编号JSON
     */
    private String goodsSpecValues;
    /**
     * 產品编号和规格值编号JSON
     */
    private List<GoodsSpecValueJsonVo> goodsSpecValueJson;
    /**
     * 產品图片列表
     */
    private List<GoodsImage> goodsImageList;
    /**
     * 產品参数
     */

    private List<GoodsAttrVo> goodsAttrList;
    /**
     * 產品列表
     */
    private List<Goods> goodsList;
    /**
     * 產品销售与形式
     */
    private int goodsModal;
    /**
     * 起购量0
     */
    private int batchNum0;
    /**
     * 起购量0  终点
     */
    private int batchNum0End;
    /**
     * 起购量1
     */
    private int batchNum1;
    /**
     * 起购点1 终点
     */
    private int batchNum1End;
    /**
     * 起购量2
     */
    private int batchNum2;
    /**
     * 起购价0-原价
     */
    private BigDecimal batchPrice0;
    /**
     * 起购价1
     */
    private BigDecimal batchPrice1;
    /**
     * 起购价2
     */
    private BigDecimal batchPrice2;
    /**
     * PC端起购价0
     */
    private BigDecimal webPrice0;
    /**
     * PC端起购价1
     */
    private BigDecimal webPrice1;
    /**
     * PC端起购价2
     */
    private BigDecimal webPrice2;
    /**
     * 產品最低价
     */
    private BigDecimal webPriceMin = BigDecimal.ZERO;
    /**
     * PC端促销进行状态
     */
    private int webUsable;
    /**
     * APP端起购价0
     */
    private BigDecimal appPrice0;
    /**
     * APP端起购价1
     */
    private BigDecimal appPrice1;
    /**
     * APP端起购价2
     */
    private BigDecimal appPrice2;
    /**
     * 產品最低价
     */
    private BigDecimal appPriceMin = BigDecimal.ZERO;
    /**
     * APP端促销进行状态
     */
    private int appUsable;
    /**
     * 微信端起购价0
     */
    private BigDecimal wechatPrice0;
    /**
     * 微信端起购价1
     */
    private BigDecimal wechatPrice1;
    /**
     * 微信端起购价2
     */
    private BigDecimal wechatPrice2;
    /**
     * 產品最低价
     */
    private BigDecimal wechatPriceMin = BigDecimal.ZERO;
    /**
     * 微信端促销进行状态
     */
    private int wechatUsable;
    /**
     * 限时折扣促销编号
     */
    private int promotionId;
    /**
     * 促销开始时间
     */

    private String promotionStartTime;
    /**
     * 促销结束时间
     */

    private String promotionEndTime;
    /**
     * 活动类型
     */
    private int promotionType;
    /**
     * 活动类型文字
     */
    private String promotionTypeText;
    /**
     * 促销倒计时(秒)
     * 未满足条件为0
     */
    private long promotionCountDownTime = 0;
    /**
     * 促销倒计时类型
     *
     * future 即将开始
     * ongoing 已经开始
     */
    private String promotionCountDownTimeType = "";
    /**
     * 活动状态
     */
    private int promotionState;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 折扣活动
     */
    private Discount discount;
    /**
     * 店铺满优惠活动列表
     */
    private List<Conform> conformList = new ArrayList<>();
    /**
     * 定金SPU数据
     */
    private Book book;
    /**
     * 定金SKU列表
     */
    private List<Book> bookList = new ArrayList<>();
    /**
     * 立即购买按钮   1显示，0隐藏
     */
    private int buyNow = 1;
    /**
     * 加入购物车按钮   1显示，0隐藏
     */
    private int addCart = 1;
    /**
     * 是否有赠品
     */
    private Integer isGift;
    /**
     * 赠品列表
     */
    private List<GiftVo> giftVoList = new ArrayList<>();
    /**
     * 团购编号
     */
    private Integer groupId;
    /**
     * 消保数组
     */
    private List<ContractVo> contractVoList = new ArrayList<>();
    public int web = 0;
    public int app = 0;
    public int wechat = 0;
    /**
     * 支持过期退款
     */
    private Integer virtualOverdueRefund = 0;
    /**
     * 海外產品税率
     */
    private BigDecimal foreignTaxRate = BigDecimal.ZERO;
    /**
     * 秒杀
     */
    private int isSeckill = 0;
    /**
     * 优惠券
     */
    private List<GoodsDetailCouponVo> goodsDetailCouponVoList = new ArrayList<>();

    /**
     * 是否是虛擬產品
     */
    private int isVirtual = 0;

    /**
     * 產品視頻鏈接
     */
    private String goodsVideo;

    /**
     * 介紹視頻鏈接
     */
    private String detailVideo;

    /**
     * 是否开启水印
     */
    private int isWaterMark;

    /**
     * 水印图片
     */
    private String waterMarkImage;

    /**
     * 水印位置-东南西北英文缩写
     */
    private String waterMarkPosition;

    /**
     * 產品產地地址
     */
    private AdminCountry adminCountry;

    /**
     * 產品关注状态
     */
    private int isFavorite;


    /**
     * 產品点赞状态
     */
    private int isLike;

    /**
     * 產品点赞数量
     */
    private int goodsLike;

    /**
     * 產品評論
     */
    private int commentCount;

    /**
     * 產品sku列表
     */
    private List<GoodsInfoVo> goodsInfoVoList;

    /**
     * 店铺视图属性
     */
    private StoreVo storeVo;

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

    public Integer getGoodsCountry() {
        return goodsCountry;
    }

    public void setGoodsCountry(Integer goodsCountry) {
        this.goodsCountry = goodsCountry;
    }

    public String getJingle() {
        return jingle;
    }

    public void setJingle(String jingle) {
        this.jingle = jingle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getGoodsQRCode() {
        return goodsQRCode;
    }

    public void setGoodsQRCode(String goodsQRCode) {
        this.goodsQRCode = goodsQRCode;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getGoodsBody() {
        return goodsBody;
    }

    public void setGoodsBody(String goodsBody) {
        this.goodsBody = goodsBody;
    }

    public String getMobileBody() {
        return mobileBody;
    }

    public void setMobileBody(String mobileBody) {
        this.mobileBody = mobileBody;
    }

    public String getGoodsSerial() {
        return goodsSerial;
    }

    public void setGoodsSerial(String goodsSerial) {
        this.goodsSerial = goodsSerial;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getAreaId1() {
        return areaId1;
    }

    public void setAreaId1(int areaId1) {
        this.areaId1 = areaId1;
    }

    public int getAreaId2() {
        return areaId2;
    }

    public void setAreaId2(int areaId2) {
        this.areaId2 = areaId2;
    }

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
    }

    public int getGoodsClick() {
        return goodsClick;
    }

    public void setGoodsClick(int goodsClick) {
        this.goodsClick = goodsClick;
    }

    public Integer getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(Integer evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public Integer getGoodsRate() {
        return goodsRate;
    }

    public void setGoodsRate(Integer goodsRate) {
        this.goodsRate = goodsRate;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getJoinBigSale() {
        return joinBigSale;
    }

    public void setJoinBigSale(int joinBigSale) {
        this.joinBigSale = joinBigSale;
    }

    public List<SpecJsonVo> getSpecJson() {
        return specJson;
    }

    public void setSpecJson(List<SpecJsonVo> specJson) {
        this.specJson = specJson;
    }

    public List<String> getGoodsSpecNameList() {
        return goodsSpecNameList;
    }

    public void setGoodsSpecNameList(List<String> goodsSpecNameList) {
        this.goodsSpecNameList = goodsSpecNameList;
    }

    public String getGoodsSpecValues() {
        return goodsSpecValues;
    }

    public void setGoodsSpecValues(String goodsSpecValues) {
        this.goodsSpecValues = goodsSpecValues;
    }

    public List<GoodsSpecValueJsonVo> getGoodsSpecValueJson() {
        return goodsSpecValueJson;
    }

    public void setGoodsSpecValueJson(List<GoodsSpecValueJsonVo> goodsSpecValueJson) {
        this.goodsSpecValueJson = goodsSpecValueJson;
    }

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

    public List<GoodsAttrVo> getGoodsAttrList() {
        return goodsAttrList;
    }

    public void setGoodsAttrList(List<GoodsAttrVo> goodsAttrList) {
        this.goodsAttrList = goodsAttrList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
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

    public BigDecimal getBatchPrice0() {
        return batchPrice0;
    }

    public void setBatchPrice0(BigDecimal batchPrice0) {
        this.batchPrice0 = batchPrice0;
    }

    public BigDecimal getBatchPrice1() {
        return batchPrice1;
    }

    public void setBatchPrice1(BigDecimal batchPrice1) {
        this.batchPrice1 = batchPrice1;
    }

    public BigDecimal getBatchPrice2() {
        return batchPrice2;
    }

    public void setBatchPrice2(BigDecimal batchPrice2) {
        this.batchPrice2 = batchPrice2;
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

    public BigDecimal getWebPriceMin() {
        return webPriceMin;
    }

    public void setWebPriceMin(BigDecimal webPriceMin) {
        this.webPriceMin = webPriceMin;
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

    public BigDecimal getAppPriceMin() {
        return appPriceMin;
    }

    public void setAppPriceMin(BigDecimal appPriceMin) {
        this.appPriceMin = appPriceMin;
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

    public BigDecimal getWechatPriceMin() {
        return wechatPriceMin;
    }

    public void setWechatPriceMin(BigDecimal wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
    }

    public int getWechatUsable() {
        return wechatUsable;
    }

    public void setWechatUsable(int wechatUsable) {
        this.wechatUsable = wechatUsable;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionStartTime() {
        return promotionStartTime;
    }

    public void setPromotionStartTime(String promotionStartTime) {
        this.promotionStartTime = promotionStartTime;
    }

    public String getPromotionEndTime() {
        return promotionEndTime;
    }

    public void setPromotionEndTime(String promotionEndTime) {
        this.promotionEndTime = promotionEndTime;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionTypeText() {
        return promotionTypeText;
    }

    public void setPromotionTypeText(String promotionTypeText) {
        this.promotionTypeText = promotionTypeText;
    }

    public long getPromotionCountDownTime() {
        return promotionCountDownTime;
    }

    public void setPromotionCountDownTime(long promotionCountDownTime) {
        this.promotionCountDownTime = promotionCountDownTime;
    }

    public String getPromotionCountDownTimeType() {
        return promotionCountDownTimeType;
    }

    public void setPromotionCountDownTimeType(String promotionCountDownTimeType) {
        this.promotionCountDownTimeType = promotionCountDownTimeType;
    }

    public int getPromotionState() {
        return promotionState;
    }

    public void setPromotionState(int promotionState) {
        this.promotionState = promotionState;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public List<Conform> getConformList() {
        return conformList;
    }

    public void setConformList(List<Conform> conformList) {
        this.conformList = conformList;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public int getBuyNow() {
        return buyNow;
    }

    public void setBuyNow(int buyNow) {
        this.buyNow = buyNow;
    }

    public int getAddCart() {
        return addCart;
    }

    public void setAddCart(int addCart) {
        this.addCart = addCart;
    }

    public Integer getIsGift() {
        return isGift;
    }

    public void setIsGift(Integer isGift) {
        this.isGift = isGift;
    }

    public List<GiftVo> getGiftVoList() {
        return giftVoList;
    }

    public void setGiftVoList(List<GiftVo> giftVoList) {
        this.giftVoList = giftVoList;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public List<ContractVo> getContractVoList() {
        return contractVoList;
    }

    public void setContractVoList(List<ContractVo> contractVoList) {
        this.contractVoList = contractVoList;
    }

    public int getWeb() {
        return web;
    }

    public void setWeb(int web) {
        this.web = web;
    }

    public int getApp() {
        return app;
    }

    public void setApp(int app) {
        this.app = app;
    }

    public int getWechat() {
        return wechat;
    }

    public void setWechat(int wechat) {
        this.wechat = wechat;
    }

    public Integer getVirtualOverdueRefund() {
        return virtualOverdueRefund;
    }

    public void setVirtualOverdueRefund(Integer virtualOverdueRefund) {
        this.virtualOverdueRefund = virtualOverdueRefund;
    }

    public BigDecimal getForeignTaxRate() {
        return foreignTaxRate;
    }

    public void setForeignTaxRate(BigDecimal foreignTaxRate) {
        this.foreignTaxRate = foreignTaxRate;
    }

    public int getIsSeckill() {
        return isSeckill;
    }

    public void setIsSeckill(int isSeckill) {
        this.isSeckill = isSeckill;
    }

    public List<GoodsDetailCouponVo> getGoodsDetailCouponVoList() {
        return goodsDetailCouponVoList;
    }

    public void setGoodsDetailCouponVoList(List<GoodsDetailCouponVo> goodsDetailCouponVoList) {
        this.goodsDetailCouponVoList = goodsDetailCouponVoList;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getGoodsVideo() {
        return goodsVideo;
    }

    public void setGoodsVideo(String goodsVideo) {
        this.goodsVideo = goodsVideo;
    }

    public String getDetailVideo() {
        return detailVideo;
    }

    public void setDetailVideo(String detailVideo) {
        this.detailVideo = detailVideo;
    }

    public int getIsWaterMark() {
        return isWaterMark;
    }

    public void setIsWaterMark(int isWaterMark) {
        this.isWaterMark = isWaterMark;
    }

    public String getWaterMarkImage() {
        return waterMarkImage;
    }

    public void setWaterMarkImage(String waterMarkImage) {
        this.waterMarkImage = waterMarkImage;
    }

    public String getWaterMarkPosition() {
        return waterMarkPosition;
    }

    public void setWaterMarkPosition(String waterMarkPosition) {
        this.waterMarkPosition = waterMarkPosition;
    }

    public AdminCountry getAdminCountry() {
        return adminCountry;
    }

    public void setAdminCountry(AdminCountry adminCountry) {
        this.adminCountry = adminCountry;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public int getGoodsLike() {
        return goodsLike;
    }

    public void setGoodsLike(int goodsLike) {
        this.goodsLike = goodsLike;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<GoodsInfoVo> getGoodsInfoVoList() {
        return goodsInfoVoList;
    }

    public void setGoodsInfoVoList(List<GoodsInfoVo> goodsInfoVoList) {
        this.goodsInfoVoList = goodsInfoVoList;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    @Override
    public String toString() {
        return "GoodsDetailVo{" +
                "goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCountry=" + goodsCountry +
                ", jingle='" + jingle + '\'' +
                ", categoryId=" + categoryId +
                ", goodsQRCode='" + goodsQRCode + '\'' +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", goodsBody='" + goodsBody + '\'' +
                ", mobileBody='" + mobileBody + '\'' +
                ", goodsSerial='" + goodsSerial + '\'' +
                ", goodsStatus=" + goodsStatus +
                ", colorId=" + colorId +
                ", areaId1=" + areaId1 +
                ", areaId2=" + areaId2 +
                ", goodsFavorite=" + goodsFavorite +
                ", goodsClick=" + goodsClick +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", goodsSaleNum=" + goodsSaleNum +
                ", imageSrc='" + imageSrc + '\'' +
                ", joinBigSale=" + joinBigSale +
                ", specJson=" + specJson +
                ", goodsSpecNameList=" + goodsSpecNameList +
                ", goodsSpecValues='" + goodsSpecValues + '\'' +
                ", goodsSpecValueJson=" + goodsSpecValueJson +
                ", goodsImageList=" + goodsImageList +
                ", goodsAttrList=" + goodsAttrList +
                ", goodsList=" + goodsList +
                ", goodsModal=" + goodsModal +
                ", batchNum0=" + batchNum0 +
                ", batchNum0End=" + batchNum0End +
                ", batchNum1=" + batchNum1 +
                ", batchNum1End=" + batchNum1End +
                ", batchNum2=" + batchNum2 +
                ", batchPrice0=" + batchPrice0 +
                ", batchPrice1=" + batchPrice1 +
                ", batchPrice2=" + batchPrice2 +
                ", webPrice0=" + webPrice0 +
                ", webPrice1=" + webPrice1 +
                ", webPrice2=" + webPrice2 +
                ", webPriceMin=" + webPriceMin +
                ", webUsable=" + webUsable +
                ", appPrice0=" + appPrice0 +
                ", appPrice1=" + appPrice1 +
                ", appPrice2=" + appPrice2 +
                ", appPriceMin=" + appPriceMin +
                ", appUsable=" + appUsable +
                ", wechatPrice0=" + wechatPrice0 +
                ", wechatPrice1=" + wechatPrice1 +
                ", wechatPrice2=" + wechatPrice2 +
                ", wechatPriceMin=" + wechatPriceMin +
                ", wechatUsable=" + wechatUsable +
                ", promotionId=" + promotionId +
                ", promotionStartTime=" + promotionStartTime +
                ", promotionEndTime=" + promotionEndTime +
                ", promotionType=" + promotionType +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", promotionCountDownTime=" + promotionCountDownTime +
                ", promotionCountDownTimeType='" + promotionCountDownTimeType + '\'' +
                ", promotionState=" + promotionState +
                ", unitName='" + unitName + '\'' +
                ", discount=" + discount +
                ", conformList=" + conformList +
                ", book=" + book +
                ", bookList=" + bookList +
                ", buyNow=" + buyNow +
                ", addCart=" + addCart +
                ", isGift=" + isGift +
                ", giftVoList=" + giftVoList +
                ", groupId=" + groupId +
                ", contractVoList=" + contractVoList +
                ", web=" + web +
                ", app=" + app +
                ", wechat=" + wechat +
                ", virtualOverdueRefund=" + virtualOverdueRefund +
                ", foreignTaxRate=" + foreignTaxRate +
                ", isSeckill=" + isSeckill +
                ", goodsDetailCouponVoList=" + goodsDetailCouponVoList +
                ", isVirtual=" + isVirtual +
                ", goodsVideo='" + goodsVideo + '\'' +
                ", detailVideo='" + detailVideo + '\'' +
                ", isWaterMark=" + isWaterMark +
                ", waterMarkImage='" + waterMarkImage + '\'' +
                ", waterMarkPosition='" + waterMarkPosition + '\'' +
                ", adminCountry=" + adminCountry +
                ", isFavorite=" + isFavorite +
                ", isLike=" + isLike +
                ", goodsLike=" + goodsLike +
                ", goodsInfoVoList=" + goodsInfoVoList +
                ", storeVo=" + storeVo +
                '}';
    }
}

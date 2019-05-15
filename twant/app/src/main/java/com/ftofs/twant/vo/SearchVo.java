package com.ftofs.twant.vo;

import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsImage;
import com.ftofs.twant.vo.goods.BatchNumPriceVo;
import com.ftofs.twant.vo.store.ServiceVo;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 搜索商品
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:00
 */
public class SearchVo {
    /**
     * 商品SPU编号
     */
    
    private int commonId;
    /**
     * 商品名称
     */
    
    private String goodsName;
    /**
     * 商品產地
     */
    private Integer goodsCountry=0;
    /**
     * 商品名称[带关键词高亮]
     */
    private String goodsNameHighlight;
    /**
     * 商品卖点
     */
    
    private String jingle;
    /**
     * 商品分类编号
     */
    
    private int categoryId;
    /**
     * 店铺编号
     */
    
    private int storeId;
    /**
     * 品牌编号
     */
    
    private int brandId;
    /**
     * 商品状态<br>
     * 0下架，1正常，10违规禁售
     */
    
    private int goodsState;
    /**
     * 审核状态<br>
     * 0未通过，1已通过，10审核中
     */
    
    private int goodsVerify;
    /**
     * 商品状态<br>
     * 可以购买1，不可购买0
     */
    
    private int goodsStatus=0;
    /**
     * 省市县(区)内容
     */
    
    private String areaInfo;
    /**
     * 商品运费
     */
    
    private double goodsFreight;
    /**
     * 运费模板ID
     */
    
    private int freightTemplateId = 0;
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
     * 起购点1 终点
     */
    private int batchNum1End = 0;
    /**
     * 起购量2
     */
    
    private int batchNum2;
    /**
     * 起购价0
     */
    
    private double batchPrice0;
    /**
     * 起购价1
     */
    
    private double batchPrice1;
    /**
     * 起购价2
     */
    
    private double batchPrice2;
    /**
     * PC端起购价0
     */
    
    private double webPrice0;
    /**
     * PC端起购价1
     */
    
    private double webPrice1;
    /**
     * PC端起购价2
     */
    
    private double webPrice2;
    /**
     * 商品最低价
     */
    
    private double webPriceMin;
    /**
     * PC端促销进行状态
     */
    
    private int webUsable = 0;
    /**
     * APP端起购价0
     */
    
    private double appPrice0;
    /**
     * APP端起购价1
     */
    
    private double appPrice1;
    /**
     * APP端起购价2
     */
    
    private double appPrice2;
    /**
     * 商品最低价
     */
    
    private double appPriceMin;
    /**
     * APP端促销进行状态
     */
    
    private int appUsable = 0;
    /**
     * 微信端起购价0
     */
    
    private double wechatPrice0;
    /**
     * 微信端起购价1
     */
    
    private double wechatPrice1;
    /**
     * 微信端起购价2
     */
    
    private double wechatPrice2;
    /**
     * 商品最低价
     */
    
    private double wechatPriceMin;
    /**
     * 微信端促销进行状态
     */
    
    private int wechatUsable = 0;
    /**
     * 计量单位
     */
    
    private String unitName = "";
    /**
     * 促销编号
     */
    
    private int promotionId = 0;
    /**
     * 促销开始时间
     */
    
    private String promotionStartTime;
    /**
     * 促销结束时间
     */
    
    private String promotionEndTime;
    /**
     * 活动状态
     */
    
    private int promotionState = 0;
    /**
     * 活动类型
     */
    
    private int promotionType = 1;
    /**
     * 活动类型文字
     */
    private String promotionTypeText;
    /**
     * 销售模式
     * 零售1  批发2
     */
    
    private int goodsModal;
    /**
     * 被關注数量
     */
    
    private int goodsFavorite = 0;
    /**
     * 评价数量
     */
    
    private int evaluateNum = 0;
    /**
     * 好评率
     */
    
    private int goodsRate = 0;
    /**
     * 销售数量
     */
    
    private int goodsSaleNum = 0;
    /**
     * 图片名称
     */
    
    private String imageName;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 是否有赠品
     */
    
    private int isGift;
    /**
     * 商品分类名称
     */
    
    private String categoryName="";
    /**
     * 店铺名称
     */
    
    private String storeName="";

    /**
     * 店铺头像
     * Modify By yangjian 2018/12/29 18:02
     */
    private String storeAvatarUrl="";

    /**
     * 店铺客服
     * Modify By yangjian 2018/12/29 18:02
     */
    private List<ServiceVo> storePresalesList;
    /**
     * 商品产地
     * Modify By yangjian 2019/1/9 17:29
     */
    private AdminCountry adminCountry;

    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    
    private int isOwnShop=0;
    /**
     * 卖家id</br>
     */
    
    private int sellerId;
    /**
     * 分佣比例
     */
    
    private int commissionRate;
    /**
     * web佣金
     */
    
    private BigDecimal webCommission;
    /**
     * app佣金
     */
    
    private BigDecimal appCommission;
    /**
     * wechat佣金
     */
    
    private BigDecimal wechatCommission;
    /**
     * 可用店铺券（该字段已弃用2017/10/30）
     */
    
    private int usableVoucher;
    /**
     * 总推广量（订单数量）
     */
    
    private long ordersCount;
    /**
     * 总佣金
     */
    
    private double commissionTotal;
    /**
     * 判断im是否在线
     */
    private int isOnline = 0;
    /**
     * 商品sku编号
     */
    private int goodsId;
    /**
     * 商品sku规格字符串
     */
    private String goodsSpec;

    /**
     * 商品門店所在地區(不存在於搜索引擎)
     */
    private String chainAreaInfo;
    /**
     * 预留字段
     */
    private String extendString0 = "";
    private String extendString1 = "";
    private String extendString2 = "";
    private String extendString3 = "";
    private String extendString4 = "";
    private String extendString5 = "";
    private String extendString6 = "";
    private String extendString7 = "";
    private String extendString8 = "";
    private String extendString9 = "";

    //Modify By liusf 2019/3/5 18:26 國家產地ID
    private int extendInt0 = 0;

    //Modify By liusf 2019/4/2 18:58 商品評論
    private int extendInt1 = 0;

    //Modify By liusf 2019/4/3 17:01 1是0否有商品視頻
    private int extendInt2 = 0;

    private int extendInt3 = 0;
    private int extendInt4 = 0;
    private int extendInt5 = 0;
    private int extendInt6 = 0;
    private int extendInt7 = 0;
    private int extendInt8 = 0;
    private int extendInt9 = 0;
    private double extendPrice0 = 0.0;
    private double extendPrice1 = 0.0;
    private double extendPrice2 = 0.0;
    private double extendPrice3 = 0.0;
    private double extendPrice4 = 0.0;
    private double extendPrice5 = 0.0;
    private double extendPrice6 = 0.0;
    private double extendPrice7 = 0.0;
    private double extendPrice8 = 0.0;
    private double extendPrice9 = 0.0;

    //Modify By liusf 2019/1/2 14:58 商品創建時間
    private String extendTime0 = "";

    private String extendTime1 = "";
    private String extendTime2 = "";
    private String extendTime3 = "";
    private String extendTime4 = "";
    private String extendTime5 = "";
    private String extendTime6 = "";
    private String extendTime7 = "";
    private String extendTime8 = "";
    private String extendTime9 = "";

    /**
     * 商品图片列表
     */
    private List<GoodsImage> goodsImageList=new ArrayList<>();
    /**
     * sku列表
     */
    private List<Goods> goodsList=new ArrayList<>();
    /**
     * 阶梯下个，key是区间，value价格
     */
    private List<BatchNumPriceVo> batchNumPriceVoList = new ArrayList<>();

    /**
     * 客服列表
     */
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    public SearchVo() {
    }

    public SearchVo(SearchGoodsVo s) {
        this.commonId = s.getCommonId();
        this.goodsName = s.getGoodsName();
        this.jingle = s.getJingle();
        this.categoryId = s.getCategoryId();
        this.storeId = s.getStoreId();
        this.brandId = s.getBrandId();
        this.goodsState = s.getGoodsState();
        this.goodsVerify = s.getGoodsVerify();
        this.goodsStatus = s.getGoodsStatus();
        this.goodsFreight = Double.parseDouble(s.getGoodsFreight().toString());
        this.freightTemplateId = s.getFreightTemplateId();
        this.batchNum0 = s.getBatchNum0();
        this.batchNum0End = s.getBatchNum0End();
        this.batchNum1 = s.getBatchNum1();
        this.batchNum1End = s.getBatchNum1End();
        this.batchNum2 = s.getBatchNum2();
        this.batchPrice0 = Double.parseDouble(s.getBatchPrice0().toString());
        this.batchPrice1 = Double.parseDouble(s.getBatchPrice1().toString());
        this.batchPrice2 = Double.parseDouble(s.getBatchPrice2().toString());
        this.webPrice0 = Double.parseDouble(s.getWebPrice0().toString());
        this.webPrice1 = Double.parseDouble(s.getWebPrice2().toString());
        this.webPrice2 = Double.parseDouble(s.getWebPrice2().toString());
        this.webPriceMin = Double.parseDouble(s.getWebPriceMin().toString());
        this.webUsable = s.getWebUsable();
        this.appPrice0 = Double.parseDouble(s.getAppPrice0().toString());
        this.appPrice1 = Double.parseDouble(s.getAppPrice1().toString());
        this.appPrice2 = Double.parseDouble(s.getAppPrice2().toString());
        this.appPriceMin = Double.parseDouble(s.getAppPriceMin().toString());
        this.appUsable = s.getAppUsable();
        this.wechatPrice0 = Double.parseDouble(s.getWechatPrice0().toString());
        this.wechatPrice1 = Double.parseDouble(s.getWechatPrice1().toString());
        this.wechatPrice2 = Double.parseDouble(s.getWechatPrice2().toString());
        this.wechatPriceMin = Double.parseDouble(s.getWechatPriceMin().toString());
        this.wechatUsable = s.getWebUsable();
        this.unitName = s.getUnitName();
        this.promotionId = s.getPromotionId();
        this.promotionState = s.getPromotionState();
        this.promotionType = s.getPromotionType();
        this.goodsModal = s.getGoodsModal();
        this.goodsFavorite = s.getGoodsFavorite();
        this.evaluateNum = s.getEvaluateNum();
        this.goodsRate = s.getGoodsRate();
        this.goodsSaleNum = s.getGoodsSaleNum();
        this.imageName = s.getImageName();
        this.isGift = s.getIsGift();
        this.categoryName = s.getCategoryName();
        this.storeName = s.getStoreName();
        this.isOwnShop = s.getIsOwnShop();
        this.sellerId = s.getSellerId();
        this.commissionRate = s.getCommissionRate();
        this.webCommission = s.getWebCommission();
        this.appCommission = s.getAppCommission();
        this.wechatCommission = s.getWechatCommission();
        this.usableVoucher = s.getUsableVoucher();
        this.ordersCount = s.getOrdersCount();
        this.commissionTotal = Double.parseDouble(s.getCommissionTotal().toString());
        this.extendString0 = s.getExtendString0();
        this.extendString1 = s.getExtendString1();
        this.extendString2 = s.getExtendString2();
        this.extendString3 = s.getExtendString3();
        this.extendString4 = s.getExtendString4();
        this.extendString5 = s.getExtendString5();
        this.extendString6 = s.getExtendString6();
        this.extendString7 = s.getExtendString7();
        this.extendString8 = s.getExtendString8();
        this.extendString9 = s.getExtendString9();
        this.extendInt0 = s.getExtendInt0();
        this.extendInt1 = s.getExtendInt1();
        this.extendInt2 = s.getExtendInt2();
        this.extendInt3 = s.getExtendInt3();
        this.extendInt4 = s.getExtendInt4();
        this.extendInt5 = s.getExtendInt5();
        this.extendInt6 = s.getExtendInt6();
        this.extendInt7 = s.getExtendInt7();
        this.extendInt8 = s.getExtendInt8();
        this.extendInt9 = s.getExtendInt9();
        this.extendPrice0 = s.getExtendInt0();
        this.extendPrice1 = s.getBatchNum1();
        this.extendPrice2 = s.getBatchNum2();
        this.extendPrice3 = s.getExtendInt3();
        this.extendPrice4 = s.getExtendInt4();
        this.extendPrice5 = s.getExtendInt5();
        this.extendPrice6 = s.getExtendInt6();
        this.extendPrice7 = s.getExtendInt7();
        this.extendPrice8 = s.getExtendInt8();
        this.extendPrice9 = s.getExtendInt9();
        this.extendTime0 = s.getExtendTime0().toString();
        this.goodsImageList = s.getGoodsImageList();
        this.batchNumPriceVoList = s.getBatchNumPriceVoList();
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

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
    }

    public int getGoodsVerify() {
        return goodsVerify;
    }

    public void setGoodsVerify(int goodsVerify) {
        this.goodsVerify = goodsVerify;
    }

    public int getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(int goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }

    public double getGoodsFreight() {
        return goodsFreight;
    }

    public void setGoodsFreight(double goodsFreight) {
        this.goodsFreight = goodsFreight;
    }

    public int getFreightTemplateId() {
        return freightTemplateId;
    }

    public void setFreightTemplateId(int freightTemplateId) {
        this.freightTemplateId = freightTemplateId;
    }

    public int getBatchNum0() {
        return batchNum0;
    }

    public void setBatchNum0(int batchNum0) {
        this.batchNum0 = batchNum0;
    }

    public int getBatchNum0End() {
        return (batchNum1 == 0) ? batchNum0End : batchNum1 - 1;
    }

    public int getBatchNum1() {
        return batchNum1;
    }

    public void setBatchNum1(int batchNum1) {
        this.batchNum1 = batchNum1;
    }

    public int getBatchNum1End() {
        return (batchNum2 == 0) ? batchNum1End : batchNum2 - 1;
    }

    public int getBatchNum2() {
        return batchNum2;
    }

    public void setBatchNum2(int batchNum2) {
        this.batchNum2 = batchNum2;
    }

    public double getBatchPrice0() {
        return batchPrice0;
    }

    public void setBatchPrice0(double batchPrice0) {
        this.batchPrice0 = batchPrice0;
    }

    public double getBatchPrice1() {
        return batchPrice1;
    }

    public void setBatchPrice1(double batchPrice1) {
        this.batchPrice1 = batchPrice1;
    }

    public double getBatchPrice2() {
        return batchPrice2;
    }

    public void setBatchPrice2(double batchPrice2) {
        this.batchPrice2 = batchPrice2;
    }

    public void setBatchNum0End(int batchNum0End) {
        this.batchNum0End = batchNum0End;
    }

    public void setBatchNum1End(int batchNum1End) {
        this.batchNum1End = batchNum1End;
    }

    public double getWebPrice0() {
        if (getWebUsable() == 0 || webPrice0 == 0) {
            return batchPrice0;
        } else {
            return webPrice0;
        }
    }

    public void setWebPrice0(double webPrice0) {
        this.webPrice0 = webPrice0;
    }

    public double getWebPrice1() {
        if (getWebUsable() == 0 || webPrice1 == 0) {
            return batchPrice1;
        } else {
            return webPrice1;
        }
    }

    public void setWebPrice1(double webPrice1) {
        this.webPrice1 = webPrice1;
    }

    public double getWebPrice2() {
        if (getWebUsable() == 0 || webPrice2 == 0) {
            return batchPrice2;
        } else {
            return webPrice2;
        }
    }

    public void setWebPrice2(double webPrice2) {
        this.webPrice2 = webPrice2;
    }

    public double getWebPriceMin() {
        if (getWebUsable() == 0 || webPriceMin == 0) {
            return batchPrice2;
        } else {
            return webPriceMin;
        }
    }

    public void setWebPriceMin(double webPriceMin) {
        this.webPriceMin = webPriceMin;
    }

    public int getWebUsable() {
        return 1;
    }

    public void setWebUsable(int webUsable) {
        this.webUsable = webUsable;
    }

    public double getAppPrice0() {
        if (getAppUsable() == 0 || appPrice0 == 0) {
            return batchPrice0;
        } else {
            return appPrice0;
        }
    }

    public void setAppPrice0(double appPrice0) {
        this.appPrice0 = appPrice0;
    }

    public double getAppPrice1() {
        if (getAppUsable() == 0 || appPrice1 == 0) {
            return batchPrice1;
        } else {
            return appPrice1;
        }
    }

    public void setAppPrice1(double appPrice1) {
        this.appPrice1 = appPrice1;
    }

    public double getAppPrice2() {
        if (getAppUsable() == 0 || appPrice2 == 0) {
            return batchPrice2;
        } else {
            return appPrice2;
        }
    }

    public void setAppPrice2(double appPrice2) {
        this.appPrice2 = appPrice2;
    }

    public double getAppPriceMin() {
        if (getAppUsable() == 0 || appPriceMin == 0) {
            return batchPrice2;
        } else {
            return appPriceMin;
        }
    }

    public void setAppPriceMin(double appPriceMin) {
        this.appPriceMin = appPriceMin;
    }

    public int getAppUsable() {
        return 1;
    }

    public void setAppUsable(int appUsable) {
        this.appUsable = appUsable;
    }

    public double getWechatPrice0() {
        if (getWechatUsable() == 0 || wechatPrice0 == 0) {
            return batchPrice0;
        } else {
            return wechatPrice0;
        }
    }

    public void setWechatPrice0(double wechatPrice0) {
        this.wechatPrice0 = wechatPrice0;
    }

    public double getWechatPrice1() {
        if (getWechatUsable() == 0 || wechatPrice1 == 0) {
            return batchPrice1;
        } else {
            return wechatPrice1;
        }
    }

    public void setWechatPrice1(double wechatPrice1) {
        this.wechatPrice1 = wechatPrice1;
    }

    public double getWechatPrice2() {
        if (getWechatUsable() == 0 || wechatPrice2 == 0) {
            return batchPrice2;
        } else {
            return wechatPrice2;
        }
    }

    public void setWechatPrice2(double wechatPrice2) {
        this.wechatPrice2 = wechatPrice2;
    }

    public double getWechatPriceMin() {
        if (getWechatUsable() == 0 || wechatPriceMin == 0) {
            return batchPrice2;
        } else {
            return wechatPriceMin;
        }
    }

    public void setWechatPriceMin(double wechatPriceMin) {
        this.wechatPriceMin = wechatPriceMin;
    }

    public int getWechatUsable() {
        return 0;
    }

    public void setWechatUsable(int wechatUsable) {
        this.wechatUsable = wechatUsable;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public int getPromotionState() {
        return promotionState;
    }

    public void setPromotionState(int promotionState) {
        this.promotionState = promotionState;
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

    public int getGoodsModal() {
        return goodsModal;
    }

    public void setGoodsModal(int goodsModal) {
        this.goodsModal = goodsModal;
    }

    public int getGoodsFavorite() {
        return goodsFavorite;
    }

    public void setGoodsFavorite(int goodsFavorite) {
        this.goodsFavorite = goodsFavorite;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public int getGoodsRate() {
        return goodsRate;
    }

    public void setGoodsRate(int goodsRate) {
        this.goodsRate = goodsRate;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getIsGift() {
        return isGift;
    }

    public void setIsGift(int isGift) {
        this.isGift = isGift;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public int getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(int commissionRate) {
        this.commissionRate = commissionRate;
    }

    public List<GoodsImage> getGoodsImageList() {
        return goodsImageList;
    }

    public void setGoodsImageList(List<GoodsImage> goodsImageList) {
        this.goodsImageList = goodsImageList;
    }

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public List<BatchNumPriceVo> getBatchNumPriceVoList() {
        return batchNumPriceVoList;
    }

    public void setBatchNumPriceVoList(List<BatchNumPriceVo> batchNumPriceVoList) {
        this.batchNumPriceVoList = batchNumPriceVoList;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public long getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(long ordersCount) {
        this.ordersCount = ordersCount;
    }

    public double getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(double commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public BigDecimal getWebCommission() {
        return BigDecimal.ZERO;
    }

    public void setWebCommission(BigDecimal webCommission) {
        this.webCommission = webCommission;
    }

    public BigDecimal getAppCommission() {
        return BigDecimal.ZERO;
    }

    public void setAppCommission(BigDecimal appCommission) {
        this.appCommission = appCommission;
    }

    public BigDecimal getWechatCommission() {
        return BigDecimal.ZERO;
    }

    public void setWechatCommission(BigDecimal wechatCommission) {
        this.wechatCommission = wechatCommission;
    }

    public int getUsableVoucher() {
        return usableVoucher;
    }

    public void setUsableVoucher(int usableVoucher) {
        this.usableVoucher = usableVoucher;
    }

    public String getGoodsNameHighlight() {
        return goodsNameHighlight;
    }

    public void setGoodsNameHighlight(String goodsNameHighlight) {
        this.goodsNameHighlight = goodsNameHighlight;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public String getExtendString0() {
        return extendString0;
    }

    public void setExtendString0(String extendString0) {
        this.extendString0 = extendString0;
    }

    public String getExtendString1() {
        return extendString1;
    }

    public void setExtendString1(String extendString1) {
        this.extendString1 = extendString1;
    }

    public String getExtendString2() {
        return extendString2;
    }

    public void setExtendString2(String extendString2) {
        this.extendString2 = extendString2;
    }

    public String getExtendString3() {
        return extendString3;
    }

    public void setExtendString3(String extendString3) {
        this.extendString3 = extendString3;
    }

    public String getExtendString4() {
        return extendString4;
    }

    public void setExtendString4(String extendString4) {
        this.extendString4 = extendString4;
    }

    public String getExtendString5() {
        return extendString5;
    }

    public void setExtendString5(String extendString5) {
        this.extendString5 = extendString5;
    }

    public String getExtendString6() {
        return extendString6;
    }

    public void setExtendString6(String extendString6) {
        this.extendString6 = extendString6;
    }

    public String getExtendString7() {
        return extendString7;
    }

    public void setExtendString7(String extendString7) {
        this.extendString7 = extendString7;
    }

    public String getExtendString8() {
        return extendString8;
    }

    public void setExtendString8(String extendString8) {
        this.extendString8 = extendString8;
    }

    public String getExtendString9() {
        return extendString9;
    }

    public void setExtendString9(String extendString9) {
        this.extendString9 = extendString9;
    }

    public int getExtendInt0() {
        return extendInt0;
    }

    public void setExtendInt0(int extendInt0) {
        this.extendInt0 = extendInt0;
    }

    public int getExtendInt1() {
        return extendInt1;
    }

    public void setExtendInt1(int extendInt1) {
        this.extendInt1 = extendInt1;
    }

    public int getExtendInt2() {
        return extendInt2;
    }

    public void setExtendInt2(int extendInt2) {
        this.extendInt2 = extendInt2;
    }

    public int getExtendInt3() {
        return extendInt3;
    }

    public void setExtendInt3(int extendInt3) {
        this.extendInt3 = extendInt3;
    }

    public int getExtendInt4() {
        return extendInt4;
    }

    public void setExtendInt4(int extendInt4) {
        this.extendInt4 = extendInt4;
    }

    public int getExtendInt5() {
        return extendInt5;
    }

    public void setExtendInt5(int extendInt5) {
        this.extendInt5 = extendInt5;
    }

    public int getExtendInt6() {
        return extendInt6;
    }

    public void setExtendInt6(int extendInt6) {
        this.extendInt6 = extendInt6;
    }

    public int getExtendInt7() {
        return extendInt7;
    }

    public void setExtendInt7(int extendInt7) {
        this.extendInt7 = extendInt7;
    }

    public int getExtendInt8() {
        return extendInt8;
    }

    public void setExtendInt8(int extendInt8) {
        this.extendInt8 = extendInt8;
    }

    public int getExtendInt9() {
        return extendInt9;
    }

    public void setExtendInt9(int extendInt9) {
        this.extendInt9 = extendInt9;
    }

    public double getExtendPrice0() {
        return extendPrice0;
    }

    public void setExtendPrice0(double extendPrice0) {
        this.extendPrice0 = extendPrice0;
    }

    public double getExtendPrice1() {
        return extendPrice1;
    }

    public void setExtendPrice1(double extendPrice1) {
        this.extendPrice1 = extendPrice1;
    }

    public double getExtendPrice2() {
        return extendPrice2;
    }

    public void setExtendPrice2(double extendPrice2) {
        this.extendPrice2 = extendPrice2;
    }

    public double getExtendPrice3() {
        return extendPrice3;
    }

    public void setExtendPrice3(double extendPrice3) {
        this.extendPrice3 = extendPrice3;
    }

    public double getExtendPrice4() {
        return extendPrice4;
    }

    public void setExtendPrice4(double extendPrice4) {
        this.extendPrice4 = extendPrice4;
    }

    public double getExtendPrice5() {
        return extendPrice5;
    }

    public void setExtendPrice5(double extendPrice5) {
        this.extendPrice5 = extendPrice5;
    }

    public double getExtendPrice6() {
        return extendPrice6;
    }

    public void setExtendPrice6(double extendPrice6) {
        this.extendPrice6 = extendPrice6;
    }

    public double getExtendPrice7() {
        return extendPrice7;
    }

    public void setExtendPrice7(double extendPrice7) {
        this.extendPrice7 = extendPrice7;
    }

    public double getExtendPrice8() {
        return extendPrice8;
    }

    public void setExtendPrice8(double extendPrice8) {
        this.extendPrice8 = extendPrice8;
    }

    public double getExtendPrice9() {
        return extendPrice9;
    }

    public void setExtendPrice9(double extendPrice9) {
        this.extendPrice9 = extendPrice9;
    }

    public String getExtendTime0() {
        return extendTime0;
    }

    public void setExtendTime0(String extendTime0) {
        this.extendTime0 = extendTime0;
    }

    public String getExtendTime1() {
        return extendTime1;
    }

    public void setExtendTime1(String extendTime1) {
        this.extendTime1 = extendTime1;
    }

    public String getExtendTime2() {
        return extendTime2;
    }

    public void setExtendTime2(String extendTime2) {
        this.extendTime2 = extendTime2;
    }

    public String getExtendTime3() {
        return extendTime3;
    }

    public void setExtendTime3(String extendTime3) {
        this.extendTime3 = extendTime3;
    }

    public String getExtendTime4() {
        return extendTime4;
    }

    public void setExtendTime4(String extendTime4) {
        this.extendTime4 = extendTime4;
    }

    public String getExtendTime5() {
        return extendTime5;
    }

    public void setExtendTime5(String extendTime5) {
        this.extendTime5 = extendTime5;
    }

    public String getExtendTime6() {
        return extendTime6;
    }

    public void setExtendTime6(String extendTime6) {
        this.extendTime6 = extendTime6;
    }

    public String getExtendTime7() {
        return extendTime7;
    }

    public void setExtendTime7(String extendTime7) {
        this.extendTime7 = extendTime7;
    }

    public String getExtendTime8() {
        return extendTime8;
    }

    public void setExtendTime8(String extendTime8) {
        this.extendTime8 = extendTime8;
    }

    public String getExtendTime9() {
        return extendTime9;
    }

    public void setExtendTime9(String extendTime9) {
        this.extendTime9 = extendTime9;
    }

    public String getStoreAvatarUrl() {
        return storeAvatarUrl;
    }

    public void setStoreAvatarUrl(String storeAvatarUrl) {
        this.storeAvatarUrl = storeAvatarUrl;
    }

    public List<ServiceVo> getStorePresalesList() {
        return storePresalesList;
    }

    public void setStorePresalesList(List<ServiceVo> storePresalesList) {
        this.storePresalesList = storePresalesList;
    }

    public Integer getGoodsCountry() {
        return goodsCountry;
    }

    public void setGoodsCountry(Integer goodsCountry) {
        this.goodsCountry = goodsCountry;
    }

    public AdminCountry getAdminCountry() {
        return adminCountry;
    }

    public void setAdminCountry(AdminCountry adminCountry) {
        this.adminCountry = adminCountry;
    }


    public List<StoreServiceStaffVo> getStoreServiceStaffVoList() {
        return storeServiceStaffVoList;
    }

    public void setStoreServiceStaffVoList(List<StoreServiceStaffVo> storeServiceStaffVoList) {
        this.storeServiceStaffVoList = storeServiceStaffVoList;
    }

    public String getChainAreaInfo() {
        return chainAreaInfo;
    }

    public void setChainAreaInfo(String chainAreaInfo) {
        this.chainAreaInfo = chainAreaInfo;
    }

    @Override
    public String toString() {
        return "SearchVo{" +
                "commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCountry=" + goodsCountry +
                ", goodsNameHighlight='" + goodsNameHighlight + '\'' +
                ", jingle='" + jingle + '\'' +
                ", categoryId=" + categoryId +
                ", storeId=" + storeId +
                ", brandId=" + brandId +
                ", goodsState=" + goodsState +
                ", goodsVerify=" + goodsVerify +
                ", goodsStatus=" + goodsStatus +
                ", areaInfo='" + areaInfo + '\'' +
                ", goodsFreight=" + goodsFreight +
                ", freightTemplateId=" + freightTemplateId +
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
                ", unitName='" + unitName + '\'' +
                ", promotionId=" + promotionId +
                ", promotionStartTime='" + promotionStartTime + '\'' +
                ", promotionEndTime='" + promotionEndTime + '\'' +
                ", promotionState=" + promotionState +
                ", promotionType=" + promotionType +
                ", promotionTypeText='" + promotionTypeText + '\'' +
                ", goodsModal=" + goodsModal +
                ", goodsFavorite=" + goodsFavorite +
                ", evaluateNum=" + evaluateNum +
                ", goodsRate=" + goodsRate +
                ", goodsSaleNum=" + goodsSaleNum +
                ", imageName='" + imageName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", isGift=" + isGift +
                ", categoryName='" + categoryName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeAvatarUrl='" + storeAvatarUrl + '\'' +
                ", storePresalesList=" + storePresalesList +
                ", isOwnShop=" + isOwnShop +
                ", sellerId=" + sellerId +
                ", commissionRate=" + commissionRate +
                ", webCommission=" + webCommission +
                ", appCommission=" + appCommission +
                ", wechatCommission=" + wechatCommission +
                ", usableVoucher=" + usableVoucher +
                ", ordersCount=" + ordersCount +
                ", commissionTotal=" + commissionTotal +
                ", isOnline=" + isOnline +
                ", goodsId=" + goodsId +
                ", goodsSpec='" + goodsSpec + '\'' +
                ", extendString0='" + extendString0 + '\'' +
                ", extendString1='" + extendString1 + '\'' +
                ", extendString2='" + extendString2 + '\'' +
                ", extendString3='" + extendString3 + '\'' +
                ", extendString4='" + extendString4 + '\'' +
                ", extendString5='" + extendString5 + '\'' +
                ", extendString6='" + extendString6 + '\'' +
                ", extendString7='" + extendString7 + '\'' +
                ", extendString8='" + extendString8 + '\'' +
                ", extendString9='" + extendString9 + '\'' +
                ", extendInt0=" + extendInt0 +
                ", extendInt1=" + extendInt1 +
                ", extendInt2=" + extendInt2 +
                ", extendInt3=" + extendInt3 +
                ", extendInt4=" + extendInt4 +
                ", extendInt5=" + extendInt5 +
                ", extendInt6=" + extendInt6 +
                ", extendInt7=" + extendInt7 +
                ", extendInt8=" + extendInt8 +
                ", extendInt9=" + extendInt9 +
                ", extendPrice0=" + extendPrice0 +
                ", extendPrice1=" + extendPrice1 +
                ", extendPrice2=" + extendPrice2 +
                ", extendPrice3=" + extendPrice3 +
                ", extendPrice4=" + extendPrice4 +
                ", extendPrice5=" + extendPrice5 +
                ", extendPrice6=" + extendPrice6 +
                ", extendPrice7=" + extendPrice7 +
                ", extendPrice8=" + extendPrice8 +
                ", extendPrice9=" + extendPrice9 +
                ", extendTime0='" + extendTime0 + '\'' +
                ", extendTime1='" + extendTime1 + '\'' +
                ", extendTime2='" + extendTime2 + '\'' +
                ", extendTime3='" + extendTime3 + '\'' +
                ", extendTime4='" + extendTime4 + '\'' +
                ", extendTime5='" + extendTime5 + '\'' +
                ", extendTime6='" + extendTime6 + '\'' +
                ", extendTime7='" + extendTime7 + '\'' +
                ", extendTime8='" + extendTime8 + '\'' +
                ", extendTime9='" + extendTime9 + '\'' +
                ", goodsImageList=" + goodsImageList +
                ", goodsList=" + goodsList +
                ", batchNumPriceVoList=" + batchNumPriceVoList +
                ", adminCountry=" + adminCountry +
                '}';
    }
}

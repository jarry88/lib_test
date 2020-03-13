package com.ftofs.twant.vo;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.promotion.Conform;
import com.ftofs.twant.domain.promotion.VoucherTemplate;
import com.ftofs.twant.vo.contract.ContractVo;
import com.ftofs.twant.vo.evaluate.EvaluateStoreVo;
import com.ftofs.twant.vo.store.ServiceVo;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 搜索店铺
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:00
 */
public class SearchStoreVo {
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 商家编号
     */
    private int sellerId;
    /**
     * 商家用户名
     */
    private String sellerName;
    /**
     * 店铺主营產品
     */
    private String storeZy = "";
    /**
     * 描述相符度分数
     */
    private double storeDesccredit = 5.0;
    /**
     * 服务态度分数
     */
    private double storeServicecredit = 5.0;
    /**
     * 发货速度分数
     */
    private double storeDeliverycredit = 5.0;
    /**
     * 店铺關注数量
     */
    private int storeCollect = 0;
    /**
     * 店铺销量
     */
    private int storeSales = 0;
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    private int isOwnShop;
    /**
     * 店铺头像
     */
    private String storeAvatar;
    /**
     * 店铺头像URL
     */
    private String storeAvatarUrl;
    /**
     * 公司名称
     */
    private String companyName = "";
    /**
     * 所在地
     */
    private String companyArea = "";
    /**
     * 所在地编号
     */
    private Integer companyAreaId = 0;
    /**
     * 查询店铺產品数量
     */
    private long goodsCommonCount = 0;
    /**
     * 店铺评分
     */
    private EvaluateStoreVo evaluateStoreVo = new EvaluateStoreVo();
    /**
     * 產品SPU列表(5个)
     */
    private List<GoodsCommon> goodsCommonList = new ArrayList<>();
    /**
     * 满优惠列表（1个）
     */
    private List<Conform> conformList = new ArrayList<>();
    /**
     * 店铺券列表（4个，免费）
     */
    private List<VoucherTemplate> voucherTemplateList = new ArrayList<>();
    /**
     * 消保服务
     */
    private List<ContractVo> contractVoList = new ArrayList<>();

    /**
     * 判断im是否在线
     */
    private int isOnline = 0;

    /**
     * 商店形象圖
     */
    private String storeFigureImage;

    /**
     * 店铺客服
     * Modify By yangjian 2019/1/4 11:37
     */
    @Deprecated
    private List<ServiceVo> storePresalesList;

    /**
     * 开店天数大於365顯示1年
     * Modify By yangjian 2019/1/4 11:37
     */
    private String shopDay;

    /**
     * 点赞數量
     * Modify By yangjian 2019/1/4 11:37
     */
    private Integer likeCount;

    /**
     * 當前位置與商店閒的距離(格式化為字符串顯示，單位m)
     */
    private String distance = "0";

    //Modify By liusf 2019/1/21 15:45 客服列表
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    /**
     * 門店地區 最下級
     */
    private String chainArea;

    /**
     * 門店詳細地區 一級、三級
     */
    private String chainAreaInfo ;

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

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getStoreZy() {
        return storeZy;
    }

    public void setStoreZy(String storeZy) {
        this.storeZy = storeZy;
    }

    public double getStoreDesccredit() {
        return storeDesccredit;
    }

    public void setStoreDesccredit(double storeDesccredit) {
        this.storeDesccredit = storeDesccredit;
    }

    public double getStoreServicecredit() {
        return storeServicecredit;
    }

    public void setStoreServicecredit(double storeServicecredit) {
        this.storeServicecredit = storeServicecredit;
    }

    public double getStoreDeliverycredit() {
        return storeDeliverycredit;
    }

    public void setStoreDeliverycredit(double storeDeliverycredit) {
        this.storeDeliverycredit = storeDeliverycredit;
    }

    public int getStoreCollect() {
        return storeCollect;
    }

    public void setStoreCollect(int storeCollect) {
        this.storeCollect = storeCollect;
    }

    public int getStoreSales() {
        return storeSales;
    }

    public void setStoreSales(int storeSales) {
        this.storeSales = storeSales;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public String getStoreAvatarUrl() {
        return storeAvatarUrl;
    }

    public void setStoreAvatarUrl(String storeAvatarUrl) {
        this.storeAvatarUrl = storeAvatarUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyArea() {
        return companyArea;
    }

    public void setCompanyArea(String companyArea) {
        this.companyArea = companyArea;
    }

    public Integer getCompanyAreaId() {
        return companyAreaId;
    }

    public void setCompanyAreaId(Integer companyAreaId) {
        this.companyAreaId = companyAreaId;
    }

    public long getGoodsCommonCount() {
        return goodsCommonCount;
    }

    public void setGoodsCommonCount(long goodsCommonCount) {
        this.goodsCommonCount = goodsCommonCount;
    }

    public EvaluateStoreVo getEvaluateStoreVo() {
        return evaluateStoreVo;
    }

    public void setEvaluateStoreVo(EvaluateStoreVo evaluateStoreVo) {
        this.evaluateStoreVo = evaluateStoreVo;
    }

    public List<GoodsCommon> getGoodsCommonList() {
        return goodsCommonList;
    }

    public void setGoodsCommonList(List<GoodsCommon> goodsCommonList) {
        this.goodsCommonList = goodsCommonList;
    }

    public List<Conform> getConformList() {
        return conformList;
    }

    public void setConformList(List<Conform> conformList) {
        this.conformList = conformList;
    }

    public List<VoucherTemplate> getVoucherTemplateList() {
        return voucherTemplateList;
    }

    public void setVoucherTemplateList(List<VoucherTemplate> voucherTemplateList) {
        this.voucherTemplateList = voucherTemplateList;
    }

    public List<ContractVo> getContractVoList() {
        return contractVoList;
    }

    public void setContractVoList(List<ContractVo> contractVoList) {
        this.contractVoList = contractVoList;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getStoreFigureImage() {
        return storeFigureImage;
    }

    public void setStoreFigureImage(String storeFigureImage) {
        this.storeFigureImage = storeFigureImage;
    }

    public List<ServiceVo> getStorePresalesList() {
        return storePresalesList;
    }

    public void setStorePresalesList(List<ServiceVo> storePresalesList) {
        this.storePresalesList = storePresalesList;
    }

    public String getShopDay() {
        return shopDay;
    }

    public void setShopDay(String shopDay) {
        this.shopDay = shopDay;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<StoreServiceStaffVo> getStoreServiceStaffVoList() {
        return storeServiceStaffVoList;
    }

    public void setStoreServiceStaffVoList(List<StoreServiceStaffVo> storeServiceStaffVoList) {
        this.storeServiceStaffVoList = storeServiceStaffVoList;
    }

    public String getChainArea() {
        return chainArea;
    }

    public void setChainArea(String chainArea) {
        this.chainArea = chainArea;
    }

    public String getChainAreaInfo() {
        return chainAreaInfo;
    }

    public void setChainAreaInfo(String chainAreaInfo) {
        this.chainAreaInfo = chainAreaInfo;
    }

    @Override
    public String toString() {
        return "SearchStoreVo{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", storeZy='" + storeZy + '\'' +
                ", storeDesccredit=" + storeDesccredit +
                ", storeServicecredit=" + storeServicecredit +
                ", storeDeliverycredit=" + storeDeliverycredit +
                ", storeCollect=" + storeCollect +
                ", storeSales=" + storeSales +
                ", isOwnShop=" + isOwnShop +
                ", storeAvatar=" + storeAvatar +
                ", storeAvatarUrl='" + storeAvatarUrl + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyArea='" + companyArea + '\'' +
                ", companyAreaId=" + companyAreaId +
                ", goodsCommonCount=" + goodsCommonCount +
                ", evaluateStoreVo=" + evaluateStoreVo +
                ", goodsCommonList=" + goodsCommonList +
                ", conformList=" + conformList +
                ", voucherTemplateList=" + voucherTemplateList +
                ", contractVoList=" + contractVoList +
                ", isOnline=" + isOnline +
                ", storeFigureImage='" + storeFigureImage + '\'' +
                ", storePresalesList=" + storePresalesList +
                ", shopDay='" + shopDay + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}

package com.ftofs.twant.domain.store;

import com.ftofs.twant.domain.chain.Chain;



public class Store {
    /**
     * 店铺编号
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 店铺等级编号
     */
    private int gradeId;

    /**
     * 店铺等级名称
     */
    private String gradeName;

    /**
     * 商家编号
     */
    private int sellerId;

    /**
     * 商家用户名
     */
    private String sellerName;

    /**
     * 店铺分类编号
     */
    private int classId;

    /**
     * 店铺分类名称
     */
    private String className;

    /**
     * 店铺状态
     * 0-关闭 1-开启
     */
    private int state;

    /**
     * 店铺地址
     */
    private String storeAddress;

    /**
     * 店铺创建时间
     */
    private String storeCreateTime;

    /**
     * 店铺过期时间
     */
    private String storeEndTime;

    /**
     * 店铺Logo
     */
    private String storeLogo= "";

    /**
     * 店铺横幅
     */
    private String storeBanner = "";

    /**
     * 店铺头像
     */
    private String storeAvatar = "";

    /**
     * 店铺seo关键字
     */
    private String storeSeoKeywords = "";

    /**
     * 店铺seo描述
     */
    private String storeSeoDescription = "";

    /**
     * 店铺商家电话
     */
    private String storePhone = "";

    /**
     * 店铺主营商品
     */
    private String storeZy = "";

    /**
     * 是否推荐店铺
     * 0-否 1-是，默认为0
     */
    private int isRecommend = 0;

    /**
     * 店铺当前主题
     */
    private String storeTheme = "default";

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
     * 售前客服
     */
    private String storePresales = "";

    /**
     * 售后客服
     */
    private String storeAftersales = "";

    /**
     * 工作时间
     */
    private String storeWorkingtime = "";

    /**
     * 是否自营店铺
     * 0-否 1-是
     */
    private int isOwnShop;

    /**
     * 店铺Logo URL
     */
    private String storeLogoUrl;

    /**
     * 店铺条幅URL
     */
    private String storeBannerUrl;

    /**
     * 店铺头像URL
     */
    private String storeAvatarUrl;

    /**
     * 店铺设置的快递公司Id
     */
    private String shipCompany;

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
     * 结算周期
     */
    private int billCycle = 1;

    /**
     * 结算类型 1按月结/2按天结
     */
    private int billCycleType = 1;

    /**
     * 是否开启店铺装修 1-开启 0-关闭
     */
    private int decorationState = 0;

    /**
     * 是否仅显示店铺装修内容 1-是 0-否
     */
    private int decorationOnly = 0;

    /**
     * 店铺装修主色
     */
    private String decorationColor = "#483E39";

    /**
     * 店铺装修对应的专题编号
     */
    private int decorationId = 0;

    /**
     * 卖家服务
     */
    private String storeCommitment = "";

    /**
     * 营业执照电子版
     */
    private String businessLicenceImage;

    /**
     * 营业执照地址
     */
    private String businessLicenceImageUrl;

    /**
     * 是否有营业执照
     */
    private int hasBusinessLicence;

    /**
     * 食品流通许可证电子版
     */
    private String foodCirculationPermitImage;

    /**
     * 食品流通许可证地址
     */
    private String foodCirculationPermitImageUrl;

    /**
     * 是否有食品流通许可证
     */
    private int hasFoodCirculationPermit;

    /**
     * 发布海外购商品
     */
    private int allowForeignGoods = 0;

    /**
     * 店鋪形象圖(店內局部圖)
     */
    private String storeFigureImage;

    /**
     * 店鋪視頻鏈接
     */
    private String videoUrl;

    /**
     * 門店信息
     */
    private Chain chain;

    /**
     * 店鋪簽名
     */
    private String storeSignature = "";

    /**
     * 店鋪介紹
     */
    private String storeIntroduce = "";

    /**
     * 店鋪形象圖(店內全景圖)
     */
    private String storeFigureImageInner;

    /**
     * 店鋪形象圖(店外全景圖)
     */
    private String storeFigureImageOuter;

    /**
     * 開業日期
     */
    private String startBusiness = "";

    /**
     * 工作日開店時間
     */
    private String weekDayStart;

    /**
     * 工作日關店時間
     */
    private String weekDayEnd;

    /**
     * 休息日開店時間
     */
    private String restDayStart;

    /**
     * 休息日關店時間
     */
    private String restDayEnd;

    /**
     * 是否營業
     */
    private int isOpen;

    /**
     * 商圈編號
     */
    private Integer districtId;

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getWeekDayStart() {
        return weekDayStart;
    }

    public void setWeekDayStart(String weekDayStart) {
        this.weekDayStart = weekDayStart;
    }

    public String getWeekDayEnd() {
        return weekDayEnd;
    }

    public void setWeekDayEnd(String weekDayEnd) {
        this.weekDayEnd = weekDayEnd;
    }

    public String getRestDayStart() {
        return restDayStart;
    }

    public void setRestDayStart(String restDayStart) {
        this.restDayStart = restDayStart;
    }

    public String getRestDayEnd() {
        return restDayEnd;
    }

    public void setRestDayEnd(String restDayEnd) {
        this.restDayEnd = restDayEnd;
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

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreCreateTime() {
        return storeCreateTime;
    }

    public void setStoreCreateTime(String storeCreateTime) {
        this.storeCreateTime = storeCreateTime;
    }

    public String getStoreEndTime() {
        return storeEndTime;
    }

    public void setStoreEndTime(String storeEndTime) {
        this.storeEndTime = storeEndTime;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreBanner() {
        return storeBanner;
    }

    public void setStoreBanner(String storeBanner) {
        this.storeBanner = storeBanner;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public String getStoreSeoKeywords() {
        return storeSeoKeywords;
    }

    public void setStoreSeoKeywords(String storeSeoKeywords) {
        this.storeSeoKeywords = storeSeoKeywords;
    }

    public String getStoreSeoDescription() {
        return storeSeoDescription;
    }

    public void setStoreSeoDescription(String storeSeoDescription) {
        this.storeSeoDescription = storeSeoDescription;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreZy() {
        return storeZy;
    }

    public void setStoreZy(String storeZy) {
        this.storeZy = storeZy;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getStoreTheme() {
        return storeTheme;
    }

    public void setStoreTheme(String storeTheme) {
        this.storeTheme = storeTheme;
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

    public String getStorePresales() {
        return storePresales;
    }

    public void setStorePresales(String storePresales) {
        this.storePresales = storePresales;
    }

    public String getStoreAftersales() {
        return storeAftersales;
    }

    public void setStoreAftersales(String storeAftersales) {
        this.storeAftersales = storeAftersales;
    }

    public String getStoreWorkingtime() {
        return storeWorkingtime;
    }

    public void setStoreWorkingtime(String storeWorkingtime) {
        this.storeWorkingtime = storeWorkingtime;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public String getStoreLogoUrl() {
        return storeLogoUrl;
    }

    public void setStoreLogoUrl(String storeLogoUrl) {
        this.storeLogoUrl = storeLogoUrl;
    }

    public String getStoreBannerUrl() {
        return storeBannerUrl;
    }

    public void setStoreBannerUrl(String storeBannerUrl) {
        this.storeBannerUrl = storeBannerUrl;
    }

    public String getStoreAvatarUrl() {
        return storeAvatarUrl;
    }

    public void setStoreAvatarUrl(String storeAvatarUrl) {
        this.storeAvatarUrl = storeAvatarUrl;
    }

    public String getShipCompany() {
        return shipCompany;
    }

    public void setShipCompany(String shipCompany) {
        this.shipCompany = shipCompany;
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

    public int getBillCycle() {
        return billCycle;
    }

    public void setBillCycle(int billCycle) {
        this.billCycle = billCycle;
    }

    public int getBillCycleType() {
        return billCycleType;
    }

    public void setBillCycleType(int billCycleType) {
        this.billCycleType = billCycleType;
    }

    /**
     * 返回结算周期与结算周期计算单位的整体描述
     * by hbj 2016/02/16
     * @return
     */
    public String getBillCyleDescription() {
        return billCycle + "";
    }

    public int getDecorationState() {
        return decorationState;
    }

    public void setDecorationState(int decorationState) {
        this.decorationState = decorationState;
    }

    public int getDecorationOnly() {
        return decorationOnly;
    }

    public void setDecorationOnly(int decorationOnly) {
        this.decorationOnly = decorationOnly;
    }

    public String getDecorationColor() {
        return decorationColor;
    }

    public void setDecorationColor(String decorationColor) {
        this.decorationColor = decorationColor;
    }

    public int getDecorationId() {
        return decorationId;
    }

    public void setDecorationId(int decorationId) {
        this.decorationId = decorationId;
    }

    public String getStoreCommitment() {
        return storeCommitment;
    }

    public void setStoreCommitment(String storeCommitment) {
        this.storeCommitment = storeCommitment;
    }

    public String getBusinessLicenceImage() {
        return businessLicenceImage;
    }

    public void setBusinessLicenceImage(String businessLicenceImage) {
        this.businessLicenceImage = businessLicenceImage;
    }

    public String getBusinessLicenceImageUrl() {
        return businessLicenceImageUrl;
    }

    public void setBusinessLicenceImageUrl(String businessLicenceImageUrl) {
        this.businessLicenceImageUrl = businessLicenceImageUrl;
    }

    public int getHasBusinessLicence() {
       return 1;
    }

    public void setHasBusinessLicence(int hasBusinessLicence) {
        this.hasBusinessLicence = hasBusinessLicence;
    }

    public String getFoodCirculationPermitImage() {
        return foodCirculationPermitImage;
    }

    public void setFoodCirculationPermitImage(String foodCirculationPermitImage) {
        this.foodCirculationPermitImage = foodCirculationPermitImage;
    }

    public String getFoodCirculationPermitImageUrl() {
        return foodCirculationPermitImageUrl;
    }

    public void setFoodCirculationPermitImageUrl(String foodCirculationPermitImageUrl) {
        this.foodCirculationPermitImageUrl = foodCirculationPermitImageUrl;
    }

    public int getHasFoodCirculationPermit() {
        return 1;
    }

    public void setHasFoodCirculationPermit(int hasFoodCirculationPermit) {
        this.hasFoodCirculationPermit = hasFoodCirculationPermit;
    }

    public int getAllowForeignGoods() {
        return allowForeignGoods;
    }

    public void setAllowForeignGoods(int allowForeignGoods) {
        this.allowForeignGoods = allowForeignGoods;
    }

    public String getStoreFigureImage() {
        return storeFigureImage;
    }

    public void setStoreFigureImage(String storeFigureImage) {
        this.storeFigureImage = storeFigureImage;
    }

    public Chain getChain() {
        return chain;
    }

    public void setChain(Chain chain) {
        this.chain = chain;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getStoreSignature() {
        return storeSignature;
    }

    public void setStoreSignature(String storeSignature) {
        this.storeSignature = storeSignature;
    }

    public String getStoreIntroduce() {
        return storeIntroduce;
    }

    public void setStoreIntroduce(String storeIntroduce) {
        this.storeIntroduce = storeIntroduce;
    }

    public String getStoreFigureImageInner() {
        return storeFigureImageInner;
    }

    public void setStoreFigureImageInner(String storeFigureImageInner) {
        this.storeFigureImageInner = storeFigureImageInner;
    }

    public String getStoreFigureImageOuter() {
        return storeFigureImageOuter;
    }

    public void setStoreFigureImageOuter(String storeFigureImageOuter) {
        this.storeFigureImageOuter = storeFigureImageOuter;
    }

    public String getStartBusiness() {
        return startBusiness;
    }

    public void setStartBusiness(String startBusiness) {
        this.startBusiness = startBusiness;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", gradeId=" + gradeId +
                ", gradeName='" + gradeName + '\'' +
                ", sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", classId=" + classId +
                ", className='" + className + '\'' +
                ", state=" + state +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeCreateTime=" + storeCreateTime +
                ", storeEndTime=" + storeEndTime +
                ", storeLogo='" + storeLogo + '\'' +
                ", storeBanner='" + storeBanner + '\'' +
                ", storeAvatar='" + storeAvatar + '\'' +
                ", storeSeoKeywords='" + storeSeoKeywords + '\'' +
                ", storeSeoDescription='" + storeSeoDescription + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeZy='" + storeZy + '\'' +
                ", isRecommend=" + isRecommend +
                ", storeTheme='" + storeTheme + '\'' +
                ", storeDesccredit=" + storeDesccredit +
                ", storeServicecredit=" + storeServicecredit +
                ", storeDeliverycredit=" + storeDeliverycredit +
                ", storeCollect=" + storeCollect +
                ", storeSales=" + storeSales +
                ", storePresales='" + storePresales + '\'' +
                ", storeAftersales='" + storeAftersales + '\'' +
                ", storeWorkingtime='" + storeWorkingtime + '\'' +
                ", isOwnShop=" + isOwnShop +
                ", storeLogoUrl='" + storeLogoUrl + '\'' +
                ", storeBannerUrl='" + storeBannerUrl + '\'' +
                ", storeAvatarUrl='" + storeAvatarUrl + '\'' +
                ", shipCompany='" + shipCompany + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyArea='" + companyArea + '\'' +
                ", companyAreaId=" + companyAreaId +
                ", billCycle=" + billCycle +
                ", billCycleType=" + billCycleType +
                ", decorationState=" + decorationState +
                ", decorationOnly=" + decorationOnly +
                ", decorationColor='" + decorationColor + '\'' +
                ", decorationId=" + decorationId +
                ", storeCommitment='" + storeCommitment + '\'' +
                ", businessLicenceImage='" + businessLicenceImage + '\'' +
                ", businessLicenceImageUrl='" + businessLicenceImageUrl + '\'' +
                ", hasBusinessLicence=" + hasBusinessLicence +
                ", foodCirculationPermitImage='" + foodCirculationPermitImage + '\'' +
                ", foodCirculationPermitImageUrl='" + foodCirculationPermitImageUrl + '\'' +
                ", hasFoodCirculationPermit=" + hasFoodCirculationPermit +
                ", allowForeignGoods=" + allowForeignGoods +
                ", storeFigureImage='" + storeFigureImage + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", chain=" + chain +
                ", storeSignature='" + storeSignature + '\'' +
                ", storeIntroduce='" + storeIntroduce + '\'' +
                ", storeFigureImageInner='" + storeFigureImageInner + '\'' +
                ", storeFigureImageOuter='" + storeFigureImageOuter + '\'' +
                ", startBusiness='" + startBusiness + '\'' +
                ", weekDayStart='" + weekDayStart + '\'' +
                ", weekDayEnd='" + weekDayEnd + '\'' +
                ", restDayStart='" + restDayStart + '\'' +
                ", restDayEnd='" + restDayEnd + '\'' +
                ", isOpen=" + isOpen +
                '}';
    }
}

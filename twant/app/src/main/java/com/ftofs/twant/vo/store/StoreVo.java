package com.ftofs.twant.vo.store;

import com.ftofs.twant.domain.chain.Chain;
import com.ftofs.twant.domain.store.Store;

import java.util.List;

/**
 * @Description: 店鋪視圖對象
 * @Auther: yangjian
 * @Date: 2018/10/25 11:30
 */
public class StoreVo {
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺分类名称
     */
    private String className;
    /**
     * 店铺地址
     */
    private String storeAddress;
    /**
     * 店铺Logo
     */
    private String storeLogo= "";
    /**
     * 店铺头像
     */
    private String storeAvatar = "";
    /**
     * 店铺主营商品
     */
    private String storeZy = "";
    /**
     * 是否自营店铺</br>
     * 0-否 1-是
     */
    private int isOwnShop;
    /**
     * 店铺状态</br>
     * 0-关闭 1-开启
     */
    private int state;
    /**
     * 工作时间
     */
    private String storeWorkingtime = "";
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
     * 店鋪形象圖(店內局部圖)
     */
    private String storeFigureImage;
    /**
     * 售前客服
     */
    private String storePresales = "";
    /**
     * 售後客服
     */
    private String storeAftersales = "";
    /**
     * 門店地區詳情
     */
    private String chainAreaInfo = "";
    /**
     * 門店地址
     */
    private String chainAddress = "";
    /**
     * 門店電話
     */
    private String chainPhone = "";

    /**
     * 門店開店時間段
     */
    private String  chainOpeningTime = "";

    /**
     * 總訪問量
     */
    private Integer allVisitCount;

    /**
     * 關注數量
     */
    private Integer collectCount;

    /**
     * 點想數量
     */
    private Integer likeCount;

    /**
     * 是否营业 1营业 0打烊
     */
    private int isOpen = 1;

    /**
     * Modify By yangjian 2018/11/26 18:38
     * 店鋪首頁視頻地址
     */
    private String videoUrl = "";

    /**
     * Modify By yangjian 2018/12/1 18:37 description 修改店鋪客服數據結構
     * 店鋪客服
     */
    private List<ServiceVo> storePresalesList;

    /**
     * 开店天数大於365顯示1年
     * Modify By yangjian 2018/12/1 18:37
     */
    private String shopDay;
    /**
     * 店鋪簽名
     */
    private String storeSignature;
    /**
     * 店鋪介紹
     */
    private String storeIntroduce;

    /**
     * 是否已關注 0否1是
     */
    private int isFavorite = 0;

    /**
     * 是否點贊
     */
    private int isLike = 0;

    /**
     * 高德经度
     */
    private Double lng = 0d;

    /**
     * 字符串形式
     */
    private String lngString = "";

    /**
     * 高德纬度
     */
    private Double lat = 0d;
    /**
     * 字符串形式
     */
    private String latString = "";

    /**
     * 店鋪形象圖(店內全景圖)
     */
    private String storeFigureImageInner;

    /**
     * 店鋪形象圖(店外全景圖)
     */
    private String storeFigureImageOuter;

    /**
     * 当前位置与门店位置距离(m)
     */
    private String distance = "0";

    //Modify By liusf 2019/1/21 15:45 客服列表
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    /**
     * 店铺评论回复数
     */
    private int storeReply = 0;

    /**
     * 商家用戶名
     */
    private String sellerName;

    /**
     * 聯係人電話
     */
    private String contactsPhone;

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
     * 店鋪logo地址
     */
    private String storeLogoUrl;

    /**
     * 商品spu數量
     */
    private long goodsCommonCount = 0;

    public StoreVo(){}

    public StoreVo(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.className = store.getClassName();
        this.storeAddress = store.getStoreAddress();
        this.storeLogo = store.getStoreLogo();
        this.storeLogoUrl = store.getStoreLogoUrl();
        this.storeAvatar = store.getStoreAvatarUrl();
        this.storeZy = store.getStoreZy();
        this.isOwnShop = store.getIsOwnShop();
        this.storeWorkingtime = store.getStoreWorkingtime();
        this.companyName = store.getCompanyName();
        this.companyArea = store.getCompanyArea();
        this.companyAreaId = store.getCompanyAreaId();
        this.storeFigureImage = store.getStoreFigureImage();
        this.storePresales = store.getStorePresales();
        this.storeAftersales = store.getStoreAftersales();
        this.state = store.getState();
        this.videoUrl = store.getVideoUrl();
        this.collectCount = store.getStoreCollect();
        this.storeSignature = store.getStoreSignature();
        this.storeIntroduce = store.getStoreIntroduce();
        this.storeFigureImageInner = store.getStoreFigureImageInner();
        this.storeFigureImageOuter = store.getStoreFigureImageOuter();
        this.isOpen = store.getIsOpen();
        this.weekDayStart = store.getWeekDayStart();
        this.weekDayEnd = store.getWeekDayEnd();
        this.restDayStart = store.getRestDayStart();
        this.restDayEnd = store.getRestDayEnd();
    }

    public StoreVo(Store store, Chain chain) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.className = store.getClassName();
        this.storeAddress = store.getStoreAddress();
        this.storeLogo = store.getStoreLogo();
        this.storeLogoUrl = store.getStoreLogoUrl();
        this.storeAvatar = store.getStoreAvatarUrl();
        this.storeZy = store.getStoreZy();
        this.isOwnShop = store.getIsOwnShop();
        this.storeWorkingtime = store.getStoreWorkingtime();
        this.companyName = store.getCompanyName();
        this.companyArea = store.getCompanyArea();
        this.companyAreaId = store.getCompanyAreaId();
        this.storeFigureImage = store.getStoreFigureImage();
        this.chainAreaInfo = chain.getAreaInfo();
        this.chainAddress = chain.getAddress();
        this.chainPhone = chain.getChainPhone();
        this.chainOpeningTime = chain.getChainOpeningTime();
        this.storePresales = store.getStorePresales();
        this.storeAftersales = store.getStoreAftersales();
        this.state = store.getState();
        this.videoUrl = store.getVideoUrl();
        this.collectCount = store.getStoreCollect();
        this.storeSignature = store.getStoreSignature();
        this.storeIntroduce = store.getStoreIntroduce();
        this.lng = chain.getLng();
        this.lngString = chain.getLngString();
        this.lat = chain.getLat();
        this.latString = chain.getLatString();
        this.storeFigureImageInner = store.getStoreFigureImageInner();
        this.storeFigureImageOuter = store.getStoreFigureImageOuter();
        this.sellerName = store.getSellerName();
        this.isOpen = store.getIsOpen();
        this.contactsPhone = chain.getContactsPhone();
        this.weekDayStart = store.getWeekDayStart();
        this.weekDayEnd = store.getWeekDayEnd();
        this.restDayStart = store.getRestDayStart();
        this.restDayEnd = store.getRestDayEnd();
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreAvatar() {
        return storeAvatar;
    }

    public void setStoreAvatar(String storeAvatar) {
        this.storeAvatar = storeAvatar;
    }

    public String getStoreZy() {
        return storeZy;
    }

    public void setStoreZy(String storeZy) {
        this.storeZy = storeZy;
    }

    public int getIsOwnShop() {
        return isOwnShop;
    }

    public void setIsOwnShop(int isOwnShop) {
        this.isOwnShop = isOwnShop;
    }

    public String getStoreWorkingtime() {
        return storeWorkingtime;
    }

    public void setStoreWorkingtime(String storeWorkingtime) {
        this.storeWorkingtime = storeWorkingtime;
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

    public String getStoreFigureImage() {
        return storeFigureImage;
    }

    public void setStoreFigureImage(String storeFigureImage) {
        this.storeFigureImage = storeFigureImage;
    }

    public String getChainAreaInfo() {
        return chainAreaInfo;
    }

    public void setChainAreaInfo(String chainAreaInfo) {
        this.chainAreaInfo = chainAreaInfo;
    }

    public String getChainAddress() {
        return chainAddress;
    }

    public void setChainAddress(String chainAddress) {
        this.chainAddress = chainAddress;
    }

    public String getChainPhone() {
        return chainPhone;
    }

    public void setChainPhone(String chainPhone) {
        this.chainPhone = chainPhone;
    }

    public String getChainOpeningTime() {
        return chainOpeningTime;
    }

    public void setChainOpeningTime(String chainOpeningTime) {
        this.chainOpeningTime = chainOpeningTime;
    }

    public Integer getAllVisitCount() {
        return allVisitCount;
    }

    public void setAllVisitCount(Integer allVisitCount) {
        this.allVisitCount = allVisitCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Deprecated
    public List<ServiceVo> getStorePresalesList() {
        return storePresalesList;
    }

    @Deprecated
    public void setStorePresalesList(List<ServiceVo> storePresalesList) {
        this.storePresalesList = storePresalesList;
    }

    public String getShopDay() {
        return shopDay;
    }

    public void setShopDay(String shopDay) {
        this.shopDay = shopDay;
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

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getLngString() {
        return lngString;
    }

    public void setLngString(String lngString) {
        this.lngString = lngString;
    }

    public String getLatString() {
        return latString;
    }

    public void setLatString(String latString) {
        this.latString = latString;
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

    public int getStoreReply() {
        return storeReply;
    }

    public void setStoreReply(int storeReply) {
        this.storeReply = storeReply;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
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

    public String getStoreLogoUrl() {
        return storeLogoUrl;
    }

    public void setStoreLogoUrl(String storeLogoUrl) {
        this.storeLogoUrl = storeLogoUrl;
    }

    public long getGoodsCommonCount() {
        return goodsCommonCount;
    }

    public void setGoodsCommonCount(long goodsCommonCount) {
        this.goodsCommonCount = goodsCommonCount;
    }

    @Override
    public String toString() {
        return "StoreVo{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", className='" + className + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", storeLogo='" + storeLogo + '\'' +
                ", storeAvatar='" + storeAvatar + '\'' +
                ", storeZy='" + storeZy + '\'' +
                ", isOwnShop=" + isOwnShop +
                ", state=" + state +
                ", storeWorkingtime='" + storeWorkingtime + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyArea='" + companyArea + '\'' +
                ", companyAreaId=" + companyAreaId +
                ", storeFigureImage='" + storeFigureImage + '\'' +
                ", storePresales='" + storePresales + '\'' +
                ", storeAftersales='" + storeAftersales + '\'' +
                ", chainAreaInfo='" + chainAreaInfo + '\'' +
                ", chainAddress='" + chainAddress + '\'' +
                ", chainPhone='" + chainPhone + '\'' +
                ", chainOpeningTime='" + chainOpeningTime + '\'' +
                ", allVisitCount=" + allVisitCount +
                ", collectCount=" + collectCount +
                ", likeCount=" + likeCount +
                ", isOpen=" + isOpen +
                ", videoUrl='" + videoUrl + '\'' +
                ", storePresalesList=" + storePresalesList +
                ", shopDay='" + shopDay + '\'' +
                ", storeSignature='" + storeSignature + '\'' +
                ", storeIntroduce='" + storeIntroduce + '\'' +
                ", isFavorite=" + isFavorite +
                ", isLike=" + isLike +
                ", lng=" + lng +
                ", lngString='" + lngString + '\'' +
                ", lat=" + lat +
                ", latString='" + latString + '\'' +
                ", storeFigureImageInner='" + storeFigureImageInner + '\'' +
                ", storeFigureImageOuter='" + storeFigureImageOuter + '\'' +
                ", distance='" + distance + '\'' +
                ", storeServiceStaffVoList=" + storeServiceStaffVoList +
                ", storeReply=" + storeReply +
                ", sellerName='" + sellerName + '\'' +
                ", contactsPhone='" + contactsPhone + '\'' +
                ", weekDayStart='" + weekDayStart + '\'' +
                ", weekDayEnd='" + weekDayEnd + '\'' +
                ", restDayStart='" + restDayStart + '\'' +
                ", restDayEnd='" + restDayEnd + '\'' +
                ", storeLogoUrl='" + storeLogoUrl + '\'' +
                '}';
    }
}

package com.ftofs.twant.vo.promotion;

import com.ftofs.twant.domain.promotion.VoucherTemplate;
import com.ftofs.twant.domain.promotion.platform.coupon.CouponActivity;
import com.ftofs.twant.domain.store.Store;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 搜索的优惠券活动实体（平台券或者店铺券）
 *
 * @author zxy
 * Created 2017/9/26 16:36
 */
public class SearchCouponActivityVo {
    //以下为平台券活动基本设置
    /**
     * 活动自增ID
     */
    private int activityId;
    /**
     * 活动名称
     */
    private String activityName = "";
    /**
     * 活动说明
     */
    private String activityExplain = "";
    /**
     * 类型 CouponActivityType
     */
    private int activityType = 0;
    /**
     * 券面额
     */
    private BigDecimal couponPrice = new BigDecimal(0);

    //以下为平台券领取规则
    /**
     * 可领取券总数（卡密领取类型券最多限制1000个）
     */
    private int totalNum = 0;
    /**
     * 领取限制的会员等级
     */
    private int limitMemberGradeLevel = 0;
    /**
     * 领取限制的会员等级名称
     */
    private String limitMemberGradeName = "";
    /**
     * 券领取所需积分（积分领取类型）
     */
    private int expendPoints = 0;
    /**
     * 活动开始时间（即开始领取券时间，为空表示立即开始）
     */

    private String activityStartTime = null;

    //以下为平台券使用规则
    /**
     * 消费限额（为0表示不限额使用）
     */
    private BigDecimal limitAmount = new BigDecimal(0);
    /**
     * 券使用有效期开始时间
     */

    private String useStartTime = null;
    /**
     * 券使用有效期结束时间
     */

    private String useEndTime = null;
    /**
     * 从领取时间开始券有效天数，券分为设置有效开始时间和结束时间或者设置有效天数两种（活动赠送类型）
     */
    private int validDays = 0;
    /**
     * 券限制适用品类的范围类型 CouponUseGoodsRange
     */
    private int useGoodsRange = 0;
    /**
     * 券限制适用品类的范围说明
     */
    private String useGoodsRangeExplain = "";
    /**
     * PC端是否可用
     */
    private int webUsable = 0;
    /**
     * APP端是否可用（包括wap、ios、android）
     */
    private int appUsable = 0;
    /**
     * 微信端是否可用
     */
    private int wechatUsable = 0;

    //以下为平台券结算规则
    /**
     * 店铺承担比例（结算时店铺对优惠金额的承担比例）
     */
    private double storeCommitmentRate = 0;

    //以下为平台券附加信息
    /**
     * 类型为卡密兑换是否已经生成下属卡密 0未生成 1已生成
     */
    private int haveCreated = 0;
    /**
     * 活动创建时间
     */

    private String addTime = null;
    /**
     * 活动最后更新时间
     */

    private String updateTime = null;
    /**
     * 最后更新的管理员编号
     */
    private int adminId = 0;
    /**
     * 最后更新的管理员名称
     */
    private String adminName = "";
    /**
     * 活动状态 CouponActivityState
     */
    private int activityState = 0;
    /**
     * 活动已发放的券数量
     */
    private int giveoutNum = 0;
    /**
     * 活动已经使用过的券数量
     */
    private int usedNum = 0;

    //非数据库属性
    /**
     * 活动类型文本
     */
    private String activityTypeText = "";
    /**
     * 客户端类型标识
     */
    private String usableClientType= "";
    /**
     * 客户端类型文本
     */
    private String usableClientTypeText= "";
    /**
     * 活动当前状态标识
     */
    private String activityCurrentStateSign = "";
    /**
     * 活动当前状态文本
     */
    private String activityCurrentStateText = "";
    /**
     * 过期状态 0未过期，1已过期
     */
    private int expiredState = 0;
    /**
     * 完成状态 0未完成，1已完成
     */
    private int completeState = 0;
    /**
     * 有效期开始时间
     */
    private String useStartTimeText = "";
    /**
     * 有效期结束时间
     */
    private String useEndTimeText = "";
    /**
     * 券限制适用品类的范围类型文本
     */
    private String useGoodsRangeText = "";
    /**
     * 消费限额文本
     */
    private String limitAmountText = "";
    /**
     * 推荐状态 0不推荐 1推荐（将在领券中心显示）
     */
    private int recommendState = 0;

    //店铺券特定属性
    /**
     * 店铺编号
     */
    private int storeId = 0;
    /**
     * 店铺名称
     */
    private String storeName = "";

    //搜索引擎特定属性
    /**
     * 搜索引擎中的唯一标识（格式为searchDataSource_activityId）
     */
    private String searchSn = "";
    /**
     * 搜索引擎中优惠券适用品类的范围类型（SearchEngineCouponUseGoodsRange由于平台券和店铺券适用品类范围字段不同，所以搜索引擎新增特定字段）
     */
    private int searchUseGoodsRange = 0;
    /**
     * 搜索引擎中适用品类范围一级商品分类ID
     */
    private List<Integer> searchCategoryId1 = new ArrayList<>();
    /**
     * 搜索引擎中适用品类范围末级商品分类ID（仅平台券时使用）
     */
    private List<Integer> searchCategoryId = new ArrayList<>();
    /**
     * 搜索引擎中使用部分商品范围commonId（仅平台券时使用）
     */
    private List<Integer> searchCommonId = new ArrayList<>();
    /**
     * 搜索引擎中适用商品详细信息Json（目前仅存储商品图片名称）
     */
    private String searchGoodsCommonShort = "";
    /**
     * 搜索引擎中优惠券活动数据来源 值为coupon(平台券)、voucher(店铺券)
     */
    private String searchDataSource = "";
    /**
     * 搜索引擎中的优惠券领取进度百分比
     */
    private int searchGiveoutRate = 0;
    /**
     * 搜索引擎中的优惠券领取进度百分比文字
     */
    private String searchGiveoutRateText = "";

    //不需要向搜索引擎推送以下字段，仅便于程序处理使用
    /**
     * 搜索引擎中适用商品详细信息列表（目前仅存储商品图片名称）
     */
    private List<HashMap<String,Object>> searchGoodsCommonShortList = new ArrayList<>();
    /**
     * 距离活动开始时间的天数
     */
    private long activityStartTimeDays = 0L;

    public SearchCouponActivityVo() {}

    /**
     * 平台券
     * @param couponActivity
     */
    public SearchCouponActivityVo(CouponActivity couponActivity, List<Integer> categoryId1List, List<Integer> categoryIdList, List<Integer> commonIdList, String goodsCommonShortJson) {
        this.activityId = couponActivity.getActivityId();
        this.activityName = couponActivity.getActivityName();
        this.activityExplain = couponActivity.getActivityExplain();
        this.activityType = couponActivity.getActivityType();
        this.couponPrice = couponActivity.getCouponPrice();
        this.totalNum = couponActivity.getTotalNum();
        this.limitMemberGradeLevel = couponActivity.getLimitMemberGradeLevel();
        this.limitMemberGradeName = couponActivity.getLimitMemberGradeName();
        this.expendPoints = couponActivity.getExpendPoints();
        this.activityStartTime = couponActivity.getActivityStartTime();
        this.limitAmount = couponActivity.getLimitAmount();
        this.useStartTime = couponActivity.getUseStartTime();
        this.useEndTime = couponActivity.getUseEndTime();
        this.validDays = couponActivity.getValidDays();
        this.useGoodsRange = couponActivity.getUseGoodsRange();
        this.useGoodsRangeExplain = couponActivity.getUseGoodsRangeExplain();
        this.webUsable = couponActivity.getWebUsable();
        this.appUsable = couponActivity.getAppUsable();
        this.wechatUsable = couponActivity.getWechatUsable();
        this.storeCommitmentRate = couponActivity.getStoreCommitmentRate();
        this.haveCreated = couponActivity.getHaveCreated();
        this.addTime = couponActivity.getAddTime();
        this.updateTime = couponActivity.getUpdateTime();
        this.adminId = couponActivity.getAdminId();
        this.adminName = couponActivity.getAdminName();
        this.activityState = couponActivity.getActivityState();
        this.giveoutNum = couponActivity.getGiveoutNum();
        this.usedNum = couponActivity.getUsedNum();
        this.activityTypeText = couponActivity.getActivityTypeText();
        this.usableClientType= couponActivity.getUsableClientType();
        this.usableClientTypeText= couponActivity.getUsableClientTypeText();
        this.completeState = couponActivity.getCompleteState();
        this.useStartTimeText = couponActivity.getUseStartTimeText();
        this.useEndTimeText = couponActivity.getUseEndTimeText();
        this.useGoodsRangeText = couponActivity.getUseGoodsRangeText();
        this.limitAmountText = couponActivity.getLimitAmountText();
        this.recommendState = couponActivity.getRecommendState();
        this.storeId = 0;
        this.storeName = "";
        this.searchCategoryId1 = categoryId1List;
        this.searchCategoryId = categoryIdList;
        this.searchCommonId = commonIdList;
        this.searchGoodsCommonShort = goodsCommonShortJson;
        this.searchGiveoutRate = (int)(((float)couponActivity.getGiveoutNum()/(float)couponActivity.getTotalNum())*100);
        this.searchGiveoutRateText = this.searchGiveoutRate>=100 ? "已抢100%" : "已抢"+this.searchGiveoutRate+"%";
    }

    /**
     * 店铺券
     * @param voucherTemplate
     */
    public SearchCouponActivityVo(VoucherTemplate voucherTemplate, List<Integer> categoryId1List, String goodsCommonShortJson, Store store) {
        this.activityId = voucherTemplate.getTemplateId();
        this.activityName = voucherTemplate.getTemplateTitle();
        this.activityExplain = voucherTemplate.getTemplateDescribe();
        this.activityType = voucherTemplate.getTemplateType();
        this.couponPrice = voucherTemplate.getTemplatePrice();
        this.totalNum = voucherTemplate.getTotalNum();
        this.limitMemberGradeLevel = voucherTemplate.getLimitMemberGradeLevel();
        this.limitMemberGradeName = voucherTemplate.getLimitMemberGradeName();
        this.expendPoints = 0;
        this.activityStartTime = voucherTemplate.getTemplateStartTime();
        this.limitAmount = voucherTemplate.getLimitAmount();
        this.useStartTime = voucherTemplate.getUseStartTime();
        this.useEndTime = voucherTemplate.getUseEndTime();
        this.validDays = voucherTemplate.getValidDays();
        this.useGoodsRange = 0;
        this.useGoodsRangeExplain = "仅"+store.getStoreName()+"適用";
        this.webUsable = voucherTemplate.getWebUsable();
        this.appUsable = voucherTemplate.getAppUsable();
        this.wechatUsable = voucherTemplate.getWechatUsable();
        this.storeCommitmentRate = 0;
        this.haveCreated = voucherTemplate.getHaveCreated();
        this.addTime = voucherTemplate.getAddTime();
        this.updateTime = voucherTemplate.getUpdateTime();
        this.adminId = 0;
        this.adminName = "";
        this.activityState = voucherTemplate.getTemplateState();
        this.giveoutNum = voucherTemplate.getGiveoutNum();
        this.usedNum = voucherTemplate.getUsedNum();
        this.activityTypeText = voucherTemplate.getTemplateTypeText();
        this.usableClientType= voucherTemplate.getUsableClientType();
        this.usableClientTypeText= voucherTemplate.getUsableClientTypeText();
        this.completeState = voucherTemplate.getCompleteState();
        this.useStartTimeText = voucherTemplate.getUseStartTimeText();
        this.useEndTimeText = voucherTemplate.getUseEndTimeText();
        this.useGoodsRangeText = "";
        this.limitAmountText = voucherTemplate.getLimitAmountText();
        this.recommendState = voucherTemplate.getVoucherCenterRecommend();
        this.storeId = voucherTemplate.getStoreId();
        this.storeName = voucherTemplate.getStoreName();
        this.searchCategoryId1 = categoryId1List;
        this.searchCategoryId = new ArrayList<>();
        this.searchCommonId = new ArrayList<>();
        this.searchGoodsCommonShort = goodsCommonShortJson;
        this.searchGiveoutRate = (int)(((float)voucherTemplate.getGiveoutNum()/(float)voucherTemplate.getTotalNum())*100);
        this.searchGiveoutRateText = this.searchGiveoutRate>=100 ? "已抢100%" : "已抢"+this.searchGiveoutRate+"%";
    }


    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityExplain() {
        return activityExplain;
    }

    public void setActivityExplain(String activityExplain) {
        this.activityExplain = activityExplain;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getLimitMemberGradeLevel() {
        return limitMemberGradeLevel;
    }

    public void setLimitMemberGradeLevel(int limitMemberGradeLevel) {
        this.limitMemberGradeLevel = limitMemberGradeLevel;
    }

    public String getLimitMemberGradeName() {
        return limitMemberGradeName;
    }

    public void setLimitMemberGradeName(String limitMemberGradeName) {
        this.limitMemberGradeName = limitMemberGradeName;
    }

    public int getExpendPoints() {
        return expendPoints;
    }

    public void setExpendPoints(int expendPoints) {
        this.expendPoints = expendPoints;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public String getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(String useStartTime) {
        this.useStartTime = useStartTime;
    }

    public String getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }

    public int getValidDays() {
        return validDays;
    }

    public void setValidDays(int validDays) {
        this.validDays = validDays;
    }

    public int getUseGoodsRange() {
        return useGoodsRange;
    }

    public void setUseGoodsRange(int useGoodsRange) {
        this.useGoodsRange = useGoodsRange;
    }

    public String getUseGoodsRangeExplain() {
        return useGoodsRangeExplain;
    }

    public void setUseGoodsRangeExplain(String useGoodsRangeExplain) {
        this.useGoodsRangeExplain = useGoodsRangeExplain;
    }

    public int getWebUsable() {
        return webUsable;
    }

    public void setWebUsable(int webUsable) {
        this.webUsable = webUsable;
    }

    public int getAppUsable() {
        return appUsable;
    }

    public void setAppUsable(int appUsable) {
        this.appUsable = appUsable;
    }

    public int getWechatUsable() {
        return wechatUsable;
    }

    public void setWechatUsable(int wechatUsable) {
        this.wechatUsable = wechatUsable;
    }

    public double getStoreCommitmentRate() {
        return storeCommitmentRate;
    }

    public void setStoreCommitmentRate(double storeCommitmentRate) {
        this.storeCommitmentRate = storeCommitmentRate;
    }

    public int getHaveCreated() {
        return haveCreated;
    }

    public void setHaveCreated(int haveCreated) {
        this.haveCreated = haveCreated;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getActivityState() {
        return activityState;
    }

    public void setActivityState(int activityState) {
        this.activityState = activityState;
    }

    public int getGiveoutNum() {
        return giveoutNum;
    }

    public void setGiveoutNum(int giveoutNum) {
        this.giveoutNum = giveoutNum;
    }

    public int getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
    }

    public String getActivityTypeText() {
        return activityTypeText;
    }

    public void setActivityTypeText(String activityTypeText) {
        this.activityTypeText = activityTypeText;
    }

    public String getUsableClientType() {
        return usableClientType;
    }

    public void setUsableClientType(String usableClientType) {
        this.usableClientType = usableClientType;
    }

    public String getUsableClientTypeText() {
        return usableClientTypeText;
    }

    public void setUsableClientTypeText(String usableClientTypeText) {
        this.usableClientTypeText = usableClientTypeText;
    }

    public String getActivityCurrentStateSign() {
        return activityCurrentStateSign;
    }

    public void setActivityCurrentStateSign(String activityCurrentStateSign) {
        this.activityCurrentStateSign = activityCurrentStateSign;
    }

    public String getActivityCurrentStateText() {
        return activityCurrentStateText;
    }

    public void setActivityCurrentStateText(String activityCurrentStateText) {
        this.activityCurrentStateText = activityCurrentStateText;
    }

    public int getExpiredState() {
        return expiredState;
    }

    public void setExpiredState(int expiredState) {
        this.expiredState = expiredState;
    }

    public int getCompleteState() {
        return completeState;
    }

    public void setCompleteState(int completeState) {
        this.completeState = completeState;
    }

    public String getUseStartTimeText() {
        return useStartTimeText;
    }

    public void setUseStartTimeText(String useStartTimeText) {
        this.useStartTimeText = useStartTimeText;
    }

    public String getUseEndTimeText() {
        return useEndTimeText;
    }

    public void setUseEndTimeText(String useEndTimeText) {
        this.useEndTimeText = useEndTimeText;
    }

    public String getUseGoodsRangeText() {
        return useGoodsRangeText;
    }

    public void setUseGoodsRangeText(String useGoodsRangeText) {
        this.useGoodsRangeText = useGoodsRangeText;
    }

    public String getLimitAmountText() {
        return limitAmountText;
    }

    public void setLimitAmountText(String limitAmountText) {
        this.limitAmountText = limitAmountText;
    }

    public int getRecommendState() {
        return recommendState;
    }

    public void setRecommendState(int recommendState) {
        this.recommendState = recommendState;
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

    public String getSearchSn() {
        return searchSn;
    }

    public void setSearchSn(String searchSn) {
        this.searchSn = searchSn;
    }

    public int getSearchUseGoodsRange() {
        return searchUseGoodsRange;
    }

    public void setSearchUseGoodsRange(int searchUseGoodsRange) {
        this.searchUseGoodsRange = searchUseGoodsRange;
    }

    public List<Integer> getSearchCategoryId1() {
        return searchCategoryId1;
    }

    public void setSearchCategoryId1(List<Integer> searchCategoryId1) {
        this.searchCategoryId1= searchCategoryId1;
    }

    public List<Integer> getSearchCategoryId() {
        return searchCategoryId;
    }

    public void setSearchCategoryId(List<Integer> searchCategoryId) {
        this.searchCategoryId = searchCategoryId;
    }

    public List<Integer> getSearchCommonId() {
        return searchCommonId;
    }

    public void setSearchCommonId(List<Integer> searchCommonId) {
        this.searchCommonId = searchCommonId;
    }

    public String getSearchGoodsCommonShort() {
        return searchGoodsCommonShort;
    }

    public void setSearchGoodsCommonShort(String searchGoodsCommonShort) {
        this.searchGoodsCommonShort = searchGoodsCommonShort;
    }

    public String getSearchDataSource() {
        return searchDataSource;
    }

    public void setSearchDataSource(String searchDataSource) {
        this.searchDataSource = searchDataSource;
    }

    public int getSearchGiveoutRate() {
        return searchGiveoutRate;
    }

    public void setSearchGiveoutRate(int searchGiveoutRate) {
        this.searchGiveoutRate = searchGiveoutRate;
    }

    public String getSearchGiveoutRateText() {
        return searchGiveoutRateText;
    }

    public void setSearchGiveoutRateText(String searchGiveoutRateText) {
        this.searchGiveoutRateText = searchGiveoutRateText;
    }

    public List<HashMap<String, Object>> getSearchGoodsCommonShortList() {
        return searchGoodsCommonShortList;
    }

    public void setSearchGoodsCommonShortList(List<HashMap<String, Object>> searchGoodsCommonShortList) {
        this.searchGoodsCommonShortList = searchGoodsCommonShortList;
    }

    public long getActivityStartTimeDays() {
        return activityStartTimeDays;
    }

    public void setActivityStartTimeDays(long activityStartTimeDays) {
        this.activityStartTimeDays = activityStartTimeDays;
    }

    @Override
    public String toString() {
        return "SearchCouponActivityVo{" +
                "activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", activityExplain='" + activityExplain + '\'' +
                ", activityType=" + activityType +
                ", couponPrice=" + couponPrice +
                ", totalNum=" + totalNum +
                ", limitMemberGradeLevel=" + limitMemberGradeLevel +
                ", limitMemberGradeName='" + limitMemberGradeName + '\'' +
                ", expendPoints=" + expendPoints +
                ", activityStartTime=" + activityStartTime +
                ", limitAmount=" + limitAmount +
                ", useStartTime=" + useStartTime +
                ", useEndTime=" + useEndTime +
                ", validDays=" + validDays +
                ", useGoodsRange=" + useGoodsRange +
                ", useGoodsRangeExplain='" + useGoodsRangeExplain + '\'' +
                ", webUsable=" + webUsable +
                ", appUsable=" + appUsable +
                ", wechatUsable=" + wechatUsable +
                ", storeCommitmentRate=" + storeCommitmentRate +
                ", haveCreated=" + haveCreated +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", activityState=" + activityState +
                ", giveoutNum=" + giveoutNum +
                ", usedNum=" + usedNum +
                ", activityTypeText='" + activityTypeText + '\'' +
                ", usableClientType='" + usableClientType + '\'' +
                ", usableClientTypeText='" + usableClientTypeText + '\'' +
                ", activityCurrentStateSign='" + activityCurrentStateSign + '\'' +
                ", activityCurrentStateText='" + activityCurrentStateText + '\'' +
                ", expiredState=" + expiredState +
                ", completeState=" + completeState +
                ", useStartTimeText='" + useStartTimeText + '\'' +
                ", useEndTimeText='" + useEndTimeText + '\'' +
                ", useGoodsRangeText='" + useGoodsRangeText + '\'' +
                ", limitAmountText='" + limitAmountText + '\'' +
                ", recommendState=" + recommendState +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", searchSn='" + searchSn + '\'' +
                ", searchUseGoodsRange=" + searchUseGoodsRange +
                ", searchCategoryId1=" + searchCategoryId1 +
                ", searchCategoryId=" + searchCategoryId +
                ", searchCommonId=" + searchCommonId +
                ", searchGoodsCommonShort='" + searchGoodsCommonShort + '\'' +
                ", searchDataSource='" + searchDataSource + '\'' +
                ", searchGiveoutRate=" + searchGiveoutRate +
                ", searchGiveoutRateText='" + searchGiveoutRateText + '\'' +
                ", searchGoodsCommonShortList=" + searchGoodsCommonShortList +
                ", activityStartTimeDays=" + activityStartTimeDays +
                '}';
    }
}

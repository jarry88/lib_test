package com.ftofs.twant.vo.seckill;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.seckill.SeckillGoodsCommon;
import com.ftofs.twant.domain.seckill.SeckillSchedule;
import com.ftofs.twant.domain.store.Store;

import java.math.BigDecimal;


/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 秒杀商品
 *
 * @author shopnc.feng
 * Created on 2017/9/15 17:52
 */
public class SeckillGoodsCommonVo {
    /**
     * 主键
     */
    private int seckillCommonId;
    /**
     * 主键
     */
    private int seckillGoodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 图片路径
     */
    private String imageSrc;
    /**
     * 商品SKU编号
     */
    private int goodsId;
    /**
     * 商品SPU
     */
    private int commonId;
    /**
     * 商品价格
     */
    private BigDecimal goodsPrice = BigDecimal.ZERO;
    /**
     * 商品价格
     */
    private BigDecimal seckillGoodsPrice = BigDecimal.ZERO;
    /**
     * 库存
     */
    private int goodsStorage;
    /**
     * 限购数量
     */
    private int limitAmount;
    /**
     * 秒杀排期编号
     */
    private int scheduleId;
    /**
     * 开始时间
     */

    private String startTime = "";
    /**
     * 结束时间
     */

    private String endTime = "";
    /**
     * 状态文字
     */
    private int scheduleState = 1;
    /**
     * 审核失败原因
     */
    private String verifyRemark;
    /**
     * 状态文字
     */
    private String scheduleStateText;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 排期名称
     */
    private String scheduleName;
    /**
     * 销售数量
     */
    private int goodsSaleNum = 0;
    /**
     * 销售百分百（%）
     */
    private int salesPercentage = 0;
    /**
     * 是否设置提醒
     */
    private int isNotice = 0;

    public SeckillGoodsCommonVo( SeckillGoodsCommon seckillGoodsCommon, GoodsCommon goodsCommon, SeckillSchedule seckillSchedule) {
        this.goodsName = goodsCommon.getGoodsName();
        this.imageSrc = goodsCommon.getImageSrc();
        this.commonId = seckillGoodsCommon.getCommonId();
        this.seckillGoodsPrice = seckillGoodsCommon.getGoodsPrice();
        this.goodsStorage = seckillGoodsCommon.getGoodsStorage();
        this.scheduleId = seckillGoodsCommon.getScheduleId();
        this.startTime = seckillGoodsCommon.getStartTime();
        this.endTime = seckillGoodsCommon.getEndTime();
        this.scheduleState = seckillGoodsCommon.getScheduleState();
        this.verifyRemark = seckillGoodsCommon.getVerifyRemark();
        this.scheduleStateText = seckillGoodsCommon.getScheduleStateText();
        this.scheduleName = seckillSchedule.getScheduleName();
    }

    /**
     * 后台秒杀商品列表使用
     * @param goodsCommon
     * @param seckillGoodsCommon
     * @param store
     */
    public SeckillGoodsCommonVo(SeckillGoodsCommon seckillGoodsCommon, GoodsCommon goodsCommon, Store store) {
        this.seckillCommonId = seckillGoodsCommon.getSeckillCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.imageSrc = goodsCommon.getImageSrc();
        this.commonId = seckillGoodsCommon.getCommonId();
        this.seckillGoodsPrice = seckillGoodsCommon.getGoodsPrice();
        this.goodsStorage = seckillGoodsCommon.getGoodsStorage();
        this.scheduleId = seckillGoodsCommon.getScheduleId();
        this.startTime = seckillGoodsCommon.getStartTime();
        this.endTime = seckillGoodsCommon.getEndTime();
        this.scheduleState = seckillGoodsCommon.getScheduleState();
        this.verifyRemark = seckillGoodsCommon.getVerifyRemark();
        this.scheduleStateText = seckillGoodsCommon.getScheduleStateText();
        this.storeName = store.getStoreName();
    }
    /**
     * 后台秒杀审核商品列表使用
     * @param goodsCommon
     * @param seckillGoodsCommon
     * @param store
     */
    public SeckillGoodsCommonVo(SeckillGoodsCommon seckillGoodsCommon, GoodsCommon goodsCommon, Store store, SeckillSchedule seckillSchedule) {
        this.seckillCommonId = seckillGoodsCommon.getSeckillCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.imageSrc = goodsCommon.getImageSrc();
        this.commonId = seckillGoodsCommon.getCommonId();
        this.seckillGoodsPrice = seckillGoodsCommon.getGoodsPrice();
        this.goodsStorage = seckillGoodsCommon.getGoodsStorage();
        this.scheduleId = seckillGoodsCommon.getScheduleId();
        this.startTime = seckillGoodsCommon.getStartTime();
        this.endTime = seckillGoodsCommon.getEndTime();
        this.scheduleState = seckillGoodsCommon.getScheduleState();
        this.verifyRemark = seckillGoodsCommon.getVerifyRemark();
        this.scheduleStateText = seckillGoodsCommon.getScheduleStateText();
        this.storeName = store.getStoreName();
        this.scheduleName = seckillSchedule.getScheduleName();
    }

    public SeckillGoodsCommonVo( SeckillGoodsCommon seckillGoodsCommon, GoodsCommon goodsCommon) {
        this.seckillCommonId = seckillGoodsCommon.getSeckillCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.imageSrc = goodsCommon.getImageSrc();
        this.commonId = goodsCommon.getCommonId();
        this.goodsPrice = goodsCommon.getBatchPrice2();
        this.seckillGoodsPrice = seckillGoodsCommon.getGoodsPrice();
        this.goodsStorage = seckillGoodsCommon.getGoodsStorage();
        this.scheduleId = seckillGoodsCommon.getScheduleId();
        this.startTime = seckillGoodsCommon.getStartTime();
        this.endTime = seckillGoodsCommon.getEndTime();
        this.scheduleState = seckillGoodsCommon.getScheduleState();
        this.verifyRemark = seckillGoodsCommon.getVerifyRemark();
        this.scheduleStateText = seckillGoodsCommon.getScheduleStateText();
        this.goodsSaleNum = seckillGoodsCommon.getGoodsSaleNum();
        this.salesPercentage = seckillGoodsCommon.getGoodsSaleNum()*100/(seckillGoodsCommon.getGoodsSaleNum() + seckillGoodsCommon.getGoodsStorage());
    }

    public int getSeckillCommonId() {
        return seckillCommonId;
    }

    public void setSeckillCommonId(int seckillCommonId) {
        this.seckillCommonId = seckillCommonId;
    }

    public int getSeckillGoodsId() {
        return seckillGoodsId;
    }

    public void setSeckillGoodsId(int seckillGoodsId) {
        this.seckillGoodsId = seckillGoodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
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

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public BigDecimal getSeckillGoodsPrice() {
        return seckillGoodsPrice;
    }

    public void setSeckillGoodsPrice(BigDecimal seckillGoodsPrice) {
        this.seckillGoodsPrice = seckillGoodsPrice;
    }

    public int getGoodsStorage() {
        return goodsStorage;
    }

    public void setGoodsStorage(int goodsStorage) {
        this.goodsStorage = goodsStorage;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getScheduleState() {
        return scheduleState;
    }

    public void setScheduleState(int scheduleState) {
        this.scheduleState = scheduleState;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public String getScheduleStateText() {
        return scheduleStateText;
    }

    public void setScheduleStateText(String scheduleStateText) {
        this.scheduleStateText = scheduleStateText;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public int getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(int goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public int getSalesPercentage() {
        return salesPercentage;
    }

    public void setSalesPercentage(int salesPercentage) {
        this.salesPercentage = salesPercentage;
    }

    public int getIsNotice() {
        return isNotice;
    }

    public void setIsNotice(int isNotice) {
        this.isNotice = isNotice;
    }

    @Override
    public String toString() {
        return "SeckillGoodsCommonVo{" +
                "seckillCommonId=" + seckillCommonId +
                ", seckillGoodsId=" + seckillGoodsId +
                ", goodsName='" + goodsName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsPrice=" + goodsPrice +
                ", seckillGoodsPrice=" + seckillGoodsPrice +
                ", goodsStorage=" + goodsStorage +
                ", limitAmount=" + limitAmount +
                ", scheduleId=" + scheduleId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", scheduleState=" + scheduleState +
                ", verifyRemark='" + verifyRemark + '\'' +
                ", scheduleStateText='" + scheduleStateText + '\'' +
                ", storeName='" + storeName + '\'' +
                ", scheduleName='" + scheduleName + '\'' +
                ", goodsSaleNum=" + goodsSaleNum +
                ", salesPercentage=" + salesPercentage +
                ", isNotice=" + isNotice +
                '}';
    }
}

package com.ftofs.twant.domain.promotion;

import java.io.Serializable;

public class Discount implements Serializable {
	/**
	 * 折扣促销编号
	 */
	private int discountId=0;

	/**
	 * 店铺编号
	 */
	private int storeId=0;

	/**
	 * 店铺名称
	 * 暂时给后台列表使用
	 */
	private String storeName="";

	/**
	 * 折扣名称
	 */
	private String discountName="";

	/**
	 * 折扣
	 */
	private double discountRate=0;

	/**
	 * 折扣标题
	 */
	private String discountTitle="";

	/**
	 * 折扣标题，用于外部显示
	 */
	private String discountTitleFinal;

	/**
	 * 折扣说明
	 */
	private String discountExplain="";

	/**
	 * 开始时间
	 */
	private String startTime="";

	/**
	 * 结束时间
	 */
	private String endTime="";

	/**
	 * 活动状态
	 */
	private int discountState=0;

    /**
     * 限购数量
     */
    private int limitAmount;

	/**
	 * 活动状态文字
	 */
    private String discountStateText="";

	/**
	 * 促销倒计时(秒)
	 * 未满足条件为0
	 */
	private long promotionCountDownTime=0;

	/**
	 * 促销倒计时文字描述
	 */
	private String promotionCountDownTimeText="";

	/**
	 * 参数產品数量
	 */
	private long goodsCount=0;

	public int getDiscountId() {
		return discountId;
	}

	public void setDiscountId(int discountId) {
		this.discountId = discountId;
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

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	public double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountTitle() {
		return discountTitle;
	}

	public void setDiscountTitle(String discountTitle) {
		this.discountTitle = discountTitle;
	}

	public String getDiscountTitleFinal() {
		return discountTitle;
	}

    public void setDiscountTitleFinal(String discountTitleFinal) {
        this.discountTitleFinal = discountTitleFinal;
    }

    public String getDiscountExplain() {
		return discountExplain;
	}

	public void setDiscountExplain(String discountExplain) {
		this.discountExplain = discountExplain;
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

	public int getDiscountState() {
		return discountState;
	}

	public void setDiscountState(int discountState) {
		this.discountState = discountState;
	}

	public String getDiscountStateText() {
		return discountStateText;
	}

    public void setDiscountStateText(String discountStateText) {
        this.discountStateText = discountStateText;
    }

    public int getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(int limitAmount) {
        this.limitAmount = limitAmount;
    }

    public long getPromotionCountDownTime() {
		return 0;
	}

    public void setPromotionCountDownTime(long promotionCountDownTime) {
        this.promotionCountDownTime = promotionCountDownTime;
    }

    public String getPromotionCountDownTimeText() {
		return "";
	}

    public void setPromotionCountDownTimeText(String promotionCountDownTimeText) {
        this.promotionCountDownTimeText = promotionCountDownTimeText;
    }

    public long getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(long goodsCount) {
		this.goodsCount = goodsCount;
	}

    @Override
    public String toString() {
        return "Discount{" +
                "discountId=" + discountId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", discountName='" + discountName + '\'' +
                ", discountRate=" + discountRate +
                ", discountTitle='" + discountTitle + '\'' +
                ", discountTitleFinal='" + discountTitleFinal + '\'' +
                ", discountExplain='" + discountExplain + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", discountState=" + discountState +
                ", limitAmount=" + limitAmount +
                ", discountStateText='" + discountStateText + '\'' +
                ", promotionCountDownTime=" + promotionCountDownTime +
                ", promotionCountDownTimeText='" + promotionCountDownTimeText + '\'' +
                ", goodsCount=" + goodsCount +
                '}';
    }
}

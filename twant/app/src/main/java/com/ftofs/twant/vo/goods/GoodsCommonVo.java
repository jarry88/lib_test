package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.store.Store;
import com.ftofs.twant.vo.store.StoreVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;

public class GoodsCommonVo implements Serializable{
	private Integer commonId;
	
	private String goodsName;
	
	private String goodsImage;
	
	private BigDecimal goodsPrice0;//最低价
	
	private BigDecimal goodsPrice2;//最高价

	/**
	 * 商品产地
	 * Modify By yangjian 2019/1/17 18:04
	 */
	private Integer goodsCountry;
	/**
	 * 商品产地对象
	 * Modify By yangjian 2019/1/17 18:04
	 */
	private AdminCountry adminCountry;

    /**
     * 店鋪ID
     */
	private Integer storeId = 0;

    /**
     * 商品視頻
     */
	private String goodsVideo = "";

    /**
     * 发布时间
     */
	private Timestamp updateTime = new Timestamp(0);

    /**
     * 商品一級分類
     */
	private int categoryId1;

    /**
     * SKU庫存總和
     */
	private long totalStorage = 0;

    /**
     * 銷量
     */
	private BigInteger goodsSaleNum = new BigInteger("0");

    /**
     * 點贊數量
     */
	private long goodsLike;

    /**
     * 點贊狀態
     */
	private int isLike;

    /**
     * 店铺信息
     */
	private StoreVo storeVo;

	public GoodsCommonVo() {
		super();
	}

	public GoodsCommonVo(Integer commonId, String goodsName, String goodsImage, BigDecimal goodsPrice0,
			BigDecimal goodsPrice2, Integer goodsCountry) {
		super();
		this.commonId = commonId;
		this.goodsName = goodsName;
		this.goodsImage = goodsImage;
		this.goodsPrice0 = goodsPrice0;
		this.goodsPrice2 = goodsPrice2;
		this.goodsCountry = goodsCountry;
	}

	public GoodsCommonVo(GoodsCommon goodsCommon){
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsImage = goodsCommon.getImageName();
        this.storeId = goodsCommon.getStoreId();
        this.goodsVideo = goodsCommon.getGoodsVideo();
        this.updateTime = goodsCommon.getUpdateTime();
        this.categoryId1 = goodsCommon.getCategoryId1();
        this.goodsPrice0 = goodsCommon.getBatchPrice0();
        this.goodsLike = goodsCommon.getGoodsLike();
    }

    public GoodsCommonVo(GoodsCommon goodsCommon, Store store){
        this.commonId = goodsCommon.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.goodsImage = goodsCommon.getImageName();
        this.storeId = goodsCommon.getStoreId();
        this.goodsVideo = goodsCommon.getGoodsVideo();
        this.updateTime = goodsCommon.getUpdateTime();
        this.categoryId1 = goodsCommon.getCategoryId1();
        this.goodsPrice0 = goodsCommon.getBatchPrice0();
        this.goodsLike = goodsCommon.getGoodsLike();
        this.storeVo = new StoreVo(store);
    }

    public Integer getCommonId() {
        return commonId;
    }

    public void setCommonId(Integer commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public BigDecimal getGoodsPrice0() {
        return goodsPrice0;
    }

    public void setGoodsPrice0(BigDecimal goodsPrice0) {
        this.goodsPrice0 = goodsPrice0;
    }

    public BigDecimal getGoodsPrice2() {
        return goodsPrice2;
    }

    public void setGoodsPrice2(BigDecimal goodsPrice2) {
        this.goodsPrice2 = goodsPrice2;
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

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getGoodsVideo() {
        return goodsVideo;
    }

    public void setGoodsVideo(String goodsVideo) {
        this.goodsVideo = goodsVideo;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(int categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public long getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(long totalStorage) {
        this.totalStorage = totalStorage;
    }

    public BigInteger getGoodsSaleNum() {
        return goodsSaleNum;
    }

    public void setGoodsSaleNum(BigInteger goodsSaleNum) {
        this.goodsSaleNum = goodsSaleNum;
    }

    public long getGoodsLike() {
        return goodsLike;
    }

    public void setGoodsLike(long goodsLike) {
        this.goodsLike = goodsLike;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    @Override
	public String toString() {
		return "GoodsCommonVo{" +
				"commonId=" + commonId +
				", goodsName='" + goodsName + '\'' +
				", goodsImage='" + goodsImage + '\'' +
				", goodsPrice0=" + goodsPrice0 +
				", goodsPrice2=" + goodsPrice2 +
				", goodsCountry=" + goodsCountry +
				", adminCountry=" + adminCountry +
				'}';
	}
}
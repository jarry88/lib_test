package com.ftofs.twant.vo.goods;

import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.vo.store.StoreVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class GoodsCommonVo implements Serializable{
	private Integer commonId;
	
	private String goodsName;
	
	private String goodsImage;
	
	private BigDecimal goodsPrice0;//最低价
	
	private BigDecimal goodsPrice2;//最高价

	/**
	 * 產品产地
	 * Modify By yangjian 2019/1/17 18:04
	 */
	private Integer goodsCountry;
	/**
	 * 產品产地对象
	 * Modify By yangjian 2019/1/17 18:04
	 */
	private AdminCountry adminCountry;

    /**
     * 商店ID
     */
	private Integer storeId = 0;

    /**
     * 產品視頻
     */
	private String goodsVideo = "";

    /**
     * 发布时间
     */
	private String updateTime = "";

    /**
     * 產品一級分類
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
     * 讚想數量
     */
	private long goodsLike;

    /**
     * 讚想狀態
     */
	private int isLike;

    /**
     * 店铺信息
     */
	private StoreVo storeVo;

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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

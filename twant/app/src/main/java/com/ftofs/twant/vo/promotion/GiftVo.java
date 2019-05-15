package com.ftofs.twant.vo.promotion;

import com.ftofs.twant.domain.goods.Goods;
import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.promotion.Gift;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 赠品
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:14
 */
public class GiftVo {
    /**
     * 赠品编号
     */
    private int giftId;
    /**
     * 商品SKU
     */
    private int goodsId;
    /**
     * 商品SPU
     */
    private int commonId;
    /**
     * 赠品数量
     */
    private int giftNum;
    /**
     * 赠品类型，1满优惠赠品、2商品赠品
     */
    private int giftType;
    /**
     * 项目编号，如满优惠编号、主商品编号
     */
    private int itemId;
    /**
     * 项目编号，记录商品赠品的主商品SPU
     */
    private Integer itemCommonId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 计量单位
     */
    private String unitName;
    /**
     * 完整规格<br>
     * 例“颜色：红色；尺码：L”
     */
    private String goodsFullSpecs;
    /**
     * 图片路径
     */
    private String imageSrc="";

    public GiftVo() {
    }

    public GiftVo(Gift gift, Goods goods, GoodsCommon goodsCommon) {
        this.giftId = gift.getGiftId();
        this.goodsId = gift.getGoodsId();
        this.commonId = gift.getCommonId();
        this.giftNum = gift.getGiftNum();
        this.giftType = gift.getGiftType();
        this.itemId = gift.getItemId();
        this.itemCommonId = gift.getCommonId();
        this.goodsName = goodsCommon.getGoodsName();
        this.unitName = goodsCommon.getUnitName();
        this.goodsFullSpecs = goods.getGoodsFullSpecs();
        this.imageSrc = goods.getImageSrc();
    }
    
    

    public GiftVo(int giftId, int goodsId, int commonId, int giftNum, int giftType, int itemId, Integer itemCommonId,
			String goodsName, String unitName, String goodsFullSpecs, String imageSrc) {
		super();
		this.giftId = giftId;
		this.goodsId = goodsId;
		this.commonId = commonId;
		this.giftNum = giftNum;
		this.giftType = giftType;
		this.itemId = itemId;
		this.itemCommonId = itemCommonId;
		this.goodsName = goodsName;
		this.unitName = unitName;
		this.goodsFullSpecs = goodsFullSpecs;
		this.imageSrc = imageSrc;
	}

	public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
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

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public Integer getItemCommonId() {
        return itemCommonId;
    }

    public void setItemCommonId(Integer itemCommonId) {
        this.itemCommonId = itemCommonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "GiftVo{" +
                "giftId=" + giftId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", giftNum=" + giftNum +
                ", giftType=" + giftType +
                ", itemId=" + itemId +
                ", itemCommonId=" + itemCommonId +
                ", goodsName='" + goodsName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}

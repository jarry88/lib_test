package com.ftofs.twant.entity;

public class GiftVo {
    /**
     * 赠品编号
     */
    public int giftId;
    /**
     * 商品SKU
     */
    public int goodsId;
    /**
     * 商品SPU
     */
    public int commonId;
    /**
     * 赠品数量
     */
    public int giftNum;
    /**
     * 赠品类型，1满优惠赠品、2商品赠品
     */
    public int giftType;
    /**
     * 项目编号，如满优惠编号、主商品编号
     */
    public int itemId;
    /**
     * 项目编号，记录商品赠品的主商品SPU
     */
    public Integer itemCommonId;
    /**
     * 商品名称
     */
    public String goodsName;
    /**
     * 计量单位
     */
    public String unitName;
    /**
     * 完整规格<br>
     * 例“颜色：红色；尺码：L”
     */
    public String goodsFullSpecs;
    /**
     * 图片路径
     */
    public String imageSrc="";

    
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

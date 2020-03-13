package com.ftofs.twant.entity;

public class GiftVo {
    /**
     * 赠品编号
     */
    public int giftId;
    /**
     * 產品SKU
     */
    public int goodsId;
    /**
     * 產品SPU
     */
    public int commonId;
    /**
     * 赠品数量
     */
    public int giftNum;
    /**
     * 赠品类型，1满优惠赠品、2產品赠品
     */
    public int giftType;
    /**
     * 项目编号，如满优惠编号、主產品编号
     */
    public int itemId;
    /**
     * 项目编号，记录產品赠品的主產品SPU
     */
    public Integer itemCommonId;
    /**
     * 產品名称
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

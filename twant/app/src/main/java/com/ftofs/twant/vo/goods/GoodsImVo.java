package com.ftofs.twant.vo.goods;

/**
 * @Description: IM商品視圖對象
 * @Auther: yangjian
 * @Date: 2019/4/9 14:35
 */
public class GoodsImVo {
    /**
     * 商品名稱
     */
    private String goodsName;
    /**
     * 商品圖片
     */
    private String goodsImage;
    /**
     * 商品id
     */
    private int commonId = 0;

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

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    @Override
    public String toString() {
        return "GoodsImVo{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", commonId=" + commonId +
                '}';
    }
}

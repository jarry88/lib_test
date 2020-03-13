package com.ftofs.twant.domain.goods;

public class GoodsCommonBody {
    /**
     * 產品SPU编号
     */
    private int commonId;

    /**
     * 產品描述
     */
    private String goodsBody;

    /**
     * 移动端產品描述
     */
    private String mobileBody;

    /**
     * 顶部关联板式编号
     */
    private int formatTop;

    /**
     * 底部关联板式编号
     */
    private int formatBottom;

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsBody() {
        return goodsBody;
    }

    public void setGoodsBody(String goodsBody) {
        this.goodsBody = goodsBody;
    }

    public String getMobileBody() {
        return mobileBody;
    }

    public void setMobileBody(String mobileBody) {
        this.mobileBody = mobileBody;
    }

    public int getFormatTop() {
        return formatTop;
    }

    public void setFormatTop(int formatTop) {
        this.formatTop = formatTop;
    }

    public int getFormatBottom() {
        return formatBottom;
    }

    public void setFormatBottom(int formatBottom) {
        this.formatBottom = formatBottom;
    }

    @Override
    public String toString() {
        return "GoodsCommonBody{" +
                "commonId=" + commonId +
                ", goodsBody='" + goodsBody + '\'' +
                ", mobileBody='" + mobileBody + '\'' +
                ", formatTop=" + formatTop +
                ", formatBottom=" + formatBottom +
                '}';
    }
}

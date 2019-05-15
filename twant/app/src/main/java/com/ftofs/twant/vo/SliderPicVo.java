package com.ftofs.twant.vo;

import com.ftofs.twant.vo.goods.GoodsCommonVo;

import java.util.List;

/**
 * @author liusf
 * @create 2019/4/20 17:29
 * @description 首页焦点图
 */
public class SliderPicVo {
    private String image = "";
    private String linkType = "";
    private String linkValue = "";
    private String goodsIds = "";
    private List<GoodsCommonVo> goodsCommons;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    public String getGoodsIds() {
        return goodsIds;
    }

    public void setGoodsIds(String goodsIds) {
        this.goodsIds = goodsIds;
    }

    public List<GoodsCommonVo> getGoodsCommons() {
        return goodsCommons;
    }

    public void setGoodsCommons(List<GoodsCommonVo> goodsCommons) {
        this.goodsCommons = goodsCommons;
    }
}

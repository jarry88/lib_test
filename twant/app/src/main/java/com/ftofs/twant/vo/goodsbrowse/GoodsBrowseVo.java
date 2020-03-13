package com.ftofs.twant.vo.goodsbrowse;

import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.vo.store.StoreVo;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 產品浏览历史
 * 
 * @author zxy
 * Created 2017/4/13 11:01
 */
public class GoodsBrowseVo {
    /**
     * 自增编码
     */
    private int browseId = 0;
    /**
     * 產品SPU编号
     */
    private int commonId = 0;
    /**
     * 会员ID
     */
    private int memberId = 0;
    /**
     * 浏览时间
     */

    private String addTime = "";
    /**
     * 浏览时间文字
     */
    private String addTimeText = "";
    /**
     * 產品分类ID
     */
    private int goodsCategoryId = 0;
    /**
     * 產品一级分类
     */
    private int goodsCategoryId1 = 0;
    /**
     * 產品二级分类
     */
    private int goodsCategoryId2 = 0;
    /**
     * 產品三级分类
     */
    private int goodsCategoryId3 = 0;
    /**
     * 產品名称
     */
    private String goodsName = "";
    /**
     * 產品SPU
     */
    private GoodsCommon goodsCommon = null;
    /**
     * 產品产地
     */
    private AdminCountry adminCountry;
    /**
     * 当前產品的店铺信息
     */
    private StoreVo storeVo = null;

    public int getBrowseId() {
        return browseId;
    }

    public void setBrowseId(int browseId) {
        this.browseId = browseId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddTimeText() {
        return addTimeText;
    }

    public void setAddTimeText(String addTimeText) {
        this.addTimeText = addTimeText;
    }

    public int getGoodsCategoryId() {
        return goodsCategoryId;
    }

    public void setGoodsCategoryId(int goodsCategoryId) {
        this.goodsCategoryId = goodsCategoryId;
    }

    public int getGoodsCategoryId1() {
        return goodsCategoryId1;
    }

    public void setGoodsCategoryId1(int goodsCategoryId1) {
        this.goodsCategoryId1 = goodsCategoryId1;
    }

    public int getGoodsCategoryId2() {
        return goodsCategoryId2;
    }

    public void setGoodsCategoryId2(int goodsCategoryId2) {
        this.goodsCategoryId2 = goodsCategoryId2;
    }

    public int getGoodsCategoryId3() {
        return goodsCategoryId3;
    }

    public void setGoodsCategoryId3(int goodsCategoryId3) {
        this.goodsCategoryId3 = goodsCategoryId3;
    }

    public String getGoodsName() {
        this.goodsName = this.getGoodsCommon().getGoodsName();
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public GoodsCommon getGoodsCommon() {
        return goodsCommon;
    }

    public void setGoodsCommon(GoodsCommon goodsCommon) {
        this.goodsCommon = goodsCommon;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }

    public AdminCountry getAdminCountry() {
        return adminCountry;
    }

    public void setAdminCountry(AdminCountry adminCountry) {
        this.adminCountry = adminCountry;
    }

    @Override
    public String toString() {
        return "GoodsBrowseVo{" +
                "browseId=" + browseId +
                ", commonId=" + commonId +
                ", memberId=" + memberId +
                ", addTime=" + addTime +
                ", addTimeText='" + addTimeText + '\'' +
                ", goodsCategoryId=" + goodsCategoryId +
                ", goodsCategoryId1=" + goodsCategoryId1 +
                ", goodsCategoryId2=" + goodsCategoryId2 +
                ", goodsCategoryId3=" + goodsCategoryId3 +
                ", goodsName='" + goodsName + '\'' +
                ", goodsCommon=" + goodsCommon +
                ", storeVo=" + storeVo +
                '}';
    }
}

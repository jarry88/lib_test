package com.ftofs.twant.vo;

import com.ftofs.twant.domain.goods.Brand;
import com.ftofs.twant.domain.goods.GoodsCommon;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 首页楼层item vo
 *
 * @author cj
 * Created 2017-4-14 下午 6:20
 */
public class IndexFloorItemVo implements Serializable {
    /**
     * item id
     */
    private int floorItemId;

    /**
     * 名称
     */
    private String floorItemName;

    /**
     * 数据类型
     */
    private String floorItemType;

    /**
     * 排序用于tag的
     */
    private int floorItemSort;

    /**
     * 数据key ，用户标识tag 的类型
     */
    private String floorItemKey;

    /**
     * 保存的json数据
     */
    private String floorItemJson;

    /**
     * 楼层编号
     */
    private int floorId;

    /**
     * 携带的商品信息
     */
    private List<GoodsCommon> goodsCommons = new ArrayList<GoodsCommon>();

    /**
     * 携带的品牌数据
     */
    private List<Brand> brands = new ArrayList<Brand>();

    public int getFloorItemId() {
        return floorItemId;
    }

    public void setFloorItemId(int floorItemId) {
        this.floorItemId = floorItemId;
    }

    public String getFloorItemName() {
        return floorItemName;
    }

    public void setFloorItemName(String floorItemName) {
        this.floorItemName = floorItemName;
    }

    public String getFloorItemType() {
        return floorItemType;
    }

    public void setFloorItemType(String floorItemType) {
        this.floorItemType = floorItemType;
    }

    public int getFloorItemSort() {
        return floorItemSort;
    }

    public void setFloorItemSort(int floorItemSort) {
        this.floorItemSort = floorItemSort;
    }

    public String getFloorItemKey() {
        return floorItemKey;
    }

    public void setFloorItemKey(String floorItemKey) {
        this.floorItemKey = floorItemKey;
    }

    public String getFloorItemJson() {
        return floorItemJson;
    }

    public void setFloorItemJson(String floorItemJson) {
        this.floorItemJson = floorItemJson;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public List<GoodsCommon> getGoodsCommons() {
        return goodsCommons;
    }

    public void setGoodsCommons(List<GoodsCommon> goodsCommons) {
        this.goodsCommons = goodsCommons;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "IndexFloorItemVo{" +
                "floorItemId=" + floorItemId +
                ", floorItemName='" + floorItemName + '\'' +
                ", floorItemType='" + floorItemType + '\'' +
                ", floorItemSort=" + floorItemSort +
                ", floorItemKey='" + floorItemKey + '\'' +
                ", floorItemJson='" + floorItemJson + '\'' +
                ", floorId=" + floorId +
                ", goodsCommons=" + goodsCommons +
                ", brands=" + brands +
                '}';
    }
}

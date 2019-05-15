package com.ftofs.twant.vo;

import com.ftofs.twant.domain.ChannelModuleItem;
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
 * 频道模块item vo
 *
 * @author cj
 * Created 2017-4-14 下午 6:18
 */
public class ChannelModuleItemVo implements Serializable {
    /**
     * item id
     */
    private int moduleItemId;
    /**
     * 名称
     */

    private String moduleItemName;

    /**
     * 数据类型
     */

    private String moduleItemType;

    /**
     * 排序用于tag的
     */

    private int moduleItemSort;

    /**
     * 数据key ，用户标识tag 的类型
     */

    private String moduleItemKey;

    /**
     * 保存的json数据
     */

    private String moduleItemJson;

    /**
     * 楼层编号
     */

    private int moduleId;

    /**
     * 携带的商品信息
     */
    private List<GoodsCommon> goodsCommons = new ArrayList<GoodsCommon>();

    /**
     * 携带的品牌数据
     */
    private List<Brand> brands = new ArrayList<Brand>();

    public ChannelModuleItemVo(ChannelModuleItem indexModuleItem) {
        this.setModuleId(indexModuleItem.getModuleId());
        this.setModuleItemId(indexModuleItem.getModuleItemId());
        this.setModuleItemJson(indexModuleItem.getModuleItemJson());
        this.setModuleItemKey(indexModuleItem.getModuleItemKey());
        this.setModuleItemName(indexModuleItem.getModuleItemName());
        this.setModuleItemSort(indexModuleItem.getModuleItemSort());
        this.setModuleItemType(indexModuleItem.getModuleItemType());
    }

    public int getModuleItemId() {
        return moduleItemId;
    }

    public void setModuleItemId(int moduleItemId) {
        this.moduleItemId = moduleItemId;
    }

    public String getModuleItemName() {
        return moduleItemName;
    }

    public void setModuleItemName(String moduleItemName) {
        this.moduleItemName = moduleItemName;
    }

    public String getModuleItemType() {
        return moduleItemType;
    }

    public void setModuleItemType(String moduleItemType) {
        this.moduleItemType = moduleItemType;
    }

    public int getModuleItemSort() {
        return moduleItemSort;
    }

    public void setModuleItemSort(int moduleItemSort) {
        this.moduleItemSort = moduleItemSort;
    }

    public String getModuleItemKey() {
        return moduleItemKey;
    }

    public void setModuleItemKey(String moduleItemKey) {
        this.moduleItemKey = moduleItemKey;
    }

    public String getModuleItemJson() {
        return moduleItemJson;
    }

    public void setModuleItemJson(String moduleItemJson) {
        this.moduleItemJson = moduleItemJson;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
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
        return "ChannelModuleItemVo{" +
                "moduleItemId=" + moduleItemId +
                ", moduleItemName='" + moduleItemName + '\'' +
                ", moduleItemType='" + moduleItemType + '\'' +
                ", moduleItemSort=" + moduleItemSort +
                ", moduleItemKey='" + moduleItemKey + '\'' +
                ", moduleItemJson='" + moduleItemJson + '\'' +
                ", moduleId=" + moduleId +
                ", goodsCommons=" + goodsCommons +
                ", brands=" + brands +
                '}';
    }
}

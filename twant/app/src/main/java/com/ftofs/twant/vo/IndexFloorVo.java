package com.ftofs.twant.vo;

import com.ftofs.twant.domain.IndexFloor;

import java.io.Serializable;

import java.util.List;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 首页楼层
 *
 * @author zxy
 * Created 2017/4/13 10:59
 */
public class IndexFloorVo implements Serializable {
    private int floorId;

    /**
     * 楼层名称
     */
    private String floorName;

    /**
     * 楼层颜色名称
     */

    private String floorColorName;

    /**
     * 模版名称
     */

    private String floorTemplateName;

    /**
     * 楼层类型
     */

    private String floorType;

    /**
     * 更新时间
     */
    private String floorUpdateTime;

    /**
     * 楼层编号
     */

    private int floorSort;
    /**
     * 是否显示
     */

    private int floorShow;

    /**
     * 首页滚动导航文字
     */
    private String floorNavText;

    /**
     * 首页滚动导航图片
     */
    private String floorNavImage;

    /**
     * flooritem
     */
    private List<IndexFloorItemVo> indexFloorItemList;

    public IndexFloorVo(IndexFloor indexFloor) {
        this.setFloorId(indexFloor.getFloorId());
        this.setFloorName(indexFloor.getFloorName());
        this.setFloorColorName(indexFloor.getFloorColorName());
        this.setFloorTemplateName(indexFloor.getFloorTemplateName());
        this.setFloorType(indexFloor.getFloorType());
        this.setFloorUpdateTime(indexFloor.getFloorUpdateTime());
        this.setFloorSort(indexFloor.getFloorSort());
        this.setFloorShow(indexFloor.getFloorShow());
        this.setFloorNavText(indexFloor.getFloorNavText());
        this.setFloorNavImage(indexFloor.getFloorNavImage());

    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getFloorColorName() {
        return floorColorName;
    }

    public void setFloorColorName(String floorColorName) {
        this.floorColorName = floorColorName;
    }

    public String getFloorTemplateName() {
        return floorTemplateName;
    }

    public void setFloorTemplateName(String floorTemplateName) {
        this.floorTemplateName = floorTemplateName;
    }

    public String getFloorUpdateTime() {
        return floorUpdateTime;
    }

    public void setFloorUpdateTime(String floorUpdateTime) {
        this.floorUpdateTime = floorUpdateTime;
    }

    public int getFloorSort() {
        return floorSort;
    }

    public void setFloorSort(int floorSort) {
        this.floorSort = floorSort;
    }

    public int getFloorShow() {
        return floorShow;
    }

    public void setFloorShow(int floorShow) {
        this.floorShow = floorShow;
    }

    public String getFloorType() {
        return floorType;
    }

    public void setFloorType(String floorType) {
        this.floorType = floorType;
    }

    public List<IndexFloorItemVo> getIndexFloorItemList() {
        return indexFloorItemList;
    }

    public void setIndexFloorItemList(List<IndexFloorItemVo> indexFloorItemList) {
        this.indexFloorItemList = indexFloorItemList;
    }

    public String getFloorNavText() {
        return floorNavText;
    }

    public void setFloorNavText(String floorNavText) {
        this.floorNavText = floorNavText;
    }

    public String getFloorNavImage() {
        return floorNavImage;
    }

    public void setFloorNavImage(String floorNavImage) {
        this.floorNavImage = floorNavImage;
    }

    @Override
    public String toString() {
        return "IndexFloorVo{" +
                "floorId=" + floorId +
                ", floorName='" + floorName + '\'' +
                ", floorColorName='" + floorColorName + '\'' +
                ", floorTemplateName='" + floorTemplateName + '\'' +
                ", floorType='" + floorType + '\'' +
                ", floorUpdateTime=" + floorUpdateTime +
                ", floorSort=" + floorSort +
                ", floorShow=" + floorShow +
                ", floorNavText='" + floorNavText + '\'' +
                ", floorNavImage='" + floorNavImage + '\'' +
                ", indexFloorItemList=" + indexFloorItemList +
                '}';
    }
}

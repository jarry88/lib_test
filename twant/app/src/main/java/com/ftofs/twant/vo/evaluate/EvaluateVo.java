package com.ftofs.twant.vo.evaluate;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 评价请求Vo
 *
 * @author dqw
 * Created 2017/10/18 11:34
 */
public class EvaluateVo {
    /**
     * 订单编号
     */
    private int ordersId;
    /**
     * 订单类型
     */
    private int ordersType;
    /**
     * 订单產品编号
     */
    private List<Integer> ordersGoodsIdList = new ArrayList<>();
    /**
     * 评价内容列表
     */
    private List<String> contentList = new ArrayList<>();
    /**
     * 评分列表
     */
    private List<Integer> scoreList = new ArrayList<>();
    /**
     * 评价图片列表
     */
    private List<String> imageList = new ArrayList<>();
    /**
     * 店铺描述相符评分
     */
    private int descriptionCredit;
    /**
     * 店铺服务态度评分
     */
    private int serviceCredit;
    /**
     * 店铺发货速度评分
     */
    private int deliveryCredit;

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public List<Integer> getOrdersGoodsIdList() {
        return ordersGoodsIdList;
    }

    public void setOrdersGoodsIdList(List<Integer> ordersGoodsIdList) {
        this.ordersGoodsIdList = ordersGoodsIdList;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public int getDescriptionCredit() {
        return descriptionCredit;
    }

    public void setDescriptionCredit(int descriptionCredit) {
        this.descriptionCredit = descriptionCredit;
    }

    public int getServiceCredit() {
        return serviceCredit;
    }

    public void setServiceCredit(int serviceCredit) {
        this.serviceCredit = serviceCredit;
    }

    public int getDeliveryCredit() {
        return deliveryCredit;
    }

    public void setDeliveryCredit(int deliveryCredit) {
        this.deliveryCredit = deliveryCredit;
    }

    @Override
    public String toString() {
        return "EvaluateVo{" +
                "ordersId=" + ordersId +
                ", ordersType=" + ordersType +
                ", ordersGoodsIdList=" + ordersGoodsIdList +
                ", contentList=" + contentList +
                ", scoreList=" + scoreList +
                ", imageList=" + imageList +
                ", descriptionCredit=" + descriptionCredit +
                ", serviceCredit=" + serviceCredit +
                ", deliveryCredit=" + deliveryCredit +
                '}';
    }
}


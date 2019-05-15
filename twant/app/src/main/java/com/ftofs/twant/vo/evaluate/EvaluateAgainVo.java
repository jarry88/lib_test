package com.ftofs.twant.vo.evaluate;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 追评Vo
 *
 * @author dqw
 * Created 2017/10/18 11:33
 */
public class EvaluateAgainVo {
    /**
     * 订单编号
     */
    private int ordersId;
    /**
     * 评价编号列表
     */
    private List<Integer> evaluateIdList = new ArrayList<>();
    /**
     * 评价内容列表
     */
    private List<String> contentList = new ArrayList<>();
    /**
     * 评价图片列表
     */
    private List<String> imageList = new ArrayList<>();

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public List<Integer> getEvaluateIdList() {
        return evaluateIdList;
    }

    public void setEvaluateIdList(List<Integer> evaluateIdList) {
        this.evaluateIdList = evaluateIdList;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "EvaluateAgainVo{" +
                "ordersId=" + ordersId +
                ", evaluateIdList=" + evaluateIdList +
                ", contentList=" + contentList +
                ", imageList=" + imageList +
                '}';
    }
}
	


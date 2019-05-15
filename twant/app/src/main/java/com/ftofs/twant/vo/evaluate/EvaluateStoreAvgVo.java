package com.ftofs.twant.vo.evaluate;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 店铺评价平均值Vo
 *
 * @author dqw
 * Created 2017/4/17 11:53
 */
public class EvaluateStoreAvgVo {
    //当前店铺描述评分
    private double storeDesEval;
    //当前店铺服务评分
    private double storeServiceEval;
    //当前店铺发货速度评分
    private double storeDeliveryEval;

    public double getStoreDesEval() {
        return storeDesEval;
    }

    public void setStoreDesEval(double storeDesEval) {
        this.storeDesEval = storeDesEval;
    }

    public double getStoreServiceEval() {
        return storeServiceEval;
    }

    public void setStoreServiceEval(double storeServiceEval) {
        this.storeServiceEval = storeServiceEval;
    }

    public double getStoreDeliveryEval() {
        return storeDeliveryEval;
    }

    public void setStoreDeliveryEval(double storeDeliveryEval) {
        this.storeDeliveryEval = storeDeliveryEval;
    }

    @Override
    public String toString() {
        return "EvaluateStoreAvgVo{" +
                "storeDesEval=" + storeDesEval +
                ", storeServiceEval=" + storeServiceEval +
                ", storeDeliveryEval=" + storeDeliveryEval +
                '}';
    }
}


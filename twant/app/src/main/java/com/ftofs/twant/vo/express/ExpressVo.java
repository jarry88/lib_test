package com.ftofs.twant.vo.express;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 快递查询一条详细内容
 * 
 * @author hbj
 * Created 2017/4/13 14:45
 */
public class ExpressVo {
    /**
     * 时间点
     */
    public String time = "";
    /**
     * 内容
     */
    public String context = "";

    public ExpressVo(String time, String context) {
        this.time = time;
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "ExpressVo{" +
                "time='" + time + '\'' +
                ", context='" + context + '\'' +
                '}';
    }
}

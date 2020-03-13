package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 產品筛选已选项VO
 *
 * @author shopnc.feng
 * Created 2017/4/13 13:56
 */
public class SearchCheckedFilterVo {
    /**
     * 名称
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 参数
     */
    private String param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "SearchCheckedFilterVo{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", param='" + param + '\'' +
                '}';
    }
}

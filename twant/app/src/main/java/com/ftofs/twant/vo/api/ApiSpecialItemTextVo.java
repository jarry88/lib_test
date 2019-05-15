package com.ftofs.twant.vo.api;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 接口专题文字类型项目数据Vo
 *
 * @author dqw
 * Created 2017/4/17 11:48
 */
public class ApiSpecialItemTextVo {
    /**
     * 文字
     */
    private String text="";
    /**
     * 操作类型
     */
    private String type="";
    /**
     * 数据
     */
    private String data="";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiSpecialItemTextVo{" +
                "text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

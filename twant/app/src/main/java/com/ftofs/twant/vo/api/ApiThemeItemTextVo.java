package com.ftofs.twant.vo.api;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 接口商城活动文字类型项目数据Vo
 *
 * @author szq
 * Created 2017/8/28 11:48
 */
public class ApiThemeItemTextVo {
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
        return "ApiThemeItemTextVo{" +
                "text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

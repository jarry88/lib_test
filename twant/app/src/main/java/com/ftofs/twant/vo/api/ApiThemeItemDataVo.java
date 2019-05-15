package com.ftofs.twant.vo.api;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 接口商城活动项目数据Vo
 *
 * @author szq
 * Created 2017/8/28 11:48
 */
public class ApiThemeItemDataVo {
    /**
     * 图片
     */
    private String image="";
    /**
     * 操作类型
     */
    private String type="";
    /**
     * 数据
     */
    private String data="";

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageUrl() {
        return image;
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
        return "ApiThemeItemDataVo{" +
                "image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

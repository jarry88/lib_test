package com.ftofs.twant.vo.store;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 移动店铺专题项目数据Vo
 *
 * @author dqw
 * Created 2017/4/17 11:56
 */
public class StoreMobileSpecialItemDataVo {
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
        return "";
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
        return "ApiSpecialItemDataVo{" +
                "image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

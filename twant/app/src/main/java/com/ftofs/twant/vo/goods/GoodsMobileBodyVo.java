package com.ftofs.twant.vo.goods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 商品手机端描述
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:07
 */
public class GoodsMobileBodyVo {
    /**
     * 类型
     */
    private String type;
    /**
     * 值
     */
    private String value;

    /**
     * 圖片寬度
     */
    private int width;

    /**
     * 圖片高度
     */
    private int height;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "GoodsMobileBodyVo{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

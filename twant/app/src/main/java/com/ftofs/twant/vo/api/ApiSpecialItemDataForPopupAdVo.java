package com.ftofs.twant.vo.api;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 接口专题项目 弹出广告数据Vo
 *
 * @author cj
 * Created 2017/11/27 11:48
 */
public class ApiSpecialItemDataForPopupAdVo {
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
    /**
     * 判断每次都弹出
     */
    private int enableEveryTime ;
    /**
     * 图片链接地址
     */
    private  String imageUrl = "" ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
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

    public int getEnableEveryTime() {
        return enableEveryTime;
    }

    public void setEnableEveryTime(int enableEveryTime) {
        this.enableEveryTime = enableEveryTime;
    }

    @Override
    public String toString() {
        return "ApiSpecialItemDataForPopupAdVo{" +
                "image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                ", enableEveryTime=" + enableEveryTime +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

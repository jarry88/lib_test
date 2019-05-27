package com.ftofs.twant.vo.advertorial;

/**
 * copyright  Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 推文 item vo
 * 
 * @author cj
 * Created 2017-9-19 上午 10:47
 */
public class AdvertorialArticleItemVo {
    /**
     * 类型
     */
    private int type;

    /**
     * 内容
     */
    private String content  = "" ;

    /**
     * 商品id
     */
    private int commonId ;

    private String imageUrl = "" ;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getImageUrl() {
        return "";
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "AdvertorialArticleItemVo{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", commonId=" + commonId +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

package com.ftofs.twant.vo.orders;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 订单商品消保实体
 *
 * @author hbj
 * Created 2017/4/13 14:45
 */
public class GoodsContractVo {
    /**
     * 图标
     */
    private String image = "";
    /**
     * 说明内容URL地址
     */
    private String url = "";
    /**
     * 名称
     */
    private String title = "";
    /**
     * 消保描述
     */
    private String content = "";

    public GoodsContractVo() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "GoodsContractVo{" +
                "image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

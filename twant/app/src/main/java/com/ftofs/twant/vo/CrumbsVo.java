package com.ftofs.twant.vo;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 面包屑
 * 
 * @author shopnc.feng
 * Created 2017/4/13 13:56
 */
public class CrumbsVo {
    /**
     * 名称
     */
    private String name;
    /**
     * 路径
     */
    private String url = "javascript:;";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "CrumbsVo{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

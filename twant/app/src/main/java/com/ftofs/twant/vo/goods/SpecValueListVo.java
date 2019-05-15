package com.ftofs.twant.vo.goods;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 规格值
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:12
 */
public class SpecValueListVo {
    private int specValueId=0;
    private String specValueName="";
    private String imageSrc="";

    public int getSpecValueId() {
        return specValueId;
    }

    public void setSpecValueId(int specValueId) {
        this.specValueId = specValueId;
    }

    public String getSpecValueName() {
        return specValueName;
    }

    public void setSpecValueName(String specValueName) {
        this.specValueName = specValueName;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    @Override
    public String toString() {
        return "SpecValueListVo{" +
                "specValueId=" + specValueId +
                ", specValueName='" + specValueName + '\'' +
                ", imageSrc='" + imageSrc + '\'' +
                '}';
    }
}

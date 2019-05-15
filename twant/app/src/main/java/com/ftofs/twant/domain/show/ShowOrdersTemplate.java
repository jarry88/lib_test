package com.ftofs.twant.domain.show;

import java.io.Serializable;

public class ShowOrdersTemplate implements Serializable {
    /**
     * 晒宝模版id
     * 主键、自增
     */
    private int templateId;

    /**
     * 展示名称
     */
    private String showText;

    /**
     * 展示图片
     */
    private String showImage;

    /**
     * 背景图片
     */
    private String backGroundImage;

    /**
     * 字体颜色
     */
    private String fontColor = "";

    /**
     * 展示图片地址全路径路径
     */
    private String showImagePath="";

    /**
     * 背景图片地址全路径路径
     */
    private String backGroundImagePath="";

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getShowText() {
        return showText;
    }

    public void setShowText(String showText) {
        this.showText = showText;
    }

    public String getShowImage() {
        return showImage;
    }

    public void setShowImage(String showImage) {
        this.showImage = showImage;
    }

    public String getBackGroundImage() {
        return backGroundImage;
    }

    public void setBackGroundImage(String backGroundImage) {
        this.backGroundImage = backGroundImage;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getShowImagePath() {
        return showImagePath;
    }

    public String getBackGroundImagePath() {
        return backGroundImagePath;
    }

    @Override
    public String toString() {
        return "ShowOrdersTemplate{" +
                "templateId=" + templateId +
                ", showText='" + showText + '\'' +
                ", showImage='" + showImage + '\'' +
                ", backGroundImage='" + backGroundImage + '\'' +
                ", fontColor='" + fontColor + '\'' +
                ", showImagePath='" + showImagePath + '\'' +
                ", backGroundImagePath='" + backGroundImagePath + '\'' +
                '}';
    }
}

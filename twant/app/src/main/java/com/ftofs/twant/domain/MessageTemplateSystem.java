package com.ftofs.twant.domain;

public class MessageTemplateSystem {
    /**
     * 消息模板编码、主键
     */
    private String tplCode;

    /**
     * 消息模板名称
     */
    private String tplName = "";

    /**
     * 标题
     */
    private String tplTitle = "";

    /**
     * 内容
     */
    private String tplContent = "";

    /**
     * 模板类型
     * 1 短信，2 邮件
     */
    private int sendType;

    /**
     * 阿里繁體模板編號
     * Modify By yangjian 2018/12/25 19:04
     */
    private String aliTplCode;

    /**
     * 阿里雲簡體模板 編號
     * Modify By yangjian 2019/1/22 17:23
     */
    private String aliTplCodeCn;

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public String getTplTitle() {
        return tplTitle;
    }

    public void setTplTitle(String tplTitle) {
        this.tplTitle = tplTitle;
    }

    public String getTplContent() {
        return tplContent;
    }

    public void setTplContent(String tplContent) {
        this.tplContent = tplContent;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getAliTplCode() {
        return aliTplCode;
    }

    public void setAliTplCode(String aliTplCode) {
        this.aliTplCode = aliTplCode;
    }

    public String getAliTplCodeCn() {
        return aliTplCodeCn;
    }

    public void setAliTplCodeCn(String aliTplCodeCn) {
        this.aliTplCodeCn = aliTplCodeCn;
    }

    @Override
    public String toString() {
        return "MessageTemplateSystem{" +
                "tplCode='" + tplCode + '\'' +
                ", tplName='" + tplName + '\'' +
                ", tplTitle='" + tplTitle + '\'' +
                ", tplContent='" + tplContent + '\'' +
                ", sendType=" + sendType +
                ", aliTplCode='" + aliTplCode + '\'' +
                ", aliTplCodeCn='" + aliTplCodeCn + '\'' +
                '}';
    }
}

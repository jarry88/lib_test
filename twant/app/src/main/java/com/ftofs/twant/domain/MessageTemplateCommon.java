package com.ftofs.twant.domain;

public class MessageTemplateCommon {
    /**
     * 消息模板编码、主键
     */
    private String tplCode;

    /**
     * 消息模板名称
     */
    private String tplName = "";

    /**
     * 消息模板类型 1-用户消息模板 2-商家消息模板（MessageTemplateCommonTplType）
     */
    private Integer tplType;

     /**
     * 站内信内容
     */
    private String noticeContent = "";

    /**
     * 短信开关 0-关闭 1-开启
     */
    private Integer smsState;

    /**
     * 短信内容
     */
    private String smsContent = "";

    /**
     * 邮件开关  0-关闭 1-开启
     */
    private Integer emailState;

    /**
     * 邮件标题
     */
    private String emailTitle = "";

    /**
     * 邮件内容
     */
    private String emailContent = "";

    /**
     * 微信开关  0-关闭 1-开启
     */
    private int weixinState;

    /**
     * 微信公众平台模板库标题
     */
    private String weixinMpTemplateStoreTitle = "";

    /**
     * 微信公众平台模板库编号
     */
    private String weixinMpTemplateStoreId = "";

    /**
     * 微信公众平台我的模板ID
     */
    private String weixinMpTemplateId = "";

    /**
     * 微信消息跳转链接
     */
    private String weixinTemplateUrl = "";

    /**
     * 微信消息内容
     */
    private String weixinDataParams = "";

    /**
     * 消息模板分类
     * 会员    交易-1001 退换货-1002 物流-1003 资产-1004  邀請入職-1006<br/>
     * 商家    交易-2001 退换货-2002 產品-2003 运营-2004
     */
    private Integer tplClass;

    /**
     * 阿里模板編號
     * Modify By yangjian 2018/12/25 19:04
     */
    private String aliTplCode;

    /**
     * 阿里雲簡體模板 編號
     * Modify By yangjian 2019/1/22 17:23
     */
    private String aliTplCodeCn;

    /**
     * 已开启的消息
     */
    private String opened = "";

    /**
     * 是否接收
     */
    private int isReceive = 0;

    /**
     * 消息分类名称
     */
    private String tplClassName;

    public String getTplCode() {
        return tplCode;
    }

    public void setTplCode(String tplCode) {
        this.tplCode = tplCode;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public Integer getTplType() {
        return tplType;
    }

    public void setTplType(Integer tplType) {
        this.tplType = tplType;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public Integer getSmsState() {
        return smsState;
    }

    public void setSmsState(Integer smsState) {
        this.smsState = smsState;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public Integer getEmailState() {
        return emailState;
    }

    public void setEmailState(Integer emailState) {
        this.emailState = emailState;
    }

    public String getEmailTitle() {
        return emailTitle;
    }

    public void setEmailTitle(String emailTitle) {
        this.emailTitle = emailTitle;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
    }

    public int getWeixinState() {
        return weixinState;
    }

    public void setWeixinState(int weixinState) {
        this.weixinState = weixinState;
    }

    public String getWeixinMpTemplateStoreTitle() {
        return weixinMpTemplateStoreTitle;
    }

    public void setWeixinMpTemplateStoreTitle(String weixinMpTemplateStoreTitle) {
        this.weixinMpTemplateStoreTitle = weixinMpTemplateStoreTitle;
    }

    public String getWeixinMpTemplateStoreId() {
        return weixinMpTemplateStoreId;
    }

    public void setWeixinMpTemplateStoreId(String weixinMpTemplateStoreId) {
        this.weixinMpTemplateStoreId = weixinMpTemplateStoreId;
    }

    public String getWeixinMpTemplateId() {
        return weixinMpTemplateId;
    }

    public void setWeixinMpTemplateId(String weixinMpTemplateId) {
        this.weixinMpTemplateId = weixinMpTemplateId;
    }

    public String getWeixinTemplateUrl() {
        return weixinTemplateUrl;
    }

    public void setWeixinTemplateUrl(String weixinTemplateUrl) {
        this.weixinTemplateUrl = weixinTemplateUrl;
    }

    public String getWeixinDataParams() {
        return weixinDataParams;
    }

    public void setWeixinDataParams(String weixinDataParams) {
        this.weixinDataParams = weixinDataParams;
    }

    public Integer getTplClass() {
        return tplClass;
    }

    public void setTplClass(Integer tplClass) {
        this.tplClass = tplClass;
    }

    public String getOpened() {
        return "";
    }

    public int getIsReceive() {
        return isReceive;
    }

    public void setIsReceive(int isReceive) {
        this.isReceive = isReceive;
    }

    public String getTplClassName() {
        return tplClassName;
    }

    public void setTplClassName(String tplClassName) {
        this.tplClassName = tplClassName;
    }

    public String getAliTplCode() {
        return aliTplCode;
    }

    public void setAliTplCode(String aliTplCode) {
        this.aliTplCode = aliTplCode;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getAliTplCodeCn() {
        return aliTplCodeCn;
    }

    public void setAliTplCodeCn(String aliTplCodeCn) {
        this.aliTplCodeCn = aliTplCodeCn;
    }

    @Override
    public String toString() {
        return "MessageTemplateCommon{" +
                "tplCode='" + tplCode + '\'' +
                ", tplName='" + tplName + '\'' +
                ", tplType=" + tplType +
                ", noticeContent='" + noticeContent + '\'' +
                ", smsState=" + smsState +
                ", smsContent='" + smsContent + '\'' +
                ", emailState=" + emailState +
                ", emailTitle='" + emailTitle + '\'' +
                ", emailContent='" + emailContent + '\'' +
                ", weixinState=" + weixinState +
                ", weixinMpTemplateStoreTitle='" + weixinMpTemplateStoreTitle + '\'' +
                ", weixinMpTemplateStoreId='" + weixinMpTemplateStoreId + '\'' +
                ", weixinMpTemplateId='" + weixinMpTemplateId + '\'' +
                ", weixinTemplateUrl='" + weixinTemplateUrl + '\'' +
                ", weixinDataParams='" + weixinDataParams + '\'' +
                ", tplClass=" + tplClass +
                ", aliTplCode='" + aliTplCode + '\'' +
                ", aliTplCodeCn='" + aliTplCodeCn + '\'' +
                ", opened='" + opened + '\'' +
                ", isReceive=" + isReceive +
                ", tplClassName='" + tplClassName + '\'' +
                '}';
    }
}

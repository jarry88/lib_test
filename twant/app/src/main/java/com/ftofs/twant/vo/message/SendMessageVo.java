package com.ftofs.twant.vo.message;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 消息发送
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:13
 */
public class SendMessageVo {
    private String number = "";
    private String title = "";
    private String content = "";
    private String templateCode = "";
    private int sendType = 0;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    @Override
    public String toString() {
        return "SendMessageVo{" +
                "number='" + number + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", sendType=" + sendType +
                '}';
    }
}

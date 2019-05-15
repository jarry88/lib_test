package com.ftofs.twant.domain.api;

import java.sql.Timestamp;

public class ApiCaptchaCode {
    /**
     * 自增编号
     */
    private int codeId = 0;

    /**
     * 验证码标识
     */
    private String codeKey = "";

    /**
     * 验证码文本
     */
    private String codeVal = "";

    /**
     * 创建时间
     */
    private Timestamp createTime = new Timestamp(0);

    /**
     * 客户端类型
     */
    private String clientType;

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey;
    }

    public String getCodeVal() {
        return codeVal;
    }

    public void setCodeVal(String codeVal) {
        this.codeVal = codeVal;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "ApiCaptchaCode{" +
                "codeId=" + codeId +
                ", codeKey='" + codeKey + '\'' +
                ", codeVal='" + codeVal + '\'' +
                ", createTime=" + createTime +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}


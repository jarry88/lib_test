package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdvertorialJoin implements Serializable {
    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 达人昵称
     */
    private String authorName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号
     */
    private String idCartNumber;

    /**
     * 身份证正面照
     */
    private String idCartFrontImage;

    /**
     * 身份证反面照
     */
    private String idCartBackImage;

    /**
     * 手持身份证照
     */
    private String idCartHandImage;

    /**
     * 审核状态
     */
    private String state = "";

    /**
     * 管理员审核信息
     */
    private String message = "";

    /**
     * 申请提交时间
     */
    private Timestamp addTime;

    /**
     * 处理时间
     */
    private Timestamp handleTime;

    /**
     *  身份证正面照 图片地址
     */
    private String idCartFrontImageUrl = "";

    /**
     *  身份证反面照 图片地址
     */
    private String idCartBackImageUrl = "";

    /**
     * 手持身份证 图片地址
     */
    private String idCartHandImageUrl = "";

    /**
     * 审核状态文字
     */
    private String stateText = "";

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCartNumber() {
        return idCartNumber;
    }

    public void setIdCartNumber(String idCartNumber) {
        this.idCartNumber = idCartNumber;
    }

    public String getIdCartFrontImage() {
        return idCartFrontImage;
    }

    public void setIdCartFrontImage(String idCartFrontImage) {
        this.idCartFrontImage = idCartFrontImage;
    }

    public String getIdCartBackImage() {
        return idCartBackImage;
    }

    public void setIdCartBackImage(String idCartBackImage) {
        this.idCartBackImage = idCartBackImage;
    }

    public String getIdCartHandImage() {
        return idCartHandImage;
    }

    public void setIdCartHandImage(String idCartHandImage) {
        this.idCartHandImage = idCartHandImage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public Timestamp getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Timestamp handleTime) {
        this.handleTime = handleTime;
    }

    public void setIdCartFrontImageUrl(String idCartFrontImageUrl) {
        this.idCartFrontImageUrl = idCartFrontImageUrl;
    }

    public void setIdCartBackImageUrl(String idCartBackImageUrl) {
        this.idCartBackImageUrl = idCartBackImageUrl;
    }

    public void setIdCartHandImageUrl(String idCartHandImageUrl) {
        this.idCartHandImageUrl = idCartHandImageUrl;
    }

    public String getIdCartFrontImageUrl() {
        return idCartFrontImage;
    }

    public String getIdCartBackImageUrl() {
        return idCartBackImage;
    }

    public String getIdCartHandImageUrl() {
        return idCartHandImage;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    @Override
    public String toString() {
        return "AdvertorialJoin{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", realName='" + realName + '\'' +
                ", idCartNumber='" + idCartNumber + '\'' +
                ", idCartFrontImage='" + idCartFrontImage + '\'' +
                ", idCartBackImage='" + idCartBackImage + '\'' +
                ", idCartHandImage='" + idCartHandImage + '\'' +
                ", state=" + state +
                ", message='" + message + '\'' +
                ", addTime=" + addTime +
                ", handleTime=" + handleTime +
                ", idCartFrontImageUrl='" + idCartFrontImageUrl + '\'' +
                ", idCartBackImageUrl='" + idCartBackImageUrl + '\'' +
                ", idCartHandImageUrl='" + idCartHandImageUrl + '\'' +
                '}';
    }
}

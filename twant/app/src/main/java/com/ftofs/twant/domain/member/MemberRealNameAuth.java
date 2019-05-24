package com.ftofs.twant.domain.member;



public class MemberRealNameAuth {
    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 真实姓名
     */
    private String authRealName;

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
    private int authState =0;

    /**
     * 管理员审核信息
     */
    private String authMessage = "";

    /**
     * 申请提交时间
     */
    private String authAddTime;

    /**
     * 处理时间
     */
    private String authHandleTime;

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
     * 真实姓名隐私文本
     */
    private String authRealNameEncrypt = "";

    /**
     * 身份证号隐私文本
     */
    private String idCartNumberEncrypt = "";

    /**
     * 审核状态文字
     */
    private String authStateText = "";

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

    public String getAuthRealName() {
        return authRealName;
    }

    public void setAuthRealName(String authRealName) {
        this.authRealName = authRealName;
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

    public int getAuthState() {
        return authState;
    }

    public void setAuthState(int authState) {
        this.authState = authState;
    }

    public String getAuthMessage() {
        return authMessage;
    }

    public void setAuthMessage(String authMessage) {
        this.authMessage = authMessage;
    }

    public String getAuthAddTime() {
        return authAddTime;
    }

    public void setAuthAddTime(String authAddTime) {
        this.authAddTime = authAddTime;
    }

    public String getAuthHandleTime() {
        return authHandleTime;
    }

    public void setAuthHandleTime(String authHandleTime) {
        this.authHandleTime = authHandleTime;
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
        return idCartFrontImageUrl;
    }

    public String getIdCartBackImageUrl() {
        return idCartBackImageUrl;
    }

    public String getIdCartHandImageUrl() {
        return idCartHandImageUrl;
    }

    public void setIdCartNumberEncrypt(String idCartNumberEncrypt) {
        this.idCartNumberEncrypt = idCartNumberEncrypt;
    }

    public void setAuthRealNameEncrypt(String realNameEncrypt) {
        this.authRealNameEncrypt = realNameEncrypt;
    }

    public String getAuthRealNameEncrypt() {
        if(authRealName == null || authRealName == "")
        {
            return "" ;
        }
        return "*" + authRealName.substring(authRealName.length() - 1);

    }

    public String getIdCartNumberEncrypt() {
        return idCartNumber.replaceAll("([0-9]{2})(.*)([0-9]{4})", "$1************$3");
    }

    public String getAuthStateText() {
       return authStateText;
    }

    public void setAuthStateText(String authStateText) {
        this.authStateText = authStateText;
    }

    @Override
    public String toString() {
        return "MemberRealNameAuth{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", authRealName='" + authRealName + '\'' +
                ", idCartNumber='" + idCartNumber + '\'' +
                ", idCartFrontImage='" + idCartFrontImage + '\'' +
                ", idCartBackImage='" + idCartBackImage + '\'' +
                ", idCartHandImage='" + idCartHandImage + '\'' +
                ", authState=" + authState +
                ", authMessage='" + authMessage + '\'' +
                ", authAddTime=" + authAddTime +
                ", authHandleTime=" + authHandleTime +
                ", idCartFrontImageUrl='" + idCartFrontImageUrl + '\'' +
                ", idCartBackImageUrl='" + idCartBackImageUrl + '\'' +
                ", idCartHandImageUrl='" + idCartHandImageUrl + '\'' +
                ", authRealNameEncrypt='" + authRealNameEncrypt + '\'' +
                ", idCartNumberEncrypt='" + idCartNumberEncrypt + '\'' +
                ", authStateText='" + authStateText + '\'' +
                '}';
    }
}

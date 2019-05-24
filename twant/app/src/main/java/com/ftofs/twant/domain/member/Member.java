package com.ftofs.twant.domain.member;

import java.io.Serializable;
import java.math.BigDecimal;


public class Member implements Serializable,Cloneable {
    /**
     * 会员自增编码
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 会员真实姓名
     */
    private String trueName = "";

    /**
     * 会员昵称
     * Modify By yangjian 2018/9/19 16:48 description 增加會員暱稱
     */
    private String nickName = "";

    /**
     * 登录密码
     */
    private String memberPwd = "";

    /**
     * 支付密码
     */
    private String payPwd = "";

    /**
     * 性别
     */
    private int memberSex = 0;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 头像
     */
    private String avatar = "";

    /**
     * 邮箱
     */
    private String email = "";

    /**
     * 是否已进行邮箱绑定 0未绑定 1已绑定
     */
    private int emailIsBind = 0;

    /**
     * 手机号
     */
    private String mobile = "";

    /**
     * 手機區號
     * create by yangjian 2019-1-19
     */
    private String mobileAreaCode = "";

    /**
     * 是否已进行手机绑定 0未绑定 1已绑定
     */
    private int mobileIsBind = 0;

    /**
     * 注册时间
     */
    private String registerTime;

    /**
     * 登录次数
     */
    private int loginNum = 0;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 上次登录时间
     */
    private String lastLoginTime;

    /**
     * 登录IP
     */
    private String loginIp = "";

    /**
     * 上次登录IP
     */
    private String lastLoginIp = "";

    /**
     * 会员积分
     */
    private int memberPoints = 0;

    /**
     * 预存款可用余额
     */
    private BigDecimal predepositAvailable = new BigDecimal(0);

    /**
     * 预存款冻结金额
     */
    private BigDecimal predepositFreeze  = new BigDecimal(0);

    /**
     * 所在地省份ID
     */
    private int addressProvinceId = 0;

    /**
     * 所在地城市ID
     */
    private int addressCityId = 0;

    /**
     * 所在地地区ID
     */
    private int addressAreaId = 0;

    /**
     * 所在地地区详情
     */
    private String addressAreaInfo = "";

    /**
     * 信賴值
     */
    private int experiencePoints = 0;

    /**
     * 会员是否有购买权限 0无权限 1有权限
     */
    private int allowBuy = 1;

    /**
     * 会员是否有发言权限 0无权限 1有权限
     */
    private int allowTalk = 1;

    /**
     * 会员状态
     */
    private int state = 1;

    /**
     * 会员名更改次数
     */
    private int modifyNum = 0;

    /**
     * 微信用户ID
     */
    private String weixinUnionid = "";

    /**
     * 微信用户信息
     */
    private String weixinUserInfo = "";

    /**
     * 微信用户公众平台openID
     */
    private String weixinMpOpenid = "";

    /**
     * QQ用户ID
     */
    private String qqOpenid = "";

    /**
     * QQ用户信息
     */
    private String qqUserInfo = "";

    /**
     * 头像路径
     */
    private String avatarUrl = "";

    /**
     * 邮箱隐私文本
     */
    private String emailEncrypt = "";

    /**
     * 手机隐私文本
     */
    private String mobileEncrypt = "";

    /**
     * 会员安全级别
     */
    private int securityLevel = 0;

    /**
     * 是否已设置支付密码
     */
    private int payPwdIsExist = 0;

    /**
     * bycj -- 是否是分销商
     * 0:不是分销商 1：是分销商
     */
    private int isDistributor = 0 ;

    /**
     * 是否已绑定QQ
     */
    private int qqIsBind = 0;

    /**
     * 是否已绑定微信
     */
    private int weixinIsBind = 0;

    /**
     * 性别文本
     */
    private String memberSexText = "";

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 會員個性簽名
     */
    private String memberSignature;

    /**
     * 會員簡介
     */
    private String memberBio;

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

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public int getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(int memberSex) {
        this.memberSex = memberSex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmailIsBind() {
        return emailIsBind;
    }

    public void setEmailIsBind(int emailIsBind) {
        this.emailIsBind = emailIsBind;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMobileIsBind() {
        return mobileIsBind;
    }

    public void setMobileIsBind(int mobileIsBind) {
        this.mobileIsBind = mobileIsBind;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public int getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(int loginNum) {
        this.loginNum = loginNum;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public int getMemberPoints() {
        return memberPoints;
    }

    public void setMemberPoints(int memberPoints) {
        this.memberPoints = memberPoints;
    }

    public BigDecimal getPredepositAvailable() {
        return predepositAvailable;
    }

    public void setPredepositAvailable(BigDecimal predepositAvailable) {
        this.predepositAvailable = predepositAvailable;
    }

    public BigDecimal getPredepositFreeze() {
        return predepositFreeze;
    }

    public void setPredepositFreeze(BigDecimal predepositFreeze) {
        this.predepositFreeze = predepositFreeze;
    }

    public int getAddressProvinceId() {
        return addressProvinceId;
    }

    public void setAddressProvinceId(int addressProvinceId) {
        this.addressProvinceId = addressProvinceId;
    }

    public int getAddressCityId() {
        return addressCityId;
    }

    public void setAddressCityId(int addressCityId) {
        this.addressCityId = addressCityId;
    }

    public int getAddressAreaId() {
        return addressAreaId;
    }

    public void setAddressAreaId(int addressAreaId) {
        this.addressAreaId = addressAreaId;
    }

    public String getAddressAreaInfo() {
        return addressAreaInfo;
    }

    public void setAddressAreaInfo(String addressAreaInfo) {
        this.addressAreaInfo = addressAreaInfo;
    }

    public int getExperiencePoints() {
        return experiencePoints;
    }

    public void setExperiencePoints(int experiencePoints) {
        this.experiencePoints = experiencePoints;
    }

    public int getAllowBuy() {
        return allowBuy;
    }

    public void setAllowBuy(int allowBuy) {
        this.allowBuy = allowBuy;
    }

    public int getAllowTalk() {
        return allowTalk;
    }

    public void setAllowTalk(int allowTalk) {
        this.allowTalk = allowTalk;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getModifyNum() {
        return modifyNum;
    }

    public void setModifyNum(int modifyNum) {
        this.modifyNum = modifyNum;
    }

    public String getWeixinUnionid() {
        return weixinUnionid;
    }

    public void setWeixinUnionid(String weixinUnionid) {
        this.weixinUnionid = weixinUnionid;
    }

    public String getWeixinUserInfo() {
        return weixinUserInfo;
    }

    public void setWeixinUserInfo(String weixinUserInfo) {
        this.weixinUserInfo = weixinUserInfo;
    }

    public String getWeixinMpOpenid() {
        return weixinMpOpenid;
    }

    public void setWeixinMpOpenid(String weixinMpOpenid) {
        this.weixinMpOpenid = weixinMpOpenid;
    }

    public String getQqOpenid() {
        return qqOpenid;
    }

    public void setQqOpenid(String qqOpenid) {
        this.qqOpenid = qqOpenid;
    }

    public String getQqUserInfo() {
        return qqUserInfo;
    }

    public void setQqUserInfo(String qqUserInfo) {
        this.qqUserInfo = qqUserInfo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getEmailEncrypt() {
        if (email != null && email.trim().length() > 0) {
            String[] emailArr = email.split("@");
            if (emailArr[0].trim().length() > 4) {
                emailEncrypt = emailArr[0].replaceAll("(.{0,4})(.{4})(.*)", "$1****$3")+"@"+emailArr[1];
            }else{
                emailEncrypt = emailArr[0].replaceAll("(.{1})(.{0,4})(.*)", "$1****$3")+"@"+emailArr[1];
            }
        }else{
            emailEncrypt = "";
        }
        return emailEncrypt;
    }

    public String getMobileEncrypt() {
        if (mobile != null) {
            if (mobile.trim().length() > 0 && mobile.trim().length() < 9){
                //香港澳門手機號隱私文本
                mobileEncrypt = mobile.substring(0, 2) + "***" + mobile.substring(5, 8);
            } else {
                //mobileEncrypt = mobile.replaceAll("([0-9]{3})([0-9]{4})([0-9]{4})","$1****$3");
                //大陸手機號隱私文本
                mobileEncrypt = mobile.substring(0, 3) + "*****" + mobile.substring(8, 11);
            }
        }else{
            mobileEncrypt = "";
        }
        return mobileEncrypt;
    }

    public int getSecurityLevel() {
        securityLevel = 0;
        if (mobileIsBind == 1) {
            securityLevel += 1;
        }
        if (payPwd!=null && payPwd.trim().length() >0) {
            securityLevel += 1;
        }
        return securityLevel;
    }

    public int getPayPwdIsExist() {
        if (payPwd!=null && payPwd.trim().length()>0) {
            return 1;
        }else{
            return 0;
        }
    }

    public int getIsDistributor() {
        return isDistributor;
    }

    public void setIsDistributor(int isDistributor) {
        this.isDistributor = isDistributor;
    }

    public int getQqIsBind() {
        return 0;
    }

    public int getWeixinIsBind() {
        return 0;
    }

    public String getMemberSexText() {
        if (memberSex==1) {
            memberSexText = "男";
        }else if (memberSex==2) {
            memberSexText = "女";
        }else{
            memberSexText = "保密";
        }
        return memberSexText;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getMobileAreaCode() {
        return mobileAreaCode;
    }

    public void setMobileAreaCode(String mobileAreaCode) {
        this.mobileAreaCode = mobileAreaCode;
    }

    public String getMemberSignature() {
        return memberSignature;
    }

    public void setMemberSignature(String memberSignature) {
        this.memberSignature = memberSignature;
    }

    public String getMemberBio() {
        return memberBio;
    }

    public void setMemberBio(String memberBio) {
        this.memberBio = memberBio;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", trueName='" + trueName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", payPwd='" + payPwd + '\'' +
                ", memberSex=" + memberSex +
                ", birthday=" + birthday +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", emailIsBind=" + emailIsBind +
                ", mobile='" + mobile + '\'' +
                ", mobileAreaCode='" + mobileAreaCode + '\'' +
                ", mobileIsBind=" + mobileIsBind +
                ", registerTime=" + registerTime +
                ", loginNum=" + loginNum +
                ", loginTime=" + loginTime +
                ", lastLoginTime=" + lastLoginTime +
                ", loginIp='" + loginIp + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", memberPoints=" + memberPoints +
                ", predepositAvailable=" + predepositAvailable +
                ", predepositFreeze=" + predepositFreeze +
                ", addressProvinceId=" + addressProvinceId +
                ", addressCityId=" + addressCityId +
                ", addressAreaId=" + addressAreaId +
                ", addressAreaInfo='" + addressAreaInfo + '\'' +
                ", experiencePoints=" + experiencePoints +
                ", allowBuy=" + allowBuy +
                ", allowTalk=" + allowTalk +
                ", state=" + state +
                ", modifyNum=" + modifyNum +
                ", weixinUnionid='" + weixinUnionid + '\'' +
                ", weixinUserInfo='" + weixinUserInfo + '\'' +
                ", weixinMpOpenid='" + weixinMpOpenid + '\'' +
                ", qqOpenid='" + qqOpenid + '\'' +
                ", qqUserInfo='" + qqUserInfo + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", emailEncrypt='" + emailEncrypt + '\'' +
                ", mobileEncrypt='" + mobileEncrypt + '\'' +
                ", securityLevel=" + securityLevel +
                ", payPwdIsExist=" + payPwdIsExist +
                ", isDistributor=" + isDistributor +
                ", qqIsBind=" + qqIsBind +
                ", weixinIsBind=" + weixinIsBind +
                ", memberSexText='" + memberSexText + '\'' +
                ", idCard='" + idCard + '\'' +
                ", memberSignature='" + memberSignature + '\'' +
                ", memberBio='" + memberBio + '\'' +
                '}';
    }
}

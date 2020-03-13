package com.ftofs.twant.vo.member;

import com.ftofs.twant.domain.member.MemberMessage;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 会员
 * 
 * @author zxy
 * Created 2017/4/13 11:01
 */
public class MemberVo {

    public boolean getFromInterface;
    /**
     * 会员自增编码
     */
    private int memberId = 0;
    /**
     * 会员名称
     */
    private String memberName = "";
    /**
     * 会员真实姓名
     */
    private String trueName = "";
    /**
     * 城友暱稱 Modify By yangjian 2018/9/19 18:02 description 新增城友暱稱
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

    private String birthday = "";
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
     * 是否已进行手机绑定 0未绑定 1已绑定
     */
    private int mobileIsBind = 0;
    /**
     * 注册时间
     */

    private String registerTime = "";
    /**
     * 登录次数
     */
    private int loginNum = 0;
    /**
     * 登录时间
     */

    private String loginTime = "";
    /**
     * 上次登录时间
     */

    private String lastLoginTime = "";
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
     * 真實值
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
     * bycj店铺券可用数量
     */
    private long voucherNum = 0 ;

    /**
     * bycj 平台券可用数量
     */
    private long couponNum = 0 ;

    /**
     * bycj -- 是否是分销商
     */
    private int isDistributor = 0 ;

    /**
     * 当前等级
     */
    private HashMap<String, Object> currGrade = new HashMap<String, Object>();

    /**
     * Modify By yangjian 2018/9/7 19:46 description 0商店入駐 1進入商店
     * 顯示商店入駐，進入商店
     */
    private int isStoreSuccess = 0;

    /**
     * Modify By yangjian 2018/11/26 16:58 description true 隱身狀態  false 不隱身
     * 購物是否隱身
     */

    private Boolean shoppingHideStatus;
    //Modify By yangjian 2019/1/18 15:47 description 订单数据加入模板
    /**
     * 未讀消息數量
     */
    private long messageUnreadCount;
    /**
     * 最新未讀消息前6條(未讀狀態0)
     */
    private List<MemberMessage> messageTopSix;
    /**
     * 待付款数量
     */
    private long ordersStateNewCount;
    /**
     * 待收货数量
     */
    private long ordersStateSendCount;
    /**
     * 待评价数量
     */
    private long ordersStateEvaluationCount;
    /**
     * 待发货数量
     */
    private long ordersStatePayCount;
    /**
     * 待退款 待退货
     */
    private long ordersStateRefundCount;
    /**
     * 手機區號
     * Modify By yangjian 2019/1/19 21:55
     */
    private String mobileAreaCode = "";
    /**
     * 是否關注 1關注 0未關注
     */
    private int follow = 0;

    /**
     * 城友個性簽名
     */
    private String memberSignature;
    /**
     * 城友簡介
     */
    private String memberBio;

    /**
     * 地區名稱
     */
    private String mobileAreaName;
    //  按照會員信息查詢接口新增memberVo類字段20/3/6
    public int role;//角色 0會員 1客服 2店主
    public String staffName;//客服名稱
    public String storeAvatar;
    public String storeName;

    public int getStoreId() {
        return storeId;
    }

    private int storeId;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmailEncrypt() {
        return emailEncrypt;
    }

    public void setEmailEncrypt(String emailEncrypt) {
        this.emailEncrypt = emailEncrypt;
    }

    public String getMobileEncrypt() {
        return mobileEncrypt;
    }

    public void setMobileEncrypt(String mobileEncrypt) {
        this.mobileEncrypt = mobileEncrypt;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public int getPayPwdIsExist() {
        return payPwdIsExist;
    }

    public void setPayPwdIsExist(int payPwdIsExist) {
        this.payPwdIsExist = payPwdIsExist;
    }

    public long getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(long voucherNum) {
        this.voucherNum = voucherNum;
    }

    public long getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(long couponNum) {
        this.couponNum = couponNum;
    }

    public int getIsDistributor() {
        return isDistributor;
    }

    public void setIsDistributor(int isDistributor) {
        this.isDistributor = isDistributor;
    }

    public HashMap<String, Object> getCurrGrade() {
        return currGrade;
    }

    public void setCurrGrade(HashMap<String, Object> currGrade) {
        this.currGrade = currGrade;
    }

    public int getIsStoreSuccess() {
        return isStoreSuccess;
    }

    public void setIsStoreSuccess(int isStoreSuccess) {
        this.isStoreSuccess = isStoreSuccess;
    }

    public Boolean getShoppingHideStatus() {
        return shoppingHideStatus;
    }

    public void setShoppingHideStatus(Boolean shoppingHideStatus) {
        this.shoppingHideStatus = shoppingHideStatus;
    }

    public long getMessageUnreadCount() {
        return messageUnreadCount;
    }

    public void setMessageUnreadCount(long messageUnreadCount) {
        this.messageUnreadCount = messageUnreadCount;
    }

    public List<MemberMessage> getMessageTopSix() {
        return messageTopSix;
    }

    public void setMessageTopSix(List<MemberMessage> messageTopSix) {
        this.messageTopSix = messageTopSix;
    }

    public long getOrdersStateNewCount() {
        return ordersStateNewCount;
    }

    public void setOrdersStateNewCount(long ordersStateNewCount) {
        this.ordersStateNewCount = ordersStateNewCount;
    }

    public long getOrdersStateSendCount() {
        return ordersStateSendCount;
    }

    public void setOrdersStateSendCount(long ordersStateSendCount) {
        this.ordersStateSendCount = ordersStateSendCount;
    }

    public long getOrdersStateEvaluationCount() {
        return ordersStateEvaluationCount;
    }

    public void setOrdersStateEvaluationCount(long ordersStateEvaluationCount) {
        this.ordersStateEvaluationCount = ordersStateEvaluationCount;
    }

    public long getOrdersStatePayCount() {
        return ordersStatePayCount;
    }

    public void setOrdersStatePayCount(long ordersStatePayCount) {
        this.ordersStatePayCount = ordersStatePayCount;
    }

    public long getOrdersStateRefundCount() {
        return ordersStateRefundCount;
    }

    public void setOrdersStateRefundCount(long ordersStateRefundCount) {
        this.ordersStateRefundCount = ordersStateRefundCount;
    }

    public String getMobileAreaCode() {
        return mobileAreaCode;
    }

    public void setMobileAreaCode(String mobileAreaCode) {
        this.mobileAreaCode = mobileAreaCode;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
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

    public String getMobileAreaName() {
        return mobileAreaName;
    }

    public void setMobileAreaName(String mobileAreaName) {
        this.mobileAreaName = mobileAreaName;
    }

    @Override
    public String toString() {
        return "MemberVo{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", storeName='" + storeName + '\'' +
                ", staffName" + staffName +'\'' +
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
                ", qqOpenid='" + qqOpenid + '\'' +
                ", qqUserInfo='" + qqUserInfo + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", emailEncrypt='" + emailEncrypt + '\'' +
                ", mobileEncrypt='" + mobileEncrypt + '\'' +
                ", securityLevel=" + securityLevel +
                ", payPwdIsExist=" + payPwdIsExist +
                ", voucherNum=" + voucherNum +
                ", couponNum=" + couponNum +
                ", isDistributor=" + isDistributor +
                ", currGrade=" + currGrade +
                ", isStoreSuccess=" + isStoreSuccess +
                ", shoppingHideStatus=" + shoppingHideStatus +
                ", messageUnreadCount=" + messageUnreadCount +
                ", messageTopSix=" + messageTopSix +
                ", ordersStateNewCount=" + ordersStateNewCount +
                ", ordersStateSendCount=" + ordersStateSendCount +
                ", ordersStateEvaluationCount=" + ordersStateEvaluationCount +
                ", ordersStatePayCount=" + ordersStatePayCount +
                ", ordersStateRefundCount=" + ordersStateRefundCount +
                ", mobileAreaCode='" + mobileAreaCode + '\'' +
                ", follow=" + follow +
                ", memberSignature='" + memberSignature + '\'' +
                ", memberBio='" + memberBio + '\'' +
                '}';
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
}

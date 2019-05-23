package com.ftofs.twant.vo.promotion;

import com.ftofs.twant.domain.member.Member;
import com.ftofs.twant.domain.promotion.PointsOrders;



/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 积分订单
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:20
 */
public class PointsOrdersVo {
    /**
     * 会员自增编码
     */
    private int memberId;
    /**
     * 会员名称
     */
    private String memberName = "";
    /**
     * 头像
     */
    private String avatar = "";
    /**
     * 头像路径
     */
    private String avatarUrl = "";
    /**
     * 下单时间
     */

    private String createTime;
    /**
     * 购买数量
     */
    private int buyNum = 0;

    public PointsOrdersVo(PointsOrders pointsOrders, Member member) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.avatar = member.getAvatar();
        this.avatarUrl = member.getAvatarUrl();
        this.createTime = pointsOrders.getCreateTime();
        this.buyNum = pointsOrders.getBuyNum();
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName.substring(0, 3) + "***";
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    @Override
    public String toString() {
        return "PointsOrdersVo{" +
                "memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", createTime=" + createTime +
                ", buyNum=" + buyNum +
                '}';
    }
}

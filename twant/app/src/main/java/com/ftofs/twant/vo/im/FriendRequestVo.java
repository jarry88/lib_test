package com.ftofs.twant.vo.im;

import com.ftofs.twant.domain.im.FriendRequest;
import com.ftofs.twant.vo.member.MemberVo;

/**
 * @author liusf
 * @create 2019/2/22 19:17
 * @description 好友申請記錄視圖類
 */
public class FriendRequestVo extends FriendRequest {

    /**
     * 申請發起方會員信息
     */
    private MemberVo memberInfo;

    public MemberVo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberVo memberInfo) {
        this.memberInfo = memberInfo;
    }
}

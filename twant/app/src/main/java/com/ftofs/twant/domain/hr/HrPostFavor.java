package com.ftofs.twant.domain.hr;

import java.io.Serializable;

public class HrPostFavor implements Serializable {

    /**
     * 想要帖id
     */
    private int postId;

    /**
     * 城友名稱
     */
    private  String memberName;

    /**
     * 關注時間
     */
    private String createTime;

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "HrPostFavor{" +
                "postId=" + postId +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

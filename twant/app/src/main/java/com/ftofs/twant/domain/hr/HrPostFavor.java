package com.ftofs.twant.domain.hr;

import java.io.Serializable;
import java.sql.Timestamp;

public class HrPostFavor implements Serializable {

    /**
     * 文章id
     */
    private int postId;

    /**
     * 會員名稱
     */
    private  String memberName;

    /**
     * 關注時間
     */
    private Timestamp createTime;

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
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

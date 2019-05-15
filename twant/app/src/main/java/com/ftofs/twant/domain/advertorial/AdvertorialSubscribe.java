package com.ftofs.twant.domain.advertorial;

import java.io.Serializable;
import java.sql.Timestamp;

public class AdvertorialSubscribe implements Serializable {
    /**
     * id
     */
    private int subscribeId;

    /**
     * 会员id
     */
    private int memberId;

    /**
     * 作者id
     */
    private int authorId;

    /**
     * 作者昵称
     */
    private String authorName ;

    /**
     * 添加时间用于排序
     */
    private Timestamp addTime;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
    }

    @Override
    public String toString() {
        return "AdvertorialSubscribe{" +
                "subscribeId=" + subscribeId +
                ", memberId=" + memberId +
                ", authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}

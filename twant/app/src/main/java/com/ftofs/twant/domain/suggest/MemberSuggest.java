package com.ftofs.twant.domain.suggest;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MemberSuggest implements Serializable {
    /**
     * 建議id
     */
    private int suggestId;

    /**
     * 反饋建議
     */
    private String suggestContent;

    /**
     * 反饋會員名稱
     */
    private String memberName;

    /**
     * 反馈时间
     */
    private Timestamp createTime;

    /**
     * 反饋圖片
     */
    private List<MemberSuggestImage> imageList = new ArrayList<>(3);

    /**
     * 反饋回復
     */
    private List<MemberSuggestReply> replyList = new ArrayList<>(2);

    /**
     * 暱稱
     */
    private String nickName;

    /**
     * 頭像
     */
    private String avatarUrl;

    /**
     * 手機號
     */
    private String mobile;

    public int getSuggestId() {
        return suggestId;
    }

    public void setSuggestId(int suggestId) {
        this.suggestId = suggestId;
    }

    public String getSuggestContent() {
        return suggestContent;
    }

    public void setSuggestContent(String suggestContent) {
        this.suggestContent = suggestContent;
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

    public List<MemberSuggestImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<MemberSuggestImage> imageList) {
        this.imageList = imageList;
    }

    public List<MemberSuggestReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<MemberSuggestReply> replyList) {
        this.replyList = replyList;
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

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "MemberSuggest{" +
                "suggestId=" + suggestId +
                ", suggestContent='" + suggestContent + '\'' +
                ", memberName='" + memberName + '\'' +
                ", createTime=" + createTime +
                ", imageList=" + imageList +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}

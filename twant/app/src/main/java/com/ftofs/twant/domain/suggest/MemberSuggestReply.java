package com.ftofs.twant.domain.suggest;



public class MemberSuggestReply {
    /**
     * 回復id
     */
    private int replyId;

    /**
     * 回復的反饋id
     */
    private int suggestId;

    /**
     * 回復內容
     */
    private String replyContent;

    /**
     * 回復時間
     */
    private String createTime;

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getSuggestId() {
        return suggestId;
    }

    public void setSuggestId(int suggestId) {
        this.suggestId = suggestId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MemberSuggestReply{" +
                "replyId=" + replyId +
                ", suggestId=" + suggestId +
                ", replyContent='" + replyContent + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

package com.ftofs.twant.vo.comment;

/**
 * @Description: 回復的評論
 * @Auther: yangjian
 * @Date: 2019/2/15 11:26
 */
public class WantCommentReplyVo {
    /**
     * 被回復的暱稱
     */
    private String nickName;
    /**
     * 被評論類容
     */
    private String content;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "WantCommentReplyVo{" +
                "nickName='" + nickName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

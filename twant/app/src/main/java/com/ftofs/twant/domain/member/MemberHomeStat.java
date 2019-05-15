package com.ftofs.twant.domain.member;

public class MemberHomeStat {
    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 人气 （专页访问量）
     */
    private int popularity;

    /**
     * 粉丝
     */
    private int follow;

    /**
     * 文章数量
     */
    private int post;

    /**
     * 背景圖
     */
    private String bgImage;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    @Override
    public String toString() {
        return "MemberHomeStat{" +
                "memberName='" + memberName + '\'' +
                ", popularity='" + popularity + '\'' +
                ", follow='" + follow + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
}

package com.ftofs.twant.domain.chain;

public class ChainEvaluate {
    /**
     * 评价编号
     */
    private int evaluateId;

    /**
     * 会员编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 会员名称
     */
    private String memberNameFinal;

    /**
     * 会员头像
     */
    private String memberAvatar;

    /**
     * 会员头像路径
     */
    private String memberAvatarUrl;

    /**
     * 门店编号
     */
    private int chainId;

    /**
     * 门店名称
     */
    private String chainName;

    /**
     * 门店头像 imageSrc2
     */
    private String chainAvatar="";

    /**
     * 门店头像路径 imageSrc2
     */
    private String chainAvatarSrc = "";

    /**
     * 订单编号
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 评分
     */
    private int scores;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价时间
     */
    private String addTime;

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
    }

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

    public String getMemberNameFinal() {
        return memberNameFinal;
    }

    public void setMemberNameFinal(String memberNameFinal) {
        this.memberNameFinal = memberNameFinal;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public int getChainId() {
        return chainId;
    }

    public void setChainId(int chainId) {
        this.chainId = chainId;
    }

    public String getChainName() {
        return chainName;
    }

    public void setChainName(String chainName) {
        this.chainName = chainName;
    }

    public String getChainAvatar() {
        return chainAvatar;
    }

    public void setChainAvatar(String chainAvatar) {
        this.chainAvatar = chainAvatar;
    }

    public String getChainAvatarSrc() {
        return chainAvatarSrc;
    }

    public void setChainAvatarSrc(String chainAvatarSrc) {
        this.chainAvatarSrc = chainAvatarSrc;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "ChainEvaluate{" +
                "evaluateId=" + evaluateId +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", memberNameFinal='" + memberNameFinal + '\'' +
                ", memberAvatar='" + memberAvatar + '\'' +
                ", memberAvatarUrl='" + memberAvatarUrl + '\'' +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", chainAvatar='" + chainAvatar + '\'' +
                ", chainAvatarSrc='" + chainAvatarSrc + '\'' +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", scores=" + scores +
                ", content='" + content + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}

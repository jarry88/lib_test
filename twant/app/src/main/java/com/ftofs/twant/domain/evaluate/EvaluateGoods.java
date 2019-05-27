package com.ftofs.twant.domain.evaluate;

public class EvaluateGoods {
    /**
     * 评价编号
     */
    private int evaluateId;

    /**
     * 订单编码
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 订单类型 0-普通订单 1-虚拟订单
     */
    private int ordersType;

    /**
     * 商品ID
     */
    private int goodsId;

    /**
     * 商品SPU ID
     */
    private Integer commonId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 完整规格
     * 例“颜色：红色，尺码：L”
     */
    private String goodsFullSpecs;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 评价人编号
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 评分1-5
     */
    private int scores;

    /**
     * 评价次数，用于判断是否可以继续追加评价
     */
    private int evaluateNum;

    /**
     * 评价时是否含有图片0-否 1-是
     */
    private Integer hasImage = 0;

    /**
     * 评价时间
     */
    private String evaluateTime;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 解释内容
     */
    private String explainContent;

    /**
     * 晒单图片
     */
    private String images;

    /**
     * 追评时间
     */
    private String evaluateTimeAgain;

    /**
     * 追加评价内容
     */
    private String contentAgain;

    /**
     * 追加评价解释内容
     */
    private String explainContentAgain;

    /**
     * 追加评价图片
     */
    private String imagesAgain;

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
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

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getCommonId() {
        return commonId;
    }

    public void setCommonId(Integer commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getEvaluateNum() {
        return evaluateNum;
    }

    public void setEvaluateNum(int evaluateNum) {
        this.evaluateNum = evaluateNum;
    }

    public Integer getHasImage() {
        return hasImage;
    }

    public void setHasImage(Integer hasImage) {
        this.hasImage = hasImage;
    }

    public String getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(String evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExplainContent() {
        return explainContent;
    }

    public void setExplainContent(String explainContent) {
        this.explainContent = explainContent;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getEvaluateTimeAgain() {
        return evaluateTimeAgain;
    }

    public void setEvaluateTimeAgain(String evaluateTimeAgain) {
        this.evaluateTimeAgain = evaluateTimeAgain;
    }

    public String getContentAgain() {
        return contentAgain;
    }

    public void setContentAgain(String contentAgain) {
        this.contentAgain = contentAgain;
    }

    public String getExplainContentAgain() {
        return explainContentAgain;
    }

    public void setExplainContentAgain(String explainContentAgain) {
        this.explainContentAgain = explainContentAgain;
    }

    public String getImagesAgain() {
        return imagesAgain;
    }

    public void setImagesAgain(String imagesAgain) {
        this.imagesAgain = imagesAgain;
    }

    @Override
    public String toString() {
        return "EvaluateGoods{" +
                "evaluateId=" + evaluateId +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersType=" + ordersType +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", scores=" + scores +
                ", evaluateNum=" + evaluateNum +
                ", hasImage=" + hasImage +
                ", evaluateTime=" + evaluateTime +
                ", content='" + content + '\'' +
                ", explainContent='" + explainContent + '\'' +
                ", images='" + images + '\'' +
                ", evaluateTimeAgain=" + evaluateTimeAgain +
                ", contentAgain='" + contentAgain + '\'' +
                ", explainContentAgain='" + explainContentAgain + '\'' +
                ", imagesAgain='" + imagesAgain + '\'' +
                '}';
    }
}


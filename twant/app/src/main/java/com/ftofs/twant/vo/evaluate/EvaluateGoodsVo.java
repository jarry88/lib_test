package com.ftofs.twant.vo.evaluate;

import com.ftofs.twant.domain.evaluate.EvaluateGoods;
import com.ftofs.twant.domain.member.Member;

import java.sql.Timestamp;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 评价Vo
 *
 * @author dqw
 * Created 2017/10/19 17:41
 */
public class EvaluateGoodsVo {
    /**
     * 评价编号
     */
    private int evaluateId;
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
    private String goodsImageUrl;
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
    private String memberNameHidden;
    /**
     * 会员头像
     */
    private String memberAvatarUrl;
    /**
     * 评分1-5
     */
    private int scores;
    /**
     * 评价时间
     */

    private Timestamp evaluateTime;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 晒单图片
     */
    private List<String> imagesUrlList;
    /**
     * 评价解释
     */
    private String explainContent;
    /**
     * 追评时间
     */

    private Timestamp evaluateTimeAgain;
    /**
     * 追评内容
     */
    private String contentAgain;
    /**
     * 追评解释
     */
    private String explainContentAgain;
    /**
     * 追评图片
     */
    private List<String> imagesAgainUrlList;
    /**
     * 追评天数文字
     */
    private String days;

    public EvaluateGoodsVo() {
    }

    public EvaluateGoodsVo(EvaluateGoods evaluateGoods, Member member) {
        this.evaluateId = evaluateGoods.getEvaluateId();
        this.commonId = evaluateGoods.getCommonId();
        this.goodsName = evaluateGoods.getGoodsName();
        this.goodsFullSpecs = evaluateGoods.getGoodsFullSpecs();
        this.goodsImageUrl = evaluateGoods.getGoodsImageUrl();
        this.storeId = evaluateGoods.getStoreId();
        this.storeName = evaluateGoods.getStoreName();
        this.memberId = evaluateGoods.getMemberId();
        this.memberNameHidden = evaluateGoods.getMemberName().substring(0,2) + "******";
        this.scores = evaluateGoods.getScores();
        this.evaluateTime = evaluateGoods.getEvaluateTime();
        this.content = evaluateGoods.getContent();
        this.explainContent = evaluateGoods.getExplainContent();
        this.imagesUrlList = evaluateGoods.getImagesUrlList();
        this.evaluateTimeAgain = evaluateGoods.getEvaluateTimeAgain();
        this.contentAgain = evaluateGoods.getContentAgain();
        this.explainContentAgain = evaluateGoods.getExplainContentAgain();
        this.imagesAgainUrlList = evaluateGoods.getImagesAgainUrlList();
        this.days = evaluateGoods.getDays();
        this.memberAvatarUrl = member.getAvatarUrl();
    }

    public int getEvaluateId() {
        return evaluateId;
    }

    public void setEvaluateId(int evaluateId) {
        this.evaluateId = evaluateId;
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

    public String getGoodsImageUrl() {
        return goodsImageUrl;
    }

    public void setGoodsImageUrl(String goodsImageUrl) {
        this.goodsImageUrl = goodsImageUrl;
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

    public String getMemberNameHidden() {
        return memberNameHidden;
    }

    public void setMemberNameHidden(String memberNameHidden) {
        this.memberNameHidden = memberNameHidden;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public Timestamp getEvaluateTime() {
        return evaluateTime;
    }

    public void setEvaluateTime(Timestamp evaluateTime) {
        this.evaluateTime = evaluateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImagesUrlList() {
        return imagesUrlList;
    }

    public void setImagesUrlList(List<String> imagesUrlList) {
        this.imagesUrlList = imagesUrlList;
    }

    public String getExplainContent() {
        return explainContent;
    }

    public void setExplainContent(String explainContent) {
        this.explainContent = explainContent;
    }

    public Timestamp getEvaluateTimeAgain() {
        return evaluateTimeAgain;
    }

    public void setEvaluateTimeAgain(Timestamp evaluateTimeAgain) {
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

    public List<String> getImagesAgainUrlList() {
        return imagesAgainUrlList;
    }

    public void setImagesAgainUrlList(List<String> imagesAgainUrlList) {
        this.imagesAgainUrlList = imagesAgainUrlList;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "EvaluateGoodsVo{" +
                "evaluateId=" + evaluateId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", goodsImageUrl='" + goodsImageUrl + '\'' +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberNameHidden='" + memberNameHidden + '\'' +
                ", memberAvatarUrl='" + memberAvatarUrl + '\'' +
                ", scores=" + scores +
                ", evaluateTime=" + evaluateTime +
                ", content='" + content + '\'' +
                ", imagesUrlList=" + imagesUrlList +
                ", explainContent='" + explainContent + '\'' +
                ", evaluateTimeAgain=" + evaluateTimeAgain +
                ", contentAgain='" + contentAgain + '\'' +
                ", explainContentAgain='" + explainContentAgain + '\'' +
                ", imagesAgainUrlList=" + imagesAgainUrlList +
                ", days='" + days + '\'' +
                '}';
    }
}


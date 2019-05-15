package com.ftofs.twant.domain.complain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Complain {
    /**
     * 主键
     */
    private int complainId;

    /**
     * 订单Id
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private String ordersSn;

    /**
     * 订单商品表主键
     */
    private int ordersGoodsId;

    /**
     * sku
     */
    private int goodsId;

    /**
     * spu
     */
    private int commonId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品图片
     */
    private String goodsImage;

    /**
     * 完整规格
     */
    private String goodsFullSpecs;

    /**
     * 原告 id
     */
    private int accuserId;

    /**
     * 原告(买家用户名)
     */
    private String accuserName;

    /**
     * 被告 seller id
     */
    private int accusedSellerId = 0;

    /**
     * 被告 店铺 id
     */
    private int accusedId;

    /**
     * 被告(商家店铺名)
     */
    private String accusedName;

    /**
     * 投诉主题(id)
     */
    private int subjectId;

    /**
     * 投诉主题
     */
    private String subjectTitle;

    /**
     * 投诉内容
     */
    private String accuserContent;

    /**
     * 投诉图片
     */
    private String accuserImages;

    /**
     * 投诉时间
     */
    private Timestamp accuserTime;

    /**
     * 管理员审核投诉时间
     */
    private Timestamp adminCheckTime;

    /**
     * 审核管理员名称
     */
    private String adminCheckName;

    /**
     * 管理员审核意见(只有当不通过关闭时，才会有内容)
     */
    private String adminCheckContent;

    /**
     * 申诉时间
     */
    private Timestamp accusedTime;

    /**
     * 申诉内容
     */
    private String accusedContent;

    /**
     * 申诉图片
     */
    private String accusedImages;

    /**
     * 管理员最终仲裁时间
     */
    private Timestamp adminConfirmTime;

    /**
     * 管理员最终仲裁意见
     */
    private String adminConfirmContent;

    /**
     * 管理员最终仲操作管理员名
     */
    private String adminConfirmName;

    /**
     * 状态
     */
    private int complainState;

    /**
     * 状态
     */
    private String complainStateName;

    /**
     * 会员上传的凭证图片列表
     */
    private List<String> accuserImagesList = new ArrayList<>();

    /**
     * 商家上传的凭证图片列表
     */
    private List<String> accusedImagesList = new ArrayList<>();

    /**
     * 会员是否可以取消投诉
     */
    private int showMemberClose;

    /**
     * 买家是否可以传投诉凭证
     */
    private int showAccuserUploadImages;

    /**
     * 管理员可否可以进行处理
     */
    private int showAdminHandle;

    /**
     * 商家是否可以处理
     */
    private int showStoreHandle;

    /**
     * 是否已经取消
     */
    private int isComplainStateCancel;

    /**
     * 是否是新投诉
     */
    private int isComplainStateNew;

    /**
     * 是否被管理员审核通过
     */
    private int isComplainStateAccess;

    /**
     * 是否对话中
     */
    private int isComplainStateTalk;

    /**
     * 是否提交了仲裁
     */
    private int isComplainStateStopTalk;

    /**
     * 是否已经完成
     */
    private int isComplainStateFinish;

    /**
     * 是否可以发送对话(买卖双方)
     */
    private int showTalkAdd;

    /**
     * 是否显示对话记录(买卖双方)
     */
    private int showTalkList;

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public String getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(String ordersSn) {
        this.ordersSn = ordersSn;
    }

    public int getOrdersGoodsId() {
        return ordersGoodsId;
    }

    public void setOrdersGoodsId(int ordersGoodsId) {
        this.ordersGoodsId = ordersGoodsId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsFullSpecs() {
        return goodsFullSpecs;
    }

    public void setGoodsFullSpecs(String goodsFullSpecs) {
        this.goodsFullSpecs = goodsFullSpecs;
    }

    public int getAccuserId() {
        return accuserId;
    }

    public void setAccuserId(int accuserId) {
        this.accuserId = accuserId;
    }

    public String getAccuserName() {
        return accuserName;
    }

    public void setAccuserName(String accuserName) {
        this.accuserName = accuserName;
    }

    public int getAccusedId() {
        return accusedId;
    }

    public void setAccusedId(int accusedId) {
        this.accusedId = accusedId;
    }

    public String getAccusedName() {
        return accusedName;
    }

    public void setAccusedName(String accusedName) {
        this.accusedName = accusedName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public String getAccuserContent() {
        return accuserContent;
    }

    public void setAccuserContent(String accuserContent) {
        this.accuserContent = accuserContent;
    }

    public String getAccuserImages() {
        return accuserImages;
    }

    public void setAccuserImagesList(List<String> accuserImagesList) {
        this.accuserImagesList = accuserImagesList;
    }

    public List<String> getAccuserImagesList() {
        return accuserImagesList;
    }

    public List<String> getAccusedImagesList() {
        return accuserImagesList;
    }

    public void setAccusedImagesList(List<String> accusedImagesList) {
        this.accusedImagesList = accusedImagesList;
    }

    public void setAccuserImages(String accuserImages) {
        this.accuserImages = accuserImages;
    }

    public Timestamp getAccuserTime() {
        return accuserTime;
    }

    public void setAccuserTime(Timestamp accuserTime) {
        this.accuserTime = accuserTime;
    }

    public Timestamp getAdminCheckTime() {
        return adminCheckTime;
    }

    public void setAdminCheckTime(Timestamp adminCheckTime) {
        this.adminCheckTime = adminCheckTime;
    }

    public String getAdminCheckName() {
        return adminCheckName;
    }

    public void setAdminCheckName(String adminCheckName) {
        this.adminCheckName = adminCheckName;
    }

    public String getAdminCheckContent() {
        return adminCheckContent;
    }

    public void setAdminCheckContent(String adminCheckContent) {
        this.adminCheckContent = adminCheckContent;
    }

    public Timestamp getAccusedTime() {
        return accusedTime;
    }

    public void setAccusedTime(Timestamp accusedTime) {
        this.accusedTime = accusedTime;
    }

    public String getAccusedContent() {
        return accusedContent;
    }

    public void setAccusedContent(String accusedContent) {
        this.accusedContent = accusedContent;
    }

    public String getAccusedImages() {
        return accusedImages;
    }

    public void setAccusedImages(String accusedImages) {
        this.accusedImages = accusedImages;
    }

    public Timestamp getAdminConfirmTime() {
        return adminConfirmTime;
    }

    public void setAdminConfirmTime(Timestamp adminConfirmTime) {
        this.adminConfirmTime = adminConfirmTime;
    }

    public String getAdminConfirmContent() {
        return adminConfirmContent;
    }

    public void setAdminConfirmContent(String adminConfirmContent) {
        this.adminConfirmContent = adminConfirmContent;
    }

    public String getAdminConfirmName() {
        return adminConfirmName;
    }

    public void setAdminConfirmName(String adminConfirmName) {
        this.adminConfirmName = adminConfirmName;
    }

    public int getComplainState() {
        return complainState;
    }

    public void setComplainState(int complainState) {
        this.complainState = complainState;
    }

    public int getShowMemberClose() {
        return complainState;
    }

    public void setShowMemberClose(int showMemberClose) {
        this.showMemberClose = showMemberClose;
    }

    public int getShowAdminHandle() {
        return complainState;
    }

    public void setShowAdminHandle(int showAdminHandle) {
        this.showAdminHandle = showAdminHandle;
    }

    public void setComplainStateName(String complainStateName) {
        this.complainStateName = complainStateName;
    }

    public int getShowAccuserUploadImages() {
        return complainState;
    }

    public void setShowAccuserUploadImages(int showAccuserUploadImages) {
        this.showAccuserUploadImages = showAccuserUploadImages;
    }

    public int getIsComplainStateCancel() {
        return complainState;
    }

    public void setIsComplainStateCancel(int isComplainStateCancel) {
        this.isComplainStateCancel = isComplainStateCancel;
    }

    public int getIsComplainStateNew() {
        return complainState;
    }

    public void setIsComplainStateNew(int isComplainStateNew) {
        this.isComplainStateNew = isComplainStateNew;
    }

    public int getIsComplainStateAccess() {
        return complainState;
    }

    public void setIsComplainStateAccess(int isComplainStateAccess) {
        this.isComplainStateAccess = isComplainStateAccess;
    }

    public int getIsComplainStateTalk() {
        return complainState;
    }

    public void setIsComplainStateTalk(int isComplainStateTalk) {
        this.isComplainStateTalk = isComplainStateTalk;
    }

    public int getIsComplainStateStopTalk() {
        return complainState;
    }

    public void setIsComplainStateStopTalk(int isComplainStateStopTalk) {
        this.isComplainStateStopTalk = isComplainStateStopTalk;
    }

    public int getIsComplainStateFinish() {
        return complainState;
    }

    public void setIsComplainStateFinish(int isComplainStateFinish) {
        this.isComplainStateFinish = isComplainStateFinish;
    }

    public String getComplainStateName() {
        return complainStateName;
    }

    public String getImageSrc() {
        return goodsImage;
    }

    public int getShowStoreHandle() {
        return complainState;
    }

    public void setShowStoreHandle(int showStoreHandle) {
        this.showStoreHandle = showStoreHandle;
    }


    public int getAccusedSellerId() {
        return accusedSellerId;
    }

    public void setAccusedSellerId(int accusedSellerId) {
        this.accusedSellerId = accusedSellerId;
    }

    public int getShowTalkAdd() {
        return getIsComplainStateNew() == 0 && getIsComplainStateAccess() == 0 && getIsComplainStateFinish() == 0 && getIsComplainStateCancel() == 0 ? 1 : 0;
    }

    public void setShowTalkAdd(int showTalkAdd) {
        this.showTalkAdd = showTalkAdd;
    }

    public int getShowTalkList() {
        return complainState;
    }

    public void setShowTalkList(int showTalkList) {
        this.showTalkList = showTalkList;
    }

    @Override
    public String toString() {
        return "Complain{" +
                "complainId=" + complainId +
                ", ordersId=" + ordersId +
                ", ordersSn='" + ordersSn + '\'' +
                ", ordersGoodsId=" + ordersGoodsId +
                ", goodsId=" + goodsId +
                ", commonId=" + commonId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImage='" + goodsImage + '\'' +
                ", goodsFullSpecs='" + goodsFullSpecs + '\'' +
                ", accuserId=" + accuserId +
                ", accuserName='" + accuserName + '\'' +
                ", accusedSellerId=" + accusedSellerId +
                ", accusedId=" + accusedId +
                ", accusedName='" + accusedName + '\'' +
                ", subjectId=" + subjectId +
                ", subjectTitle='" + subjectTitle + '\'' +
                ", accuserContent='" + accuserContent + '\'' +
                ", accuserImages='" + accuserImages + '\'' +
                ", accuserTime=" + accuserTime +
                ", adminCheckTime=" + adminCheckTime +
                ", adminCheckName='" + adminCheckName + '\'' +
                ", adminCheckContent='" + adminCheckContent + '\'' +
                ", accusedTime=" + accusedTime +
                ", accusedContent='" + accusedContent + '\'' +
                ", accusedImages='" + accusedImages + '\'' +
                ", adminConfirmTime=" + adminConfirmTime +
                ", adminConfirmContent='" + adminConfirmContent + '\'' +
                ", adminConfirmName='" + adminConfirmName + '\'' +
                ", complainState=" + complainState +
                ", complainStateName='" + complainStateName + '\'' +
                ", accuserImagesList=" + accuserImagesList +
                ", accusedImagesList=" + accusedImagesList +
                ", showMemberClose=" + showMemberClose +
                ", showAccuserUploadImages=" + showAccuserUploadImages +
                ", showAdminHandle=" + showAdminHandle +
                ", showStoreHandle=" + showStoreHandle +
                ", isComplainStateCancel=" + isComplainStateCancel +
                ", isComplainStateNew=" + isComplainStateNew +
                ", isComplainStateAccess=" + isComplainStateAccess +
                ", isComplainStateTalk=" + isComplainStateTalk +
                ", isComplainStateStopTalk=" + isComplainStateStopTalk +
                ", isComplainStateFinish=" + isComplainStateFinish +
                ", showTalkAdd=" + showTalkAdd +
                ", showTalkList=" + showTalkList +
                '}';
    }
}

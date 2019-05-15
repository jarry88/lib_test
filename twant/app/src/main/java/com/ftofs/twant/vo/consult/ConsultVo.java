package com.ftofs.twant.vo.consult;

import com.ftofs.twant.domain.goods.GoodsCommon;
import com.ftofs.twant.domain.member.Consult;

import java.sql.Timestamp;

/**
 * @copyright  Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license    http://www.shopnc.net
 * @link       http://www.shopnc.net
 *
 * 商品咨询
 *
 * @author zxy
 * Created 2017/4/13 11:00
 */
public class ConsultVo {
    /**
     * 自增编码
     */
    private int consultId = 0;
    /**
     * 商品编号
     */
    private int commonId = 0;
    /**
     * 会员ID
     */
    private int memberId = 0;
    /**
     * 会员名称
     */
    private String memberName = "";
    /**
     * 店铺ID
     */
    private int storeId = 0;
    /**
     * 店铺名称
     */
    private String storeName = "";
    /**
     * 分类ID
     */
    private int classId = 0;
    /**
     * 分类名称
     */
    private String className = "";
    /**
     * 咨询内容
     */
    private String consultContent = "";
    /**
     * 添加时间
     */

    private Timestamp addTime = new Timestamp(0);
    /**
     * 卖家是否已回复
     * 0未回复 1已回复
     */
    private int consultReplyState = 0;
    /**
     * 回复内容
     */
    private String consultReply = "";
    /**
     * 回复时间
     */

    private Timestamp replyTime = new Timestamp(0);
    /**
     * 匿名状态
     */
    private int anonymousState = 0;
    /**
     * 会员已读回复
     * 0未读 1已读
     */
    private int readState = 0;
    /**
     * 商品SPU
     */
    private GoodsCommon goodsCommon = null;

    public ConsultVo() {}
    public ConsultVo(Consult consult, GoodsCommon gc) {
        this.consultId = consult.getConsultId();
        this.commonId = consult.getCommonId();
        this.memberId = consult.getMemberId();
        this.memberName = consult.getMemberName();
        this.storeId = consult.getStoreId();
        this.storeName = consult.getStoreName();
        this.classId = consult.getClassId();
        this.className = consult.getClassName();
        this.consultContent = consult.getConsultContent();
        this.addTime = consult.getAddTime();
        this.consultReplyState = consult.getConsultReplyState();
        this.consultReply = consult.getConsultReply();
        this.replyTime = consult.getReplyTime();
        this.readState = consult.getReadState();
        this.goodsCommon = gc;
    }

    public int getConsultId() {
        return consultId;
    }

    public void setConsultId(int consultId) {
        this.consultId = consultId;
    }

    public int getCommonId() {
        return commonId;
    }

    public void setCommonId(int commonId) {
        this.commonId = commonId;
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

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getConsultContent() {
        return consultContent;
    }

    public void setConsultContent(String consultContent) {
        this.consultContent = consultContent;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getConsultReplyState() {
        return consultReplyState;
    }

    public void setConsultReplyState(int consultReplyState) {
        this.consultReplyState = consultReplyState;
    }

    public String getConsultReply() {
        return consultReply;
    }

    public void setConsultReply(String consultReply) {
        this.consultReply = consultReply;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public int getAnonymousState() {
        return anonymousState;
    }

    public void setAnonymousState(int anonymousState) {
        this.anonymousState = anonymousState;
    }

    public int getReadState() {
        return readState;
    }

    public void setReadState(int readState) {
        this.readState = readState;
    }

    public GoodsCommon getGoodsCommon() {
        return goodsCommon;
    }

    public void setGoodsCommon(GoodsCommon goodsCommon) {
        this.goodsCommon = goodsCommon;
    }
}

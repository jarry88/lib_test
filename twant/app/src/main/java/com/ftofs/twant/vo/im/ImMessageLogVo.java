package com.ftofs.twant.vo.im;

import com.ftofs.twant.vo.goods.GoodsImVo;
import com.ftofs.twant.vo.orders.OrdersImVo;
import com.ftofs.twant.vo.store.StoreVo;

/**
 * @Description: 消息視圖對象
 * @Auther: yangjian
 * @Date: 2018/11/12 14:36
 */
public class ImMessageLogVo {
    /**
     * 自增编码
     */
    private int id;
    /**
     * 發送人
     */
    private String sendFrom;
    /**
     * 群成員發送方頭像
     */
    private String sendFromAcatar = "";
    /**
     * 群成員發送方暱稱
     */
    private String sendFromNickName = "";
    /**
     * 接收人（群Id/多人/單人)
     */
    private String target;

    /**
     * 目標類型(users/chatgroups/chatrooms)
     */
    private String targetType;

    /**
     * 群名
     */
    private String groupName;

    /**
     * 消息內容
     */
    private String messageContent;

    /**
     * 消息類型(txt/img/emoji/file/audio/video)
     */
    private String messageType;

    /**
     * 發送時間
     */

    private String sendTime;

    /**
     * 发送人类型
     * 1,买家 ， 2 。卖家
     */
    private int fromUserType;

    /**
     * 接收人類型
     * 1買家，2商家，3群組，4聊天室
     */
    private int toUserType;

    /**
     * 發送人店鋪ID
     */
    private int fromUserStoreId = 0;

    /**
     * 發送人店鋪名稱
     */
    private String fromUserStoreName = "";

    /**
     * 接收人店鋪ID
     */
    private int toUserStoreId = 0;

    /**
     * 接收人店鋪名稱
     */
    private String toUserStoreName = "";

    /**
     * 群店鋪信息
     */
    private StoreVo storeInfo;

    /**
     * 商品
     */
    private GoodsImVo goodsImVo;

    /**
     * 訂單
     */
    private OrdersImVo ordersImVo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getSendFromAcatar() {
        return sendFromAcatar;
    }

    public void setSendFromAcatar(String sendFromAcatar) {
        this.sendFromAcatar = sendFromAcatar;
    }

    public String getSendFromNickName() {
        return sendFromNickName;
    }

    public void setSendFromNickName(String sendFromNickName) {
        this.sendFromNickName = sendFromNickName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getFromUserType() {
        return fromUserType;
    }

    public void setFromUserType(int fromUserType) {
        this.fromUserType = fromUserType;
    }

    public int getToUserType() {
        return toUserType;
    }

    public void setToUserType(int toUserType) {
        this.toUserType = toUserType;
    }

    public int getFromUserStoreId() {
        return fromUserStoreId;
    }

    public void setFromUserStoreId(int fromUserStoreId) {
        this.fromUserStoreId = fromUserStoreId;
    }

    public String getFromUserStoreName() {
        return fromUserStoreName;
    }

    public void setFromUserStoreName(String fromUserStoreName) {
        this.fromUserStoreName = fromUserStoreName;
    }

    public int getToUserStoreId() {
        return toUserStoreId;
    }

    public void setToUserStoreId(int toUserStoreId) {
        this.toUserStoreId = toUserStoreId;
    }

    public String getToUserStoreName() {
        return toUserStoreName;
    }

    public void setToUserStoreName(String toUserStoreName) {
        this.toUserStoreName = toUserStoreName;
    }

    public StoreVo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreVo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public GoodsImVo getGoodsImVo() {
        return goodsImVo;
    }

    public void setGoodsImVo(GoodsImVo goodsImVo) {
        this.goodsImVo = goodsImVo;
    }

    public OrdersImVo getOrdersImVo() {
        return ordersImVo;
    }

    public void setOrdersImVo(OrdersImVo ordersImVo) {
        this.ordersImVo = ordersImVo;
    }
}

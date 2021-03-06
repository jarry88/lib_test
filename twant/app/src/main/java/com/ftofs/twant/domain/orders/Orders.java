package com.ftofs.twant.domain.orders;

import java.io.Serializable;
import java.math.BigDecimal;


public class Orders implements Serializable, Cloneable {
    /**
     * 订单主键ID、自增
     */
    private int ordersId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 订单状态
     * 0-已取消 10-未支付 20-已支付 30-已发货 40-已收货
     */
    private int ordersState;

    /**
     * 订单来源
     * web/app/wechat
     */
    private String ordersFrom = "";

    /**
     * 订单详细来源
     * web/ios/android/wap/wechat
     */
    private String ordersFrom1 = "";

    /**
     * 支付单ID
     */
    private int payId;

    /**
     * 支付单号
     */
    private long paySn;

    /**
     * 店铺ID
     */
    private int storeId;

    /**
     * 店铺名称
     */
    private String storeName = "";

    /**
     * 会员ID
     */
    private int memberId;

    /**
     * 会员名称
     */
    private String memberName = "";

    /**
     * 一级地区ID
     */
    private int receiverAreaId1;

    /**
     * 二级地区ID
     */
    private int receiverAreaId2;

    /**
     * 三级地区Id
     */
    private int receiverAreaId3;

    /**
     * 四级地区
     */
    private int receiverAreaId4;

    /**
     * 省市县(区)内容
     */
    private String receiverAreaInfo = "";

    /**
     * 收货人地址
     */
    private String receiverAddress = "";

    /**
     * 收货人电话
     */
    private String receiverPhone = "";

    /**
     * 收货人姓名
     */
    private String receiverName = "";

    /**
     * 买家留言
     */
    private String receiverMessage = "";

    /**
     * 下单时间
     */
    private String createTime;

    /**
     * 下单时间 具体到天
     */
    private String createTimeDate;

    /**
     * 下单时间 小时
     */
    private String createTimeHour;

    /**
     * 线上线下支付代码online/offline
     */
    private String paymentTypeCode = "";

    /**
     * 支付方式代码
     */
    private String paymentCode = "";

    /**
     * 支付使用终端(WAP,WEB,APP)
     */
    private String paymentClientType = "";

    /**
     * 外部交易号
     */
    private String outOrdersSn = "";

    /**
     * 支付时间
     */
    private String paymentTime;

    /**
     * 发货时间
     */
    private String sendTime;

    /**
     * 订单完成时间
     */
    private String finishTime;

    /**
     * 订单金额
     */
    private BigDecimal ordersAmount = new BigDecimal(0);

    /**
     * 预存款支付金额
     */
    private BigDecimal predepositAmount = new BigDecimal(0);

    /**
     * 运费
     */
    private BigDecimal freightAmount = new BigDecimal(0);

    /**
     * 评价状态
     * 0-未评价 1-已评价
     */
    private int evaluationState = 0;

    /**
     * 追加评价状态
     * 0-未评价 1-已评价
     */
    private int evaluationAppendState = 0;

    /**
     * 评价时间
     */
    private String evaluationTime;

    /**
     * 发货单号
     */
    private String shipSn = "";

    /**
     * 快递公司
     */
    private String shipName = "";

    /**
     * 快递公司编码
     */
    private String shipCode = "";

    /**
     * 发货备注
     */
    private String shipNote = "";

    /**
     * 自动收货时间
     */
    private String autoReceiveTime;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票內容
     */
    private String invoiceContent;

    /**
     * 纳税人识别号或统一社会信用代码
     */
    private String invoiceCode;

    /**
     * 订单类型
     */
    private int ordersType = 0;

    /**
     * 订单取消原因
     */
    private Integer cancelReason = 6;

    /**
     * 关闭时间
     */
    private String cancelTime;

    /**
     * 订单关闭操作主体
     */
    private String cancelRole = "";

    /**
     * 订单收取佣金
     */
    private BigDecimal commissionAmount = new BigDecimal(0);

    /**
     * bycj -- 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal(0);

    /**
     * bycj -- 退款状态:0是无退款,1是部分退款,2是全部退款
     */
    private int refundState = 0;

    /**
     *  bycj -- 订单锁定  , 发货后用于锁定订单状态，不可自动收货
     *  0:是正常 ， 1：是锁定
     */
    private int lockState = 0;

    /**
     *  bycj -- 标识商家不能发货 , 当整单退款的时候会改写此值
     *  0:正常可以发货 ， 1：退款退货中不能发货
     */
    private int cannotShip = 0;

    /**
     * 是否进行过延迟收货操作，买家只能点击一次 0-否/1-是
     */
    private int delayReceiveState = 0;

    /**
     * 是否是管理员点击的收款
     */
    private int adminReceivePayState = 0;

    /**
     * 店铺券面额
     */
    private BigDecimal voucherPrice = new BigDecimal(0);

    /**
     * 店铺券编码
     */
    private String voucherCode = "";

    /**
     * (满送减免)金额限制
     */
    private BigDecimal limitAmount = new BigDecimal(0);

    /**
     * (满送减免)减免金额
     */
    private BigDecimal conformPrice = new BigDecimal(0);

    /**
     * (满送减免)是否包邮
     */
    private int isFreeFreight = 0;

    /**
     * (满送减免)店铺券模板编号
     */
    private int templateId = 0;

    /**
     * 拼团ID
     */
    private int groupId = 0;

    /**
     * 开团ID
     */
    private int goId = 0;

    /**
     * 删除状态 0-未删除/1-已删除
     */
    private int deleteState = 0;

    /**
     * 下单时间(下单时间数组常量的下标)
     */
    private int shipTimeType;

    /**
     * 订单下的產品实际使用的满优惠和店铺券总金额
     */
    private BigDecimal storeDiscountAmount = new BigDecimal(0);

    /**
     * 订单下的產品实际使用的平台券总金额
     */
    private BigDecimal couponAmount = new BigDecimal(0);

    /**
     * 订单的產品使用的平台券总金额中平台应承担的总金额
     */
    private BigDecimal shopCommitmentAmount = BigDecimal.ZERO;

    /**
     * 预定订单尾款实际付款金额(尾款金额 - 促销)，预定產品可退最大值
     */
    private BigDecimal finalAmount = new BigDecimal(0);

    /**
     * 海外购產品税费
     */
    private BigDecimal taxAmount = new BigDecimal(0);

    /**
     * 买家身份证号
     */
    private String idCard;

    //=====Modify By Nick.Chung 2018/8/8 修改表結構，整合門店=====
    /**
     * 线下收银员名称
     */
    private String sellerName;

    //=====Modify By Nick.Chung 2018/9/13 修改表結構，線上下單自提，門店提貨=====
    /**
     * 取貨時間
     */
    private String takeTime;

    /**
     * 提貨碼
     */
    private Integer takeCode = 0;
    //===========================================================

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
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

    public int getOrdersState() {
        return ordersState;
    }

    public void setOrdersState(int ordersState) {
        this.ordersState = ordersState;
    }

    public int getGoId() {
        return goId;
    }

    public void setGoId(int goId) {
        this.goId = goId;
    }

    /**
     * 订单状态
     * @return
     */
    public String getOrdersStateName() {
        return String.valueOf(ordersState);
    }

    public String getOrdersFrom() {
        return ordersFrom;
    }

    public void setOrdersFrom(String ordersFrom) {
        this.ordersFrom = ordersFrom;
    }

    public String getOrdersFrom1() {
        return ordersFrom1;
    }

    public void setOrdersFrom1(String ordersFrom1) {
        this.ordersFrom1 = ordersFrom1;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
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

    public int getReceiverAreaId1() {
        return receiverAreaId1;
    }

    public void setReceiverAreaId1(int receiverAreaId1) {
        this.receiverAreaId1 = receiverAreaId1;
    }

    public int getReceiverAreaId2() {
        return receiverAreaId2;
    }

    public void setReceiverAreaId2(int receiverAreaId2) {
        this.receiverAreaId2 = receiverAreaId2;
    }

    public int getReceiverAreaId3() {
        return receiverAreaId3;
    }

    public void setReceiverAreaId3(int receiverAreaId3) {
        this.receiverAreaId3 = receiverAreaId3;
    }

    public int getReceiverAreaId4() {
        return receiverAreaId4;
    }

    public void setReceiverAreaId4(int receiverAreaId4) {
        this.receiverAreaId4 = receiverAreaId4;
    }

    public String getReceiverAreaInfo() {
        return receiverAreaInfo;
    }

    public void setReceiverAreaInfo(String receiverAreaInfo) {
        this.receiverAreaInfo = receiverAreaInfo;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMessage() {
        return receiverMessage;
    }

    public void setReceiverMessage(String receiverMessage) {
        this.receiverMessage = receiverMessage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getCreateTimeDate() {
        return createTimeDate;
    }

    public void setCreateTimeDate(String createTimeDate) {
        this.createTimeDate = createTimeDate;
    }

    public String getCreateTimeHour() {
        return createTimeHour;
    }

    public void setCreateTimeHour(String createTimeHour) {
        this.createTimeHour = createTimeHour;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentClientType() {
        return paymentClientType;
    }

    public void setPaymentClientType(String paymentClientType) {
        this.paymentClientType = paymentClientType;
    }


    public String getOutOrdersSn() {
        return outOrdersSn;
    }

    public void setOutOrdersSn(String outOrdersSn) {
        this.outOrdersSn = outOrdersSn;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getPredepositAmount() {
        return predepositAmount;
    }

    public void setPredepositAmount(BigDecimal predepositAmount) {
        this.predepositAmount = predepositAmount;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public int getEvaluationState() {
        return evaluationState;
    }

    public void setEvaluationState(int evaluationState) {
        this.evaluationState = evaluationState;
    }

    public int getEvaluationAppendState() {
        return evaluationAppendState;
    }

    public void setEvaluationAppendState(int evaluationAppendState) {
        this.evaluationAppendState = evaluationAppendState;
    }

    public String getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(String evaluationTime) {
        this.evaluationTime = evaluationTime;
    }

    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote(String shipNote) {
        this.shipNote = shipNote;
    }

    public String getAutoReceiveTime() {
        return autoReceiveTime;
    }

    public void setAutoReceiveTime(String autoReceiveTime) {
        this.autoReceiveTime = autoReceiveTime;
    }


    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    /**
     * @return
     */
    public String getPaymentTypeName() {
        return "";
    }

    public String getPaymentName() {
        return paymentCode;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
    }

    public Integer getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Integer cancelReason) {
        this.cancelReason = cancelReason;
    }

    /**
     * @return
     */
    public String getCancelReasonContent() {
        return cancelReason.toString();
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelRole() {
        return cancelRole;
    }

    public void setCancelRole(String cancelRole) {
        this.cancelRole = cancelRole;
    }

    public BigDecimal getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(BigDecimal commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    /**
     * 自动取消时间
     * @return
     */
    public String getAutoCancelTime() {
        return null;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getRefundState() {
        return refundState;
    }

    public void setRefundState(int refundState) {
        this.refundState = refundState;
    }

    public int getDelayReceiveState() {
        return delayReceiveState;
    }

    public void setDelayReceiveState(int delayReceiveState) {
        this.delayReceiveState = delayReceiveState;
    }

    public int getAdminReceivePayState() {
        return adminReceivePayState;
    }

    public void setAdminReceivePayState(int adminReceivePayState) {
        this.adminReceivePayState = adminReceivePayState;
    }

    public BigDecimal getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(BigDecimal voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public BigDecimal getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(BigDecimal limitAmount) {
        this.limitAmount = limitAmount;
    }

    public BigDecimal getConformPrice() {
        return conformPrice;
    }

    public void setConformPrice(BigDecimal conformPrice) {
        this.conformPrice = conformPrice;
    }

    public int getIsFreeFreight() {
        return isFreeFreight;
    }

    public void setIsFreeFreight(int isFreeFreight) {
        this.isFreeFreight = isFreeFreight;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCannotShip() {
        return cannotShip;
    }

    public void setCannotShip(int cannotShip) {
        this.cannotShip = cannotShip;
    }

    public int getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(int deleteState) {
        this.deleteState = deleteState;
    }


    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    public int getShipTimeType() {
        return shipTimeType;
    }

    public void setShipTimeType(int shipTimeType) {
        this.shipTimeType = shipTimeType;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public BigDecimal getShopCommitmentAmount() {
        return shopCommitmentAmount;
    }

    public void setShopCommitmentAmount(BigDecimal shopCommitmentAmount) {
        this.shopCommitmentAmount = shopCommitmentAmount;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getStoreDiscountAmount() {
        return storeDiscountAmount;
    }

    public void setStoreDiscountAmount(BigDecimal storeDiscountAmount) {
        this.storeDiscountAmount = storeDiscountAmount;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    
    public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

    public String getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public int getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(int takeCode) {
        this.takeCode = takeCode;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Orders{" +
                "ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", ordersFrom='" + ordersFrom + '\'' +
                ", ordersFrom1='" + ordersFrom1 + '\'' +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", receiverAreaId1=" + receiverAreaId1 +
                ", receiverAreaId2=" + receiverAreaId2 +
                ", receiverAreaId3=" + receiverAreaId3 +
                ", receiverAreaId4=" + receiverAreaId4 +
                ", receiverAreaInfo='" + receiverAreaInfo + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", createTime=" + createTime +
                ", createTimeDate='" + createTimeDate + '\'' +
                ", createTimeHour='" + createTimeHour + '\'' +
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentClientType='" + paymentClientType + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentTime=" + paymentTime +
                ", sendTime=" + sendTime +
                ", finishTime=" + finishTime +
                ", ordersAmount=" + ordersAmount +
                ", predepositAmount=" + predepositAmount +
                ", freightAmount=" + freightAmount +
                ", evaluationState=" + evaluationState +
                ", evaluationAppendState=" + evaluationAppendState +
                ", evaluationTime=" + evaluationTime +
                ", shipSn='" + shipSn + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipCode='" + shipCode + '\'' +
                ", shipNote='" + shipNote + '\'' +
                ", autoReceiveTime=" + autoReceiveTime +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", ordersType=" + ordersType +
                ", cancelReason=" + cancelReason +
                ", cancelTime=" + cancelTime +
                ", cancelRole='" + cancelRole + '\'' +
                ", commissionAmount=" + commissionAmount +
                ", refundAmount=" + refundAmount +
                ", refundState=" + refundState +
                ", lockState=" + lockState +
                ", cannotShip=" + cannotShip +
                ", delayReceiveState=" + delayReceiveState +
                ", adminReceivePayState=" + adminReceivePayState +
                ", voucherPrice=" + voucherPrice +
                ", voucherCode='" + voucherCode + '\'' +
                ", limitAmount=" + limitAmount +
                ", conformPrice=" + conformPrice +
                ", isFreeFreight=" + isFreeFreight +
                ", templateId=" + templateId +
                ", groupId=" + groupId +
                ", goId=" + goId +
                ", deleteState=" + deleteState +
                ", shipTimeType=" + shipTimeType +
                ", storeDiscountAmount=" + storeDiscountAmount +
                ", couponAmount=" + couponAmount +
                ", shopCommitmentAmount=" + shopCommitmentAmount +
                ", finalAmount=" + finalAmount +
                ", taxAmount=" + taxAmount +
                ", idCard='" + idCard + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", takeTime=" + takeTime +
                ", takeCode=" + takeCode +
                '}';
    }
}

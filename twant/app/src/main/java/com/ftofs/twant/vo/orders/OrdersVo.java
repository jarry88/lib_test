package com.ftofs.twant.vo.orders;

import com.ftofs.twant.vo.refund.RefundItemVo;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @copyright Copyright (c) 2007-2017 ShopNC Inc. All rights reserved.
 * @license http://www.shopnc.net
 * @link http://www.shopnc.net
 *
 * 订单Vo
 *
 * @author hbj
 * Created 2017/4/13 14:47
 */
public class OrdersVo {
    /**
     * 订单商品列表
     */
    private List<OrdersGoodsVo> ordersGoodsVoList = new ArrayList<>();
    /**
     * 订单主键ID
     */
    private int ordersId;
    /**
     * 订单编号
     */
    private long ordersSn;
    /**
     * 订单状态
     */
    private int ordersState;
    /**
     * 订单状态
     */
    private String ordersStateName;
    /**
     * 订单来源
     */
    private String ordersFrom;
    /**
     * 订单来源详细
     */
    private String ordersFrom1;
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
     * sellerId
     */
    private int sellerId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 店铺电话
     */
    private String storePhone;
    /**
     * 店铺地址
     */
    private String storeAddress;
    /**
     * 卖家账号
     */
    private String sellerName;
    /**
     * 会员ID
     */
    private int memberId;
    /**
     * 会员名
     */
    private String memberName;
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
    private String receiverAreaInfo;
    /**
     * 收货人地址
     */
    private String receiverAddress;
    /**
     * 收货人电话
     */
    private String receiverPhone;
    /**
     * 收货人姓名
     */
    private String receiverName;
    /**
     * 买家留言
     */
    private String receiverMessage;
    /**
     * 下单时间
     */

    private Timestamp createTime;
    /**
     * 线上线下支付代码online/offline
     */
    private String paymentTypeCode;
    /**
     * 线上线下支付中文名
     */
    private String paymentTypeName;
    /**
     * 支付方式代码
     */
    private String paymentCode;
    /**
     * 外部交易号
     */
    private String outOrdersSn;
    /**
     * 支付方式名称
     */
    private String paymentName;
    /**
     * 支付时间
     */

    private Timestamp paymentTime;
    /**
     * 发货时间
     */

    private Timestamp sendTime;
    /**
     * 订单完成时间
     */

    private Timestamp finishTime;
    /**
     * 订单金额
     */
    private BigDecimal ordersAmount;
    /**
     * 预存款支付金额
     */
    private BigDecimal predepositAmount;
    /**
     * 运费
     */
    private BigDecimal freightAmount;
    /**
     * 评价状态
     */
    private int evaluationState;
    /**
     * 追加评价状态
     */
    private int evaluationAppendState;
    /**
     * 评价时间
     */

    private Timestamp evaluationTime;
    /**
     * 发货单号
     */
    private String shipSn;
    /**
     * 快递公司
     */
    private String shipName;
    /**
     * 快递公司编码
     */
    private String shipCode;
    /**
     * 快递公司网址
     */
    private String shipUrl;
    /**
     * 发货备注
     */
    private String shipNote;
    /**
     * 自动收货时间
     */

    private Timestamp autoReceiveTime;
    /**
     * 纳税人识别号或统一社会信用代码
     */
    private String invoiceCode;
    /**
     * 订单类型
     */
    private int ordersType;
    /**
     * 订单取消原因
     */
    private Integer cancelReason;
    /**
     * 订单取消原因
     */
    private String cancelReasonContent;
    /**
     * 关闭时间
     */

    private Timestamp cancelTime;
    /**
     * 自动关闭时间
     */

    private Timestamp autoCancelTime;
    /**
     * 自动取消周期
     */
    private int autoCancelCycle;
    /**
     * 订单关闭操作主体
     */
    private String cancelRole;
    /**
     * 订单收取佣金
     */
    private BigDecimal commissionAmount = new BigDecimal(0);
    /**
     * 删除状态 0-未删除/1-已删除
     */
    private int deleteState = 0;
    /**
     * 订单列表是否显示操作
     */
    private int showMemberCancel = 0;
    private int showMemberPay = 0;
    private int showShipSearch = 0;
    private int showMemberReceive = 0;
    /**
     * 商家取消订单
     */
    private int showStoreCancel = 0;
    /**
     * 商家修改运费
     */
    private int showStoreModifyFreight = 0;
    /**
     * 商家发货
     */
    private int showStoreSend = 0;
    /**
     * 商家编辑发货信息
     */
    private int showStoreSendModify = 0;

    private int showEvaluation = 0;
    private int showEvaluationAppend = 0;
    private int showAdminCancel = 0;
    private int showAdminPay = 0;
    private int showMemberDelayReceive = 0;
    private int showMemberRefundAll = 0;

    /**
     * 是否在发起投诉的时效内 0否/1是
     */
    private int showMemberComplain = 0;
    /**
     * 是否可以删除 0否/1是
     */
    private int showMemberDelete = 0;
    /**
     * 是否可以还原被删除的订单 0否/1是
     */
    private int showMemberRecoveryDelete = 0;
    /**
     * 是否可以再次购买 0否/1是
     */
    private int showMemberBuyAgain = 0;
    /**
     * bycj [ 退款金额 ]
     */
    private BigDecimal refundAmount;

    /**
     * bycj [ 退款状态:0是无退款,1是部分退款,2是全部退款 ]
     */
    private int refundState;

    // bycj [ 是否显示退款退货中 ]
    private int showRefundWaiting = 0;

    // bycj [ 退款信息 ]
    private RefundItemVo refundItemVo;

    //bycj [im 是否在线 ]
    private int imIsOnline = 0;

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
    private BigDecimal voucherPrice;
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
     * 支付使用终端(WAP,WEB,APP)
     */
    private String paymentClientType = "";
    /**
     * 预定时段
     */
    private List<OrdersBookVo> ordersBookVoList = new ArrayList<>();
    /**
     * 后台预定订单收款时显示收款标题
     */
    private String adminReceiveBookPayTitle = "";
    /**
     * 后台预定订单收款时显示收款金额
     */
    private BigDecimal adminReceiveBookPayAmount = new BigDecimal(0);
    /**
     * 订单赠品
     */
    private List<OrdersGiftVo> ordersGiftVoList = new ArrayList<>();
    /**
     * 拼团ID
     */
    private int groupId = 0;
    /**
     * 开团ID
     */
    private int goId = 0;
    /**
     * 多人拼团是否可发货<br>
     * 0不可以1可以
     */
    private int groupCanShip = 0;
    /**
     * 成团所需人数
     */
    private int groupNeedNum = 0;
    /**
     * 成团还差人数
     */
    private int groupRemainNum = 0;

    /**
     * 开团状态
     */
    private int goState;
    /**
     * 订单类型
     */
    private String ordersTypeName = "";
    /**
     * 发票抬头
     */
    private String invoiceTitle = "";
    /**
     * 发票内容
     */
    private String invoiceContent = "";
    /**
     * 配送时间
     */
    private String shipTime;
    /**
     * 订单下的商品使用的平台券总金额
     */
    private BigDecimal couponAmount = BigDecimal.ZERO;
    /**
     * 订单的商品使用的平台券总金额中平台应承担的总金额
     */
    private BigDecimal shopCommitmentAmount = BigDecimal.ZERO;
    /**
     * 商品总金额
     */
    private BigDecimal itemAmount = BigDecimal.ZERO;
    /**
     * 预定订单尾款实际付款金额(尾款金额 - 促销)，预定商品可退最大值
     */
    private BigDecimal finalAmount = new BigDecimal(0);
    /**
     * 海外购商品税费
     */
    private BigDecimal taxAmount = new BigDecimal(0);
    /**
     * 是否海外订单
     */
    private int isForeign;
    /**
     * 订单下的商品实际使用的满优惠和店铺券总金额
     */
    private BigDecimal storeDiscountAmount = new BigDecimal(0);
    /**
     * 买家身份证号
     */
    private String idCard;
    /**
     * 是否可以申请售后
     */
    private int showMemberApplyForSale;
    /**
     * 订单锁定状态
     */
    private int lockState = 0;

    //Modify By Nick.Chung 2018/9/18 19:18 添加取貨時間
    /**
     * 取貨時間
     */
    private Timestamp takeTime;

    //Modify By liusf 2018/10/5 14:12 添加提貨碼
    /**
     * 提貨碼
     */
    private int takeCode;

    /**
     * 店鋪客服列表
     */
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    public List<OrdersGoodsVo> getOrdersGoodsVoList() {
        return ordersGoodsVoList;
    }

    public void setOrdersGoodsVoList(List<OrdersGoodsVo> ordersGoodsVoList) {
        this.ordersGoodsVoList = ordersGoodsVoList;
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

    public String getOrdersStateName() {
        return ordersStateName;
    }

    public void setOrdersStateName(String ordersStateName) {
        this.ordersStateName = ordersStateName;
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

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public String getPaymentTypeName() {
        return paymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getOutOrdersSn() {
        return outOrdersSn;
    }

    public void setOutOrdersSn(String outOrdersSn) {
        this.outOrdersSn = outOrdersSn;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Timestamp getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Timestamp paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Timestamp finishTime) {
        this.finishTime = finishTime;
    }

    public BigDecimal getOrdersAmount() {
        return ordersAmount;
    }

    public void setOrdersAmount(BigDecimal ordersAmount) {
        this.ordersAmount = ordersAmount;
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

    public Timestamp getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(Timestamp evaluationTime) {
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

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        this.shipUrl = shipUrl;
    }

    public String getShipNote() {
        return shipNote;
    }

    public void setShipNote(String shipNote) {
        this.shipNote = shipNote;
    }

    public Timestamp getAutoReceiveTime() {
        return autoReceiveTime;
    }

    public void setAutoReceiveTime(Timestamp autoReceiveTime) {
        this.autoReceiveTime = autoReceiveTime;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
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

    public String getCancelReasonContent() {
        return cancelReasonContent;
    }

    public void setCancelReasonContent(String cancelReasonContent) {
        this.cancelReasonContent = cancelReasonContent;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Timestamp getAutoCancelTime() {
        return autoCancelTime;
    }

    public void setAutoCancelTime(Timestamp autoCancelTime) {
        this.autoCancelTime = autoCancelTime;
    }

    public int getAutoCancelCycle() {
        return autoCancelCycle;
    }

    public void setAutoCancelCycle(int autoCancelCycle) {
        this.autoCancelCycle = autoCancelCycle;
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

    public int getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(int deleteState) {
        this.deleteState = deleteState;
    }

    public int getShowMemberCancel() {
        return showMemberCancel;
    }

    public void setShowMemberCancel(int showMemberCancel) {
        this.showMemberCancel = showMemberCancel;
    }

    public int getShowMemberPay() {
        return showMemberPay;
    }

    public void setShowMemberPay(int showMemberPay) {
        this.showMemberPay = showMemberPay;
    }

    public int getShowShipSearch() {
        return showShipSearch;
    }

    public void setShowShipSearch(int showShipSearch) {
        this.showShipSearch = showShipSearch;
    }

    public int getShowMemberReceive() {
        return showMemberReceive;
    }

    public void setShowMemberReceive(int showMemberReceive) {
        this.showMemberReceive = showMemberReceive;
    }

    public int getShowStoreCancel() {
        return showStoreCancel;
    }

    public void setShowStoreCancel(int showStoreCancel) {
        this.showStoreCancel = showStoreCancel;
    }

    public int getShowStoreModifyFreight() {
        return showStoreModifyFreight;
    }

    public void setShowStoreModifyFreight(int showStoreModifyFreight) {
        this.showStoreModifyFreight = showStoreModifyFreight;
    }

    public int getShowStoreSend() {
        return showStoreSend;
    }

    public void setShowStoreSend(int showStoreSend) {
        this.showStoreSend = showStoreSend;
    }

    public int getShowStoreSendModify() {
        return showStoreSendModify;
    }

    public void setShowStoreSendModify(int showStoreSendModify) {
        this.showStoreSendModify = showStoreSendModify;
    }

    public int getShowEvaluation() {
        return showEvaluation;
    }

    public void setShowEvaluation(int showEvaluation) {
        this.showEvaluation = showEvaluation;
    }

    public int getShowEvaluationAppend() {
        return showEvaluationAppend;
    }

    public void setShowEvaluationAppend(int showEvaluationAppend) {
        this.showEvaluationAppend = showEvaluationAppend;
    }

    public int getShowAdminCancel() {
        return showAdminCancel;
    }

    public void setShowAdminCancel(int showAdminCancel) {
        this.showAdminCancel = showAdminCancel;
    }

    public int getShowAdminPay() {
        return showAdminPay;
    }

    public void setShowAdminPay(int showAdminPay) {
        this.showAdminPay = showAdminPay;
    }

    public int getShowMemberDelayReceive() {
        return showMemberDelayReceive;
    }

    public void setShowMemberDelayReceive(int showMemberDelayReceive) {
        this.showMemberDelayReceive = showMemberDelayReceive;
    }

    public int getShowMemberRefundAll() {
        return showMemberRefundAll;
    }

    public void setShowMemberRefundAll(int showMemberRefundAll) {
        this.showMemberRefundAll = showMemberRefundAll;
    }

    public int getShowMemberComplain() {
        return showMemberComplain;
    }

    public void setShowMemberComplain(int showMemberComplain) {
        this.showMemberComplain = showMemberComplain;
    }

    public int getShowMemberDelete() {
        return showMemberDelete;
    }

    public void setShowMemberDelete(int showMemberDelete) {
        this.showMemberDelete = showMemberDelete;
    }

    public int getShowMemberRecoveryDelete() {
        return showMemberRecoveryDelete;
    }

    public void setShowMemberRecoveryDelete(int showMemberRecoveryDelete) {
        this.showMemberRecoveryDelete = showMemberRecoveryDelete;
    }

    public int getShowMemberBuyAgain() {
        return showMemberBuyAgain;
    }

    public void setShowMemberBuyAgain(int showMemberBuyAgain) {
        this.showMemberBuyAgain = showMemberBuyAgain;
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

    public int getShowRefundWaiting() {
        return showRefundWaiting;
    }

    public void setShowRefundWaiting(int showRefundWaiting) {
        this.showRefundWaiting = showRefundWaiting;
    }

    public RefundItemVo getRefundItemVo() {
        return refundItemVo;
    }

    public void setRefundItemVo(RefundItemVo refundItemVo) {
        this.refundItemVo = refundItemVo;
    }

    public int getImIsOnline() {
        return imIsOnline;
    }

    public void setImIsOnline(int imIsOnline) {
        this.imIsOnline = imIsOnline;
    }

    public int getCannotShip() {
        return cannotShip;
    }

    public void setCannotShip(int cannotShip) {
        this.cannotShip = cannotShip;
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

    public String getPaymentClientType() {
        return paymentClientType;
    }

    public void setPaymentClientType(String paymentClientType) {
        this.paymentClientType = paymentClientType;
    }

    public List<OrdersBookVo> getOrdersBookVoList() {
        return ordersBookVoList;
    }

    public void setOrdersBookVoList(List<OrdersBookVo> ordersBookVoList) {
        this.ordersBookVoList = ordersBookVoList;
    }

    public String getAdminReceiveBookPayTitle() {
        return adminReceiveBookPayTitle;
    }

    public void setAdminReceiveBookPayTitle(String adminReceiveBookPayTitle) {
        this.adminReceiveBookPayTitle = adminReceiveBookPayTitle;
    }

    public BigDecimal getAdminReceiveBookPayAmount() {
        return adminReceiveBookPayAmount;
    }

    public void setAdminReceiveBookPayAmount(BigDecimal adminReceiveBookPayAmount) {
        this.adminReceiveBookPayAmount = adminReceiveBookPayAmount;
    }

    public List<OrdersGiftVo> getOrdersGiftVoList() {
        return ordersGiftVoList;
    }

    public void setOrdersGiftVoList(List<OrdersGiftVo> ordersGiftVoList) {
        this.ordersGiftVoList = ordersGiftVoList;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGoId() {
        return goId;
    }

    public void setGoId(int goId) {
        this.goId = goId;
    }

    public int getGroupCanShip() {
        return groupCanShip;
    }

    public void setGroupCanShip(int groupCanShip) {
        this.groupCanShip = groupCanShip;
    }

    public int getGroupNeedNum() {
        return groupNeedNum;
    }

    public void setGroupNeedNum(int groupNeedNum) {
        this.groupNeedNum = groupNeedNum;
    }

    public int getGroupRemainNum() {
        return groupRemainNum;
    }

    public void setGroupRemainNum(int groupRemainNum) {
        this.groupRemainNum = groupRemainNum;
    }

    public int getGoState() {
        return goState;
    }

    public void setGoState(int goState) {
        this.goState = goState;
    }

    public String getOrdersTypeName() {
        return ordersTypeName;
    }

    public void setOrdersTypeName(String ordersTypeName) {
        this.ordersTypeName = ordersTypeName;
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

    public String getShipTime() {
        return shipTime;
    }

    public void setShipTime(String shipTime) {
        this.shipTime = shipTime;
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

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
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

    public int getIsForeign() {
        return isForeign;
    }

    public void setIsForeign(int isForeign) {
        this.isForeign = isForeign;
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

    public int getShowMemberApplyForSale() {
        return showMemberApplyForSale;
    }

    public void setShowMemberApplyForSale(int showMemberApplyForSale) {
        this.showMemberApplyForSale = showMemberApplyForSale;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public Timestamp getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Timestamp takeTime) {
        this.takeTime = takeTime;
    }

    public int getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(int takeCode) {
        this.takeCode = takeCode;
    }

    public List<StoreServiceStaffVo> getStoreServiceStaffVoList() {
        return storeServiceStaffVoList;
    }

    public void setStoreServiceStaffVoList(List<StoreServiceStaffVo> storeServiceStaffVoList) {
        this.storeServiceStaffVoList = storeServiceStaffVoList;
    }

    @Override
    public String toString() {
        return "OrdersVo{" +
                "ordersGoodsVoList=" + ordersGoodsVoList +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", ordersStateName='" + ordersStateName + '\'' +
                ", ordersFrom='" + ordersFrom + '\'' +
                ", ordersFrom1='" + ordersFrom1 + '\'' +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", storeName='" + storeName + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", sellerName='" + sellerName + '\'' +
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
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", paymentTypeName='" + paymentTypeName + '\'' +
                ", paymentCode='" + paymentCode + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentName='" + paymentName + '\'' +
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
                ", shipUrl='" + shipUrl + '\'' +
                ", shipNote='" + shipNote + '\'' +
                ", autoReceiveTime=" + autoReceiveTime +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", ordersType=" + ordersType +
                ", cancelReason=" + cancelReason +
                ", cancelReasonContent='" + cancelReasonContent + '\'' +
                ", cancelTime=" + cancelTime +
                ", autoCancelTime=" + autoCancelTime +
                ", autoCancelCycle=" + autoCancelCycle +
                ", cancelRole='" + cancelRole + '\'' +
                ", commissionAmount=" + commissionAmount +
                ", deleteState=" + deleteState +
                ", showMemberCancel=" + showMemberCancel +
                ", showMemberPay=" + showMemberPay +
                ", showShipSearch=" + showShipSearch +
                ", showMemberReceive=" + showMemberReceive +
                ", showStoreCancel=" + showStoreCancel +
                ", showStoreModifyFreight=" + showStoreModifyFreight +
                ", showStoreSend=" + showStoreSend +
                ", showStoreSendModify=" + showStoreSendModify +
                ", showEvaluation=" + showEvaluation +
                ", showEvaluationAppend=" + showEvaluationAppend +
                ", showAdminCancel=" + showAdminCancel +
                ", showAdminPay=" + showAdminPay +
                ", showMemberDelayReceive=" + showMemberDelayReceive +
                ", showMemberRefundAll=" + showMemberRefundAll +
                ", showMemberComplain=" + showMemberComplain +
                ", showMemberDelete=" + showMemberDelete +
                ", showMemberRecoveryDelete=" + showMemberRecoveryDelete +
                ", showMemberBuyAgain=" + showMemberBuyAgain +
                ", refundAmount=" + refundAmount +
                ", refundState=" + refundState +
                ", showRefundWaiting=" + showRefundWaiting +
                ", refundItemVo=" + refundItemVo +
                ", imIsOnline=" + imIsOnline +
                ", cannotShip=" + cannotShip +
                ", delayReceiveState=" + delayReceiveState +
                ", adminReceivePayState=" + adminReceivePayState +
                ", voucherPrice=" + voucherPrice +
                ", voucherCode='" + voucherCode + '\'' +
                ", limitAmount=" + limitAmount +
                ", conformPrice=" + conformPrice +
                ", isFreeFreight=" + isFreeFreight +
                ", templateId=" + templateId +
                ", paymentClientType='" + paymentClientType + '\'' +
                ", ordersBookVoList=" + ordersBookVoList +
                ", adminReceiveBookPayTitle='" + adminReceiveBookPayTitle + '\'' +
                ", adminReceiveBookPayAmount=" + adminReceiveBookPayAmount +
                ", ordersGiftVoList=" + ordersGiftVoList +
                ", groupId=" + groupId +
                ", goId=" + goId +
                ", groupCanShip=" + groupCanShip +
                ", groupNeedNum=" + groupNeedNum +
                ", groupRemainNum=" + groupRemainNum +
                ", goState=" + goState +
                ", ordersTypeName='" + ordersTypeName + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                ", shipTime='" + shipTime + '\'' +
                ", couponAmount=" + couponAmount +
                ", shopCommitmentAmount=" + shopCommitmentAmount +
                ", itemAmount=" + itemAmount +
                ", finalAmount=" + finalAmount +
                ", taxAmount=" + taxAmount +
                ", isForeign=" + isForeign +
                ", storeDiscountAmount=" + storeDiscountAmount +
                ", idCard='" + idCard + '\'' +
                ", showMemberApplyForSale=" + showMemberApplyForSale +
                ", lockState=" + lockState +
                ", takeTime=" + takeTime +
                ", takeCode=" + takeCode +
                '}';
    }
}

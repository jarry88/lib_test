package com.ftofs.twant.vo.orders;

import com.ftofs.twant.domain.orders.OrdersBook;
import com.ftofs.twant.vo.refund.RefundItemVo;
import com.ftofs.twant.vo.store.ServiceVo;
import com.ftofs.twant.vo.store.StoreServiceStaffVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 会员订单列表Vo
 *
 * @author hbj
 * Created 2017/7/3 11:08
 */
public class SearchOrdersVo {
    /**
     * 订单商品列表
     */
    private List<SearchOrdersGoodsVo> ordersGoodsVoList = new ArrayList<>();
    /**
     * 订单主键ID
     */
    private int ordersId;
    /**
     * 订单编号
     */
    private long ordersSn;
    /**
     * 数据库订单状态
     */
    private int ordersState;
    /**
     * 搜索引擎订单状态
     */
    private int searchOrdersState;
    /**
     * 订单状态
     */
    private String ordersStateName;
    /**
     * 支付单ID
     */
    private int payId;
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
    private String storeName = "";
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
    private String createTime;
    /**
     * 支付方式代码
     */
    private String paymentCode;
    /**
     * 支付方式名称
     */
    private String paymentName;
    /**
     * 支付时间
     */
    private String paymentTime;
    /**
     * 订单完成时间
     */
    private String finishTime;
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
    private BigDecimal freightAmount = BigDecimal.ZERO;
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
    private String evaluationTime;
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
     * 订单表中的订单类型(详见OrdersOrderType常量)
     */
    private int ordersType;
    /**
     * 删除状态 0-未删除/1-已删除
     */
    private int deleteState = 0;
    /**
     * 订单列表是否显示操作
     */
    private int showMemberCancel = 0;
    /**
     * 快递查询
     */
    private int showShipSearch = 0;
    /**
     * 会员收货
     */
    private int showMemberReceive = 0;
    /**
     * 评价
     */
    private int showEvaluation = 0;
    /**
     * 追评
     */
    private int showEvaluationAppend = 0;
    /**
     * 延迟收货
     */
    private int showMemberDelayReceive = 0;
    /**
     * 退款
     */
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
     * 退款金额(该字段不以搜索引擎为准，会重新查库计算)
     */
    private BigDecimal refundAmount;
    /**
     * 退款状态:0是无退款,1是部分退款,2是全部退款(该字段不以搜索引擎为准，会重新查库计算)
     */
    private int refundState;
    /**
     * 是否显示退款退货中(该字段不以搜索引擎为准，会重新查库计算)
     */
    private int showRefundWaiting = 0;
    /**
     * 退款信息(该字段不以搜索引擎为准，会重新查库计算)
     */
    private RefundItemVo refundItemVo;
    /**
     * 是否处于整单退款中(申请售后列表使用)(该字段不存到搜索引擎里)
     */
    private int showViewRefundAll = 0;
    /**
     * im 是否在线(该字段不以搜索引擎为准，会重新查库计算)
     */
    private int imIsOnline = 0;
    /**
     * 支付使用终端(WAP,WEB,APP)
     */
    private String paymentClientType = "";
    /**
     * 线上线下支付代码online/offline[仅非门店订单使用]
     */
    private String paymentTypeCode;
    /**
     * 预定时段
     */
    private List<OrdersBook> ordersBookList = new ArrayList<>();
    /**
     * 订单赠品
     */
    private List<OrdersGiftVo> ordersGiftVoList = new ArrayList<>();
    /**
     * 自动收货时间[仅对非门店订单有用]
     */
    private String autoReceiveTime;
    /**
     * 拼团ID
     */
    private int groupId = 0;
    /**
     * 开团ID
     */
    private int goId = 0;
    /**
     * 订单类型
     */
    private String ordersTypeName = "";
    /**
     * 门店Id
     */
    private int chainId;
    /**
     * 门店名
     */
    private String chainName = "";
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 开团状态
     */
    private int goState = 0;
    /**
     * 成团还差人数
     */
    private int groupRemainNum = 0;
    /**
     * 成团所需人数
     */
    private int groupNeedNum = 0;
    /**
     * 门店订单类型
     */
    private int chainOrdersType = 0;
    /**
     * 是否进行过延迟收货操作，买家只能点击一次 0-否/1-是
     */
    private int delayReceiveState = 0;
    /**
     * 订单锁定状态
     */
    private int lockState = 0;
    /**
     * 是否可以在退款退货综合列表中出现
     */
    private int isShowRefundOrdersList = 0;
    /**
     * 是否虚拟订单(该字段不存到搜索引擎里)
     */
    private int isVirtual = 0;
    /**
     * 是否可以支付(仅PC会员中心首页调用前三条订单使用)，(该字段不存到搜索引擎里)
     */
    private int showMemberPay = 0;
    /**
     * 虚拟订单是否可以退款(当旗下至少有一个虚拟码可退时，就可以申请退款)(该字段不存到搜索引擎里)
     */
    private int showMemberVirtualRefund = 0;
    /**
     * 是否存在被锁定中的(即退款中)虚拟码(该字段不存到搜索引擎里)
     */
    private int showMemberVirtualRefundWaiting = 0;
    /**
     * 加密的收货人电话(该字段不存到搜索引擎里)
     */
    private String receiverPhoneEncrypt;
    /**
     * 是否可以申请售后(该字段不存到搜索引擎里)
     */
    private int showMemberApplyForSale;
    /**
     * 离订单自动取消还有多少秒(该字段不存到搜索引擎里)
     */
    private long autoCancelSecond;
    /**
     * 是否出支付倒记时，该值要和 autoCancelSecond组合判断(该字段不存到搜索引擎里)
     */
    private int showAutoCancelSecond = 1;
    /**
     * 返回4大类型(是ordersType的简化版本，将砍价，拼团，预定都归了普通订单)(该字段不存到搜索引擎里)
     */
    private int ordersTypeTag;

    /**
     * 店铺客服
     */
    private List<ServiceVo> storePresalesList;
    private List<StoreServiceStaffVo> storeServiceStaffVoList;

    public SearchOrdersVo() {
    }

    public List<SearchOrdersGoodsVo> getOrdersGoodsVoList() {
        return ordersGoodsVoList;
    }

    public void setOrdersGoodsVoList(List<SearchOrdersGoodsVo> ordersGoodsVoList) {
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

    public int getSearchOrdersState() {
        return searchOrdersState;
    }

    public void setSearchOrdersState(int searchOrdersState) {
        this.searchOrdersState = searchOrdersState;
    }

    public String getOrdersStateName() {
        return ordersStateName;
    }

    public void setOrdersStateName(String ordersStateName) {
        this.ordersStateName = ordersStateName;
    }

    public int getPayId() {
        return payId;
    }

    public void setPayId(int payId) {
        this.payId = payId;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
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

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        this.shipUrl = shipUrl;
    }

    public int getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(int ordersType) {
        this.ordersType = ordersType;
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

    public int getShowViewRefundAll() {
        return showViewRefundAll;
    }

    public void setShowViewRefundAll(int showViewRefundAll) {
        this.showViewRefundAll = showViewRefundAll;
    }

    public int getImIsOnline() {
        return imIsOnline;
    }

    public void setImIsOnline(int imIsOnline) {
        this.imIsOnline = imIsOnline;
    }

    public String getPaymentClientType() {
        return paymentClientType;
    }

    public void setPaymentClientType(String paymentClientType) {
        this.paymentClientType = paymentClientType;
    }

    public String getPaymentTypeCode() {
        return paymentTypeCode;
    }

    public void setPaymentTypeCode(String paymentTypeCode) {
        this.paymentTypeCode = paymentTypeCode;
    }

    public List<OrdersBook> getOrdersBookList() {
        return ordersBookList;
    }

    public void setOrdersBookList(List<OrdersBook> ordersBookList) {
        this.ordersBookList = ordersBookList;
    }

    public List<OrdersGiftVo> getOrdersGiftVoList() {
        return ordersGiftVoList;
    }

    public void setOrdersGiftVoList(List<OrdersGiftVo> ordersGiftVoList) {
        this.ordersGiftVoList = ordersGiftVoList;
    }

    public String getAutoReceiveTime() {
        return autoReceiveTime;
    }

    public void setAutoReceiveTime(String autoReceiveTime) {
        this.autoReceiveTime = autoReceiveTime;
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

    public String getOrdersTypeName() {
        return ordersTypeName;
    }

    public void setOrdersTypeName(String ordersTypeName) {
        this.ordersTypeName = ordersTypeName;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoState() {
        return goState;
    }

    public void setGoState(int goState) {
        this.goState = goState;
    }

    public int getGroupRemainNum() {
        return groupRemainNum;
    }

    public void setGroupRemainNum(int groupRemainNum) {
        this.groupRemainNum = groupRemainNum;
    }

    public int getGroupNeedNum() {
        return groupNeedNum;
    }

    public void setGroupNeedNum(int groupNeedNum) {
        this.groupNeedNum = groupNeedNum;
    }

    public int getChainOrdersType() {
        return chainOrdersType;
    }

    public void setChainOrdersType(int chainOrdersType) {
        this.chainOrdersType = chainOrdersType;
    }

    public int getDelayReceiveState() {
        return delayReceiveState;
    }

    public void setDelayReceiveState(int delayReceiveState) {
        this.delayReceiveState = delayReceiveState;
    }

    public int getLockState() {
        return lockState;
    }

    public void setLockState(int lockState) {
        this.lockState = lockState;
    }

    public int getIsShowRefundOrdersList() {
        return isShowRefundOrdersList;
    }

    public void setIsShowRefundOrdersList(int isShowRefundOrdersList) {
        this.isShowRefundOrdersList = isShowRefundOrdersList;
    }

    public int getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(int isVirtual) {
        this.isVirtual = isVirtual;
    }

    public int getShowMemberPay() {
        return showMemberPay;
    }

    public void setShowMemberPay(int showMemberPay) {
        this.showMemberPay = showMemberPay;
    }

    public int getShowMemberVirtualRefund() {
        return showMemberVirtualRefund;
    }

    public void setShowMemberVirtualRefund(int showMemberVirtualRefund) {
        this.showMemberVirtualRefund = showMemberVirtualRefund;
    }

    public int getShowMemberVirtualRefundWaiting() {
        return showMemberVirtualRefundWaiting;
    }

    public void setShowMemberVirtualRefundWaiting(int showMemberVirtualRefundWaiting) {
        this.showMemberVirtualRefundWaiting = showMemberVirtualRefundWaiting;
    }

    public String getReceiverPhoneEncrypt() {
        return receiverPhoneEncrypt;
    }

    public void setReceiverPhoneEncrypt(String receiverPhoneEncrypt) {
        this.receiverPhoneEncrypt = receiverPhoneEncrypt;
    }

    public int getShowMemberApplyForSale() {
        return showMemberApplyForSale;
    }

    public void setShowMemberApplyForSale(int showMemberApplyForSale) {
        this.showMemberApplyForSale = showMemberApplyForSale;
    }

    public long getAutoCancelSecond() {
        return autoCancelSecond;
    }

    public void setAutoCancelSecond(long autoCancelSecond) {
        this.autoCancelSecond = autoCancelSecond;
    }

    public int getShowAutoCancelSecond() {
        return showAutoCancelSecond;
    }

    public void setShowAutoCancelSecond(int showAutoCancelSecond) {
        this.showAutoCancelSecond = showAutoCancelSecond;
    }

    public int getOrdersTypeTag() {
        return ordersTypeTag;
    }

    public void setOrdersTypeTag(int ordersTypeTag) {
        this.ordersTypeTag = ordersTypeTag;
    }

    public List<ServiceVo> getStorePresalesList() {
        return storePresalesList;
    }

    public void setStorePresalesList(List<ServiceVo> storePresalesList) {
        this.storePresalesList = storePresalesList;
    }

    public List<StoreServiceStaffVo> getStoreServiceStaffVoList() {
        return storeServiceStaffVoList;
    }

    public void setStoreServiceStaffVoList(List<StoreServiceStaffVo> storeServiceStaffVoList) {
        this.storeServiceStaffVoList = storeServiceStaffVoList;
    }

    @Override
    public String toString() {
        return "SearchOrdersVo{" +
                "ordersGoodsVoList=" + ordersGoodsVoList +
                ", ordersId=" + ordersId +
                ", ordersSn=" + ordersSn +
                ", ordersState=" + ordersState +
                ", searchOrdersState=" + searchOrdersState +
                ", ordersStateName='" + ordersStateName + '\'' +
                ", payId=" + payId +
                ", storeId=" + storeId +
                ", sellerId=" + sellerId +
                ", storeName='" + storeName + '\'' +
                ", storePhone='" + storePhone + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", sellerName='" + sellerName + '\'' +
                ", memberId=" + memberId +
                ", memberName='" + memberName + '\'' +
                ", receiverAreaInfo='" + receiverAreaInfo + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", receiverMessage='" + receiverMessage + '\'' +
                ", createTime='" + createTime + '\'' +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentName='" + paymentName + '\'' +
                ", paymentTime='" + paymentTime + '\'' +
                ", finishTime='" + finishTime + '\'' +
                ", ordersAmount=" + ordersAmount +
                ", predepositAmount=" + predepositAmount +
                ", freightAmount=" + freightAmount +
                ", evaluationState=" + evaluationState +
                ", evaluationAppendState=" + evaluationAppendState +
                ", evaluationTime='" + evaluationTime + '\'' +
                ", shipSn='" + shipSn + '\'' +
                ", shipName='" + shipName + '\'' +
                ", shipCode='" + shipCode + '\'' +
                ", shipUrl='" + shipUrl + '\'' +
                ", ordersType=" + ordersType +
                ", deleteState=" + deleteState +
                ", showMemberCancel=" + showMemberCancel +
                ", showShipSearch=" + showShipSearch +
                ", showMemberReceive=" + showMemberReceive +
                ", showEvaluation=" + showEvaluation +
                ", showEvaluationAppend=" + showEvaluationAppend +
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
                ", showViewRefundAll=" + showViewRefundAll +
                ", imIsOnline=" + imIsOnline +
                ", paymentClientType='" + paymentClientType + '\'' +
                ", paymentTypeCode='" + paymentTypeCode + '\'' +
                ", ordersBookList=" + ordersBookList +
                ", ordersGiftVoList=" + ordersGiftVoList +
                ", autoReceiveTime='" + autoReceiveTime + '\'' +
                ", groupId=" + groupId +
                ", goId=" + goId +
                ", ordersTypeName='" + ordersTypeName + '\'' +
                ", chainId=" + chainId +
                ", chainName='" + chainName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goState=" + goState +
                ", groupRemainNum=" + groupRemainNum +
                ", groupNeedNum=" + groupNeedNum +
                ", chainOrdersType=" + chainOrdersType +
                ", delayReceiveState=" + delayReceiveState +
                ", lockState=" + lockState +
                ", isShowRefundOrdersList=" + isShowRefundOrdersList +
                ", isVirtual=" + isVirtual +
                ", showMemberPay=" + showMemberPay +
                ", showMemberVirtualRefund=" + showMemberVirtualRefund +
                ", showMemberVirtualRefundWaiting=" + showMemberVirtualRefundWaiting +
                ", receiverPhoneEncrypt='" + receiverPhoneEncrypt + '\'' +
                ", showMemberApplyForSale=" + showMemberApplyForSale +
                ", autoCancelSecond=" + autoCancelSecond +
                ", showAutoCancelSecond=" + showAutoCancelSecond +
                ", ordersTypeTag=" + ordersTypeTag +
                ", storePresalesList=" + storePresalesList +
                '}';
    }
}

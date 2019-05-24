package com.ftofs.twant.domain.orders;

import java.io.Serializable;
import java.math.BigDecimal;


public class OrdersBook implements Serializable {
    /**
     * 主键，自增
     */
    private int bookId;

    /**
     * 订单表Id
     */
    private int ordersId;

    /**
     * 预定时段,0-不分时段，全款支付、1-预定时段、2-尾款时段
     */
    private int bookStep;

    /**
     * 订单表Id
     */
    private int payId;

    /**
     * 支付单号
     */
    private long paySn;

    /**
     * 订单金额(预定金额、尾款金额+运费、全款金额+运费)
     */
    private BigDecimal bookAmount = new BigDecimal(0);

    /**
     * 预存款支付金额
     */
    private BigDecimal predepositAmount = new BigDecimal(0);

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
     * 时段1:订单自动取消时间，时段2:定金结束时间，过此时间系统发送尾款支付提示，超过X小时未付尾款订单取消
     */
    private String endTime;

    /**
     * 买家接收尾款交款通知的手机,只在第2时段有值即可
     */
    private String bookPhone = "";

    /**
     * 定金金额,只在全款支付时有值即可
     */
    private BigDecimal depositAmount = new BigDecimal(0);

    /**
     * 通知状态：0-未通知、1-已通知,该字段只对尾款时段有效
     */
    private int noticeState = 0;

    /**
     * 订单被（买家）取消后支付(定金)金额，平台结算使用
     */
    private BigDecimal realPayAmount = new BigDecimal(0);

    /**
     * 订单被取消(定金不退时)，记录的时间,结算用
     */
    private String bookCancelTime;

    /**
     * 商家ID,只有book_step是1,0时有值即可
     */
    private int storeId;

    /**
     * 订单编号
     */
    private long ordersSn;

    /**
     * 订单是否已经取消
     */
    private int cancelState = 0;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public int getBookStep() {
        return bookStep;
    }

    public void setBookStep(int bookStep) {
        this.bookStep = bookStep;
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

    public BigDecimal getBookAmount() {
        return bookAmount;
    }

    public void setBookAmount(BigDecimal bookAmount) {
        this.bookAmount = bookAmount;
    }

    public BigDecimal getPredepositAmount() {
        return predepositAmount;
    }

    public void setPredepositAmount(BigDecimal predepositAmount) {
        this.predepositAmount = predepositAmount;
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

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBookPhone() {
        return bookPhone;
    }

    public void setBookPhone(String bookPhone) {
        this.bookPhone = bookPhone;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public int getNoticeState() {
        return noticeState;
    }

    public void setNoticeState(int noticeState) {
        this.noticeState = noticeState;
    }

    public BigDecimal getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(BigDecimal realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public String getBookCancelTime() {
        return bookCancelTime;
    }

    public void setBookCancelTime(String bookCancelTime) {
        this.bookCancelTime = bookCancelTime;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public long getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(long ordersSn) {
        this.ordersSn = ordersSn;
    }

    public int getCancelState() {
        return cancelState;
    }

    public void setCancelState(int cancelState) {
        this.cancelState = cancelState;
    }

    @Override
    public String toString() {
        return "OrdersBook{" +
                "bookId=" + bookId +
                ", ordersId=" + ordersId +
                ", bookStep=" + bookStep +
                ", payId=" + payId +
                ", paySn=" + paySn +
                ", bookAmount=" + bookAmount +
                ", predepositAmount=" + predepositAmount +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentClientType='" + paymentClientType + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentTime=" + paymentTime +
                ", endTime=" + endTime +
                ", bookPhone='" + bookPhone + '\'' +
                ", depositAmount=" + depositAmount +
                ", noticeState=" + noticeState +
                ", realPayAmount=" + realPayAmount +
                ", bookCancelTime=" + bookCancelTime +
                ", storeId=" + storeId +
                ", ordersSn=" + ordersSn +
                ", cancelState=" + cancelState +
                '}';
    }
}

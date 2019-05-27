package com.ftofs.twant.vo.orders;

import com.ftofs.twant.domain.orders.OrdersBook;

import java.math.BigDecimal;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 预定订单时段Vo
 *
 * @author hbj
 * Created 2017/4/13 14:46
 */
public class OrdersBookVo {

    /**
     * 阶段名html
     */
    private String bookStepName;
    /**
     * 状态html
     */
    private String bookState = "";
    /**
     * 操作html
     */
    private String bookOperate = "";
    /**
     * 金额html
     */
    private String bookAmount = "";
    /**
     * 支付单号
     */
    private long paySn;
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
     * 时段1:订单自动取消时间，时段2:时段结束时间
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
     * 未退定金(后台结算列表使用)
     */
    private BigDecimal realPayAmount = new BigDecimal(0);
    /**
     * 订单编号(后台结算列表使用)
     */
    private String ordersSn = "";
    /**
     * 订单被取消(定金不退时)，记录的时间,(后台结算列表使用)
     */

    private String bookCancelTime;
    /**
     * 订单Id(后台结算列表使用)
     */
    private int ordersId;
    /**
     * 预定阶段
     */
    private int bookStep;

    public String getBookStepName() {
        return bookStepName;
    }

    public void setBookStepName(String bookStepName) {
        this.bookStepName = bookStepName;
    }

    public String getBookState() {
        return bookState;
    }

    public void setBookState(String bookState) {
        this.bookState = bookState;
    }

    public String getBookOperate() {
        return bookOperate;
    }

    public void setBookOperate(String bookOperate) {
        this.bookOperate = bookOperate;
    }

    public String getBookAmount() {
        return bookAmount;
    }

    public void setBookAmount(String bookAmount) {
        this.bookAmount = bookAmount;
    }

    public long getPaySn() {
        return paySn;
    }

    public void setPaySn(long paySn) {
        this.paySn = paySn;
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

    public BigDecimal getRealPayAmount() {
        return realPayAmount;
    }

    public void setRealPayAmount(BigDecimal realPayAmount) {
        this.realPayAmount = realPayAmount;
    }

    public String getOrdersSn() {
        return ordersSn;
    }

    public void setOrdersSn(String ordersSn) {
        this.ordersSn = ordersSn;
    }

    public String getBookCancelTime() {
        return bookCancelTime;
    }

    public void setBookCancelTime(String bookCancelTime) {
        this.bookCancelTime = bookCancelTime;
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

    @Override
    public String toString() {
        return "OrdersBookVo{" +
                "bookStepName='" + bookStepName + '\'' +
                ", bookState='" + bookState + '\'' +
                ", bookOperate='" + bookOperate + '\'' +
                ", bookAmount='" + bookAmount + '\'' +
                ", paySn=" + paySn +
                ", predepositAmount=" + predepositAmount +
                ", paymentCode='" + paymentCode + '\'' +
                ", paymentClientType='" + paymentClientType + '\'' +
                ", outOrdersSn='" + outOrdersSn + '\'' +
                ", paymentTime=" + paymentTime +
                ", endTime=" + endTime +
                ", bookPhone='" + bookPhone + '\'' +
                ", depositAmount=" + depositAmount +
                ", realPayAmount=" + realPayAmount +
                ", ordersSn='" + ordersSn + '\'' +
                ", bookCancelTime=" + bookCancelTime +
                ", ordersId=" + ordersId +
                ", bookStep=" + bookStep +
                '}';
    }
}

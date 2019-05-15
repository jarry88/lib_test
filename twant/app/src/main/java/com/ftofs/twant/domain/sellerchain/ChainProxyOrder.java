package com.ftofs.twant.domain.sellerchain;

import com.ftofs.twant.domain.cashier.CashierOrderGoodsItem;

import java.util.List;

/**
 * @function ChainProxyOrder
 * @description 代客下單訂單
 * @param
 * @return
 * @author Nick.Chung
 * @create 2018/9/11 18:10
 */
public class ChainProxyOrder {

    //支付方式
    private String paymentCode;
    //商品列表
    private List<CashierOrderGoodsItem> goodsItems;
    //收貨人
    private String receiverName;
    //聯系電話
    private String receiverPhone;
    //收貨地址
    private String receiverAddress;
    //發票抬頭
    private String invoiceTitle;
    //發票內容
    private String invoiceContent;
    //納稅人識別號
    private String invoiceCode;
    //送貨時間
    private String sendTime;

    public String getPaymentCode() {
        return paymentCode;
    }
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
    public List<CashierOrderGoodsItem> getGoodsItems() {
        return goodsItems;
    }
    public void setGoodsItems(List<CashierOrderGoodsItem> goodsItems) {
        this.goodsItems = goodsItems;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "ChainProxyOrder{" +
                "paymentCode='" + paymentCode + '\'' +
                ", goodsItems=" + goodsItems +
                ", receiverName='" + receiverName + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverAddress='" + receiverAddress + '\'' +
                ", invoiceTitle='" + invoiceTitle + '\'' +
                ", invoiceContent='" + invoiceContent + '\'' +
                ", invoiceCode='" + invoiceCode + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}

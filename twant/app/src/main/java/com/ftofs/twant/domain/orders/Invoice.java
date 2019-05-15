package com.ftofs.twant.domain.orders;

import java.io.Serializable;

public class Invoice implements Serializable {
    /**
     * 主键
     */
    private Integer invoiceId;

    /**
     * 会员ID
     */
    private Integer memberId;

    /**
     * 发票抬头
     */
    private String title;

    /**
     * 纳税人识别号或统一社会信用代码
     */
    private String code;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", memberId=" + memberId +
                ", title='" + title + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}

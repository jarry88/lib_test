package com.ftofs.twant.domain.store;

public class StoreCertificate {
    /**
     * 商家编号
     */
    private int sellerId;

    /**
     * 商家用户名
     */
    private String sellerName;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司地址编号
     */
    private int companyAddressId;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司详细地址
     */
    private String companyAddressDetail;

    /**
     * 公司电话
     */
    private String companyPhone;

    /**
     * 公司员工总数
     */
    private int companyEmployeeCount;

    /**
     * 注册资金
     */
    private int companyRegisteredCapital;

    /**
     * 联系人姓名
     */
    private String contactsName;

    /**
     * 联系人电话
     */
    private String contactsPhone;

    /**
     * 联系人邮箱
     */
    private String contactsEmail;

    /**
     * 营业执照号
     */
    private String businessLicenceNumber;

    /**
     * 法定经营范围
     */
    private String businessSphere;

    /**
     * 营业执照电子版
     */
    private String businessLicenceImage;

    /**
     * 法人姓名
     */
    private String legalName;

    /**
     * 法人证件号码
     */
    private String legalCode;

    /**
     * 法人证件照片
     */
    private String legalImage;

    /**
     * 银行开户名
     */
    private String bankAccountName;

    /**
     * 银行账号
     */
    private String bankAccountNumber;

    /**
     * 开户银行名称
     */
    private String bankName;

    /**
     * 支行联行号
     */
    private String bankCode;

    /**
     * 开户银行所在地
     */
    private String bankAddress;

    /**
     * 开户银行许可证电子版
     */
    private String bankLicenceImage;

    /**
     * 结算银行开户名
     */
    private String settlementBankAccountName;

    /**
     * 结算公司银行账号
     */
    private String settlementBankAccountNumber;

    /**
     * 结算开户银行名称
     */
    private String settlementBankName;

    /**
     * 结算支行联行号
     */
    private String settlementBankCode;

    /**
     * 结算开户银行所在地
     */
    private String settlementBankAddress;

    /**
     * 税务登记证号
     */
    private String taxRegistrationCertificate;

    /**
     * 纳税人识别号
     */
    private String taxpayerId;

    /**
     * 税务登记证号电子版
     */
    private String taxRegistrationImage;

    /**
     * 临时保存编辑的信息，后台审核
     */
    private String tmpModify;

    /**
     * 营业执照电子版反面
     */
    private String businessLicenceImageBack;

    /**
     * 法人证件照反面
     */
    private String legalImageBack;

    /**
     * 法人证件照手持
     */
    private String legalImageHand;

    /**
     * 公司標誌
     */
    private String companyLogo;

    /**
     * 商業登記證編號
     */
    private String businessRegisterNo;

    /**
     * 商業登記證電子版(PDF)
     */
    private String businessRegisterFile;

    /**
     * 商業名稱(葡文)
     */
    private String  companyNamePortuguese;

    /**
     * 營業稅(M8)(正面)
     */
    private String businessLicenceImg;

    /**
     * 營業稅(M8)(反面)
     */
    private String businessLicenceImgBack;

    /**
     * 存摺檔案(電子版)
     */
    private String bankBookImage;

    public String getBusinessLicenceImageBack() {
        return businessLicenceImageBack;
    }

    public String getLegalImageBack() {
        return legalImageBack;
    }

    public String getLegalImageHand() {
        return legalImageHand;
    }

    public void setBusinessLicenceImageBack(String businessLicenceImageBack) {
        this.businessLicenceImageBack = businessLicenceImageBack;
    }

    public void setLegalImageBack(String legalImageBack) {
        this.legalImageBack = legalImageBack;
    }

    public void setLegalImageHand(String legalImageHand) {
        this.legalImageHand = legalImageHand;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getCompanyAddressId() {
        return companyAddressId;
    }

    public void setCompanyAddressId(int companyAddressId) {
        this.companyAddressId = companyAddressId;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyAddressDetail() {
        return companyAddressDetail;
    }

    public void setCompanyAddressDetail(String companyAddressDetail) {
        this.companyAddressDetail = companyAddressDetail;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public int getCompanyEmployeeCount() {
        return companyEmployeeCount;
    }

    public void setCompanyEmployeeCount(int companyEmployeeCount) {
        this.companyEmployeeCount = companyEmployeeCount;
    }

    public int getCompanyRegisteredCapital() {
        return companyRegisteredCapital;
    }

    public void setCompanyRegisteredCapital(int companyRegisteredCapital) {
        this.companyRegisteredCapital = companyRegisteredCapital;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public String getBusinessLicenceNumber() {
        return businessLicenceNumber;
    }

    public void setBusinessLicenceNumber(String businessLicenceNumber) {
        this.businessLicenceNumber = businessLicenceNumber;
    }

    public String getBusinessSphere() {
        return businessSphere;
    }

    public void setBusinessSphere(String businessSphere) {
        this.businessSphere = businessSphere;
    }

    public String getBusinessLicenceImage() {
        return businessLicenceImage;
    }

    public String getBusinessLicenceImageUrl() {
        return businessLicenceImage;
    }

    public void setBusinessLicenceImage(String businessLicenceImage) {
        this.businessLicenceImage = businessLicenceImage;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalCode() {
        return legalCode;
    }

    public void setLegalCode(String legalCode) {
        this.legalCode = legalCode;
    }

    public String getLegalImage() {
        return legalImage;
    }

    public String getLegalImageUrl() {
        return legalImage;
    }

    public void setLegalImage(String legalImage) {
        this.legalImage = legalImage;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getBankLicenceImage() {
        return bankLicenceImage;
    }

    public String getBankLicenceImageUrl() {
        return bankLicenceImage;
    }

    public void setBankLicenceImage(String bankLicenceImage) {
        this.bankLicenceImage = bankLicenceImage;
    }

    public String getSettlementBankAccountName() {
        return settlementBankAccountName;
    }

    public void setSettlementBankAccountName(String settlementBankAccountName) {
        this.settlementBankAccountName = settlementBankAccountName;
    }

    public String getSettlementBankAccountNumber() {
        return settlementBankAccountNumber;
    }

    public void setSettlementBankAccountNumber(String settlementBankAccountNumber) {
        this.settlementBankAccountNumber = settlementBankAccountNumber;
    }

    public String getSettlementBankName() {
        return settlementBankName;
    }

    public void setSettlementBankName(String settlementBankName) {
        this.settlementBankName = settlementBankName;
    }

    public String getSettlementBankCode() {
        return settlementBankCode;
    }

    public void setSettlementBankCode(String settlementBankCode) {
        this.settlementBankCode = settlementBankCode;
    }

    public String getSettlementBankAddress() {
        return settlementBankAddress;
    }

    public void setSettlementBankAddress(String settlementBankAddress) {
        this.settlementBankAddress = settlementBankAddress;
    }

    public String getTaxRegistrationCertificate() {
        return taxRegistrationCertificate;
    }

    public void setTaxRegistrationCertificate(String taxRegistrationCertificate) {
        this.taxRegistrationCertificate = taxRegistrationCertificate;
    }

    public String getTaxpayerId() {
        return taxpayerId;
    }

    public void setTaxpayerId(String taxpayerId) {
        this.taxpayerId = taxpayerId;
    }

    public String getTaxRegistrationImage() {
        return taxRegistrationImage;
    }

    public String getTaxRegistrationImageUrl() {
        return taxRegistrationImage;
    }

    public void setTaxRegistrationImage(String taxRegistrationImage) {
        this.taxRegistrationImage = taxRegistrationImage;
    }

    public String getTmpModify() {
        return tmpModify;
    }

    public void setTmpModify(String tmpModify) {
        this.tmpModify = tmpModify;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getBusinessRegisterNo() {
        return businessRegisterNo;
    }

    public void setBusinessRegisterNo(String businessRegisterNo) {
        this.businessRegisterNo = businessRegisterNo;
    }

    public String getBusinessRegisterFile() {
        return businessRegisterFile;
    }

    public void setBusinessRegisterFile(String businessRegisterFile) {
        this.businessRegisterFile = businessRegisterFile;
    }

    public String getCompanyNamePortuguese() {
        return companyNamePortuguese;
    }

    public void setCompanyNamePortuguese(String companyNamePortuguese) {
        this.companyNamePortuguese = companyNamePortuguese;
    }

    public String getBusinessLicenceImg() {
        return businessLicenceImg;
    }

    public void setBusinessLicenceImg(String businessLicenceImg) {
        this.businessLicenceImg = businessLicenceImg;
    }

    public String getBusinessLicenceImgBack() {
        return businessLicenceImgBack;
    }

    public void setBusinessLicenceImgBack(String businessLicenceImgBack) {
        this.businessLicenceImgBack = businessLicenceImgBack;
    }

    public String getBankBookImage() {
        return bankBookImage;
    }

    public void setBankBookImage(String bankBookImage) {
        this.bankBookImage = bankBookImage;
    }

    @Override
    public String toString() {
        return "StoreCertificate{" +
                "sellerId=" + sellerId +
                ", sellerName='" + sellerName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyAddressId=" + companyAddressId +
                ", companyAddress='" + companyAddress + '\'' +
                ", companyAddressDetail='" + companyAddressDetail + '\'' +
                ", companyPhone='" + companyPhone + '\'' +
                ", companyEmployeeCount=" + companyEmployeeCount +
                ", companyRegisteredCapital=" + companyRegisteredCapital +
                ", contactsName='" + contactsName + '\'' +
                ", contactsPhone='" + contactsPhone + '\'' +
                ", contactsEmail='" + contactsEmail + '\'' +
                ", businessLicenceNumber='" + businessLicenceNumber + '\'' +
                ", businessSphere='" + businessSphere + '\'' +
                ", businessLicenceImage='" + businessLicenceImage + '\'' +
                ", legalName='" + legalName + '\'' +
                ", legalCode='" + legalCode + '\'' +
                ", legalImage='" + legalImage + '\'' +
                ", bankAccountName='" + bankAccountName + '\'' +
                ", bankAccountNumber='" + bankAccountNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", bankAddress='" + bankAddress + '\'' +
                ", bankLicenceImage='" + bankLicenceImage + '\'' +
                ", settlementBankAccountName='" + settlementBankAccountName + '\'' +
                ", settlementBankAccountNumber='" + settlementBankAccountNumber + '\'' +
                ", settlementBankName='" + settlementBankName + '\'' +
                ", settlementBankCode='" + settlementBankCode + '\'' +
                ", settlementBankAddress='" + settlementBankAddress + '\'' +
                ", taxRegistrationCertificate='" + taxRegistrationCertificate + '\'' +
                ", taxpayerId='" + taxpayerId + '\'' +
                ", taxRegistrationImage='" + taxRegistrationImage + '\'' +
                ", tmpModify='" + tmpModify + '\'' +
                ", businessLicenceImageBack='" + businessLicenceImageBack + '\'' +
                ", legalImageBack='" + legalImageBack + '\'' +
                ", legalImageHand='" + legalImageHand + '\'' +
                ", companyLogo='" + companyLogo + '\'' +
                '}';

    }
}


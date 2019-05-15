package com.ftofs.twant.vo.store;

/**
 * @Description: 社交工具视图对象
 * @Auther: yangjian
 * @Date: 2018/12/7 17:33
 */
public class StoreSocialAccountVo {
    /**
     * 工具id
     */
    private int id;
    /**
     * 社交账号
     */
    private String account;
    /**
     * 账号主页地址
     */
    private String accountAddress;
    /**
     * 图片地址
     */
    private String accountImageAddress;
    /**
     * 社交工具名称
     */
    private String socialName;
    /**
     * 社交工具图标地址
     */
    private String socialLogo;
    /**
     * 社交工具图标选中状态地址
     */
    private String socialLogoChecked;
    /**
     * 提供上传图片功能 1链接，2二维码
     */
    private int socialForm;

    public StoreSocialAccountVo(){}

    public StoreSocialAccountVo(int id, String account, String accountAddress, String accountImageAddress, String socialName, String socialLogo, int socialForm, String socialLogoChecked) {
        this.id = id;
        this.account = account;
        this.accountAddress = accountAddress;
        this.accountImageAddress = accountImageAddress;
        this.socialName = socialName;
        this.socialLogo = socialLogo;
        this.socialForm = socialForm;
        this.socialLogoChecked = socialLogoChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getAccountImageAddress() {
        return accountImageAddress;
    }

    public void setAccountImageAddress(String accountImageAddress) {
        this.accountImageAddress = accountImageAddress;
    }

    public String getSocialName() {
        return socialName;
    }

    public void setSocialName(String socialName) {
        this.socialName = socialName;
    }

    public String getSocialLogo() {
        return socialLogo;
    }

    public void setSocialLogo(String socialLogo) {
        this.socialLogo = socialLogo;
    }

    public int getSocialForm() {
        return socialForm;
    }

    public void setSocialForm(int socialForm) {
        this.socialForm = socialForm;
    }

    public String getSocialLogoChecked() {
        return socialLogoChecked;
    }

    public void setSocialLogoChecked(String socialLogoChecked) {
        this.socialLogoChecked = socialLogoChecked;
    }

    @Override
    public String toString() {
        return "StoreSocialAccountVo{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", accountAddress='" + accountAddress + '\'' +
                ", accountImageAddress='" + accountImageAddress + '\'' +
                ", socialName='" + socialName + '\'' +
                ", socialLogo='" + socialLogo + '\'' +
                ", socialLogoChecked='" + socialLogoChecked + '\'' +
                ", socialForm=" + socialForm +
                '}';
    }
}

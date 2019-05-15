package com.ftofs.twant.domain;

public class SocialPlatform {
    /**
     * 主键Id
     */
    private int socialId;

    /**
     * 名稱
     */
    private String socialName;

    /**
     * logo
     */
    private String socialLogo;

    /**
     * logo 選中狀態
     */
    private String socialLogoChecked;

    /**
     * 添加方式 1：链接 2：二维码
     */
    private Integer socialForm;

    public int getSocialId() {
        return socialId;
    }

    public void setSocialId(int socialId) {
        this.socialId = socialId;
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

    public Integer getSocialForm() {
        return socialForm;
    }

    public void setSocialForm(Integer socialForm) {
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
        return "SocialPlatform{" +
                "socialId=" + socialId +
                ", socialName='" + socialName + '\'' +
                ", socialLogo='" + socialLogo + '\'' +
                ", socialLogoChecked='" + socialLogoChecked + '\'' +
                ", socialForm=" + socialForm +
                '}';
    }
}

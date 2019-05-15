package com.ftofs.twant.vo;

/**
 * @Description: 社交工具分享參數
 * @Auther: yangjian
 * @Date: 2019/4/16 16:57
 */
public class ShareVo {
    /**
     * 標題
     */
    private String title;
    /**
     * 主圖
     */
    private String image="";
    /**
     * 內容
     */
    private String content="";
    /**
     * 不帶參數的地址
     */
    private String shareUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public String toString() {
        return "ShareVo{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", shareUrl='" + shareUrl + '\'' +
                '}';
    }
}

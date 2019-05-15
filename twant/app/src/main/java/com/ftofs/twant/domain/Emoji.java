package com.ftofs.twant.domain;

public class Emoji {
    /**
     * 主鍵
     */
    private int emojiId;

    /**
     * 文字代碼,例[微笑]
     */
    private String emojiCode;

    /**
     * 表情説明
     */
    private String emojiDesc;

    /**
     * 表情圖片
     */
    private String emojiImage;

    /**
     * 排序
     */
    private int sort;

    public int getEmojiId() {
        return emojiId;
    }

    public void setEmojiId(int emojiId) {
        this.emojiId = emojiId;
    }

    public String getEmojiCode() {
        return emojiCode;
    }

    public void setEmojiCode(String emojiCode) {
        this.emojiCode = emojiCode;
    }

    public String getEmojiDesc() {
        return emojiDesc;
    }

    public void setEmojiDesc(String emojiDesc) {
        this.emojiDesc = emojiDesc;
    }

    public String getEmojiImage() {
        return emojiImage;
    }

    public void setEmojiImage(String emojiImage) {
        this.emojiImage = emojiImage;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "Emoji{" +
                "emojiId=" + emojiId +
                ", emojiCode='" + emojiCode + '\'' +
                ", emojiDesc='" + emojiDesc + '\'' +
                ", emojiImage='" + emojiImage + '\'' +
                ", sort=" + sort +
                '}';
    }
}

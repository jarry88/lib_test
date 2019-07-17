package com.ftofs.twant.orm;

import org.litepal.crud.LitePalSupport;

public class Emoji extends LitePalSupport {
    public int emojiId;
    public String emojiCode;
    public String emojiDesc;
    public String emojiImage;
    public int sort;
    public String absolutePath;  // 表情圖片保存的本地路徑
}

package com.ftofs.twant.vo.wantpost;

import com.ftofs.twant.domain.wantpost.WantPostCategory;

/**
 * @author liusf
 * @create 2019/2/18 11:39
 * @description 貼文分類視圖類
 */
public class WantPostCategoryVo extends WantPostCategory {
    /**
     * 父級分類名稱
     */
    private String parentName = "";

    /**
     * 分類下貼文數量
     */
    private long wantPostCount = 0;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public long getWantPostCount() {
        return wantPostCount;
    }

    public void setWantPostCount(long wantPostCount) {
        this.wantPostCount = wantPostCount;
    }
}

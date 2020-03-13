package com.ftofs.twant.entity;


import cn.snailpad.easyjson.EasyJSONObject;

public class SearchHistoryItem {
    public String keyword;
    public long timestamp; // 搜索的時間

    public SearchHistoryItem() {
    }


    public EasyJSONObject toJSONObject() {
        return EasyJSONObject.generate(
                "keyword", keyword,
                "timestamp", timestamp);
    }
}

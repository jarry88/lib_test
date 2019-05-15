package com.ftofs.twant.domain.search;

import java.io.Serializable;
import java.sql.Timestamp;

public class SearchHistory implements Serializable {
    /**
     * 编号
     */
    private int historyId;

    /**
     * 搜索关键字
     */
    private String keyword;

    /**
     * 搜索计数
     */
    private int searchCount;

    /**
     * 最后搜索时间
     */
    private Timestamp lastTime;

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public Timestamp getLastTime() {
        return lastTime;
    }

    public void setLastTime(Timestamp lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "historyId=" + historyId +
                ", keyword='" + keyword + '\'' +
                ", searchCount=" + searchCount +
                ", lastTime=" + lastTime +
                '}';
    }
}


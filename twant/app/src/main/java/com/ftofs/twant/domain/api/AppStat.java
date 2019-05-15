package com.ftofs.twant.domain.api;

public class AppStat {
    /**
     * 编号
     */
    private int statId;

    /**
     * 统计项
     */
    private String statKey = "";

    /**
     * 操作数据
     */
    private int statCount;

    public int getStatId() {
        return statId;
    }

    public void setStatId(int statId) {
        this.statId = statId;
    }

    public String getStatKey() {
        return statKey;
    }

    public void setStatKey(String statKey) {
        this.statKey = statKey;
    }

    public int getStatCount() {
        return statCount;
    }

    public void setStatCount(int statCount) {
        this.statCount = statCount;
    }

    @Override
    public String toString() {
        return "AppStat{" +
                "statId=" + statId +
                ", statKey='" + statKey + '\'' +
                ", statCount=" + statCount +
                '}';
    }
}


package com.ftofs.twant.domain.search;

import java.io.Serializable;

public class SearchDict implements Serializable {
    /**
     * 编号
     */
    private int dictId;

    /**
     * 分词
     */
    private String word;

    /**
     * 添加时间
     */
    private String addTime;

    public int getDictId() {
        return dictId;
    }

    public void setDictId(int dictId) {
        this.dictId = dictId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    @Override
    public String toString() {
        return "SearchDict{" +
                "dictId=" + dictId +
                ", word='" + word + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}


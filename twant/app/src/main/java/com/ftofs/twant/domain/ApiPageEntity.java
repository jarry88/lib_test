package com.ftofs.twant.domain;

public class ApiPageEntity {
    private boolean hasMore;
    private int totalPage;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    @Override
    public String toString() {
        return "ApiPageEntity{" +
                "hasMore=" + hasMore +
                ", totalPage=" + totalPage +
                '}';
    }
}

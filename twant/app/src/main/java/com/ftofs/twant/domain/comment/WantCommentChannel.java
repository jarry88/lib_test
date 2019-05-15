package com.ftofs.twant.domain.comment;

public class WantCommentChannel {
    /**
     * 渠道id
     */
    private int channelId;

    /**
     * 渠道名稱
     */
    private String channelName;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

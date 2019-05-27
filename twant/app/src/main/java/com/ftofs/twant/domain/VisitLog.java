package com.ftofs.twant.domain;

import java.math.BigInteger;

public class VisitLog {
    /**
     * 訪問id
     */
    private BigInteger visitLogId;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 關聯Id
     */
    private BigInteger relateId;

    /**
     * 渠道  1貼文  2專頁
     */
    private int channel;

    /**
     * 時間
     */
    private String visitTime;

    public BigInteger getVisitLogId() {
        return visitLogId;
    }

    public void setVisitLogId(BigInteger visitLogId) {
        this.visitLogId = visitLogId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public BigInteger getRelateId() {
        return relateId;
    }

    public void setRelateId(BigInteger relateId) {
        this.relateId = relateId;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime( String visitTime) {
        this.visitTime = visitTime;
    }

    @Override
    public String toString() {
        return "VisitLog{" +
                "visitLogId=" + visitLogId +
                ", ip='" + ip + '\'' +
                ", relateId=" + relateId +
                ", channel=" + channel +
                ", visitTime=" + visitTime +
                '}';
    }
}

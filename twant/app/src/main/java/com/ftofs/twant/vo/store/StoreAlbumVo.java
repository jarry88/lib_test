package com.ftofs.twant.vo.store;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 店铺相册
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:19
 */
public class StoreAlbumVo {
    /**
     * 店铺编号
     */
    private int storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 图片数量
     */
    private long filesCount;
    /**
     * 图片占用空间
     */
    private String filesSizeSum;
    /**
     * 空间总容量
     */
    private int albumLimit;
    /**
     * 店铺等级
     */
    private String gradeName;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public long getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(long filesCount) {
        this.filesCount = filesCount;
    }

    public String getFilesSizeSum() {
        return filesSizeSum;
    }

    public void setFilesSizeSum(String filesSizeSum) {
        this.filesSizeSum = filesSizeSum;
    }

    public int getAlbumLimit() {
        return albumLimit;
    }

    public void setAlbumLimit(int albumLimit) {
        this.albumLimit = albumLimit;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    @Override
    public String toString() {
        return "StoreAlbumVo{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", filesCount=" + filesCount +
                ", filesSizeSum='" + filesSizeSum + '\'' +
                ", albumLimit=" + albumLimit +
                ", gradeName=" + gradeName +
                '}';
    }
}

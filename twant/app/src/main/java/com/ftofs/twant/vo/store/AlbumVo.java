package com.ftofs.twant.vo.store;

import com.ftofs.twant.domain.store.Album;
import com.ftofs.twant.domain.store.AlbumFiles;

/**
 * Copyright: Bizpower多用户商城系统
 * Copyright: www.bizpower.com
 * Copyright: 天津网城商动科技有限责任公司
 *
 * 相册
 * 
 * @author shopnc.feng
 * Created 2017/4/13 14:21
 */
public class AlbumVo {
    /**
     * 相册是他
     */
    private Album album;
    /**
     * 相册文件实体
     */
    private AlbumFiles albumFiles;

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public AlbumFiles getAlbumFiles() {
        return albumFiles;
    }

    public void setAlbumFiles(AlbumFiles albumFiles) {
        this.albumFiles = albumFiles;
    }

    @Override
    public String toString() {
        return "AlbumVo{" +
                "album=" + album +
                ", albumFiles=" + albumFiles +
                '}';
    }
}

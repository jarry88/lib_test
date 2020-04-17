package com.ftofs.twant.entity;

import android.graphics.Bitmap;

import java.io.File;

/**
 * 圖片下載結果
 * @author zwm
 */
public class DownloadImageResult {
    public DownloadImageResult(boolean success, String errorMessage, File file, Bitmap bitmap) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.file = file;
        this.bitmap = bitmap;
    }

    public boolean success; // 是否下載成功
    public String errorMessage; // 錯誤消息，不成功時用
    public File file; // 圖片文件
    public Bitmap bitmap; // 圖片位圖
}

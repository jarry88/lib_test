package com.ftofs.twant.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gzp.lib_common.utils.PathUtil;

import java.io.File;

/**
 * 簡單的圖片處理
 * @author zwm
 */
public class ImageProcess {
    public static class Builder {
        Context context;
        Bitmap srcBitmap;
        Bitmap destBitmap;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 加載源位圖
         * @param srcFile
         * @return
         */
        public Builder from(File srcFile) {
            srcBitmap = BitmapFactory.decodeFile(srcFile.getAbsolutePath());
            destBitmap = srcBitmap;
            return this;
        }

        /**
         * 在中心位置截取正方形位圖
         * @return
         */
        public Builder centerCrop() {
            int w = destBitmap.getWidth();
            int h = destBitmap.getHeight();

            if (w == h) {
                return this;
            }

            int x, y;
            if (w > h) { // 胖圖片，以h為準
                x = (w - h) / 2;
                y = 0;
                destBitmap = Bitmap.createBitmap(destBitmap, x, y, h, h);
            } else { // 瘦圖片，以w為準
                x = 0;
                y = (h - w) / 2;
                destBitmap = Bitmap.createBitmap(destBitmap, x, y, w, w);
            }

            return this;
        }


        /**
         * 縮放位圖
         * @param width
         * @param height
         * @return
         */
        public Builder resize(int width, int height) {
            destBitmap = Bitmap.createScaledBitmap(destBitmap, width, height, false);
            return this;
        }

        /**
         * 將處理后的位圖保存為文件
         * @param absolutePath
         */
        public void toFile(String absolutePath) {
            String ext = PathUtil.getExtension(absolutePath, true);

            Bitmap.CompressFormat format = null;
            if ("jpeg".equals(ext) || "jpg".equals(ext)) {
                format = Bitmap.CompressFormat.JPEG;
            } else if ("png".equals(ext)) {
                format = Bitmap.CompressFormat.PNG;
            } else {
                return;
            }

            BitmapUtil.Bitmap2File(destBitmap, absolutePath, format,90);
        }

        /**
         * 返回處理后的位圖
         * @return
         */
        public Bitmap toBitmap() {
            return destBitmap;
        }
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }
}

package com.ftofs.twant.util;


import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap工具类
 * @author zwm
 */
public class BitmapUtil {
    /**
     * 从bitmap转到ByteArray
     * @param bitmap
     * @return
     */
    public static byte[] Bitmap2ByteArray(Bitmap bitmap) {
        return Bitmap2ByteArray(bitmap, Bitmap.CompressFormat.JPEG, 100);
    }

    public static byte[] Bitmap2ByteArray(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        return baos.toByteArray();
    }

    /**
     * 将bitmap保存到文件
     * @param bitmap
     * @param format 格式
     * @param quality  压缩质量, 取值为 0 ~ 100  Some formats, like PNG which is lossless, will ignore the quality setting
     * @param path 要保存到的文件路径
     */
    public static void Bitmap2File(Bitmap bitmap, String path, Bitmap.CompressFormat format, int quality) {
        File file = new File(path);//设置文件名称
        String strDirectory = PathUtil.getDirectory(path);
        File directory = new File(strDirectory);

        // 先确定目录是否存在，如果不存在createNewFile会抛出异常，所以如果目录不存在，则创建
        FileUtil.createOrExistsDir(directory);

        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(format, quality, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


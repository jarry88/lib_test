package com.ftofs.twant.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 文件操作
 * @author zwm
 */
public class FileUtil {
    /*
    openFileOutput
    打開數據文件，适合放一些比较重要的数据，可在【系统-应用-清除数据】把数据清理掉

    Context.getCacheDir()
    這個目錄適合放置臨時數據，可以通过【系统-应用-清空缓存】可以把这些数据清除掉
     */
    /**
     * 獲取緩存文件
     * @param context
     * @param path 文件名： path前面不帶 / ，例如: a.txt 或 test/a.txt 或 test1/test2/a.txt
     * @return
     */
    public static File getCacheFile(Context context, String path) {
        File cacheDir = context.getCacheDir();
        String subDir = PathUtil.getDirectory(path);

        if (!StringUtil.isEmpty(subDir)) {
            cacheDir = new File(cacheDir, subDir);
            if (!cacheDir.exists()) {
                boolean success = cacheDir.mkdirs();
                if (!success) {
                    return null;
                }
            }
        }


        String filename = PathUtil.getFilename(path);
        return new File(cacheDir, filename);
    }


    /**
     * 根据Uri获取图片文件的绝对路径
     * @param context  一般传入Activity
     * @param uri
     * @return
     */
    public static String getRealFilePath(Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 讀取小文件
     * @param file
     * @return
     */
    public static byte[] readFile(File file) {
        if (!file.isFile()) {
            return null;
        }

        long size = file.length();
        byte[] buffer = new byte[(int) size];

        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(buffer);
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

package com.ftofs.twant.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.ftofs.twant.log.SLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;

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
     * 獲取壓縮后的圖片文件(同步方式)
     * @param context
     * @param file
     * @return
     */
    public static File getCompressedImageFile(Context context, File file) {
        try {
            List<File> fileList = Luban.with(context).load(file).get();

            if (fileList != null && fileList.size() > 0) {
                return fileList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

    public static String getFileProviderAuthority(Context context) {
        // 返回这个字符串  com.ftofs.twant.fileprovider
        return context.getPackageName() + ".fileprovider";
    }

    public static Uri getCompatUriFromFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
                /*
                从Android 7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，会抛出一个FileUriExposedException异常(参考《第一行代码(第2版)》第8章)
                FileProvider则是一种特殊的内容提供器，它使用了和内容提供器类似的机制来对数据进行保护，可以选择性地将封装过的Uri【共享给外部】，从而提高了应用的安全性。
                */
            String authority = getFileProviderAuthority(context);
            uri = FileProvider.getUriForFile(context, authority, file);
        }

        return uri;
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


    /**
     * 创建目录，并添加nomedia文件
     * @param dir
     */
    public static boolean createDirNomedia(File dir) throws IOException {
        List<String> folderList = new ArrayList<>();
        // 遍历每一层，都要加上nomedia文件
        while (!dir.exists()) {
            folderList.add(dir.getName());
            dir = dir.getParentFile();
        }

        for (int i = folderList.size() - 1; i >= 0; --i) {
            String folderName = folderList.get(i);
            dir = new File(dir, folderName);
            // 创建目录
            if (!dir.mkdir()) {
                return false;
            }
            // 在目录中新建.nomedia文件
            File nomediaFile = new File(dir, ".nomedia");
            nomediaFile.createNewFile();
        }

        return true;
    }

    /**
     * 获取App数据路径
     * 类似 /storage/emulated/0/twant 的这种路径
     * @return
     */
    public static String getAppDataRoot() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/twant";
    }

    public static String getImageRoot() {
        return getAppDataRoot() + "/DCIM";
    }

    /**
     * 獲取今天拍照的照片存放目錄(每天一個存放目錄)
     * @return
     */
    public static String getTodayImageRoot() {
        Jarbon jarbon = new Jarbon();
        return getImageRoot() + "/" + jarbon.format("Ymd");
    }


    /**
     * 如果指定的目錄不存在，則創建它
     * @param file 指定的目錄
     * @return
     */
    public static boolean createOrExistsDir(File file) {
        try {
            return file != null && (file.exists() ? file.isDirectory() : createDir(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建目录，并添加nomedia文件
     * @param dir
     */
    public static boolean createDir(File dir) throws IOException {
        List<String> folderList = new ArrayList<>();
        // 遍历每一层
        while (!dir.exists()) {
            folderList.add(dir.getName());
            dir = dir.getParentFile();
        }

        for (int i = folderList.size() - 1; i >= 0; --i) {
            String folderName = folderList.get(i);
            dir = new File(dir, folderName);
            // 创建目录
            if (!dir.mkdir()) {
                return false;
            }
        }

        return true;
    }
}

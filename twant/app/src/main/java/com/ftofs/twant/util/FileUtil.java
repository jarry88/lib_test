package com.ftofs.twant.util;

import android.content.Context;

import java.io.File;

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
}

package com.ftofs.twant.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.ftofs.twant.log.SLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.yokeyword.fragmentation.SupportActivity;

public class AssetsUtil {
    /**
     * 加载assets目录下的文本
     * @param context
     * @param filename  文件名，比如test.txt
     * @return
     */
    public static String loadText(Context context, String filename) {
        StringBuilder result = new StringBuilder();
        try{
            InputStream is = context.getResources().getAssets().open(filename);
            int ch;
            byte[] buffer = new byte[1024];
            while (-1 != (ch = is.read(buffer))){
                result.append(new String(buffer, 0, ch));
            }
            is.close();
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return null;
        }
        return result.toString();
    }

    /**
     * 将asset文件写入缓存
     */
    public static boolean copyAssetAndWrite(Context context, String fileName){
        try {
            File cacheDir=context.getCacheDir();
            if (!cacheDir.exists()){
                cacheDir.mkdirs();
            }
            File outFile =new File(cacheDir,fileName);
            if (!outFile.exists()){
                boolean res=outFile.createNewFile();
                if (!res){
                    return false;
                }
            } else {
                // 文件已經存在，跳過
                SLog.info("文件已經存在，大小[%d]", outFile.length());
                return true;
            }

            InputStream is=context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();

            SLog.info("file[%s], [%d], [%s]", outFile.getAbsolutePath(), outFile.length(), outFile.exists());
            return true;
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return false;
    }

    public static Typeface getTypeface(SupportActivity _mActivity, String path) {
        Typeface typeface =Typeface.createFromAsset(_mActivity.getAssets(), path);
        return typeface;
    }
}

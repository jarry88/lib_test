package com.ftofs.twant.util;

import android.util.Log;

import com.ftofs.twant.log.SLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具類
 * @author zwm
 */
public class MD5 {
    public static String get(String text) {
        String md5 = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            byte[] input = text.getBytes();  // 2 将消息变成byte数组
            byte[] buff = md.digest(input);  // 3 计算后获得字节数组,这就是那128位了
            md5 = Binary.toHexString(buff);  // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return md5;
    }


    /**
     * 获取文件的MD5值     参考：获取大文件MD5值（JAVA）  http://blog.csdn.net/zdwzzu2006/article/details/8064960
     * @param file
     * @return
     */
    public static String get(File file) {
        FileInputStream fileInputStream = null;
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }

            md5 =  Binary.toHexString(md.digest());
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }
        return md5;
    }
}

package com.ftofs.twant.log;

import android.util.Log;

import com.ftofs.twant.config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 在控制台中输出日志
 * 使用方法:
 *      SLog.info("n = %d", 666);
 * 输出类似如下内容:
 *      [2019-04-26 16:28:39.772][RxJavaDemo.java][00052]n = 666
 * @author zwm
 */
public class SLog {
    public static void info(String format, Object... args) {
        if (!Config.DEVELOPER_MODE) {
            return;
        }

        StackTraceElement[] traceArray = Thread.currentThread().getStackTrace();

        // 在Android中固定为3
        StackTraceElement trace = traceArray[3];
        String content = String.format(format, args);

        // 生成时间戳
        // 设置日期格式： 2019-04-26 16:28:39.772
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        // new Date()为获取当前系统时间
        String timestamp = sdf.format(new Date());

        String fileName = trace.getFileName();
        String lineNumber = String.format("%05d", trace.getLineNumber());

        String logContent = String.format("[%s][%s][%s]%s", timestamp, fileName, lineNumber, content);
        // 由于Logcat每条日志最大的长度是4096个字符，对于超出部分会进行截断处理，因此需要对于超长的日志进行分段处理
        boolean first = true;
        int limit = 3900;
        while (true) {
            String msg;
            if (logContent.length() > limit) {
                msg = logContent.substring(0, limit);
            } else {
                msg = logContent;
            }

            if (!first) {
                msg = "------>[" + msg + "]";
            }
            Log.e("SLog", msg);

            if (logContent.length() <= limit) {
                break;
            }
            logContent = logContent.substring(limit);
            first = false;
        }
    }
}


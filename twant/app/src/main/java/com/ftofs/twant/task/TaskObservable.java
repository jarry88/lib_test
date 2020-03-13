package com.ftofs.twant.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ftofs.twant.log.SLog;

/**
 * 耗時的任務
 * 在IO線程中執行
 * @author zwm
 */
public abstract class TaskObservable implements Runnable {
    TaskObserver taskObserver;
    Handler handler;

    public TaskObservable(TaskObserver taskObserver) {
        this.taskObserver = taskObserver;
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 做具體的耗時任務
     * @return 任務的完成結果
     */
    public abstract Object doWork();

    @Override
    public void run() {
        Object message = doWork();
        taskObserver.setMessage(message);
        // 任務完成，自動發送消息
        handler.post(taskObserver);
    }

    /**
     * 發送進度消息等
     * @param message
     */
    public void sendMessage(Object message) {
        try {
            // 必須創建一個新的TaskObserver實例，不然，多次調用sendMessage會互相覆蓋taskObserver.message
            TaskObserver anotherObserver = (TaskObserver) taskObserver.clone();
            anotherObserver.setMessage(message);
            handler.post(anotherObserver);
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }
}

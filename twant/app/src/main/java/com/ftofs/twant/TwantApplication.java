package com.ftofs.twant;

import android.app.Application;
import android.os.StrictMode;

import com.ftofs.twant.config.Config;
import com.ftofs.twant.handler.CrashHandler;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Test;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.User;
import com.orhanobut.hawk.Hawk;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * TwantApplication
 * @author zwm
 */
public class TwantApplication extends Application {
    // 線程等待隊列大小
    private static final int THREAD_QUEUE_SIZE = 1024;
    // 線程池大小
    private static final int THREAD_POOL_SIZE = 12;
    // 線程池
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();

        // 添加全局異常處理
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        // 在開發過程中，啟用 StrictMode
        if (Config.DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(
                    new StrictMode
                            .ThreadPolicy
                            .Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build());

            StrictMode.setVmPolicy(
                    new StrictMode
                            .VmPolicy
                            .Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .build());
        }

        // 初始化Hawk
        Hawk.init(this).build();

        // 初始化LitePal sqlite數據庫
        LitePal.initialize(this);
        // 注冊監聽器，可以在這里創建索引或填充數據等操作
        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                // fill some initial data
                SLog.info("創建數據庫[%s]完成", SqliteUtil.getDbName());
            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {
                // upgrade data in db
                SLog.info("升級數據庫[%s]完成", SqliteUtil.getDbName());
            }
        });

        /* 添加各個表，如果新增表，需要在這里添加表，然后增加數據庫版本號
           例如： SqliteUtil.addTables(Table1.class.getName(), Table2.class.getName()); */
        SqliteUtil.addTables(Test.class.getName());

        Fragmentation.builder()
                // show stack view. Mode: BUBBLE, SHAKE, NONE
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();

        // 初始化線程池
        executorService =
                new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(THREAD_QUEUE_SIZE));

        int userId = User.getUserId();
        if (userId > 0) {
            // 如果用戶已經登錄，則啟用用戶的數據庫
            SqliteUtil.switchUserDB(userId);
        }
    }


    public static ExecutorService getThreadPool() {
        return executorService;
    }
}

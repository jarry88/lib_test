package com.ftofs.twant.util;

import android.util.Log;

import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.SPField;
import com.gzp.lib_common.utils.SLog;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.litepal.LitePal;
import org.litepal.LitePalDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 數據庫相關工具類
 * @author zwm
 */
public class SqliteUtil {
    /**
     * 當前正在使用的數據庫表的List
     */
    private static List<String> tableList = new ArrayList<>();
    private static int count;

    /**
     * 切換用戶數據庫，主要是在用戶登錄時調用
     * 每個用戶一個數據庫，這樣就不會產生不同的登錄用戶產生數據混淆的問題
     * 數據庫命名
     *    user_db_<user_id>.db
     *    其中user_id為用戶Id，例如用戶Id為123的用戶使用的數據庫為 user_db_123.db
     * @param user_id 用戶Id
     * @return true -- 切換成功  false -- 切換失敗
     */
    public static boolean switchUserDB(int user_id) {
        if (user_id < 1) {
            // return false;
        }

        String dbName = getDbName();
        if (StringUtil.isEmpty(dbName)) {
            return false;
        }

        LitePalDB litePalDB = new LitePalDB(dbName, Config.DB_VERSION);

        // 添加各個表，如果新增表，需要在這里添加表
        for (String tableName : tableList) {
            litePalDB.addClassName(tableName);
        }

        LitePal.use(litePalDB);
        LitePal.getDatabase();

        ////////////////////////////
        // 切換用戶相關的操作
        // 檢查是否需要下載表情
        // TwantApplication.getThreadPool().execute(new DownloadEmojiTask());

        ///////////////////////////
        // 登錄環信
        ///////////////////////////
        // imLogin();  貌似不要在這里調用環信登錄，不然會與友盟的:channel進程有沖突


        return true;
    }

    /**
     * 登錄環信
     */
    public static void imLogin() {
        SLog.info("调用登录次数 %d,當前進程%s",count++,Thread.currentThread());

        EMClient emClient = EMClient.getInstance();
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
        String imToken = User.getUserInfo(SPField.FIELD_IM_TOKEN, null);
        SLog.info("memberName[%s], imToken[%s]", memberName, imToken);
        try {
            if (emClient != null && !StringUtil.isEmpty(memberName) && !StringUtil.isEmpty(imToken)) {
                emClient.loginWithToken(memberName, imToken, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        SLog.info("___threadId[%s]", Thread.currentThread().getId());
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        SLog.info("登录聊天服务器成功！");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        SLog.info("Error!登录聊天服务器失败,code[%d], message[%s]", code, message);
                    }
                });
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    /**
     * 獲取與當前用戶關聯的數據庫名稱
     * @return 如果用戶已經登錄，返回與當前用戶關聯的數據庫名稱; 如果用戶未登錄，返回null
     */
    public static String getDbName() {
        int user_id = User.getUserId();
        if (user_id < 1) {
            // return null;
        }
        // 每个用户一个数据库，数据库名称为 user_db_<user_id>.db
        return "user_db_" + user_id + ".db";
    }

    /**
     * 添加當前正在使用的表
     * 例如 addTables(table1, table2, ... , tableN);
     * @param tableNames
     */
    public static void addTables(String... tableNames) {
        for (String tableName : tableNames) {
            tableList.add(tableName);
        }
    }
}

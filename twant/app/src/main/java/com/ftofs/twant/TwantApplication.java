package com.ftofs.twant;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.orm.Test;
import com.ftofs.twant.orm.UserStatus;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.User;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.macau.pay.sdk.MPaySdk;
import com.macau.pay.sdk.base.ConstantBase;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.Fragmentation;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

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

    // IWXAPI是第三方app和微信通信的openapi接口
    public static IWXAPI wxApi;

    private static Context applicationContext = null;

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();

        //Bugly异常处理
        CrashReport.initCrashReport(getApplicationContext(), Config.BUGLY_KEY, Config.DEVELOPER_MODE);

        // 添加全局異常處理
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // crashHandler.init(getApplicationContext());

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
        SqliteUtil.addTables(Test.class.getName(), UserStatus.class.getName(), Emoji.class.getName(),
                FriendInfo.class.getName(), ImNameMap.class.getName());

        int fragmentationMode;
        if (Config.DEVELOPER_MODE) {
            fragmentationMode = Fragmentation.BUBBLE;
        } else {
            fragmentationMode = Fragmentation.NONE;
        }
        Fragmentation.builder()
                // show stack view. Mode: BUBBLE, SHAKE, NONE
                .stackViewMode(fragmentationMode)
                .debug(BuildConfig.DEBUG)
                .install();

        // 初始化線程池
        executorService =
                new ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>(THREAD_QUEUE_SIZE));

        initChat();

        int userId = User.getUserId();
        if (userId > 0) {
            // 如果用戶已經登錄，則啟用用戶的數據庫
            SqliteUtil.switchUserDB(userId);
        }

        // 設置時間處理工具的時區
        TimeZone tz = TimeZone.getDefault();
        DateTimeUtils.setTimeZone(tz.getID());


        // 設置MPay SDK環境, 默認 UAT 環境 // 0 ：生產，1：測試環境，2 :UAT
        if (Config.DEVELOPER_MODE) {
            ConstantBase.MPayPackageName = ConstantBase.mpay_uat_packetname;
            MPaySdk.setEnvironmentType(ConstantBase.ConnectUrl_UAT);
        } else {
            ConstantBase.MPayPackageName = ConstantBase.mpay_prd_packetname;
            MPaySdk.setEnvironmentType(ConstantBase.ConnectUrl_PRD);
        }

        // 初始化ZXing二維碼庫
        ZXingLibrary.initDisplayOpinion(this);

        // 打開SQLiteStudio
        SQLiteStudioService.instance().start(this);

        // MUST use app context to avoid memory leak!
        // or load with glide
        BigImageViewer.initialize(GlideImageLoader.with(this));

        // 將應用注冊到微信
        regToWx();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        SQLiteStudioService.instance().stop();
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        wxApi = WXAPIFactory.createWXAPI(this, getString(R.string.weixin_app_id), true);
        // 将应用的appId注册到微信
        wxApi.registerApp(getString(R.string.weixin_app_id));
    }

    public static ExecutorService getThreadPool() {
        return executorService;
    }

    /**
     * 初始化聊天功能
     */
    private void initChat() {
        int pid = android.os.Process.myPid();

        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            SLog.info("Warning!enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //初始化
        EMClient.getInstance().init(applicationContext, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
                SLog.info("onConnected");
            }

            @Override
            public void onDisconnected(int error) {
                SLog.info("onDisconnected, error[%d]", error);
                if(error == EMError.USER_REMOVED){
                    // 显示帐号已经被移除
                }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                } else {
                    /*
                    if (NetUtils.hasNetwork(MainActivity.this))
                    //连接不到聊天服务器
                else
                    //当前网络不可用，请检查网络设置
                    */
                }
            }
        });

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                SLog.info("onContactInvited, username[%s],reason[%s]", username, reason);
            }

            @Override
            public void onFriendRequestAccepted(String s) {
                //好友请求被同意
                SLog.info("onFriendRequestAccepted, s[%s]", s);
            }

            @Override
            public void onFriendRequestDeclined(String s) {
                //好友请求被拒绝
                SLog.info("onFriendRequestDeclined, s[%s]", s);
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                SLog.info("onContactDeleted, username[%s]", username);
            }

            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                SLog.info("onContactAdded, username[%s]", username);
            }
        });

        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                SLog.info("收到消息，條數[%d]", messages.size());
                for (EMMessage message : messages) {
                    String msgId = message.getMsgId();
                    EMMessage.Type type = message.getType();
                    EMMessageBody body = message.getBody();
                    String from = message.getFrom();
                    String to = message.getTo();
                    SLog.info("msgId[%s], type[%s], from[%s], to[%s], body[%s]", msgId, type, from, to, body.toString());


                    ChatMessage chatMessage = new ChatMessage();

                    chatMessage.timestamp = message.getMsgTime();
                    chatMessage.origin = ChatMessage.YOUR_MESSAGE;
                    chatMessage.avatar = "";
                    chatMessage.fromMemberName = message.getFrom();
                    chatMessage.toMemberName = message.getTo();
                    chatMessage.nickname = "";
                    chatMessage.messageType = ChatUtil.getIntMessageType(message);
                    if (type == EMMessage.Type.TXT) {
                        SLog.info("收到文本消息");
                        chatMessage.content = StringUtil.getEMMessageText(message.getBody().toString());


                    } else if (type == EMMessage.Type.IMAGE) {
                        SLog.info("收到圖片消息");
                        EMImageMessageBody imageMessageBody = (EMImageMessageBody) message.getBody();
                        String localUrl = imageMessageBody.getLocalUrl();
                        String remoteUrl = imageMessageBody.getRemoteUrl();

                        chatMessage.content = EasyJSONObject.generate("absolutePath", localUrl, "imgUrl", remoteUrl).toString();
                        SLog.info("chatMessage.content[%s]", chatMessage.content);
                    } else {
                        SLog.info("收到不支持的消息");
                    }

                    SLog.info("chatMessage[%s]", chatMessage);

                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE, chatMessage);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
                SLog.info("收到透传消息");
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {
                //收到已读回执
                SLog.info("收到已读回执");
            }

            @Override
            public void onMessageDelivered(List<EMMessage> message) {
                //收到已送达回执
                SLog.info("收到已送达回执");
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                //消息被撤回
                SLog.info("消息被撤回");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
                SLog.info("消息状态变动");
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}

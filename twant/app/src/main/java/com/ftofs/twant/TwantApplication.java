package com.ftofs.twant;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.StrictMode;

import androidx.core.app.NotificationCompat;

import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.ChatMessage;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.TimeInfo;
import com.ftofs.twant.fragment.MessageFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Conversation;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.orm.Test;
import com.ftofs.twant.orm.UserStatus;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.HawkUtil;
import com.ftofs.twant.util.MsgNotifyReceiver;
import com.ftofs.twant.util.SqliteUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.vo.member.MemberVo;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.huawei.hms.aaid.HmsInstanceId;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.push.EMPushConfig;
import com.macau.pay.sdk.MPaySdk;
import com.macau.pay.sdk.base.ConstantBase;
import com.orhanobut.hawk.Hawk;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONException;
import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.Fragmentation;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;
import static com.orhanobut.hawk.Hawk.init;

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

    String umengDeviceToken;


    // IWXAPI是第三方app和微信通信的openapi接口
    public static IWXAPI wxApi;

    private static TwantApplication instance = null;
    private NotificationManager mNotificationManager;
    private int notificationId;
    private int count;

    public static TwantApplication getInstance() {
        return instance;
    }

    PushAgent mPushAgent;
    //保存當前會員身份信息，目前主要用於取得role，來判斷用戶身份
    private MemberVo currMemberInfo=null;
    @Override
    public void onCreate() {
        super.onCreate();

        SLog.info("Launch performance...");

        int pid = android.os.Process.myPid();

        String processAppName = getAppName(pid);
        SLog.info("TwantApplication::onCreate(), pid[%d], processName[%s]", pid, processAppName);
        instance = this;

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

        // 解决android 7.0以上版本 exposed beyond app through ClipData.Item.getUri()问题
        // 參考：https://www.jianshu.com/p/8dd6054051af
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        // 初始化Hawk
        init(this).build();

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
                FriendInfo.class.getName(), ImNameMap.class.getName(), Conversation.class.getName());

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
        //保存一些伴隨app生命進程的變量
        currMemberInfo = new MemberVo();//初始化防止空指針
//        updateCurrMemberInfo();
        //初始化Im消息推送通知
        initNotification();
        initChat();
        int userId = User.getUserId();

        SqliteUtil.switchUserDB(userId);
        if (isMainPid()) {
            SqliteUtil.imLogin();
        }


        // 設置時間處理工具的時區
        TimeZone tz = TimeZone.getDefault();
        DateTimeUtils.setTimeZone(tz.getID());


        // 設置MPay SDK環境, 默認 UAT 環境 // 0 ：生產，1：測試環境，2 :UAT
        if (Config.DEVELOPER_MODE) {
            ConstantBase.setMPayPackageName(ConstantBase.ConnectUrl_UAT);
            MPaySdk.setEnvironmentType(ConstantBase.ConnectUrl_UAT);
        } else {
            ConstantBase.setMPayPackageName(ConstantBase.ConnectUrl_PRD);
            MPaySdk.setEnvironmentType(ConstantBase.ConnectUrl_PRD);
        }

        // 初始化ZXing二維碼庫
        ZXingLibrary.initDisplayOpinion(this);

        // 打開SQLiteStudio
        SQLiteStudioService.instance().start(this);

        // MUST use app context to avoid memory leak!
        // or load with glide
        BigImageViewer.initialize(GlideImageLoader.with(this));

        initUmeng();

        initAmapLocation();

        // 將應用注冊到微信
        regToWx();
        //創建通知等級
    }


    private void initNotification() {
        //创建自定义通知渠道，和全局通知管理器
        if (!Hawk.contains(SPField.USER_RECEIVE_NEWS)) {
            SLog.info("news  newsDetail 不存在");
            Hawk.put(SPField.USER_RECEIVE_NEWS,true);
            Hawk.put(SPField.USER_RECEIVE_NEWS_DETAIL,true);
            SLog.info("news  newsDetail %s",Hawk.contains(SPField.USER_RECEIVE_NEWS));
        }
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "imChannel";
        CharSequence channelName = "New Message";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.tw_red);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
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


    private void initUmeng() {
        SLog.info("initUmeng");
        int pid = android.os.Process.myPid();

        String processAppName = getAppName(pid);
        SLog.info("Umeng init processName[%s], pid[%d]", processAppName, pid);
        // mPushAgent.register方法应该在主进程和channel进程中都被调用
        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            // SLog.info("Warning!UMENG enter the service process!processAppName[%s]", processAppName);

            // 则此application::onCreate 是被service 调用的，直接返回
            // return;
        }


        if (Config.DEVELOPER_MODE) {
            UMConfigure.setLogEnabled(true);
        }
        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
        // 参数一：当前上下文context；
        // 参数二：应用申请的Appkey（需替换）；
        // 参数三：渠道名称；
        // 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
        // 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, getString(R.string.umeng_push_app_key), "official", UMConfigure.DEVICE_TYPE_PHONE, getString(R.string.umeng_push_message_secret));

        // 友盟統計：选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //获取消息推送代理示例
        mPushAgent = PushAgent.getInstance(this);
        // mPushAgent.setNotificaitonOnForeground(false); // 如果应用在前台，您可以设置不显示通知栏消息。默认情况下，应用在前台是显示通知的。此方法请在mPushAgent.register方法之前调用。

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            /**
             * 通知的回调方法（通知送达时会回调）
             */
            @Override
            public void dealWithNotificationMessage(Context context, UMessage msg) {
                SLog.info("dealWithNotificationMessage, msg[%s]", dumpUmengMessage(msg));
                //调用super，会展示通知，不调用super，则不展示通知。
                super.dealWithNotificationMessage(context, msg);
            }

            /**
             * 自定义消息的回调方法
             */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                SLog.info("dealWithCustomMessage, msg[%s]", dumpUmengMessage(msg));
            }

            /**
             * 自定义通知栏样式的回调方法
             */
            @Override
            public Notification getNotification(Context context, UMessage uMessage) {
                SLog.info("getNotification, msg[%s]", dumpUmengMessage(uMessage));
                return super.getNotification(context, uMessage);
            }
        };
        mPushAgent.setMessageHandler(messageHandler);


        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

            @Override
            public void launchApp(Context context, UMessage msg) {
                super.launchApp(context, msg);
            }

            @Override
            public void openUrl(Context context, UMessage msg) {
                super.openUrl(context, msg);
            }

            @Override
            public void openActivity(Context context, UMessage msg) {
                super.openActivity(context, msg);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                SLog.info("dealWithCustomAction, message[%s]", msg.custom);
            }
        };
        //使用自定义的NotificationHandler
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        SLog.info("mPushAgent.register");
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                SLog.info("mPushAgent.register 注册成功：deviceToken：-------->  " + deviceToken);
                umengDeviceToken = deviceToken;

                setUmengAlias(Constant.ACTION_ADD);
            }
            @Override
            public void onFailure(String s, String s1) {
                SLog.info("mPushAgent.register 注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
    }

    public String getUmengDeviceToken() {
        return umengDeviceToken;
    }

    /**
     * 格式化友盟消息
     * @param msg
     * @return
     */
    private String dumpUmengMessage(UMessage msg) {
        EasyJSONObject extra = EasyJSONObject.generate();
        try {
            for (String key : msg.extra.keySet()) {
                extra.set(key, msg.extra.get(key));
            }
        } catch (Exception e) {

        }

        return String.format("custom[%s], extra[%s]", msg.custom, extra.toString());
    }


    public void setUmengAlias(int action) {
        if (mPushAgent == null) {
            return;
        }
        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
        if (StringUtil.isEmpty(memberName)) {
            return;
        }

        if (StringUtil.isEmpty(umengDeviceToken)) {
            return;
        }
        if (action == Constant.ACTION_ADD) {
            mPushAgent.addAlias(memberName, Constant.UMENG_ALIAS_TYPE, new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean b, String s) {
                    SLog.info("addAlias.UTrack.ICallBack.onMessage[%s][%s]", b, s);
                }
            });
        } else if (action == Constant.ACTION_EDIT) {
            mPushAgent.setAlias(memberName, Constant.UMENG_ALIAS_TYPE, new UTrack.ICallBack() {
                @Override
                public void onMessage(boolean b, String s) {
                    SLog.info("setAlias.UTrack.ICallBack.onMessage[%s][%s]", b, s);
                }
            });
        }
    }

    public void delUmengAlias() {
        if (mPushAgent == null) {
            return;
        }

        String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
        if (StringUtil.isEmpty(memberName)) {
            return;
        }

        mPushAgent.deleteAlias(memberName, Constant.UMENG_ALIAS_TYPE, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                SLog.info("deleteAlias.UTrack.ICallBack.onMessage[%s][%s]", isSuccess, message);
            }
        });
    }


    /**
     * 初始化聊天功能
     */
    private void initChat() {


        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (!isMainPid()) {
            return;
        }

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        EMPushConfig.Builder builder = new EMPushConfig.Builder(getApplicationContext());
        builder.enableHWPush();
        options.setPushConfig(builder.build());
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //初始化
        EMClient.getInstance().init(instance, options);
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

        //IM消息处理源头
        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                //showNotification();
                SLog.info("收到消息，條數[%d]", messages.size());
                boolean showNotification=HawkUtil.getUserData(SPField.USER_RECEIVE_NEWS, true);
                boolean showNotificationDetail=showNotification&&HawkUtil.getUserData(SPField.USER_RECEIVE_NEWS_DETAIL, true);
                boolean isMessageActivity = isMessageFragmentActivity();

                for (EMMessage message : messages) {
                    String msgId = message.getMsgId();
                    EMMessage.Type type = message.getType();
                    EMMessageBody body = message.getBody();
                    Map<String, Object> ext = message.ext();
                    String from = message.getFrom();
                    String to = message.getTo();
                    SLog.info("msgId[%s], type[%s], from[%s], to[%s], body[%s],ext[%s]，", msgId, type, from, to, body.toString(),ext);

                    // 獲取拓展字段
                    String extNickname = message.getStringAttribute("nickName", "");
                    String extAvatar = message.getStringAttribute("avatar", "");
                    int extRole = Integer.valueOf(message.getStringAttribute("role", "-1"));//舊版取角色方法暫時保留，按-1默認值暫時理解爲好友狀態,但是這裏還是有問題
//                    int extMemberRole = Integer.valueOf(message.getStringAttribute("role", "0"));//暫時用extRole取from的商城身份，不預留好友身份字段，但是這裏應該後面會有小問題，2020/3/8
                    String storeName = message.getStringAttribute("storeName", "");
                    int storeId = Integer.valueOf(message.getStringAttribute("storeId", "0"));
                    long extSendTime = Long.valueOf(message.getStringAttribute("sendTime", "-1"));
                    String em_push_content ="收到新消息";
                    try {
                        SLog.info("em_apans_ext %s",message.getJSONObjectAttribute("em_apns_ext").toString());
                        em_push_content = message.getJSONObjectAttribute("em_apns_ext").getString("em_push_content");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //新增getext獲取拓展字段，參考ios
                    SLog.info("拓展字段, extNickname[%s], extAvatar[%s], extRole[%s], extSendTime[%s],\n messageToString[%s]\n getfrom[%s],getTo[%s],", extNickname, extAvatar, extRole, extSendTime,message.toString(),message.getFrom(),message.getTo());

                    FriendInfo.upsertFriendInfo(from, extNickname, extAvatar, extRole,extRole,storeName,storeId);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.timestamp = message.getMsgTime();
                    chatMessage.origin = ChatMessage.YOUR_MESSAGE;
                    chatMessage.avatar = extAvatar;
                    chatMessage.fromMemberName = message.getFrom();
                    chatMessage.toMemberName = message.getTo();
                    chatMessage.nickname = extNickname;
                    chatMessage.messageType = ChatUtil.getIntMessageType(message);
                    chatMessage.ext = message.ext();


                    // 更新FriendInfo
                    FriendInfo.upsertFriendInfo(chatMessage.toMemberName, extNickname, extAvatar, extRole,extRole,storeName,storeId);//這裏爲甚要更新
                    // 更新conversation屬性
                    String imNickName = extNickname;
                    //按照4156需求，對話框顯示的客服名稱應該為 店鋪 客服昵稱
                    if (extRole>ChatUtil.ROLE_MEMBER) {
                        imNickName = storeName + " " + extNickname;
                    }
                    //这里有什么用
                    ChatUtil.getConversation(message.getFrom(), imNickName, extAvatar, extRole);

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
                    if (showNotificationDetail&&!isMessageActivity) {//設置允許推送，并且消息類頁面不活躍時可以推送
                        pushNotification(chatMessage,em_push_content);
                    }
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE, chatMessage);
                }
                if (showNotification && !showNotificationDetail&&!isMessageActivity) {//設置不顯示詳情，并且消息類頁面不在前臺時要推送
                    ChatMessage chatMessage = new ChatMessage();
                    String pushContent = String.format("你收到了%d條想聊消息", messages.size());
                    pushNotification(chatMessage,pushContent);
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
                SLog.info("消息状态变动, message[%s], change[%s]", message, change);
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    private boolean isMessageFragmentActivity() {
        MainActivity mainActivity = MainActivity.getInstance();
        if (mainActivity != null&&mainActivity.getMessageFragmentsActivity()) {
            return true;
        }
        return false;
    }

    private boolean isMainPid() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            SLog.info("Warning!enter the service process!processAppName[%s]", processAppName);

            // 则此application::onCreate 是被service 调用的，直接返回
            return false;
        }
        return true;
    }

    private void pushNotification(ChatMessage chatMessage,String pushContent) {
//        try {
//            URL url=new URL(chatMessage.avatar);

            Notification notification = null;
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constant.MSG_NOTIFY_HXID, "chatMessage.fromMemberName");
            Intent s[]=new Intent[1];
//            s[0] = intent;
//            ToastUtil.success(getApplicationContext(),intent.getExtras().toString());
            PendingIntent pi = getActivity(getApplicationContext(),1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
//            SLog.info(intent.getExtras().toString());
//            try {
                notification = new NotificationCompat.Builder(getApplicationContext(), "imChannel")
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(pushContent)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setLargeIcon(BitmapFactory.decodeStream(url.openStream()))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.shape_btn_corners_solid))
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .build();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            mNotificationManager.notify(count++, notification);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    private void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon__bottom_bar_cart)
                .setContentTitle("textTitle")
                .setContentText("textContent")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            String channelId = "some_channel_id";
            CharSequence channelName = "Some Channel";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.tw_red);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(notificationChannel);
            Bitmap myBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon_to_be_paid);
            builder=new NotificationCompat.Builder(getApplicationContext(),notificationChannel.getId())
                    .setSmallIcon(R.drawable.icon_enc_mini)
                    .setContentTitle("imageTitle")
                    .setContentText("cription")
                    .setLargeIcon(myBitmap)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(myBitmap)
                            .bigLargeIcon(null));
        }


        mNotificationManager.notify(notificationId++,builder.build());
    }

    /**
     * 初始化高德地圖定位
     */
    private void initAmapLocation() {
        int pid = android.os.Process.myPid();

        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//        twLocation = new TwLocation(this);
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

    public static String getStringRes(int resId) {
        if (instance != null) {
            return instance.getString(resId);
        }
        return null;
    }

    public void updateCurrMemberInfo() {
        ApiUtil.getImInfo(getApplicationContext(),User.getUserInfo(SPField.FIELD_MEMBER_NAME,null),(memberVo)->{
            currMemberInfo = (MemberVo) memberVo;
            currMemberInfo.getFromInterface = true;
            SLog.info("成功獲取了當前登錄用戶的信息");
        });
    }

    public MemberVo getMemberVo() {
        return currMemberInfo;
    }

    public void setMemberVo(MemberVo memberVo) {
        this.currMemberInfo = memberVo;
    }

}

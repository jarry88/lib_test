package com.ftofs.twant

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Process
import android.os.StrictMode
import android.util.Log
import androidx.core.app.NotificationCompat
import cn.snailpad.easyjson.EasyJSONObject
import com.ftofs.twant.activity.MainActivity
import com.ftofs.twant.config.Config
import com.ftofs.twant.constant.Constant
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.constant.SPField
import com.ftofs.twant.di.koinModule
import com.ftofs.twant.entity.ChatMessage
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.MainFragment
import com.ftofs.twant.orm.*
import com.ftofs.twant.util.*
import com.ftofs.twant.vo.member.MemberVo
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.gzp.lib_common.base.BaseApplication
import com.gzp.lib_common.utils.AppUtil
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMContactListener
import com.hyphenate.EMError
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMImageMessageBody
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions
import com.hyphenate.push.EMPushConfig
import com.macau.pay.sdk.MPaySdk
import com.macau.pay.sdk.base.ConstantBase
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.TokenResultListener
import com.orhanobut.hawk.Hawk
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.uuzuche.lib_zxing.activity.ZXingLibrary
import me.yokeyword.fragmentation.Fragmentation
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.litepal.LitePal
import org.litepal.tablemanager.callback.DatabaseListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TwantApplication :BaseApplication(){
    // 線程等待隊列大小
    private val THREAD_QUEUE_SIZE = 1024

    // 線程池大小
    private val THREAD_POOL_SIZE = 12
    var wxApi: IWXAPI? = null

    companion object{
        // 線程池
        private var executorService: ExecutorService? = null
//        var instance: TwantApplication? = null

        private var instance: TwantApplication? = null
            get() {
                if (field == null) {
                    field = TwantApplication()
                }
                return field
            }
        fun getThreadPool(): ExecutorService {
            return executorService!!
        }

        fun get(): TwantApplication {
            return instance!!
        }
    }

    // IWXAPI是第三方app和微信通信的openapi接口

    private var mNotificationManager: NotificationManager? = null
    private var notificationId = 0
    private var count = 0


    //保存當前會員身份信息，目前主要用於取得role，來判斷用戶身份
    var memberVo: MemberVo? = null
    init { //使用static代码段可以防止内存泄漏
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> ClassicsHeader(context) }

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshFooterCreator{ context, _ ->  ClassicsFooter(context).setDrawableSize(20f) }
    }

    override fun onCreate() {
        super.onCreate()
        instance=this
        BaseContext.instance.init(this)//初始話instance

        AppUtil.app=this
        PhoneNumberAuthHelper.getInstance(this,object :TokenResultListener{
            override fun onTokenFailed(p0: String?) {
                Log.e("init", "onTokenFaild: $p0")
            }

            override fun onTokenSuccess(p0: String?) {
                Log.e("init", "onTokenSuccess: $p0")
            }

        }).apply { setAuthSDKInfo("gg+CTOzZf+lDYnx+JNodeuceDBDHIef/DjPospYtY8puMEuVeUJM8lS7elL36rSB+oeAV1Rli9rGMnrMxnbJ4kCNEEo46l/l1VzH+q92nrd4du5f9KHBZ+e6uFt9i7WznSBR1s+/0LLl8CCD9F10NpH4yPa5xkY0LvDP1xgCNPZDn70mPq0Dl3vZz7TdGEaZ3euShG5sa04hFZiMN34YidOfHwr6SVRu37Mz9ehOHsLnoeCzgx9IkICa3KI2nPTjlniBi+bkj9CDq6iK6u6NNlODVfsrZcar")
            reporter.setLoggerEnable(BuildConfig.DEBUG)
        }

        initUmeng(this){MainFragment.getInstance()?.let { it.handleUmengCustomAction() }}
        startKoin {
            androidLogger()
            androidContext(this@TwantApplication)
            koin.loadModules(koinModule)
            koin.createRootScope()
        }
        SLog.info("Launch performance...")

        val processAppName=getAppName(android.os.Process.myPid())
        SLog.info("TwantApplication::onCreate(), processName[%s]", processAppName)


        //Bugly异常处理
        if (!Config.DEVELOPER_MODE) {
            SLog.info("initReleaseBugly")
            CrashReport.initCrashReport(applicationContext, Config.BUGLY_KEY, Config.DEVELOPER_MODE)
        } else {
            SLog.info("initDebugBugly")
            CrashReport.initCrashReport(applicationContext, Config.DEBUG_BUGLY_KEY, Config.DEVELOPER_MODE)
        }
        // 在開發過程中，啟用 StrictMode

        // 添加全局異常處理
        // CrashHandler crashHandler = CrashHandler.getInstance();
        // crashHandler.init(getApplicationContext());

        // 在開發過程中，啟用 StrictMode
        if (Config.DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(
                    StrictMode.ThreadPolicy.Builder()
                            .detectDiskReads()
                            .detectDiskWrites()
                            .detectDiskWrites()
                            .detectNetwork()
                            .penaltyLog()
                            .build())
            StrictMode.setVmPolicy(
                    StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .build())
        }

        // 解决android 7.0以上版本 exposed beyond app through ClipData.Item.getUri()问题
        // 參考：https://www.jianshu.com/p/8dd6054051af

        // 解决android 7.0以上版本 exposed beyond app through ClipData.Item.getUri()问题
        // 參考：https://www.jianshu.com/p/8dd6054051af
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        // 初始化Hawk

        // 初始化Hawk
        Hawk.init(this).build()

        // 初始化LitePal sqlite數據庫

        // 初始化LitePal sqlite數據庫
        LitePal.initialize(this)
        // 注冊監聽器，可以在這里創建索引或填充數據等操作
        // 注冊監聽器，可以在這里創建索引或填充數據等操作
        LitePal.registerDatabaseListener(object : DatabaseListener {
            override fun onCreate() {
                // fill some initial data
                SLog.info("創建數據庫[%s]完成", SqliteUtil.getDbName())
            }

            override fun onUpgrade(oldVersion: Int, newVersion: Int) {
                // upgrade data in db
                SLog.info("升級數據庫[%s]完成", SqliteUtil.getDbName())
            }
        })

        /* 添加各個表，如果新增表，需要在這里添加表，然后增加數據庫版本號
           例如： SqliteUtil.addTables(Table1.class.getName(), Table2.class.getName()); */

        /* 添加各個表，如果新增表，需要在這里添加表，然后增加數據庫版本號
           例如： SqliteUtil.addTables(Table1.class.getName(), Table2.class.getName()); */SqliteUtil.addTables(Test::class.java.name, UserStatus::class.java.name, Emoji::class.java.name,
                FriendInfo::class.java.name, ImNameMap::class.java.name, Conversation::class.java.name)

        val fragmentationMode: Int
        fragmentationMode = if (Config.DEVELOPER_MODE) {
            Fragmentation.NONE
        } else {
            Fragmentation.NONE
        }
        Fragmentation.builder() // show stack view. Mode: BUBBLE, SHAKE, NONE
                .stackViewMode(fragmentationMode)
                .debug(BuildConfig.DEBUG)
                .install()


        // 初始化線程池
        executorService = ThreadPoolExecutor(THREAD_POOL_SIZE, THREAD_POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue<Runnable>(THREAD_QUEUE_SIZE))
        //保存一些伴隨app生命進程的變量
        //保存一些伴隨app生命進程的變量
        memberVo = MemberVo() //初始化防止空指針
//        updateCurrMemberInfo();
        //初始化Im消息推送通知
        initNotification()
        initChat()
        val userId = User.getUserId()

        SqliteUtil.switchUserDB(userId)
        if (isMainPid()) {
            SqliteUtil.imLogin()
        }


        // 設置MPay SDK環境, 默認 UAT 環境 // 0 ：生產，1：測試環境，2 :UAT
        if (Config.DEVELOPER_MODE) {
            ConstantBase.setMPayPackageName(ConstantBase.ConnectUrl_UAT)
            MPaySdk.setEnvironmentType(ConstantBase.ConnectUrl_UAT)
        } else {
            ConstantBase.setMPayPackageName(ConstantBase.ConnectUrl_PRD)
            MPaySdk.setEnvironmentType(ConstantBase.ConnectUrl_PRD)
        }

        // 初始化ZXing二維碼庫

        // 初始化ZXing二維碼庫
        ZXingLibrary.initDisplayOpinion(this)


        // MUST use app context to avoid memory leak!
        // or load with glide


        // MUST use app context to avoid memory leak!
        // or load with glide
        BigImageViewer.initialize(GlideImageLoader.with(this))

        initAmapLocation()

        // 將應用注冊到微信

        // 將應用注冊到微信
        regToWx()
    }

    private fun initNotification() {
        //创建自定义通知渠道，和全局通知管理器
        if (!Hawk.contains(SPField.USER_RECEIVE_NEWS)) {
            SLog.info("news  newsDetail 不存在")
            Hawk.put(SPField.USER_RECEIVE_NEWS, true)
            Hawk.put(SPField.USER_RECEIVE_NEWS_DETAIL, true)
            SLog.info("news  newsDetail %s", Hawk.contains(SPField.USER_RECEIVE_NEWS))
        }
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "imChannel"
        val channelName: CharSequence = "New Message"
        val importance = NotificationManager.IMPORTANCE_HIGH
//        var notificationChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = R.color.tw_red
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            mNotificationManager?.createNotificationChannel(notificationChannel)
        }
    }
    private fun regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        wxApi = WXAPIFactory.createWXAPI(this, getString(R.string.weixin_app_id), true)
        // 将应用的appId注册到微信
        wxApi?.registerApp(getString(R.string.weixin_app_id))
    }



    /**
     * 初始化聊天功能
     */
    private fun initChat() {

        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (!isMainPid()) {
            return
        }
        val options = EMOptions()
        // 默认添加好友时，是不需要验证的，改成需要验证
        val builder = EMPushConfig.Builder(applicationContext)
        builder.enableHWPush()
        options.pushConfig = builder.build()
        options.acceptInvitationAlways = false
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.autoTransferMessageAttachments = true
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true)

        //初始化
        EMClient.getInstance().init(this, options)
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true)

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(object : EMConnectionListener {
            override fun onConnected() {
                SLog.info("onConnected")
            }

            override fun onDisconnected(error: Int) {
                SLog.info("onDisconnected, error[%d]", error)
                if (error == EMError.USER_REMOVED) {
                    // 显示帐号已经被移除
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
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
        })
        EMClient.getInstance().contactManager().setContactListener(object : EMContactListener {
            override fun onContactInvited(username: String, reason: String) {
                //收到好友邀请
                SLog.info("onContactInvited, username[%s],reason[%s]", username, reason)
            }

            override fun onFriendRequestAccepted(s: String) {
                //好友请求被同意
                SLog.info("onFriendRequestAccepted, s[%s]", s)
            }

            override fun onFriendRequestDeclined(s: String) {
                //好友请求被拒绝
                SLog.info("onFriendRequestDeclined, s[%s]", s)
            }

            override fun onContactDeleted(username: String) {
                //被删除时回调此方法
                SLog.info("onContactDeleted, username[%s]", username)
            }

            override fun onContactAdded(username: String) {
                //增加了联系人时回调此方法
                SLog.info("onContactAdded, username[%s]", username)
            }
        })

        //IM消息处理源头
        val msgListener: EMMessageListener = object : EMMessageListener {
            override fun onMessageReceived(messages: List<EMMessage>) {
                //收到消息
                //showNotification();
                SLog.info("收到消息，條數[%d]", messages.size)
                val showNotification = HawkUtil.getUserData(SPField.USER_RECEIVE_NEWS, true)
                val showNotificationDetail = showNotification && HawkUtil.getUserData(SPField.USER_RECEIVE_NEWS_DETAIL, true)
                val isMessageActivity: Boolean = isMessageFragmentActivity()
                for (message in messages) {
                    val msgId = message.msgId
                    val type = message.type
                    val body = message.body
                    val ext = message.ext()
                    val from = message.from
                    val to = message.to
                    SLog.info("msgId[%s], type[%s], from[%s], to[%s], body[%s],ext[%s]，", msgId, type, from, to, body.toString(), ext)

                    // 獲取拓展字段
                    val extNickname = message.getStringAttribute("nickName", "")
                    val extAvatar = message.getStringAttribute("avatar", "")
                    val extRole = Integer.valueOf(message.getStringAttribute("role", "-1")) //舊版取角色方法暫時保留，按-1默認值暫時理解爲好友狀態,但是這裏還是有問題
                    //                    int extMemberRole = Integer.valueOf(message.getStringAttribute("role", "0"));//暫時用extRole取from的商城身份，不預留好友身份字段，但是這裏應該後面會有小問題，2020/3/8
                    val storeName = message.getStringAttribute("storeName", "")
                    val storeId = Integer.valueOf(message.getStringAttribute("storeId", "0"))
                    val extSendTime = java.lang.Long.valueOf(message.getStringAttribute("sendTime", "-1"))
                    var em_push_content: String? = "收到新消息"
                    try {
                        SLog.info("em_apans_ext %s", message.getJSONObjectAttribute("em_apns_ext").toString())
                        em_push_content = message.getJSONObjectAttribute("em_apns_ext").getString("em_push_content")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    //新增getext獲取拓展字段，參考ios
                    SLog.info("拓展字段, extNickname[%s], extAvatar[%s], extRole[%s], extSendTime[%s],\n messageToString[%s]\n getfrom[%s],getTo[%s],", extNickname, extAvatar, extRole, extSendTime, message.toString(), message.from, message.to)
                    FriendInfo.upsertFriendInfo(from, extNickname, extAvatar, extRole, extRole, storeName, storeId)
                    val chatMessage = ChatMessage()
                    chatMessage.timestamp = message.msgTime
                    chatMessage.origin = ChatMessage.YOUR_MESSAGE
                    chatMessage.avatar = extAvatar
                    chatMessage.fromMemberName = message.from
                    chatMessage.toMemberName = message.to
                    chatMessage.nickname = extNickname
                    chatMessage.messageType = ChatUtil.getIntMessageType(message)
                    chatMessage.ext = message.ext()


                    // 更新FriendInfo
                    FriendInfo.upsertFriendInfo(chatMessage.toMemberName, extNickname, extAvatar, extRole, extRole, storeName, storeId) //這裏爲甚要更新
                    // 更新conversation屬性
                    var imNickName = extNickname
                    //按照4156需求，對話框顯示的客服名稱應該為 店鋪 客服昵稱
                    if (extRole > ChatUtil.ROLE_MEMBER) {
                        imNickName = "$storeName $extNickname"
                    }
                    //这里有什么用
                    ChatUtil.getConversation(message.from, imNickName, extAvatar, extRole)
                    if (type == EMMessage.Type.TXT) {
                        SLog.info("收到文本消息")
                        chatMessage.content = StringUtil.getEMMessageText(message.body.toString())
                    } else if (type == EMMessage.Type.IMAGE) {
                        SLog.info("收到圖片消息")
                        val imageMessageBody = message.body as EMImageMessageBody
                        val localUrl = imageMessageBody.localUrl
                        val remoteUrl = imageMessageBody.remoteUrl
                        chatMessage.content = EasyJSONObject.generate("absolutePath", localUrl, "imgUrl", remoteUrl).toString()
                        SLog.info("chatMessage.content[%s]", chatMessage.content)
                    } else {
                        SLog.info("收到不支持的消息")
                    }
                    SLog.info("chatMessage[%s]", chatMessage)
                    if (showNotificationDetail && !isMessageActivity) { //設置允許推送，并且消息類頁面不活躍時可以推送
                        em_push_content?.let { pushNotification(chatMessage, it)}
                    }
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_NEW_CHAT_MESSAGE, chatMessage)
                }
                if (showNotification && !showNotificationDetail && !isMessageActivity) { //設置不顯示詳情，并且消息類頁面不在前臺時要推送
                    val chatMessage = ChatMessage()
                    val pushContent = String.format("你收到了%d條想聊消息", messages.size)
                    pushNotification(chatMessage, pushContent)
                }
            }

            override fun onCmdMessageReceived(messages: List<EMMessage>) {
                //收到透传消息
                SLog.info("收到透传消息")
            }

            override fun onMessageRead(messages: List<EMMessage>) {
                //收到已读回执
                SLog.info("收到已读回执")
            }

            override fun onMessageDelivered(message: List<EMMessage>) {
                //收到已送达回执
                SLog.info("收到已送达回执")
            }

            override fun onMessageRecalled(messages: List<EMMessage>) {
                //消息被撤回
                SLog.info("消息被撤回")
            }

            override fun onMessageChanged(message: EMMessage, change: Any) {
                //消息状态变动
                SLog.info("消息状态变动, message[%s], change[%s]", message, change)
            }
        }
        EMClient.getInstance().chatManager().addMessageListener(msgListener)
    }
    private fun isMessageFragmentActivity(): Boolean {
        return MainActivity.getInstance()?.messageFragmentsActivity ?:false
    }

    private fun isMainPid(): Boolean {
        val processAppName = getAppName(Process.myPid())
        if (!processAppName.equals(packageName, ignoreCase = true)) {
            SLog.info("Warning!enter the service process!processAppName[%s]", processAppName)

            // 则此application::onCreate 是被service 调用的，直接返回
            return false
        }
        return true
    }
    private fun pushNotification(chatMessage: ChatMessage, pushContent: String) {
//        try {
//            URL url=new URL(chatMessage.avatar);
//        var notification: Notification? = null
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(Constant.MSG_NOTIFY_HXID, "chatMessage.fromMemberName")
//        val s = arrayOfNulls<Intent>(1)
        //            s[0] = intent;
//            ToastUtil.success(getApplicationContext(),intent.getExtras().toString());
        val pi = PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //            SLog.info(intent.getExtras().toString());
//            try {
        val notification = NotificationCompat.Builder(applicationContext, "imChannel")
                .setContentTitle(resources.getString(R.string.app_name))
                .setContentText(pushContent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher) //                        .setLargeIcon(BitmapFactory.decodeStream(url.openStream()))
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_large_icon_bg))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build()
        //            } catch (IOException e) {
//                e.printStackTrace();
//            }
        mNotificationManager?.notify(count++, notification)
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 初始化高德地圖定位
     */
    private fun initAmapLocation() {
        val processAppName = getAppName(Process.myPid())
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (!processAppName.equals(packageName, ignoreCase = true)) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return
        }

    }
    fun getStringRes(resId: Int): String? {
        return getString(resId)
//        return getString(resId)
    }
//    override fun onTerminate() {
//        super.onTerminate()
//    }
}
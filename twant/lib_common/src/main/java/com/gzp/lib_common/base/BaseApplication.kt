package com.gzp.lib_common.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Notification
import android.content.Context
import android.os.Bundle
import android.os.Process
import android.util.Log
import cat.ereza.customactivityoncrash.config.CaocConfig
import cn.snailpad.easyjson.EasyJSONObject
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.gzp.lib_common.R
import com.gzp.lib_common.config.Config
import com.gzp.lib_common.constant.Constant
import com.gzp.lib_common.constant.SPField
import com.gzp.lib_common.constant.Vendor
import com.gzp.lib_common.model.User
import com.gzp.lib_common.service.AppService
import com.gzp.lib_common.utils.AppUtil
import com.gzp.lib_common.utils.SLog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.hawk.Hawk
import com.tencent.mmkv.MMKV
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage
import com.wzq.mvvmsmart.base.AppManagerMVVM
import com.wzq.mvvmsmart.net.net_utils.Utils
import com.wzq.mvvmsmart.utils.KLog
import com.wzq.mvvmsmart.utils.KLog.init
import com.wzq.mvvmsmart.utils.Tasks
import io.github.prototypez.appjoint.AppJoint
import org.android.agoo.huawei.HuaWeiRegister
import org.koin.core.context.startKoin

open class BaseApplication:Application() {
    companion object {
        private var instance: BaseApplication? = null
            get() {
                if (field == null) {
                    field = BaseApplication()
                }
                return field
            }
        fun get(): BaseApplication{
            //细心的小伙伴肯定发现了，这里不用getInstance作为为方法名，是因为在伴生对象声明时，内部已有getInstance方法，所以只能取其他名字
            KLog.e("拿到App了")
            AppUtil.app= instance
            return instance!!
        }
    }

    var mPushAgent: PushAgent? = null
    var umengDeviceToken:String?=""

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)
        MMKV.initialize(this) // 替换sp
        initMVVM()
    }
    private fun initMVVM() {
        Tasks.init()
        Utils.init(this)
        //是否开启打印日志
//        KLog.init(BuildConfig.DEBUG);
        init(BuildConfig.DEBUG)
        //初始化全局异常崩溃
        initCrash()
        LiveEventBus // 事件儿总线通信
                .config().supportBroadcast(this) // 配置支持跨进程、跨APP通信，传入Context，需要在application onCreate中配置
                .lifecycleObserverAlwaysActive(true) //    整个生命周期（从onCreate到onDestroy）都可以实时收到消息
        setActivityLifecycle(this)
    }

    /**
     * app 崩溃重启的配置
     */
    open fun initCrash() {
        val appService=AppJoint.service(AppService::class.java)
        CaocConfig.Builder.create().backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                //todo 设置重启activity
                .restartActivity(appService.getMainActivity()) //重新启动后的activity
                //                                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //                                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply()
    }

    /**
     * 当主工程没有继承BaseApplication时，可以使用setApplication方法初始化BaseApplication
     *
     * @param application
     */
    @Synchronized
    open fun setActivityLifecycle(application: Application) {
        //注册监听每个activity的生命周期,便于堆栈式管理
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                AppManagerMVVM.get().addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                AppManagerMVVM.get().removeActivity(activity)
            }
        })
    }

    open  fun getAppName(pID: Int): String {
        var processName = ""
        val am = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val l = am.runningAppProcesses ?: return ""
        val i = l.iterator()
        val pm = this.packageManager
        while (i.hasNext()) {
            val info = i.next() as ActivityManager.RunningAppProcessInfo
            try {
                if (info.pid == pID) {
                    processName = info.processName
                    return processName
                }
            } catch (e: java.lang.Exception) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName
    }
    open  fun initUmeng(setMainFragment:()->Unit?) {
        SLog.info("initUmeng")
        val pid = Process.myPid()
        val processAppName: String = getAppName(pid)
        SLog.info("Umeng init processName[%s], pid[%d]", processAppName, pid)
        // mPushAgent.register方法应该在主进程和channel进程中都被调用
        if (!processAppName.equals(packageName, ignoreCase = true)) {
            // SLog.info("Warning!UMENG enter the service process!processAppName[%s]", processAppName);

            // 则此application::onCreate 是被service 调用的，直接返回
            // return;
        }
        if (Config.DEVELOPER_MODE) {
            UMConfigure.setLogEnabled(true)
        }
        // 在此处调用基础组件包提供的初始化函数 相应信息可在应用管理 -> 应用信息 中找到 http://message.umeng.com/list/apps
        // 参数一：当前上下文context；
        // 参数二：应用申请的Appkey（需替换
        // 参数三：渠道名称；
        // 参数四：设备类型，必须参数，传参数为UMConfigure.DEVICE_TYPE_PHONE则表示手机；传参数为UMConfigure.DEVICE_TYPE_BOX则表示盒子；默认为手机；
        // 参数五：Push推送业务的secret 填充Umeng Message Secret对应信息（需替换）
        UMConfigure.init(this, getString(R.string.umeng_push_app_key), "official", UMConfigure.DEVICE_TYPE_PHONE, getString(R.string.umeng_push_message_secret))

        // 友盟統計：选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)

        //获取消息推送代理示例
        mPushAgent = PushAgent.getInstance(this)
        // mPushAgent.setNotificaitonOnForeground(false); // 如果应用在前台，您可以设置不显示通知栏消息。默认情况下，应用在前台是显示通知的。此方法请在mPushAgent.register方法之前调用。
        val messageHandler: UmengMessageHandler = object : UmengMessageHandler() {
            /**
             * 通知的回调方法（通知送达时会回调）
             */
            override fun dealWithNotificationMessage(context: Context?, msg: UMessage) {
                SLog.info("dealWithNotificationMessage, msg[%s][%s]", dumpUmengMessage(msg), msg.msg_id)

                //调用super，会展示通知，不调用super，则不展示通知。
                super.dealWithNotificationMessage(context, msg)
            }

            /**
             * 自定义消息的回调方法
             */
            override fun dealWithCustomMessage(context: Context?, msg: UMessage) {
                SLog.info("dealWithCustomMessage, msg[%s]", dumpUmengMessage(msg))
            }

            /**
             * 自定义通知栏样式的回调方法
             */
            override fun getNotification(context: Context?, uMessage: UMessage): Notification? {
                SLog.info("getNotification, msg[%s][%s]", dumpUmengMessage(uMessage), uMessage.msg_id)
                return super.getNotification(context, uMessage)
            }
        }
        mPushAgent?.setMessageHandler(messageHandler)
        /**
         * 自定义行为的回调处理，参考文档：高级功能-通知的展示及提醒-自定义通知打开动作
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         */
        val notificationClickHandler: UmengNotificationClickHandler = object : UmengNotificationClickHandler() {
            override fun launchApp(context: Context?, msg: UMessage) {
                super.launchApp(context, msg)
                SLog.info("launchApp()")
                val extraDataStr = getUmengExtraData(msg)
                Hawk.put(SPField.FIELD_UNHANDLED_UMENG_MESSAGE_PARAMS, extraDataStr)
                SLog.info("extraDataStr[%s]", extraDataStr)
                // TODO: 2020/9/14 启动mainfragment
                setMainFragment()

            }

            override fun openUrl(context: Context?, msg: UMessage?) {
                super.openUrl(context, msg)
                SLog.info("openUrl()")
            }

            override fun openActivity(context: Context?, msg: UMessage?) {
                super.openActivity(context, msg)
                SLog.info("openActivity()")
            }

            override fun dealWithCustomAction(context: Context?, msg: UMessage) {
                SLog.info("dealWithCustomAction, message[%s][%s]", msg.custom, msg.msg_id)
                val extraDataStr = getUmengExtraData(msg)
                Hawk.put(SPField.FIELD_UNHANDLED_UMENG_MESSAGE_PARAMS, extraDataStr)
                SLog.info("extraDataStr[%s]", extraDataStr)
                setMainFragment()

            }
        }
        //使用自定义的NotificationHandler
        mPushAgent?.setNotificationClickHandler(notificationClickHandler)
        SLog.info("mPushAgent.register")
        if (Vendor.VENDOR_HUAWEI == Vendor.getVendorType()) {
            HuaWeiRegister.register(this)
            //            SLog.info("VENDOR_HUAWEI.register");
        }
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent?.register(object : IUmengRegisterCallback{
            override fun onSuccess(deviceToken: String) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                SLog.info("mPushAgent.register 注册成功：deviceToken：-------->  $deviceToken")
                umengDeviceToken = deviceToken
                setUmengAlias(Constant.ACTION_ADD)
            }

            override fun onFailure(s: String, s1: String) {
                SLog.info("mPushAgent.register 注册失败：-------->  s:$s,s1:$s1")
            }
        })
    }


    /**
     * 格式化友盟消息
     * @param msg
     * @return
     */
    private  fun dumpUmengMessage(msg: UMessage): String? {
        return java.lang.String.format("custom[%s], extra[%s]", msg.custom, getUmengExtraData(msg))
    }

    /**
     * 獲取友盟推送自定義參數
     * @param msg
     * @return
     */
    private fun getUmengExtraData(msg: UMessage): String {
        val extra: EasyJSONObject = EasyJSONObject.generate()
        try {
            for (key in msg.extra.keys) {
                extra.set(key, msg.extra[key])
            }
        } catch (e: Exception) {
            SLog.info("Error!message[%s], trace[%s]", e.message, Log.getStackTraceString(e))
        }
        SLog.info(extra.toString())
        return extra.toString()
    }


    open fun setUmengAlias(action: Int) {
        if (mPushAgent == null) {
            return
        }
        val memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null)
        if (memberName.isNullOrEmpty()) {
            return
        }
        if (umengDeviceToken.isNullOrEmpty()) {
            return
        }
        if (action == Constant.ACTION_ADD) {
            mPushAgent!!.addAlias(memberName,Constant.UMENG_ALIAS_TYPE) { b, s -> SLog.info("addAlias.UTrack.ICallBack.onMessage[%s][%s]", b, s) }
        } else if (action == Constant.ACTION_EDIT) {
            mPushAgent!!.setAlias(memberName, Constant.UMENG_ALIAS_TYPE) { b, s -> SLog.info("setAlias.UTrack.ICallBack.onMessage[%s][%s]", b, s) }
        }
    }

    open fun delUmengAlias() {
        if (mPushAgent == null) {
            return
        }
        val memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null)
        if (memberName.isNullOrEmpty()) {
            return
        }
        mPushAgent!!.deleteAlias(memberName, Constant.UMENG_ALIAS_TYPE) { isSuccess, message -> SLog.info("deleteAlias.UTrack.ICallBack.onMessage[%s][%s]", isSuccess, message) }
    }

}
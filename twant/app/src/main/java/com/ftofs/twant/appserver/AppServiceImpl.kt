package com.ftofs.twant.appserver

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentationMagician
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.ftofs.twant.activity.MainActivity
import com.ftofs.twant.activity.TwantCaptureActivity
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.AddPostFragment
import com.ftofs.twant.fragment.ChatFragment
import com.ftofs.twant.fragment.H5GameFragment
import com.ftofs.twant.fragment.MainFragment
import com.ftofs.twant.orm.FriendInfo
import com.ftofs.twant.util.ChatUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.util.Util
import com.github.richardwrq.krouter.annotation.Provider
import com.gzp.lib_common.base.BaseFragment
import com.gzp.lib_common.constant.SPField
import com.gzp.lib_common.service.AppService
import com.gzp.lib_common.service.ConstantsPath
import com.gzp.lib_common.utils.BaseContext
import com.gzp.lib_common.utils.SLog
import me.yokeyword.fragmentation.SupportFragment
import java.util.*

//同一個module依賴注入失敗
@Provider(ConstantsPath.APP_SERVICE_PATH)
class AppServiceImpl:AppService {
    companion object {
        val instance =Singleton.holder
        object Singleton {
            val holder = AppServiceImpl()
        }
        fun getCaptureIntent(): Intent {
            return Intent(BaseContext.instance.getContext(), TwantCaptureActivity::class.java)
        }

    }
    override fun startActivityOfApp(context: Context) {
        //可以在这里启动mainactivity
//        TODO("Not yet implemented")
    }

    override fun errorPopToMainFragment(context: Context) {
        if (context is FragmentActivity) {
            Util.popToMainFragment(context);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
        }
    }

    override fun getMainActivity(): Class<out Activity>? {
        return MainActivity::class.java
    }

    override fun init(context: Context) {

    }

    override fun updateMainSelectedFragment(fragment: BaseFragment, selectedFragmentIndex: Int) {
        val mainFragment =fragment.parentFragment as MainFragment?
        if (mainFragment != null) {
            mainFragment.selectedFragmentIndex = selectedFragmentIndex;
        }
    }

    override fun startActivityShopping() {
        Util.startActivityShopping()
    }

    override fun shareDialog(title: String, desc: String, uri: Uri) {
        val content: ShareLinkContent = ShareLinkContent.Builder()
                .setContentTitle(title)
                .setContentDescription(desc)
                .setContentUrl(uri)
                .build()
        ShareDialog.show(MainActivity.getInstance(), content)
    }

    override fun startH5Fragment(title: String, u: String, isNeedLogin: String) {
        var url =u
        if ("1" == isNeedLogin) {
            val token: String = User.getToken()
            val memberToken: String = User.getUserInfo(SPField.FIELD_MEMBER_TOKEN, "")
            if (token.isEmpty()) {
                Util.showLoginFragmentWithoutContext(null)
                return
            }
            if (url.contains("?")) {
                SLog.info("!!!HERE")
                //                    url += "&token=" + token;
                url += "&token=$memberToken"
            } else {
//                    url += "?token=" + token;
                url += "?token=$memberToken"
                SLog.info("!!!HERE")
            }
        }
//            url = url + "#/20191226/index";
//            SLog.info("url[%s]",url);

        //            url = url + "#/20191226/index";
//            SLog.info("url[%s]",url);
        Util.startFragment(H5GameFragment.newInstance(url, true, true, title, H5GameFragment.ARTICLE_ID_INVALID, null))
    }

    override fun sendWant() {
        Util.startFragment(AddPostFragment.newInstance(true))

    }

    override fun goActivityHome() {
        val mainActivity = MainActivity.getInstance() ?: return

        val packageName = mainActivity.packageName
        val popFragmentList: MutableList<SupportFragment> = ArrayList() // 需要pop出去的Fragment


        var enqueueFlag = false
        val fragmentList = FragmentationMagician.getActiveFragments(mainActivity.supportFragmentManager)
        for (fragment in fragmentList) {
            val className = fragment.javaClass.name
            SLog.info("className[%s], packageName[%s]", className, packageName)
            if (!className.startsWith(packageName)) {
                continue
            }
            if (fragment !is H5GameFragment) {
                continue
            }
            val h5GameFragment = fragment
            val url = h5GameFragment.url
            SLog.info("url[%s]", url)
            if (url != null && url.contains(Util.CHRISTMAS_APP_GAME)) {  // 根據Url來確定是要pop出去
                enqueueFlag = true
            }
            if (enqueueFlag) {
                popFragmentList.add(h5GameFragment)
            }
        }

        for (i in popFragmentList.size - 1 downTo 1) {
            popFragmentList[i].pop()
        }
    }

    override fun goLogin(supportFragment: SupportFragment, data: String) {

        val memberToken: String = User.getUserInfo(SPField.FIELD_MEMBER_TOKEN, null)!!
        if (memberToken.isNullOrEmpty()) {
            Util.showLoginFragmentWithoutContext(supportFragment)
        }
        val fragment = supportFragment as H5GameFragment
        SLog.info(fragment.url)
        val url = java.lang.String.format("%s?memberToken=%s", fragment.url, memberToken)
        fragment.loadUrl(url)
    }

    override fun startChatFragment(memberName: String, nickname: String, avatarUrl: String) {
        val friendInfo = FriendInfo()
        friendInfo.memberName = memberName
        friendInfo.nickname = nickname
        friendInfo.avatarUrl = avatarUrl
        friendInfo.role = ChatUtil.ROLE_MEMBER
       Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER), friendInfo))
    }
}
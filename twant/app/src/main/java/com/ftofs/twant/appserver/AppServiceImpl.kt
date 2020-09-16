package com.ftofs.twant.appserver

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.ftofs.twant.activity.MainActivity
import com.ftofs.twant.activity.TwantCaptureActivity
import com.ftofs.twant.constant.EBMessageType
import com.ftofs.twant.entity.EBMessage
import com.ftofs.twant.fragment.MainFragment
import com.ftofs.twant.util.Util
import com.gzp.lib_common.base.BaseFragment
import com.gzp.lib_common.service.AppService
import com.gzp.lib_common.utils.AppUtil
import com.gzp.lib_common.utils.BaseContext
import io.github.prototypez.appjoint.core.ServiceProvider

@ServiceProvider
class AppServiceImpl:AppService {
    override fun startActivityOfApp(context: Context) {
        //可以在这里启动mainactivity
//        TODO("Not yet implemented")
    }

    override fun getCaptureIntent(): Intent {
        return Intent(BaseContext.instance.getContext(), TwantCaptureActivity::class.java)
    }
    override fun errorPopToMainFragment(context: Context) {
        if (context is FragmentActivity) {
            Util.popToMainFragment(context as FragmentActivity);
            EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
        }
    }

    override fun getMainActivity(): Class<out Activity>? {
        return MainActivity::class.java
    }

    override fun updateMainSelectedFragment(fragment: BaseFragment,selectedFragmentIndex: Int) {
        val mainFragment =fragment.parentFragment as MainFragment
        if (mainFragment != null) {
            mainFragment.selectedFragmentIndex = selectedFragmentIndex;
        }
    }
}
package com.gzp.lib_common.service

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.github.richardwrq.krouter.api.interfaces.IProvider
import com.gzp.lib_common.base.BaseFragment
import me.yokeyword.fragmentation.SupportFragment

interface AppService :IProvider{

    // start Activity from app module
    fun startActivityOfApp(context: Context)
    //原来ToastUtil的一段代码
    fun errorPopToMainFragment(context: Context)
    fun updateMainSelectedFragment(fragment: BaseFragment, selectedFragmentIndex: Int)
    fun getMainActivity(): Class<out Activity>?
    fun startActivityShopping() {

    }

    fun shareDialog(title:String,desc:String,uri: Uri)
    fun startH5Fragment(title: String, url: String, isNeedLogin: String)
    fun sendWant()
    fun goActivityHome()
    fun goLogin(supportFragment: SupportFragment,data: String)
    fun startChatFragment(memberName: String, nickname: String, avatarUrl: String)
//    // get Fragment from app module
//    fun obtainFragmentOfApp(): Fragment
//
//    // call synchronous method from app module
//    fun callMethodSyncOfApp(): String
//
//    // call asynchronous method from app module
//    fun callMethodAsyncOfApp(callback: AppCallback<AppEntity>)
//
//    // get RxJava Observable from app module
//    fun observableOfApp(): Observable<AppEntity>
}
package com.gzp.lib_common.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.gzp.lib_common.base.BaseFragment
interface AppService {

    fun getCaptureIntent(): Intent

    // start Activity from app module
    fun startActivityOfApp(context: Context)
    //原来ToastUtil的一段代码
    fun errorPopToMainFragment(context: Context)
    fun updateMainSelectedFragment(fragment: BaseFragment, selectedFragmentIndex: Int)
    fun getMainActivity(): Class<out Activity>?
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
package com.gzp.lib_common.base

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * @author: Eli Shaw
 * @Date: 2020-04-27 17:20:45
 * @Description：创建相应的 ViewModel
 */
object ViewModelFactory {

    /**
     * @Description: 创建 FragmentActivity 页面的 ViewModel
     * @Author: Eli
     * @Date: 2020-05-09 14:41:04
     */
    fun <T : ViewModel?> createViewModel(
            activity: FragmentActivity,
            cls: Class<T>?
    ): T {
        return ViewModelProvider(activity).get(cls!!)
    }

    /**
     * @Description: 创建 Fragment 页面的 ViewModel
     * @Author: Eli
     * @Date: 2020-05-09 14:40:36
     */
    fun <T : ViewModel?> createViewModel(
            fragment: Fragment,
            cls: Class<T>?
    ): T {
        return ViewModelProvider(fragment).get(cls!!)
    }

    /**
     * @Description:创建 FragmentActivity 页面的 AndroidViewModel
     * @Author: Eli
     * @Date: 2020-05-09 14:36:39
     */
    fun <T : ViewModel?> createViewModel(
            activity: FragmentActivity,
            application: Application,
            cls: Class<T>?
    ): T {
        return ViewModelProvider(
                activity,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(cls!!)
    }

    /**
     * @Description:创建 Fragment 页面的 AndroidViewModel
     * @Author: Eli
     * @Date: 2020-05-09 14:36:39
     */
    fun <T : ViewModel?> createViewModel(
            fragment: Fragment,
            application: Application,
            cls: Class<T>?
    ): T {
        return ViewModelProvider(
                fragment,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(cls!!)
    }

    /**
     * @Description: 创建带参数的 AndroidViewModel
     * @Author: Eli
     * @Date: 2020-05-09 14:36:24
     */
    fun <T : ViewModel?> createViewModel(
            activity: FragmentActivity,
            application: Application,
            name: String,
            cls: Class<T>?
    ): T {
        return ViewModelProvider(
                activity,
                AndroidViewModelFactory.getInstance(application, name)
        ).get(cls!!)
    }
}
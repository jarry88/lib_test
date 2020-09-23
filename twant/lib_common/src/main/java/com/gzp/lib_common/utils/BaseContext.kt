package com.gzp.lib_common.utils

import android.content.Context
import com.gzp.lib_common.base.BaseApplication

/**
 * Create by liwen on 2020/6/4
 */
fun getBaseApplication()=BaseContext.instance.getContext() as BaseApplication
class BaseContext private constructor() {


    private lateinit var mContext: Context

    fun init(context: Context) {
        mContext = context
    }

    fun getContext(): Context {
        return mContext
    }

    companion object {

        val instance = Singleton.holder

        object Singleton {
            val holder = BaseContext()
        }

    }

}
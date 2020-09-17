package com.gzp.lib_common.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AndroidViewModelFactory private constructor(
        private val mApplication: Application,
        private val mName: String
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (AndroidViewModel::class.java.isAssignableFrom(modelClass)) {
            try {
                modelClass.getConstructor(
                        Application::class.java, String::class.java
                ).newInstance(mApplication, mName)
            } catch (e: Exception) {
                throw RuntimeException("Cannot create an instance of $modelClass", e)
            }
        } else super.create(modelClass)
    }

    companion object {
        private var sInstance: AndroidViewModelFactory? = null
        fun getInstance(
                application: Application,
                name: String
        ): AndroidViewModelFactory {
            if (sInstance == null) {
                sInstance = AndroidViewModelFactory(application, name)
            }
            return sInstance as AndroidViewModelFactory
        }
    }
}
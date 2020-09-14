package com.gzp.lib_common.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseTwantActivityMVVM< T: BaseViewModel,M:ViewDataBinding>:BaseActivity() {

    lateinit var mViewModel: T
    lateinit var mViewBinding: M
    val tag = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//沉浸状态栏 操作
//        StatusBarKt.fitSystemBar(this)
        mViewBinding = DataBindingUtil.setContentView(this, getLayoutResId())
        initViewModel()


    }
    abstract fun initData()
    abstract fun initView()

    abstract fun getLayoutResId(): Int
    @SuppressLint("NewApi")
    private fun initViewModel() {

//        val types = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
//        mViewModel = ViewModelProvider(this).get<T>(types[0] as Class<T>)

        val clazz =
                this.javaClass.kotlin.supertypes[0].arguments[0].type!!.classifier!! as KClass<T>
        mViewModel = getViewModel<T>(clazz) //koin 注入

    }
}
package com.gzp.lib_common.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wzq.mvvmsmart.base.BaseViewModelMVVM
import com.wzq.mvvmsmart.base.IBaseViewMVVM
import com.wzq.mvvmsmart.widget.EmptyViewHelper
import java.lang.reflect.ParameterizedType

abstract class BaseTwantActivityMVVM< VM: BaseViewModelMVVM,V:ViewDataBinding>:BaseActivity(),IBaseViewMVVM {

    protected lateinit var binding: V
    protected lateinit var viewModel: VM
    private var viewModelId = 0
    private var emptyViewHelper: EmptyViewHelper? = null
    val tag = javaClass.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//沉浸状态栏 操作
//        StatusBarKt.fitSystemBar(this)
        //页面接受的参数方法
        initParam()
        //私有的初始化 binding和ViewModel方法
        initViewDataBinding(savedInstanceState)
        initToolbar()
        //页面数据初始化方法
        initData()
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable()


    }
    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
    open fun initToolbar() {}
    override fun initData() {}
    override fun initViewObservable() {
        //私有的ViewModel与View的契约事件回调逻辑
    }

    override fun onContentReload() {
        //  有列表的页面,无数据的时候点击空白页重新加载网络
    }

    /**
     * 注入绑定
     */
    private fun initViewDataBinding(savedInstanceState: Bundle?) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.binding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))
        viewModelId = initVariableId()
        //        viewModel = initViewModel()
        binding.lifecycleOwner = this
        val modelClass: Class<VM>
        val type = javaClass.genericSuperclass

        modelClass = if (type is ParameterizedType) {
            type.actualTypeArguments[1] as Class<VM>
        } else {
            //如果没有指定泛型参数，则默认使用BaseViewModel
            BaseViewModelMVVM::class.java as Class<VM>
        }
        viewModel = createViewModel(this, modelClass)
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel)
        //让ViewModel拥有View的生命周期感应
        lifecycle.addObserver(viewModel)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun initParam() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }


    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    abstract fun initContentView(savedInstanceState: Bundle?): Int

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    abstract fun initVariableId(): Int

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */


    /**
     * @param cls 类
     * @param <T> 泛型参数,必须继承ViewMode
     * @return 生成的viewMode实例
    </T> */
    private fun <T : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
        return ViewModelProvider(activity).get(cls)
    }

    protected fun showNormalLayout(view: View?) {
        if (emptyViewHelper == null) {
            emptyViewHelper = EmptyViewHelper(this)
            emptyViewHelper?.setReloadCallBack(this)
        }
        emptyViewHelper?.loadNormallLayout(view)
    }

    /***
     * 加载无数据、无网络、数据异常布局
     * @param target 被替换的view
     * @param text 显示的文字
     * @param imgId 占位图
     * @param reload 是否显示重新加载按钮
     */
    protected fun showEmptyLayout(target: View?, text: String?, imgId: Int, reload: Boolean) {
        if (emptyViewHelper == null) {
            emptyViewHelper = EmptyViewHelper(this)
            emptyViewHelper?.setReloadCallBack(this)
        }
        emptyViewHelper?.loadPlaceLayout(target, text, imgId, reload)
    }
}
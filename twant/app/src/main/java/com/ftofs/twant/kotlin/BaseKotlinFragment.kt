package com.ftofs.twant.kotlin

import android.os.Bundle
import android.os.Handler
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ftofs.twant.TwantApplication
import com.ftofs.twant.fragment.BaseFragment
import com.ftofs.twant.kotlin.observer.LoadingObserver

abstract class BaseKotlinFragment <T : ViewDataBinding>: BaseFragment(){

    abstract val layoutId: Int
    lateinit var binding: T
    val loadingState = MutableLiveData<Boolean>()



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this
        binding.executePendingBindings()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingState.observe(this, LoadingObserver(activity!!))
    }
}
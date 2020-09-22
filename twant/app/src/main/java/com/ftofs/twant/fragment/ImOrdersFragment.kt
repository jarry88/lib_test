package com.ftofs.twant.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.gzp.lib_common.constant.PopupType
import com.ftofs.twant.databinding.ImOrdersLayoutBinding
import com.ftofs.twant.databinding.ImStoreOrderItemBinding
import com.ftofs.lib_net.model.ImStoreOrderItem
import com.gzp.lib_common.base.callback.OnSelectedListener
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.viewmodel.ImOrdersPageModel
import com.wzq.mvvmsmart.event.StateLiveData
import com.wzq.mvvmsmart.utils.KLog

class ImOrdersFragment(val imName: String, val sendOrder: OnSelectedListener) : BaseTwantFragmentMVVM<ImOrdersLayoutBinding, ImOrdersPageModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.im_orders_layout

    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }
    val mAdapter  by lazy {
        object :DataBoundAdapter<ImStoreOrderItem,ImStoreOrderItemBinding>(){
            override val layoutId: Int
                get() = R.layout.im_store_order_item

            override fun initView(binding: ImStoreOrderItemBinding, item: ImStoreOrderItem) {
                binding.vo=item
                binding.root.apply {
                    setOnClickListener {
                        sendOrder.onSelected(PopupType.IM_CHAT_SEND_ORDER,0,item)
                        hideSoftInputPop()
                    }
                }
            }

        }.apply { showEmptyView(true) }

    }
    override fun initData() {
        binding.rlTitleContainer.tvTitle.text=resources.getString(R.string.my_bill)
        binding.rlTitleContainer.btnBack.setOnClickListener {
            onBackPressedSupport()
        }
        binding.orderPage.rvGoodsList.adapter=mAdapter
        binding.orderPage.toolBar.visibility= View.GONE
        binding.orderPage.etKeyword.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if ((actionId == EditorInfo.IME_ACTION_SEARCH) or (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) or (0 == KeyEvent.KEYCODE_ENTER )) {
                    val search =text?.toString()?.trim()
                    if (!search.isNullOrEmpty()) viewModel.keyword.postValue(search)
                    else ToastUtil.success(context,"請輸入" + getString(R.string.input_order_search_hint))
                }
                true
            }

        }
        binding.orderPage.btnClearAll.setOnClickListener { binding.orderPage.etKeyword.text?.clear() }
        binding.orderPage.refreshLayout.setOnRefreshListener { viewModel.getImOrdersSearch() }
    }

    override fun initViewObservable() {
        viewModel.ordersList.observe(this, Observer {
            mAdapter.addAll(it,viewModel.isRefresh)
        })
        viewModel.keyword.observe(this, Observer { viewModel.getImOrdersSearch(it) })

        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this, Observer {
            when (it) {
                StateLiveData.StateEnum.Loading -> {
//                    binding.orderPage.refreshLayout.id
                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Error -> {
                    binding.orderPage.refreshLayout.finishRefresh()
                    binding.orderPage.refreshLayout.finishLoadMore()
                    ToastUtil.error(context,viewModel.errorMessage)
                    KLog.e("请求数据中--显示loading")
                }
                StateLiveData.StateEnum.Success -> {
                    binding.orderPage.refreshLayout.finishRefresh()
                    binding.orderPage.refreshLayout.finishLoadMore()
                    KLog.e("数据获取成功--关闭loading")
                }
                StateLiveData.StateEnum.Idle -> {
                    KLog.e("空闲状态--关闭loading")
//                    binding.refreshLayout.finishRefresh()
//                    binding.refreshLayout.finishLoadMore()
//                    loadingUtil?.hideLoading()
                }
                StateLiveData.StateEnum.NoData -> {
                    KLog.e("空闲状态--关闭loading")
                    binding.orderPage.refreshLayout.finishRefresh()
                    binding.orderPage.refreshLayout.finishLoadMore()
                }
                else -> {
                    KLog.e("其他状态--关闭loading")
                    binding.orderPage.refreshLayout.finishRefresh()
                    binding.orderPage.refreshLayout.finishLoadMore()
                }
            }
        })
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        viewModel.imName.value =imName
        viewModel.getImOrdersSearch()
    }

    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return true
    }
}
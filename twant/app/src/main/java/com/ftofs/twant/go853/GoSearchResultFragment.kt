package com.ftofs.twant.go853

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.ftofs.twant.R
import com.ftofs.twant.BR
import com.ftofs.twant.databinding.GoSearchResultListFragmentBinding
import com.ftofs.twant.util.SearchHistoryUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.SearchHistoryPopup
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback

private const val KEYWORD = "keyword"
private const val ARG_PARAM2 = "param2"
class GoSearchResultFragment:BaseTwantFragmentMVVM<GoSearchResultListFragmentBinding,GoHouseViewModel>() {
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_search_result_list_fragment
    }
    private var keyword:String?=null
    private var searchHistoryPopup: SearchHistoryPopup?=null
    private var historyPopupDismiss=true


    companion object {
        @JvmStatic
        fun newInstance(keyword: String)= GoSearchResultFragment().apply {
            arguments=Bundle().apply {
                putString(KEYWORD, keyword)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            keyword=getString(KEYWORD)
        }
    }
    override fun initVariableId(): Int {
        return BR.viewModel
    }
    override fun initData() {
        binding.title.apply {
            setLeftLayoutClickListener{onBackPressedSupport()}
            setLeftImageResource(R.drawable.icon_back)
            showSearchWidget("點擊搜索靚屋") {
                if (it.isEmpty()) {
                    ToastUtil.error(context, "请输入关键词")
                } else {
                    hideSoftInput()
                    SearchHistoryUtil.saveSearchHistory(GoSearchType.All.ordinal, it)
                    viewModel.currPage=0
                    binding.refreshLayout.autoRefresh()
                }}
            editKeyWord?.setOnTouchListener { v: View?, event: MotionEvent ->
                if (event.action == MotionEvent.ACTION_UP) {
                   showHistoryContainer()
                }
                false
            }
        }
        binding.refreshLayout.setNoMoreData(true)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.currPage=0
            viewModel.getPropertyList(search = binding.title.getSearchWord()
            )
        }

    }

    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding.refreshLayout.finishRefresh()
            binding.refreshLayout.finishLoadMore()
        }
    }
    private fun showHistoryContainer() {
        if(!historyPopupDismiss) return
        searchHistoryPopup= XPopup.Builder(_mActivity)
                .setPopupCallback(object : XPopupCallback {
                    override fun onCreated(p0: BasePopupView?) {
//                        TODO("Not yet implemented")
                    }

                    override fun beforeShow(p0: BasePopupView?) {
//                        TODO("Not yet implemented")
                    }

                    override fun onShow(p0: BasePopupView?) {
//                        TODO("Not yet implemented")
                    }

                    override fun onDismiss(p0: BasePopupView?) {
                        historyPopupDismiss = true
                    }

                    override fun beforeDismiss(p0: BasePopupView?) {
//                        TODO("Not yet implemented")
                    }

                    override fun onBackPressed(p0: BasePopupView?) = true
                })
                .dismissOnTouchOutside(false).atView(binding.vwAnchor).asCustom(SearchHistoryPopup(_mActivity, GoSearchType.All) {
                    binding.title.editKeyWord?.setText(it.toString())
                    binding.refreshLayout.autoRefresh()
                }) as SearchHistoryPopup
        searchHistoryPopup?.show().apply { historyPopupDismiss=false }
//        showSoftInput(view)


    }

}
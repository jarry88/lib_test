package com.ftofs.twant.kotlin

import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.ftofs.twant.kotlin.extension.hideKeyboard
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener

@BindingAdapter(
        value = ["refreshing", "moreLoading", "hasMore"],
        requireAll = false
)
fun bindSmartRefreshLayout(
        smartLayout: SmartRefreshLayout,
        refreshing: Boolean,
        moreLoading: Boolean,
        hasMore: Boolean

) {//状态绑定，控制停止刷新
    if (!refreshing) smartLayout.finishRefresh()
    if (!moreLoading) smartLayout.finishLoadMore()
    smartLayout.setEnableLoadMore(hasMore)
}

@BindingAdapter(
        value = ["autoRefresh"]
)
fun bindSmartRefreshLayout(
        smartLayout: SmartRefreshLayout,
        autoRefresh: Boolean
) {//控制自动刷新
    if (autoRefresh) smartLayout.autoRefresh()
}

@BindingAdapter(//下拉刷新，加载更多
        value = ["onRefreshListener", "onLoadMoreListener"],
        requireAll = false
)
fun bindListener(
        smartLayout: SmartRefreshLayout,
        refreshListener: OnRefreshListener?,
        loadMoreListener: OnLoadMoreListener?
) {
    smartLayout.setOnRefreshListener(refreshListener)
    smartLayout.setOnLoadMoreListener(loadMoreListener)
}

//绑定软键盘搜索
@BindingAdapter(value = ["searchAction"])
fun bindSearch(et: EditText, callback: () -> Unit) {
    et.setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            callback()
            et.hideKeyboard()
        }
        true
    }
}
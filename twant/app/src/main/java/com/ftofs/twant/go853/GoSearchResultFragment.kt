package com.ftofs.twant.go853

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ftofs.lib_net.model.PropertyVo
import com.ftofs.twant.BR
import com.ftofs.twant.R
import com.ftofs.twant.config.Config
import com.ftofs.twant.databinding.GoSearchResultListFragmentBinding
import com.ftofs.twant.databinding.ItemHouseVoBinding
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.util.EditTextUtil
import com.ftofs.twant.util.SearchHistoryUtil
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.Util
import com.ftofs.twant.widget.SearchHistoryPopup
import com.google.android.material.tabs.TabLayout
import com.gzp.lib_common.base.BaseTwantFragmentMVVM
import com.gzp.lib_common.utils.SLog
import com.gzp.lib_common.utils.pushUmengEvent
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.XPopupCallback
import com.wzq.mvvmsmart.event.StateLiveData

private const val KEYWORD = "keyword"
class GoSearchResultFragment:BaseTwantFragmentMVVM<GoSearchResultListFragmentBinding, GoHouseViewModel>() {
    var firstLoad =true
    override fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.go_search_result_list_fragment
    }
    private var drawListView: BasePopupView?=null

    private var keyword:String?=null
    var firstTabSelected =true
    var tabFold =true
    private val mAdapter by lazy {
        object : DataBoundAdapter<PropertyVo, ItemHouseVoBinding>(emptyId = R.layout.go_placeholder_no_data){
            override val layoutId: Int
                get() = R.layout.item_house_vo

            override fun initView(binding: ItemHouseVoBinding, item: PropertyVo) {
                binding.vo=item
                binding.root.setOnClickListener {
                    Util.startFragment(GoPropertyDetailFragment(item.pid, item))
                }
            }

        }.apply { showEmptyView(true) }
    }


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
                    binding.llSearchHistoryContainer.visibility= View.GONE
                    pushUmengEvent(Config.PROD, GO853_SEARCH_KEYWORD,hashMapOf("keyword" to it))
                    SearchHistoryUtil.saveSearchHistory(GoSearchType.All.ordinal, it)
                    binding.refreshLayout.autoRefresh()
                }}
            editKeyWord?.setSimpleCallback {
                (it as? Int)?.let {
                    if(it==R.id.et_keyword) showSearchHistoryContainer()
                }
            }
        }
        binding.flMaskEmptyArea.setOnClickListener { binding.llSearchHistoryContainer.visibility=View.GONE
            SLog.info("點擊空白")
        }
        binding.llClearAll.setOnClickListener{
            binding.flSearchHistoryContainer.removeAllViews()
            SearchHistoryUtil.clearSearchHistory(GoSearchType.All.ordinal)
        }
        binding.refreshLayout.setNoMoreData(true)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.currPage=0
            viewModel.getPropertyList(search = binding.title.getSearchWord())
        }
        binding.refreshLayout.setOnLoadMoreListener { viewModel.getPropertyList(search = binding.title.getSearchWord()) }
        binding.rvList.adapter=mAdapter
        binding.tabLayout.apply {
            setSelectedTabIndicatorColor(resources.getColor(R.color.tw_blue))
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (firstTabSelected) {
                        firstTabSelected = false
                        return
                    }
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)
                    val textView = tab?.customView?.findViewById<TextView>(R.id.tag_text)
                    textView?.setTextColor(resources.getColor(R.color.tw_blue))

                    iconView?.let {
                        Glide.with(context).load(R.drawable.up_arrow_blue).centerCrop().into(it)
                    }
                    tab?.customView?.let {
                        showDrawListView(it, selectedTabPosition)
                    }

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    val textView = tab?.customView?.findViewById<TextView>(R.id.tag_text)
                    textView?.setTextColor(resources.getColor(R.color.black))
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)
                    iconView?.let {
                        Glide.with(context).load(R.drawable.down_black_arrow).centerCrop().into(it)
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    val iconView = tab?.customView?.findViewById<ImageView>(R.id.icon_exp)

                    iconView?.let {
                        if (tabFold) {
                            Glide.with(context).load(R.drawable.up_arrow_blue).centerCrop().into(it)
                            tab.customView?.let { v ->
                                showDrawListView(v, selectedTabPosition)
                            }
                        } else {
                            Glide.with(context).load(R.drawable.down_arrow_blue).centerCrop().into(it)
                        }
                    }

                }

            })
            addTab(newTab().apply {
                customView = tagViewFactory("房產", true)
            }
            )
            addTab(newTab().apply {
                customView = tagViewFactory("出售")
            })
            addTab(newTab().apply {
                customView = tagViewFactory("區域")

            })
            addTab(newTab().apply {
                customView = tagViewFactory("價格")

            })

        }
        showSearchHistoryContainer()
    }

    private fun showDrawListView(view: View, selectedTabPosition: Int) {
        val tagView=view.findViewById<TextView>(R.id.tag_text)
        val iconView=view.findViewById<ImageView>(R.id.icon_exp)
        drawListView=XPopup.Builder(context).moveUpToKeyboard(false)
                .hasShadowBg(false)
                .atView(view)
                .setPopupCallback(object : XPopupCallback {
                    override fun onCreated(p0: BasePopupView?) {
//                                            TODO("Not yet implemented")
                    }

                    override fun beforeShow(p0: BasePopupView?) {
//                                            TODO("Not yet implemented")
                    }

                    override fun onShow(p0: BasePopupView?) {
//                                            TODO("Not yet implemented")
                    }

                    override fun onDismiss(p0: BasePopupView?) {
                        tabFold = true
                    }

                    override fun beforeDismiss(p0: BasePopupView?) {
                        iconView?.let {
                            Glide.with(requireContext()).load(R.drawable.down_arrow_blue).centerCrop().into(it)
                        }
                    }

                    override fun onBackPressed(p0: BasePopupView?): Boolean {
                        return true
                    }

                })
                .asCustom(
                        when (selectedTabPosition) {
                            PROPERTY_TYPE_BUTTON -> GoDropdownMenu(requireContext(), viewModel.propertyTypeList, tagView?.text.toString()) { s ->
                                pushUmengEvent(Config.PROD,GO853_FILTER_PROPERTY,hashMapOf("type" to s))
                                binding.rvList.scrollToPosition(0)
                                viewModel.savePropertyType(s)
                                drawListView?.dismiss()
                            }
                            SALE_TYPE_BUTTON -> GoDropdownMenu(requireContext(), viewModel.saleTypeList, tagView?.text.toString()) { s ->
                                pushUmengEvent(Config.PROD,GO853_FILTER_SALE,hashMapOf("type" to s))

                                viewModel.saveSaleType(s)
                                drawListView?.dismiss()
                            }

                            CITY_TYPE_BUTTON -> GoDropdownMenu(requireContext(), viewModel.cityTypeList, tagView?.text.toString()) { s ->
                                pushUmengEvent(Config.PROD,GO853_FILTER_CITY,hashMapOf("type" to s))
                                binding.rvList.scrollToPosition(0)

                                viewModel.saveCityString(s)
                                drawListView?.dismiss()
                            }

                            PRICE_TYPE_BUTTON ->viewModel.getPriceDescList()?.let {
//                                binding.tabLayout.getTabAt(SALE_TYPE_BUTTON)?.customView?.findViewById<TextView>(R.id.tag_text)?.let { v->
//                                    viewModel.saleTypeList.get(2).let{s ->
//                                        v.text=s
//                                    }
//                                }
                                GoDropdownMenu(requireContext(),it , tagView?.text.toString()) { s ->
                                pushUmengEvent(Config.PROD,GO853_FIlTER_PRICE, hashMapOf("type" to s))
                                binding.rvList.scrollToPosition(0)
                                when(viewModel.saleTypeLiveData.value){
                                    SELLING_SALE_TYPE ->viewModel.saveSellingPriceRange(s)
                                    RENT_SALE_TYPE ->viewModel.saveRentPriceRang(s)
                                    else ->viewModel.clearPriceRange()
                                }
                                drawListView?.dismiss()
                            } }?: run {
                                GoDropdownMenu(requireContext()) }//为空时自动dismiss

                            else -> GoDropdownMenu(requireContext())
                        })
                .show().apply {
                    tabFold=false }
    }
    private fun showSearchHistoryContainer() {
        initSearchHistory()
        binding.llSearchHistoryContainer.visibility= View.VISIBLE
    }
    private fun tagViewFactory(tagText: String, visible: Boolean = true)=with(LayoutInflater.from(context).inflate(R.layout.tab_red_count_item, null, false)) {
        findViewById<TextView>(R.id.tag_text)?.apply {
            text=tagText
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f)
            setTextColor(resources.getColor(R.color.tw_black))
        }
        if(visible)findViewById<ImageView>(R.id.icon_exp)?.apply {
            visibility=View.VISIBLE
            Glide.with(context).load(R.drawable.down_black_arrow).centerCrop().into(this)
        }
        this
    }
    override fun initViewObservable() {
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding.refreshLayout.finishRefresh()
            binding.refreshLayout.finishLoadMore()
        }
        viewModel.propertyList.observe(this){
            SLog.info("觀測到數據變化")
            it.propertyList?.apply {
                SLog.info("觀測到數據變化${it.propertyList?.size}")
                mAdapter.addAll(this, viewModel.currPage <= 1)
            }
        }
        viewModel.isTypeLiveData.observe(this){ value ->
            binding.tabLayout.getTabAt(PROPERTY_TYPE_BUTTON)?.customView?.findViewById<TextView>(R.id.tag_text)?.let{
                it.text= viewModel.propertyTypeList[value]
                SLog.info(".isTyp")
                binding.refreshLayout.autoRefresh()
            }
        }
        viewModel.saleTypeLiveData.observe(this){ value ->
            binding.tabLayout.getTabAt(SALE_TYPE_BUTTON)?.customView?.findViewById<TextView>(R.id.tag_text)?.let{
                SLog.info("saleisTyp")
                if (firstLoad) {
                    firstLoad = false
                } else {
                    it.text= viewModel.saleTypeList[value]
                    binding.refreshLayout.autoRefresh()
                }
            }
        }
        viewModel.cityTypeLiveData.observe(this){city ->
            binding.tabLayout.getTabAt(CITY_TYPE_BUTTON)?.customView?.findViewById<TextView>(R.id.tag_text)?.let{
                it.text= city
                SLog.info("city")

                binding.refreshLayout.autoRefresh()
            }
        }
        viewModel.toastError.observe(this){
            mAdapter.addAll(listOf(),true)
//            ToastUtil.error(context,it)
        }
        viewModel.stateLiveData.stateEnumMutableLiveData.observe(this){
            binding.refreshLayout.finishRefresh()
            binding.refreshLayout.finishLoadMore()
            if(it==StateLiveData.StateEnum.Error){
                mAdapter.addAll(listOf(),true)
                if(!viewModel.errorMessage.isNullOrEmpty()){
//                    mAdapter.emptyText=viewModel.errorMessage
                }
            }
            if(!viewModel.hasMore)binding.refreshLayout.setNoMoreData(true)
//            when(it){
//                StateLiveData.StateEnum.NoData ->mAdapter.
//            }
        }
    }
    private fun initSearchHistory() {
        val historyItemList = SearchHistoryUtil.loadSearchHistory(GoSearchType.All.ordinal)
        binding.flSearchHistoryContainer.apply {
            removeAllViews()
            for (item in historyItemList) {
                val textView = LayoutInflater.from(_mActivity)
                        .inflate(R.layout.search_history_popup_item, this, false) as TextView
                textView.text = item.keyword
                textView.tag = SearchHistoryPopup.SEARCH_GO_HISTORY_ITEM
                textView.setOnClickListener{updataKeyword(item.keyword)}
                addView(textView)
            }
        }


    }

    private fun updataKeyword(keyword: String?) {
        hideSoftInput()
        keyword?.let { s ->
            SLog.info(s)
            binding.title.editKeyWord?.let {
                it.setText(s)
                EditTextUtil.cursorSeekToEnd(it)
            }
        }
    }



}
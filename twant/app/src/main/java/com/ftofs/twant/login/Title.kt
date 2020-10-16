package com.ftofs.twant.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ftofs.twant.R
import com.ftofs.twant.widget.TouchEditText

class Title @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        initView(attrs)
    }
    private val titlebarLeft by lazy {findViewById<RelativeLayout>(R.id.titlebar_leftlayout)  }
    private val titlebarLeftImage by lazy {findViewById<ImageView>(R.id.titlebar_leftimage)  }
    private val titlebarRightImage by lazy {findViewById<ImageView>(R.id.titlebar_rightimage)  }
    private val titlebarFollowImage by lazy {findViewById<ImageView>(R.id.follow_img)  }
    private val titlebarRight by lazy {findViewById<RelativeLayout>(R.id.titlebar_rightlayout)  }
    private val titleText by lazy {findViewById<TextView>(R.id.titlebar_title)  }
    private val iconSearch by lazy {findViewById<ImageView>(R.id.icon_search)  }
    private val searchWight by lazy {findViewById<RelativeLayout>(R.id.rl_search)  }
    val editKeyWord: TouchEditText? by lazy {findViewById(R.id.et_keyword)  }
    private val btnClear by lazy {findViewById<ImageView>(R.id.btn_clear_all)  }
    @SuppressLint("ResourceType")
    private fun initView(attrs: AttributeSet?) {
      View.inflate(context, R.layout.layout_login_tilte, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Title)
        typedArray.getString(R.styleable.Title_title_text)?.let {
            rootView.findViewById<TextView>(R.id.titlebar_title)?.text=it
        }
        //不設置的時候是顯示的
        typedArray.getBoolean(R.styleable.Title_login_info, false).takeIf { it }?.let {
            rootView.findViewById<View>(R.id.tv_info)?.visibility=View.VISIBLE
        }
//        typedArray.getBoolean(R.attr.login_info,false).let {
//            if(it) rootView.findViewById<TextView>(R.id.tv_info)?.visibility= VISIBLE
//        }
        typedArray.recycle()

    }
    var text:String?
        get() = ""
        set(value) {titleText?.text=value}
    //    左侧布局监听
    fun setLeftLayoutClickListener(listener: OnClickListener) {
        titlebarLeft!!.setOnClickListener(listener)
    }
    //   通过资源id设置左侧图片样式
    fun setLeftImageResource(resId: Int) {
        titlebarLeftImage!!.setImageResource(resId)
    }//   通过资源id设置左侧图片样式
    fun setRightImageResource(resId: Int) {
        titlebarRightImage!!.setImageResource(resId)
        titlebarRightImage!!.visibility= VISIBLE
    }//   通过资源id设置左侧图片样式
    fun setFollowImageResource(resId: Int) {
        titlebarFollowImage!!.setImageResource(resId)
    }
    fun setFollowImageClickListener(listener: OnClickListener){
        titlebarFollowImage!!.setOnClickListener(listener)
    }
    //   通过资源id设置左侧图片样式
    fun setTitleClickListener(listener: OnClickListener) {
        titleText.setOnClickListener(listener)
    }
    //   右侧布局监听
    fun setRightLayoutClickListener(listener: OnClickListener) {
        titlebarRight!!.setOnClickListener(listener)
    }
    //  展示搜索栏,并设置回调
    fun showSearchWidget(hintText:String="请输入关键词",searchRes:Int?=null,a:(keyword:String)->Unit){
        titleText.visibility= GONE
        searchWight.visibility= VISIBLE
        btnClear.setOnClickListener{editKeyWord?.text=null}
        searchRes?.let {
            iconSearch?.let { v->
                Glide.with(context).load(it).centerCrop().into(v)
            }
        }
        editKeyWord?.apply {
            searchRes?.let {  }
            hint=hintText
            setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    val keyword = textView.text.toString().trim { it <= ' ' }
                    if (keyword.isNotEmpty()) {
                        a(keyword)
                    }
                    true
                } else {
                    false
                }
            }
        }
    }
    fun getSearchWord()=editKeyWord?.text?.toString()
}
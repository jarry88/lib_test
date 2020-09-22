package com.ftofs.twant.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.ftofs.twant.R
import de.hdodenhof.circleimageview.CircleImageView

class ThirdLogin @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var btnWechat :ImageView
    lateinit var btnFaceBook :ImageView
    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ThirdLogin)
        View.inflate(context, R.layout.third_login_wighet, this)
        btnWechat=rootView.findViewById<CircleImageView>(R.id.btn_wechat)
        btnFaceBook=rootView.findViewById(R.id.btn_facebook)
        typedArray.recycle()
    }

    fun setBtnWeChat(listener: OnClickListener) = btnWechat.setOnClickListener(listener)
    fun setFaceBook(listener: OnClickListener)=btnFaceBook.setOnClickListener(listener)

}
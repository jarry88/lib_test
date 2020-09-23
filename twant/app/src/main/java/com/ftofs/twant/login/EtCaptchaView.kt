package com.ftofs.twant.login


import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.core.widget.doAfterTextChanged
import com.ftofs.twant.R
import com.ftofs.lib_net.model.MobileZone
import com.gzp.lib_common.utils.SLog
import java.util.regex.Pattern

/**
 * 1、在xml、fragment中声明并绑定控件
 * 2、调用接口获取 mobilelist 数据 赋值给控件
 * 3、onselect时传递序号进来
 * 4、调取checkerror检查状态
 * 5、返回true时 取值使用
 * 6、返回false时 查看msg
 */
@SuppressLint("CustomViewStyleable")
class EtCaptchaView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val minLength=4
    private val etCaptcha:EditText by lazy {
        rootView.findViewById(R.id.et_captcha)
    }
    private var errorText:TextView?=null
    private var llErrorContainer:LinearLayout?=null
    var btnCaptchaView:CaptchaView?=null
    var isRight = false //默认为异常
    var msg = "未輸入號碼"


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingCaptchaView)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        View.inflate(context,R.layout.wdget_edit_captcha,this)
        errorText=rootView.findViewById(R.id.tv_mobile_error)
        llErrorContainer=rootView.findViewById(R.id.ll_container_mobile_error)
        btnCaptchaView=rootView.findViewById(R.id.btn_refresh_captcha)
        initTextChangedListener()
    }

    /**
     * 有异常返回true
     */
    fun checkError():Pair<Boolean,String>{
        SLog.info(  "isRight %s,msg %s",isRight,msg)
        return Pair(isRight,msg)
    }
    fun getCaptcha():String?{
        return etCaptcha.text?.toString()
    }
    private fun initTextChangedListener(){
        etCaptcha.doAfterTextChanged { updateRight() }
        etCaptcha.setOnFocusChangeListener { v, hasFocus ->
           updateRight()
            if (!hasFocus) {
                llErrorContainer?.apply {
                    visibility=if(isRight) GONE else VISIBLE
                }
            } else llErrorContainer?.visibility=View.GONE
        }
    }

    fun showError() {
        llErrorContainer?.visibility=if (isRight) GONE else VISIBLE
    }
    fun updateRight() {
        isRight= getCaptcha()?.run {
            length>=minLength
        }?:false
    }
}
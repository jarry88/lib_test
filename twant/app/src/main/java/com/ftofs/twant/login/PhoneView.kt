package com.ftofs.twant.login

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.ftofs.ft_login.R
import com.ftofs.lib_net.model.MobileZone
import com.gzp.lib_common.utils.SLog
import java.util.regex.Pattern

const val text_invalid_mobile="你輸入的%s手機號碼有誤，請重新輸入"
const val errorValidTip="此號碼無法用於進行註冊或登入"
const val LandIndex=2//内地
val regex= listOf(
        "",
        "^[569][0-9]{7}$", // 香港
        "^1[0-9]{10}$",    // 大陸
        "^6[0-9]{7}$"   // 澳門
)
val isValidRegex ="^1(([7][0,1])|([6][2,5,7])|[3][4][9])[0-9]{0,10}$"
//3、虛擬運營商號段：
//
//電信：1700、1701、1702、162
//
//移動：1703、1705、1706 、165
//聯通：1704、1707、1708、1709、167、171
//衛星通訊：1349
/**
 * 1、在xml、fragment中声明并绑定控件
 * 2、调用接口获取 mobilelist 数据 赋值给控件
 * 3、onselect时传递序号进来
 * 4、调取checkerror检查状态
 * 5、返回true时 取值使用
 * 6、返回false时 查看msg
 */
@SuppressLint("CustomViewStyleable")
class PhoneView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var currAreaCode: String?=null
    var mobileList= listOf<MobileZone>()
    private val mContext:Context=context
    private val areaArray = arrayOf(
            "",
            "香港",
           "内地",
           "澳門"
    )

    private var zoneIndex:Int=0//弹窗位置序号
    private val etMobile:EditText by lazy {
        rootView.findViewById(R.id.et_mobile)
    }
    private var errorText:TextView?=null
    private var tvZone:TextView?=null
    private var llErrorContainer:LinearLayout?=null
    private var llZoneSelect:LinearLayout?=null
    var isRight = false //默认为异常
    var msg = "手機號碼錯誤"


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingPhoneView)
        initView()
        typedArray.recycle()
    }

    private fun initView() {
        View.inflate(context,R.layout.wdget_phone,this)
        errorText=rootView.findViewById(R.id.tv_mobile_error)
        llErrorContainer=rootView.findViewById(R.id.ll_container_mobile_error)
        llZoneSelect=rootView.findViewById(R.id.ll_zone_select)
        tvZone=rootView.findViewById(R.id.tv_zone)
        initTextChangedListener()
    }
    fun setZoneIndex(index:Int){
        if( mobileList.size<=index)return
        zoneIndex=mobileList[index].areaId
        etMobile.text=etMobile.text
        tvZone?.text=mobileList[index].areaName
        currAreaCode=mobileList[index].areaCode
    }
    fun setZoneSelect(listener: OnClickListener){llZoneSelect?.setOnClickListener(listener)}

    /**
     * 有异常返回true
     */
    fun checkError():Pair<Boolean,String>{
        SLog.info(  "isRight %s,msg %s",isRight,msg)
        return Pair(isRight,msg)
    }
    fun getPhone():String{
        return if(mobileList.isEmpty()) ""
        else currAreaCode?.plus(",")?.plus(etMobile.text?:"") ?:""
    }
    fun getEditMobile():String{
        return etMobile.text?.toString()?:""
    }
    fun setPhone(phone:String?){
        etMobile.setText(phone)
    }
    private fun initTextChangedListener(){
        etMobile.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                llErrorContainer?.apply {
                    if (!isRight) {
                        this.visibility = View.VISIBLE
                        errorText?.text = msg

                    } else this.visibility = View.GONE
                }
            } else llErrorContainer?.visibility=View.GONE
        }
        etMobile.doAfterTextChanged { text->
            SLog.info(regex[zoneIndex]+areaArray[zoneIndex]+text)
            isRight=true
            if(text==null||text.isEmpty()){
                isRight=false
                msg="手機號碼錯誤"
            }else if(mobileList.size<zoneIndex){
                isRight=false
                msg="网络异常"
            }else{
                val matchResult=Pattern.compile(regex[zoneIndex]).matcher(text).matches()//首先匹配基本规则
                if (matchResult) {
                    if (zoneIndex == LandIndex) {
                        if (Pattern.compile(isValidRegex).matcher(text).matches()) {
                            msg = errorValidTip
                            isRight = false
                        }
                    }
                }else{
                    msg = text_invalid_mobile.format(areaArray[zoneIndex])
                    isRight=false
                }
             }

        }
    }

    fun getIndex(): Int? {
        return if(mobileList.isEmpty()) null else 0
    }

    fun showError() {
        llErrorContainer?.visibility=if (isRight) GONE else VISIBLE
    }

}
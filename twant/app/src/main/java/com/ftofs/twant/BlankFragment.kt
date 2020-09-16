package com.ftofs.twant

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.gzp.lib_common.base.BaseFragment
import com.ftofs.twant.kotlin.BlankAdapter
import com.ftofs.lib_net.BaseRepository
import com.gzp.lib_common.utils.SLog
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.lyrebirdstudio.croppylib.Croppy
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyTheme
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.util.*
import kotlin.concurrent.timerTask

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : BaseFragment() , CoroutineScope by MainScope() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val  imageViewCropped by lazy {
        view?.findViewById<AppCompatImageView>(R.id.imageViewCropped)
    }
//    private val viewModel by lazy {
//        BlackViewModel()
//    }
    private val  rvList by lazy {
        view?.findViewById<RecyclerView>(R.id.rv_list)
    }
    private val  buttonChoose by lazy {
        view?.findViewById<Button>(R.id.buttonChoose)
    }
    private val  buttonPost by lazy {
        view?.findViewById<Button>(R.id.btn_post)
    }
    private val  buttonPost1 by lazy {
        view?.findViewById<Button>(R.id.btn_post1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    val adapter by  lazy { BlankAdapter()

//        override fun onBindViewHolder(holder: DataBoundViewHolder<VerificationGoodsItemBinding>, position: Int, payloads: MutableList<Any>) {
//            holder.da
//        }
//
//        open fun getItem(position: Int) {
//            return mDiffer.
//        }
    }
    override fun simpleBind(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_blank
    }
    val net by lazy {
        object : BaseRepository(){}
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                BlankFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        private const val RC_CROP_IMAGE = 102
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = listOf(
                OrdersGoodsVo().apply { goodsName="3"
                    imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
                },
                OrdersGoodsVo().apply { goodsName="3"
                    imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
                },OrdersGoodsVo().apply { goodsName="3"
            imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
        },OrdersGoodsVo(),OrdersGoodsVo()
                ,OrdersGoodsVo(),OrdersGoodsVo().apply { goodsName="3"
            imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
        },OrdersGoodsVo().apply { goodsName="3"
            imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
        }
        )
        rvList?.adapter=adapter
        SLog.info("绑定资源股")

        var current =list.size-1
        fun h(action: ()->Unit){
            Timer().schedule(timerTask {
                val a = listOf(
                        OrdersGoodsVo().apply { goodsName="3"
                            imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
                        },
                        OrdersGoodsVo().apply { goodsName="33333"
                            imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
                        },OrdersGoodsVo().apply { goodsName="3"
                    imageSrc="image/67/8d/678d4bc4271b8472660b0de98299e976.jpg"
                },OrdersGoodsVo(),OrdersGoodsVo())
                adapter.submitList(a)
                action()
            },1200)
        }
        buttonPost?.setOnClickListener{
            SLog.info( "执行$current")
            h{current--}
        }
//        h {h{h{h{current--}}}}
        list[0].goodsName="sdfdf"
        list[1].goodsName="11"
        list[2].goodsName="2222"
        list[3].goodsName="3333"
        list[4].goodsName="44444"

        adapter.submitList(list)
//        adapter.addAll(list,true)



//        buttonChoose?.setOnClickListener {
//            startCroppy()
//        }
//        buttonPost1?.setOnClickListener {
//            net.run {
//                launch {
//                    val result=simpleGet(api.testPost1(User.getToken()))
//                    when(result){
//                        is Result.Success -> ToastUtil.error(context,result.datas.message)
//                        is Result.DataError -> ToastUtil.error(context,result.datas.error)
//                        is Result.Error -> SLog.info(result.exception.toString())
//                    }
//                }
//            }
//        }
//        buttonPost?.setOnClickListener{
//            val token = User.getToken()
//
//            val api=object :BaseRepository(){
//                suspend fun testPost(): com.gzp.lib_common.constant.Result<ZoneInfo> {
//                    return safeApiCall(call = {
//                        executeResponse(
////                                api.testPost(
////                                        RequestBody.create(
////                                                MediaType.parse("application/json; charset=utf-8"),
////                                                GsonUtil.bean2String(
////                                                        mapOf(
////                                                                "token" to token
////                                                        )
////                                                )
////                                        )
////                                )
//                                        api.testPost1(token)
//                        )
//                    }
//                    )
//                }
//            }
//            launch {
//                api.testPost()
//            }
//        }

//        val  adapter =OrderGoodsVoListAdapter()
//        rvList?.adapter=adapter
//        adapter.addAll(listOf(OrdersGoodsVo()),false)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_CROP_IMAGE) {
            data?.data?.let {
                SLog.info("TEST Croppy Data:[%s]",data.toString())

                imageViewCropped?.setImageURI(it)
            }
        }
    }

    private fun startCroppy() {
        val uri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.aa))
                .appendPath(resources.getResourceTypeName(R.drawable.aa))
                .appendPath(resources.getResourceEntryName(R.drawable.aa))
                .build()

//        //Saves to external and return uri
//        val externalCropRequest = CropRequest.Auto(
//                sourceUri = uri,
//                requestCode = RC_CROP_IMAGE
//        )
//
//        //Saves to cache and return uri
//        val cacheCropRequest = CropRequest.Auto(
//                sourceUri = uri,
//                requestCode = RC_CROP_IMAGE,
//                storageType = StorageType.CACHE
//        )

        // Save to given destination uri.
        val destinationUri =
                FileCreator
                        .createFile(FileOperationRequest.createRandom(), _mActivity)
                        .toUri()
//
//        val manualCropRequest = CropRequest.Manual(
//                sourceUri = uri,
//                destinationUri = destinationUri,
//                requestCode = RC_CROP_IMAGE
//        )
//
//        val excludeAspectRatiosCropRequest = CropRequest.Manual(
//                sourceUri = uri,
//                destinationUri = destinationUri,
//                requestCode = RC_CROP_IMAGE,
//                excludedAspectRatios = arrayListOf(AspectRatio.ASPECT_FREE)
//        )

        val themeCropRequest = CropRequest.Manual(
                sourceUri = uri,
                destinationUri = destinationUri,
                requestCode = RC_CROP_IMAGE,
                croppyTheme = CroppyTheme(R.color.blue)
        )

        activity?.let { Croppy.start(it, themeCropRequest) }
    }

    override fun onBackPressedSupport(): Boolean {
        hideSoftInputPop()
        return true
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        cancel()
    }
}
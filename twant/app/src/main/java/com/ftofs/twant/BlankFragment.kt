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
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ftofs.twant.databinding.FragmentBlankBinding
import com.ftofs.twant.databinding.VerificationGoodsItemBinding
import com.ftofs.twant.entity.Goods
import com.ftofs.twant.fragment.BaseFragment
import com.ftofs.twant.kotlin.BlackViewModel
import com.ftofs.twant.kotlin.BuyerGoodsListAdapter
import com.ftofs.twant.kotlin.OrderGoodsVoListAdapter
import com.ftofs.twant.kotlin.adapter.DataBoundAdapter
import com.ftofs.twant.kotlin.bean.ZoneInfo
import com.ftofs.twant.kotlin.net.BaseRepository
import com.ftofs.twant.kotlin.net.Result
import com.ftofs.twant.log.SLog
import com.ftofs.twant.util.ToastUtil
import com.ftofs.twant.util.User
import com.ftofs.twant.vo.orders.OrdersGoodsVo
import com.ftofs.twant.widget.VerificationPopup
import com.google.android.material.button.MaterialButton
import com.lxj.xpopup.XPopup
import com.lyrebirdstudio.aspectratiorecyclerviewlib.aspectratio.model.AspectRatio
import com.lyrebirdstudio.croppylib.Croppy
import com.lyrebirdstudio.croppylib.main.CropRequest
import com.lyrebirdstudio.croppylib.main.CroppyTheme
import com.lyrebirdstudio.croppylib.main.StorageType
import com.lyrebirdstudio.croppylib.util.file.FileCreator
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest
import com.wzq.mvvmsmart.net.net_utils.GsonUtil
import kotlinx.android.synthetic.main.fragment_blank.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

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


    override fun simpleBind(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int {
        return R.layout.fragment_blank
    }
    val net by lazy {
        object :BaseRepository(){}
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
        buttonChoose?.setOnClickListener {
            startCroppy()
        }
        buttonPost1?.setOnClickListener {
            net.run {
                launch {
                    val result=simpleGet(api.testPost1(User.getToken()))
                    when(result){
                        is Result.Success -> ToastUtil.error(context,result.datas.message)
                        is Result.DataError -> ToastUtil.error(context,result.datas.error)
                        is Result.Error -> SLog.info(result.exception.toString())
                    }
                }
            }
        }
        buttonPost?.setOnClickListener{
            val token = User.getToken()

            val api=object :BaseRepository(){
                suspend fun testPost(): com.ftofs.twant.kotlin.net.Result<ZoneInfo> {
                    return safeApiCall(call = {
                        executeResponse(
//                                api.testPost(
//                                        RequestBody.create(
//                                                MediaType.parse("application/json; charset=utf-8"),
//                                                GsonUtil.bean2String(
//                                                        mapOf(
//                                                                "token" to token
//                                                        )
//                                                )
//                                        )
//                                )
                                        api.testPost1(token)
                        )
                    }
                    )
                }
            }
            launch {
                api.testPost()
            }
        }

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
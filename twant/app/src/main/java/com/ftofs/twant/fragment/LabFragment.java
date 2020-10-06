package com.ftofs.twant.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ftofs.twant.BlankFragment;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.CategoryCommodity;
import com.ftofs.twant.entity.StoreVoucher;
import com.ftofs.twant.hot_zone.HotZoneFragment;
import com.ftofs.twant.kotlin.BlackTestFragment;
import com.ftofs.twant.util.CroppyInitUtilKt;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;
import com.ftofs.twant.widget.CheckPhoneView;
import com.ftofs.twant.widget.NineLuckPan;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.lyrebirdstudio.aspectratiorecyclerviewlib.aspectratio.model.AspectRatio;
import com.lyrebirdstudio.croppylib.Croppy;
import com.lyrebirdstudio.croppylib.main.CropRequest;
import com.lyrebirdstudio.croppylib.main.CroppyTheme;
import com.lyrebirdstudio.croppylib.main.StorageType;
import com.lyrebirdstudio.croppylib.util.file.FileCreator;
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest;
import com.gzp.lib_common.smart.utils.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 實驗性Fragment
 *
 * @author zwm
 */
public class LabFragment extends BaseFragment implements View.OnClickListener {
    private static final int RC_CROP_IMAGE = 2;
    private static final String TAG = LabFragment.class.getSimpleName();

    private NineLuckPan luckpan;
    private AppCompatImageView imageViewCropped;
    private CropRequest.Manual themeCropRequest;
    private CropRequest.Manual excludeAspectRatiosCropRequest;
    private CropRequest.Manual manualCropRequest;
    private CropRequest.Auto cacheCropRequest;
    private CropRequest.Auto externalCropRequest;
    private RecyclerView recyclerView;
    private TextView btnTest3;

    public static LabFragment newInstance() {
        LabFragment fragment = new LabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int simpleBind(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_lab;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Util.setOnClickListener(view, R.id.btn_test1, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
        Util.setOnClickListener(view, R.id.btn_goto, this);
        Util.setOnClickListener(view, R.id.btn_themeCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_cacheCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_excludeAspectRatiosCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_manualCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_externalCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_post1, this);

        btnTest3 = view.findViewById(R.id.btn_test2);
        recyclerView = view.findViewById(R.id.rv_list);
        luckpan = view.findViewById(R.id.luckpan);
        imageViewCropped = view.findViewById(R.id.imageViewCropped);
        luckpan.setOnLuckPanAnimEndListener(new NineLuckPan.OnLuckPanAnimEndListener() {
            @Override
            public void onAnimEnd(int position, String msg) {
                Toast.makeText(_mActivity, "位置："+position+"提示信息："+msg, Toast.LENGTH_SHORT).show();
            }
        });
        productRequest();
        View vw = view.findViewById(R.id.vw);
        vw.setBackground(BackgroundDrawable.create(Color.CYAN, Util.dip2px(_mActivity, 8)));
        CheckPhoneView checkPhoneView = view.findViewById(R.id.check_phone);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, 3);
        TestBindAdapter adapter = new TestBindAdapter();
        List<CategoryCommodity> list = new ArrayList<>();
        list.add(new CategoryCommodity(10, "sdf",   "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/24/3e/243ec09e3c46e872517fa020b65f9ed4.png"));
        list.add(new CategoryCommodity(112, "a", "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/24/3e/243ec09e3c46e872517fa020b65f9ed4.png"));
        list.add(new CategoryCommodity(143, "b", "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/24/3e/243ec09e3c46e872517fa020b65f9ed4.png"));
        list.add(new CategoryCommodity(14, "c", "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/24/3e/243ec09e3c46e872517fa020b65f9ed4.png"));
        list.add(new CategoryCommodity(20, "m",   "https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/24/3e/243ec09e3c46e872517fa020b65f9ed4.png"));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });
        adapter.showHeadView(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.addAll(list,true);
        btnTest3.setText("打開熱區頁");
    }

    private void productRequest() {
        Uri uri= new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getResources().getResourcePackageName(R.drawable.aa))
                .appendPath(getResources().getResourceTypeName(R.drawable.aa))
                .appendPath(getResources().getResourceEntryName(R.drawable.aa))
                .build();
        Uri destinationUri = CroppyInitUtilKt.toUri(FileCreator.INSTANCE
                .createFile(FileOperationRequest.Companion.createRandom(), _mActivity));
        //下面是調用kotlin的方法生成
//        CropRequest cacheCropRequest=CroppyInitUtilKt.cacheCropRequest(_mActivity, Constant.RC_CROP_IMAGE,uri);
//        CropRequest externalCropRequest=CroppyInitUtilKt.externalCropRequest(_mActivity, Constant.RC_CROP_IMAGE,uri);
//        CropRequest manualCropRequest=CroppyInitUtilKt.manualCropRequest(_mActivity, Constant.RC_CROP_IMAGE,uri);
//        CropRequest excludeAspectRatiosCropRequest=CroppyInitUtilKt.excludeAspectRatiosCropRequest(_mActivity, Constant.RC_CROP_IMAGE,uri);
//        CropRequest themeCropRequest=CroppyInitUtilKt.themeCropRequest(_mActivity, Constant.RC_CROP_IMAGE,uri);
        //下面是用java形式調用
        cacheCropRequest = new CropRequest.Auto(
                uri,
                Constant.RC_CROP_IMAGE,
                StorageType.CACHE,
                new ArrayList<AspectRatio>(),
                new CroppyTheme(R.color.blue)
        );
        externalCropRequest = new CropRequest.Auto(
                uri,
                Constant.RC_CROP_IMAGE,
                StorageType.EXTERNAL,
                new ArrayList<AspectRatio>(),

                new CroppyTheme(R.color.blue)
        );
        manualCropRequest = new CropRequest.Manual(
                uri,
                destinationUri,
                Constant.RC_CROP_IMAGE,
                new ArrayList<AspectRatio>(),

                new CroppyTheme(R.color.blue)
        );
         excludeAspectRatiosCropRequest = new CropRequest.Manual(
                uri,
                destinationUri,
                Constant.RC_CROP_IMAGE,
                 new ArrayList<AspectRatio>(),

                 new CroppyTheme(R.color.blue)
        );
        themeCropRequest=new CropRequest.Manual(
                uri,
                destinationUri,
                RC_CROP_IMAGE,
                new ArrayList<AspectRatio>(),

                new CroppyTheme(R.color.blue)
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_test1) {
            ToastUtil.info(_mActivity, "拉起登陆页");
            SLog.info(TAG);
            KLog.INSTANCE.e(TAG, "4");
//            start(new MessageFragment("18578276310",false,false));
            // RestartApp.restartThroughIntentCompatMakeRestartActivityTask(_mActivity);
//            ToastUtil.success(getContext(),"打開測試activity");
//            startActivity(new Intent(getActivity(), TestActivity.class));
//            start(TestFragment.newInstance());
        }else if (id == R.id.btn_post1) {
            ToastUtil.info(_mActivity, "btn_post1");
            new XPopup.Builder(getContext()).asCustom(new MoonVoucherListPopup(getContext(),new ArrayList<StoreVoucher>(),"10")).show();
        } else if (id == R.id.btn_test2) {
            ToastUtil.info(_mActivity, "進入熱區頁 12");
            KLog.INSTANCE.e("進入熱區頁");
            //49  8080
            start(new BlackTestFragment());
//            RestartApp.restartThroughPendingIntentAlarmManager(_mActivity);
        } else if (id == R.id.btn_goto) {
            ToastUtil.info(_mActivity, "way3");
            start(BlankFragment.newInstance("a","b"));
        }else if (id == R.id.btn_themeCropRequest) {
            ToastUtil.info(_mActivity, "btn_themeCropRequest");
            Croppy.INSTANCE.start(getActivity(), themeCropRequest);
        }else if (id == R.id.btn_cacheCropRequest) {
            ToastUtil.info(_mActivity, "cacheCropRequest");
            Croppy.INSTANCE.start(getActivity(), cacheCropRequest);

        }else if (id == R.id.btn_externalCropRequest) {

            ToastUtil.info(_mActivity, "externalCropRequest");
            Croppy.INSTANCE.start(getActivity(), externalCropRequest);

        }else if (id == R.id.btn_manualCropRequest) {
            ToastUtil.info(_mActivity, "manualCropRequest");
            Croppy.INSTANCE.start(getActivity(), manualCropRequest);

        }else if (id == R.id.btn_excludeAspectRatiosCropRequest) {
            ToastUtil.info(_mActivity, "excludeAspectRatiosCropRequest");
            Croppy.INSTANCE.start(getActivity(), excludeAspectRatiosCropRequest);

        }
    }

    private void startCroppy() {

        Croppy.INSTANCE.start(getActivity(), themeCropRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SLog.info("__requestCode[%d], resultCode[%d]", requestCode, resultCode);
        if (requestCode == Constant.RC_CROP_IMAGE) {
            if(data!=null){
                SLog.info("TEST Croppy Data:[%s]",data.toString());
                imageViewCropped.setImageURI(Uri.parse(data.toString()));
            }
//                binding.imageViewCropped.setImageURI(it)

        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

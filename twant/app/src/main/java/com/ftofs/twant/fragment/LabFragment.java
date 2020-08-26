package com.ftofs.twant.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.databinding.FragmentLabBinding;
import com.ftofs.twant.kotlin.net.BaseRepository;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.CroppyInitUtilKt;
import com.ftofs.twant.util.RestartApp;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;
import com.ftofs.twant.widget.CheckPhoneView;
import com.ftofs.twant.widget.NineLuckPan;

import com.lyrebirdstudio.aspectratiorecyclerviewlib.aspectratio.model.AspectRatio;
import com.lyrebirdstudio.croppylib.Croppy;
import com.lyrebirdstudio.croppylib.main.CropRequest;
import com.lyrebirdstudio.croppylib.main.CroppyTheme;
import com.lyrebirdstudio.croppylib.main.StorageType;
import com.lyrebirdstudio.croppylib.util.file.FileCreator;
import com.lyrebirdstudio.croppylib.util.file.FileOperationRequest;

import java.net.URI;
import java.util.ArrayList;

/**
 * 實驗性Fragment
 *
 * @author zwm
 */
public class LabFragment extends BaseFragment implements View.OnClickListener {
    private static final int RC_CROP_IMAGE = 2;
    private NineLuckPan luckpan;
    private AppCompatImageView imageViewCropped;
    private CropRequest.Manual themeCropRequest;
    private CropRequest.Manual excludeAspectRatiosCropRequest;
    private CropRequest.Manual manualCropRequest;
    private CropRequest.Auto cacheCropRequest;
    private CropRequest.Auto externalCropRequest;

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
        FragmentLabBinding fragmentLabBinding = (FragmentLabBinding) simpleBind;

        Util.setOnClickListener(view, R.id.btn_test1, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
        Util.setOnClickListener(view, R.id.btn_themeCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_cacheCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_excludeAspectRatiosCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_manualCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_externalCropRequest, this);
        Util.setOnClickListener(view, R.id.btn_post1, this);

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
            ToastUtil.info(_mActivity, "way1");
            RestartApp.restartThroughIntentCompatMakeRestartActivityTask(_mActivity);
        }else if (id == R.id.btn_post1) {
            ToastUtil.info(_mActivity, "btn_post1");

        } else if (id == R.id.btn_test2) {
            ToastUtil.info(_mActivity, "way2");
            RestartApp.restartThroughPendingIntentAlarmManager(_mActivity);
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

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

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.CroppyInitUtilKt;
import com.ftofs.twant.util.RestartApp;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;
import com.ftofs.twant.widget.CheckPhoneView;
import com.ftofs.twant.widget.NineLuckPan;

import com.lyrebirdstudio.croppylib.Croppy;
import com.lyrebirdstudio.croppylib.main.CropRequest;

import java.net.URI;

/**
 * 實驗性Fragment
 *
 * @author zwm
 */
public class LabFragment extends BaseFragment implements View.OnClickListener {
    private static final int RC_CROP_IMAGE = 2;
    private NineLuckPan luckpan;
    private AppCompatImageView imageViewCropped;

    public static LabFragment newInstance() {
        LabFragment fragment = new LabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_test1, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
        Util.setOnClickListener(view, R.id.btn_croppy, this);

        luckpan = view.findViewById(R.id.luckpan);
        imageViewCropped = view.findViewById(R.id.imageViewCropped);
        luckpan.setOnLuckPanAnimEndListener(new NineLuckPan.OnLuckPanAnimEndListener() {
            @Override
            public void onAnimEnd(int position, String msg) {
                Toast.makeText(_mActivity, "位置："+position+"提示信息："+msg, Toast.LENGTH_SHORT).show();
            }
        });

        View vw = view.findViewById(R.id.vw);
        vw.setBackground(BackgroundDrawable.create(Color.CYAN, Util.dip2px(_mActivity, 8)));
        CheckPhoneView checkPhoneView = view.findViewById(R.id.check_phone);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_test1) {
            ToastUtil.info(_mActivity, "way1");
            RestartApp.restartThroughIntentCompatMakeRestartActivityTask(_mActivity);
        } else if (id == R.id.btn_test2) {
            ToastUtil.info(_mActivity, "way2");
            RestartApp.restartThroughPendingIntentAlarmManager(_mActivity);
        }else if (id == R.id.btn_croppy) {
            ToastUtil.info(_mActivity, "croppy");
            startCroppy();
        }
    }

    private void startCroppy() {
        Uri uri= new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(getResources().getResourcePackageName(R.drawable.aa))
                .appendPath(getResources().getResourceTypeName(R.drawable.aa))
                .appendPath(getResources().getResourceEntryName(R.drawable.aa))
                .build();
        CropRequest request=CroppyInitUtilKt.croppyExampleParams(_mActivity, Constant.RC_CROP_IMAGE,uri);
        Croppy.INSTANCE.start(getActivity(), request);
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

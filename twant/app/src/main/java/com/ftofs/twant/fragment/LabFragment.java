package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.entity.DownloadImageResult;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.RestartApp;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.NineLuckPan;

import java.io.File;
import java.io.FileInputStream;

/**
 * 實驗性Fragment
 *
 * @author zwm
 */
public class LabFragment extends BaseFragment implements View.OnClickListener {
    private NineLuckPan luckpan;

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

        luckpan = view.findViewById(R.id.luckpan);
        luckpan.setOnLuckPanAnimEndListener(new NineLuckPan.OnLuckPanAnimEndListener() {
            @Override
            public void onAnimEnd(int position, String msg) {
                Toast.makeText(_mActivity, "位置："+position+"提示信息："+msg, Toast.LENGTH_SHORT).show();
            }
        });
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
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

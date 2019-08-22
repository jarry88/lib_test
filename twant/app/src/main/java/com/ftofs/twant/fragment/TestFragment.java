package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;
import com.ftofs.twant.widget.QuickClickButton;
import com.ftofs.twant.widget.TwProgressBar;

import java.io.File;
import java.io.IOException;
import java.util.List;

import top.zibin.luban.Luban;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    int i = 0;
    TwProgressBar progressBar;
    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view, R.id.btn_test, this);

        progressBar = view.findViewById(R.id.tw_progress_bar);
        progressBar.setColor(TwProgressBar.COLOR_ORANGE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_test) {
            SLog.info("path[%s]", Environment.getExternalStorageDirectory().getAbsolutePath());

            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/DCIM/Camera/IMG_20190813_104425.jpg";
            File file = new File(filePath);
            SLog.info("fileSize[%d]", file.length());

            try {
                List<File> fileList = Luban.with(_mActivity).load(file).get();
                for (File item : fileList) {
                    SLog.info("item[%s][%d]", item.getAbsolutePath(), item.length());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}

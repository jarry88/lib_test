package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.ImageProcess;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.Poster;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.Jarbon;
import com.gzp.lib_common.utils.SLog;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;

import org.jetbrains.annotations.NotNull;

public class Test2Fragment extends BaseFragment implements View.OnClickListener {
    Poster poster;

    public static Test2Fragment newInstance() {
        Bundle args = new Bundle();

        Test2Fragment fragment = new Test2Fragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        poster = view.findViewById(R.id.poster);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_test, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_test) {
            Bitmap bitmap = QMUIDrawableHelper.createBitmapFromView(poster);
            String path = Environment.getExternalStorageDirectory() + "/1/" + (new Jarbon()).format("Y-m-d H-i-s") + ".jpg";
            SLog.info("path[%s]", path);
            BitmapUtil.Bitmap2File(bitmap, path, Bitmap.CompressFormat.JPEG,75);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

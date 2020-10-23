package com.ftofs.twant.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.util.BitmapUtil;
import com.ftofs.twant.util.ImageProcess;
import com.ftofs.twant.util.QRCode;
import com.ftofs.twant.util.QRCodeUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.Poster;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.Jarbon;
import com.gzp.lib_common.utils.SLog;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;

import org.jetbrains.annotations.NotNull;

public class Test2Fragment extends BaseFragment implements View.OnClickListener {
    ImageView imgOriginal;
    ImageView imgTrimmed;
    ImageView imgTrimmed2;

    EditText etUrl;
    EditText etBlankWidth;

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

        etUrl = view.findViewById(R.id.et_url);
        etBlankWidth = view.findViewById(R.id.et_blank_width);

        imgOriginal = view.findViewById(R.id.img_original);
        imgTrimmed = view.findViewById(R.id.img_trimmed);
        imgTrimmed2 = view.findViewById(R.id.img_trimmed2);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_test, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_test) {
            String url = etUrl.getText().toString();
            Bitmap qrCode = QRCodeUtil.createQRCodeBitmap(url, 500, 500);
            SLog.info("qrCode, w[%d], h[%d]", qrCode.getWidth(), qrCode.getHeight());
            Glide.with(_mActivity).load(qrCode).centerCrop().into(imgOriginal);

            int blank = Integer.parseInt(etBlankWidth.getText().toString());
            Bitmap trimmed = Bitmap.createBitmap(qrCode, blank, blank,
                    qrCode.getWidth() - 2 * blank, qrCode.getHeight() - 2 * blank);
            SLog.info("trimmed, blank[%d], w[%d], h[%d]", blank, trimmed.getWidth(), trimmed.getHeight());
            Glide.with(_mActivity).load(trimmed).centerCrop().into(imgTrimmed);

            Bitmap trimmed2 = QRCodeUtil.createTrimmedBitmap(url);
            Glide.with(_mActivity).load(trimmed2).centerCrop().into(imgTrimmed2);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

package com.ftofs.twant.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;

import org.jetbrains.annotations.NotNull;


/**
 * 查看圖片Fragment
 * @author zwm
 */
public class ImageViewerFragment extends BaseFragment implements View.OnClickListener {
    String imagePath;
    boolean clickEnable;
    public static ImageViewerFragment newInstance(String imagePath) {
        Bundle args = new Bundle();

        args.putString("imagePath", imagePath);
        SLog.info("imagePath[%s]", imagePath);
        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static ImageViewerFragment newInstance(String imagePath,boolean click) {
        Bundle args = new Bundle();

        args.putString("imagePath", imagePath);
        SLog.info("imagePath[%s]", imagePath);
        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        fragment.clickEnable=click;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_viewer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        imagePath = args.getString("imagePath");

        // 如果是本地路徑，還要添加上file://協議前綴
        if (imagePath.startsWith("/")) {
            imagePath = "file://" + imagePath;
        }
        Util.setOnClickListener(view,R.id.ll_container,this);
        Util.setOnClickListener(view,R.id.btn_back,this);
        BigImageView bigImageView = view.findViewById(R.id.big_image_view);
        bigImageView.setProgressIndicator(new ProgressPieIndicator());
        bigImageView.showImage(Uri.parse(imagePath));
    }
    
    
    @Override
    public void onClick(View v) {

        int id = v.getId();
        SLog.info("點擊事件");
        if (id == R.id.ll_container) {
            if (clickEnable) {
                hideSoftInputPop();
            }
        } else if (id == R.id.btn_back) {
            pop();
        }

    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

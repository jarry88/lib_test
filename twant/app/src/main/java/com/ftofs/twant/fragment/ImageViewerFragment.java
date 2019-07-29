package com.ftofs.twant.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ftofs.twant.R;
import com.ftofs.twant.log.SLog;
import com.github.piasy.biv.indicator.progresspie.ProgressPieIndicator;
import com.github.piasy.biv.view.BigImageView;


/**
 * 查看圖片Fragment
 * @author zwm
 */
public class ImageViewerFragment extends BaseFragment implements View.OnClickListener {
    String imagePath;

    public static ImageViewerFragment newInstance(String imagePath) {
        Bundle args = new Bundle();

        args.putString("imagePath", imagePath);
        SLog.info("imagePath[%s]", imagePath);
        ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        BigImageView bigImageView = view.findViewById(R.id.big_image_view);
        bigImageView.setProgressIndicator(new ProgressPieIndicator());
        bigImageView.showImage(Uri.parse(imagePath));
    }
    
    
    @Override
    public void onClick(View v) {
        
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}

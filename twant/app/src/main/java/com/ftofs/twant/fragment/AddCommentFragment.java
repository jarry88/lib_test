package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;

/**
 * 發表評論Fragment 
 * @author zwm
 */
public class AddCommentFragment extends BaseFragment implements View.OnClickListener {
    RelativeLayout commentImageContainer;

    ImageView btnAddImage;
    ScaledButton btnRemoveImage;
    ImageView commentImage;

    public static AddCommentFragment newInstance() {
        Bundle args = new Bundle();

        AddCommentFragment fragment = new AddCommentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnAddImage.setOnClickListener(this);

        btnRemoveImage = view.findViewById(R.id.btn_remove_image);
        btnRemoveImage.setOnClickListener(this);

        commentImage = view.findViewById(R.id.comment_image);
        commentImageContainer = view.findViewById(R.id.comment_image_container);

        Util.setOnClickListener(view, R.id.btn_back, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add_image) {
            startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (id == R.id.btn_remove_image) {
            commentImageContainer.setVisibility(View.GONE);
            btnRemoveImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri = data.getData();
        String absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
        SLog.info("absolutePath[%s]", absolutePath);

        Glide.with(_mActivity).load(absolutePath).centerCrop().into(commentImage);
        btnAddImage.setVisibility(View.GONE);
        commentImageContainer.setVisibility(View.VISIBLE);
    }
}

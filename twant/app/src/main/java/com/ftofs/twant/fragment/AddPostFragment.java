package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CustomerServiceStaffListAdapter;
import com.ftofs.twant.adapter.PostImageListAdapter;
import com.ftofs.twant.log.SLog;

import java.util.ArrayList;
import java.util.List;


/**
 * 發表帖文Fragment
 * @author zwm
 */
public class AddPostFragment extends BaseFragment implements View.OnClickListener {
    BaseQuickAdapter adapter;
    List<String> postImageList = new ArrayList<>();
    public static AddPostFragment newInstance() {
        Bundle args = new Bundle();

        AddPostFragment fragment = new AddPostFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayout glPostImageList = view.findViewById(R.id.gl_post_image_list);
        for (int i = 1; i <= 141; i ++) {
            String url = String.format("http://www.gogo.so/emoticon/emoticon_%d.png", i);
            SLog.info("url[%s]", url);
            postImageList.add(url);


            ConstraintLayout itemView = (ConstraintLayout) LayoutInflater.from(_mActivity)
                    .inflate(R.layout.post_image_item, glPostImageList, false);
            ImageView imageView = itemView.findViewById(R.id.post_image);
            SLog.info("imageView[%s]", imageView);
            Glide.with(_mActivity).load(url).centerCrop().into(imageView);

            glPostImageList.addView(itemView);
        }



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

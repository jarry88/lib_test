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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BudgetPricePopup;
import com.ftofs.twant.widget.DateSelectPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.XPopup;

import java.util.ArrayList;
import java.util.List;


/**
 * 發表帖文Fragment
 * @author zwm
 */
public class AddPostFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    BaseQuickAdapter adapter;
    List<String> postImageList = new ArrayList<>();

    String title = "";
    String keyword = "";

    TextView tvTitle;
    TextView tvKeyword;
    String deadline;
    TextView tvDeadline;
    String budgetPrice = "";
    TextView tvBudgetPrice;

    String currencyTypeSign;

    SquareGridLayout postContentImageContainer;
    RelativeLayout rlPostCoverImageContainer;
    ImageView btnAddPostCoverImage;
    ImageView postCoverImage;

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

        currencyTypeSign = getString(R.string.currency_type_sign);

        tvTitle = view.findViewById(R.id.tv_title);
        tvKeyword = view.findViewById(R.id.tv_keyword);

        deadline = Time.date("Y-m-d");
        tvDeadline = view.findViewById(R.id.tv_deadline);
        tvDeadline.setText(deadline);

        tvBudgetPrice = view.findViewById(R.id.tv_budget_price);

        rlPostCoverImageContainer = view.findViewById(R.id.rl_post_cover_image_container);
        postContentImageContainer = view.findViewById(R.id.post_content_image_container);

        btnAddPostCoverImage = view.findViewById(R.id.btn_add_post_cover_image);
        btnAddPostCoverImage.setOnClickListener(this);

        postCoverImage = view.findViewById(R.id.post_cover_image);

        Util.setOnClickListener(view, R.id.btn_input_title, this);
        Util.setOnClickListener(view, R.id.btn_input_keyword, this);
        Util.setOnClickListener(view, R.id.btn_deadline, this);
        Util.setOnClickListener(view, R.id.btn_budget_price, this);

        Util.setOnClickListener(view, R.id.btn_remove_cover_image, this);
        Util.setOnClickListener(view, R.id.btn_add_post_content_image, this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_add_post_content_image) {
            startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (id == R.id.btn_remove_cover_image) {
            rlPostCoverImageContainer.setVisibility(View.GONE);
            btnAddPostCoverImage.setVisibility(View.VISIBLE);
        } else if (id == R.id.btn_add_post_cover_image) {
            startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), RequestCode.PICK_POST_COVER.ordinal()); // 打开相册
        } else if (id == R.id.btn_input_title) {
            startForResult(PostTitleEditorFragment.newInstance(PostTitleEditorFragment.FOR_TITLE, title), RequestCode.EDIT_TITLE.ordinal());
        } else if (id == R.id.btn_input_keyword) {
            startForResult(PostTitleEditorFragment.newInstance(PostTitleEditorFragment.FOR_KEYWORD, keyword), RequestCode.EDIT_KEYWORD.ordinal());
        } else if (id == R.id.btn_deadline) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new DateSelectPopup(_mActivity, Constant.POPUP_TYPE_DEADLINE, deadline, this))
                    .show();
        } else if (id == R.id.btn_budget_price) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(true)
                    .asCustom(new BudgetPricePopup(_mActivity, budgetPrice, this))
                    .show();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        SLog.info("onFragmentResult");
        super.onFragmentResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == RequestCode.EDIT_TITLE.ordinal()) {
            title = data.getString("content");
            tvTitle.setText(title);
        } else if (requestCode == RequestCode.EDIT_KEYWORD.ordinal()) {
            keyword = data.getString("content");
            tvKeyword.setText(keyword);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        // 選擇照片
        if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
            Uri uri = data.getData();
            String absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
            SLog.info("absolutePath[%s]", absolutePath);

            final View itemView = LayoutInflater.from(_mActivity).inflate(R.layout.post_content_image_widget, postContentImageContainer, false);
            ImageView postImage = itemView.findViewById(R.id.post_image);
            Glide.with(_mActivity).load(absolutePath).centerCrop().into(postImage);

            ScaledButton btnRemoveImage = itemView.findViewById(R.id.btn_remove_image);
            btnRemoveImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postContentImageContainer.removeView(itemView);
                }
            });

            int childCount = postContentImageContainer.getChildCount();
            if (childCount > 0) {
                postContentImageContainer.addView(itemView, childCount - 1);
            }
        } else if (requestCode == RequestCode.PICK_POST_COVER.ordinal()) {
            Uri uri = data.getData();
            String absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
            SLog.info("absolutePath[%s]", absolutePath);

            Glide.with(_mActivity).load(absolutePath).centerCrop().into(postCoverImage);
            btnAddPostCoverImage.setVisibility(View.GONE);
            rlPostCoverImageContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        if (type == Constant.POPUP_TYPE_DEADLINE) {
            deadline = (String) extra;
            tvDeadline.setText(deadline);
        } else if (type == Constant.POPUP_TYPE_BUDGET_PRICE) {
            budgetPrice = (String) extra;
            tvBudgetPrice.setText(currencyTypeSign + budgetPrice);
        }

    }
}

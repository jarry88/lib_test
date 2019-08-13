package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 貼文詳情Fragment
 * @author zwm
 */
public class PostDetailFragment extends BaseFragment implements View.OnClickListener {
    int postId;

    TextView tvPostTitle;
    ImageView imgAuthorAvatar;
    TextView tvNickname;
    TextView tvCreatetime;
    TextView tvDeadline;
    TextView tvBudgetPrice;
    TextView tvContent;

    SquareGridLayout sglImageContainer;
    TextView tvViewCount;
    ImageView imgThumb;
    TextView tvThumbCount;
    ImageView imgLike;
    TextView tvLikeCount;

    public static PostDetailFragment newInstance(int postId) {
        Bundle args = new Bundle();

        args.putInt("postId", postId);
        PostDetailFragment fragment = new PostDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        postId = args.getInt("postId", 0);

        tvPostTitle = view.findViewById(R.id.tv_post_title);
        imgAuthorAvatar = view.findViewById(R.id.img_author_avatar);
        tvNickname = view.findViewById(R.id.tv_nickname);
        tvCreatetime = view.findViewById(R.id.tv_create_time);
        tvDeadline = view.findViewById(R.id.tv_deadline);
        tvBudgetPrice = view.findViewById(R.id.tv_budget_price);
        tvContent = view.findViewById(R.id.tv_content);

        sglImageContainer = view.findViewById(R.id.sgl_image_container);

        tvViewCount = view.findViewById(R.id.tv_view_count);

        imgThumb = view.findViewById(R.id.img_thumb);
        tvThumbCount = view.findViewById(R.id.tv_thumb_count);
        imgLike = view.findViewById(R.id.img_like);
        tvLikeCount = view.findViewById(R.id.tv_like_count);

        Util.setOnClickListener(view, R.id.btn_back, this);
        loadData();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "postId", postId);

        Api.postUI(Api.PATH_POST_DETAIL, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONObject wantPostVoInfo = (EasyJSONObject) responseObj.get("datas.wantPostVoInfo");
                    EasyJSONObject memberVo = wantPostVoInfo.getObject("memberVo");


                    String title = wantPostVoInfo.getString("postCategory") + " | " + wantPostVoInfo.getString("title");
                    tvPostTitle.setText(title);

                    String avatarUrl = memberVo.getString("avatar");
                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(avatarUrl)).centerCrop().into(imgAuthorAvatar);

                    String nickname = memberVo.getString("nickName");
                    tvNickname.setText(nickname);

                    String createTime = wantPostVoInfo.getString("createTime");
                    tvCreatetime.setText(createTime);

                    String deadline = wantPostVoInfo.getString("expiresDate");
                    tvDeadline.setText(deadline);

                    float budgetPrice = (float) wantPostVoInfo.getDouble("budgetPrice");
                    tvBudgetPrice.setText(StringUtil.formatPrice(_mActivity, budgetPrice, 0));

                    String content = wantPostVoInfo.getString("content");
                    tvContent.setText(StringUtil.translateEmoji(_mActivity, content, (int) tvContent.getTextSize()));
                    EasyJSONArray wantPostImages = wantPostVoInfo.getArray("wantPostImages");
                    for (Object object : wantPostImages) {
                        EasyJSONObject imageObj = (EasyJSONObject) object;

                        String imageUrl = imageObj.getString("imageUrl");
                        ImageView imageView = new ImageView(_mActivity);

                        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        layoutParams.topMargin = Util.dip2px(_mActivity, 3);
                        layoutParams.bottomMargin = Util.dip2px(_mActivity, 3);
                        layoutParams.leftMargin = Util.dip2px(_mActivity, 3);
                        layoutParams.rightMargin = Util.dip2px(_mActivity, 3);

                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(imageUrl)).centerCrop().into(imageView);

                        sglImageContainer.addView(imageView, layoutParams);

                    }

                    int postView = wantPostVoInfo.getInt("postView"); // 瀏覽次數
                    int postLike = wantPostVoInfo.getInt("postLike");  // 點贊次數
                    int postFavor = wantPostVoInfo.getInt("postFavor");  // 喜歡次數

                    String viewCount = String.format(getString(R.string.text_view_count), postView);
                    tvViewCount.setText(viewCount);

                    int isLike = wantPostVoInfo.getInt("isLike");
                    if (isLike == 1) {
                        imgThumb.setImageResource(R.drawable.icon_post_thumb_blue);
                    } else {
                        imgThumb.setImageResource(R.drawable.icon_post_thumb_black);
                    }


                    int isFavor = wantPostVoInfo.getInt("isFavor");
                    if (isFavor == 1) {
                        imgLike.setImageResource(R.drawable.icon_post_like_blue);
                    } else {
                        imgLike.setImageResource(R.drawable.icon_post_like_black);
                    }

                    tvThumbCount.setText(String.valueOf(postLike));
                    tvLikeCount.setText(String.valueOf(postFavor));
                } catch (Exception e) {

                }
            }
        });
    }
}

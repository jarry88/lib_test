package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.MemberCommentAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.CommentItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ApiUtil;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 個人專頁-互動-我的評論Fragment
 * @author zwm
 */
public class MyCommentFragment extends BaseFragment implements View.OnClickListener {

    String memberName;
    MemberCommentAdapter memberCommentAdapter;
    List<CommentItem> memberCommentList = new ArrayList<>();
    RecyclerView rv_list;
    public static MyCommentFragment newInstance() {
        Bundle args = new Bundle();

        MyCommentFragment fragment = new MyCommentFragment();
        fragment.setArguments(args);

        return fragment;
    }
    public static MyCommentFragment newInstance(String memberName) {
        Bundle args = new Bundle();
        args.putString("memberName",memberName);
        MyCommentFragment fragment = new MyCommentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_comment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        replaceWord(view);
        rv_list = view.findViewById(R.id.rv_comment_list);
        //當前adapter只顯示想要帖評論
        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(layoutManager);
        memberCommentAdapter = new MemberCommentAdapter(R.layout.member_comment_item,memberCommentList);
        memberCommentAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener(){
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommentItem commentItem = memberCommentList.get(position);
                int id = view.getId();
                if(id==R.id.rv_post_list){
                    SLog.info("HERE?");
                    start(PostDetailFragment.newInstance(commentItem.relatePostId));
                }else if (id == R.id.btn_delete){
                    String title = "你確定要刪除該評論嗎？";
                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                            .setPopupCallback(new XPopupCallback() {
                                @Override
                                public void onShow() {
                                }
                                @Override
                                public void onDismiss() {
                                }
                            }).asCustom(new TwConfirmPopup(_mActivity, title, null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                            deleteComment(position);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                            .show();
                }

            }
        });
        rv_list.setAdapter(memberCommentAdapter);
        Util.setOnClickListener(view, R.id.btn_back, this);
        loadData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void loadData() {
        try {
            //String memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME, null);
            if (StringUtil.isEmpty(memberName)) {
                return;
            }
            EasyJSONObject params = EasyJSONObject.generate("memberName", memberName);

            int year = 2019;

            params.set("year", year);

            final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

            SLog.info("params[%s]", params);

            Api.getUI(Api.PATH_MEMBER_PAGE_COMMENT, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    loadingPopup.dismiss();
                    try {
                        SLog.info("responseStr[%s]", responseStr);

                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }
                        EasyJSONArray commentList = (EasyJSONArray) responseObj.getSafeArray("datas.commentList");
                        for (Object object:commentList) {
                            EasyJSONObject comment = (EasyJSONObject) object;
                            SLog.info("object[%s]\n\n",object);
                            if (comment.getInt("commentType")==CommentItem.TYPE_POST &&
                                    !Util.isJsonNull(comment.getObject("wantPostVo")))
                                {
                                    CommentItem commentItem = new CommentItem();
                                    commentItem.commentId = comment.getInt("commentId");
                                    commentItem.relatePostId = comment.getInt("relatePostId");
                                    commentItem.content = comment.getSafeString("content");
                                    commentItem.date = comment.getLong("createTime");
                                    commentItem.commentTime = new Jarbon(commentItem.date).format("Y年m月d日");
                                    EasyJSONArray images = comment.getSafeArray("images");
                                    if (images.length()>0) {
                                        commentItem.imageUrl = images.getString(0);
                                    }
                                    commentItem.configurePostUrl = comment.getSafeString("wantPostVo.coverImage");
                                    commentItem.postTitle = comment.getSafeString("wantPostVo.title");
                                    commentItem.postContent = comment.getSafeString("wantPostVo.content");

                                    if(!Util.isJsonNull(comment.getObject("wantPostVo.memberVo"))){
                                        commentItem.postAuthorAvatar = comment.getSafeString("wantPostVo.memberVo.avatar");
                                        commentItem.postAuthorName = comment.getSafeString("wantPostVo.memberVo.nickName");
                                    }
                                    memberCommentList.add(commentItem);
                                }

                        }

                        memberCommentAdapter.setNewData(memberCommentList);

                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {

        }
    }
    private void replaceWord(View v){
        memberName = User.getUserInfo(SPField.FIELD_MEMBER_NAME,null);
        if(getArguments().containsKey("memberName")){
            if(!memberName.equals(getArguments().getString("memberName"))){
                memberName = getArguments().getString("memberName");
                ((TextView) v.findViewById(R.id.tv_fragment_title)).setText(getString(R.string.text_him_comment));
            }
        }
    }

    private void deleteComment(int position) {
        ApiUtil.deleteComment(getContext(), memberCommentList.get(position).commentId, data -> {
            memberCommentList.remove(position);
            memberCommentAdapter.notifyDataSetChanged();
        });
    }
}



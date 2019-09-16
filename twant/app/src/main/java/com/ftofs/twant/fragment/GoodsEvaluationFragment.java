package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ButtonClickInfo;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class GoodsEvaluationFragment extends BaseFragment implements View.OnClickListener {
    SquareGridLayout sglImageContainer;
    ImageView btnAddImage;
    RelativeLayout rlButtonContainer;
    EditText etContent;

    public static GoodsEvaluationFragment newInstance() {
        Bundle args = new Bundle();

        GoodsEvaluationFragment fragment = new GoodsEvaluationFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_evaluation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        etContent = view.findViewById(R.id.et_content);
        sglImageContainer = view.findViewById(R.id.sgl_image_container);
        rlButtonContainer = view.findViewById(R.id.rl_button_container);
        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnAddImage.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_add_image:
                openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
                break;
            case R.id.btn_commit:
                commitComment();
                break;
            default:
                break;
        }
    }


    private void commitComment() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String content = etContent.getText().toString().trim();
        if (StringUtil.isEmpty(content)) {
            ToastUtil.error(_mActivity, getString(R.string.text_comment_content_can_not_be_empty));
            return;
        }

        // 收集圖片列表
        List<String> imagePathList = new ArrayList<>();
        int childCount = sglImageContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = sglImageContainer.getChildAt(i);
            if (view instanceof ImageView) {  // 如果是添加按鈕，則跳過
                continue;
            }
            imagePathList.add((String) view.getTag());
        }

        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                String responseStr = (String) message;
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ToastUtil.success(_mActivity, "提交成功");
                pop();
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                EasyJSONArray imageList = EasyJSONArray.generate();

                // 上傳圖片文件
                for (String imagePath : imagePathList) {
                    String name = Api.syncUploadFile(new File(imagePath));
                    SLog.info("name[%s]", name);
                    if (StringUtil.isEmpty(name)) {
                        continue;
                    }

                    imageList.append(EasyJSONObject.generate("imageUrl", name));
                }

                EasyJSONObject params = EasyJSONObject.generate(
                        "suggestContent", content,
                        "imageList", imageList);

                SLog.info("params[%s]", params.toString());

                String path = Api.PATH_COMMIT_FEEDBACK + Api.makeQueryString(EasyJSONObject.generate("token", token));
                String responseStr = Api.syncPostJson(path, params.toString());
                SLog.info("responseStr[%s]", responseStr);
                return responseStr;
            }
        });
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

        SLog.info("resultCode[%d]", resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri = data.getData();
        String imageAbsolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
        SLog.info("imageAbsolutePath[%s]", imageAbsolutePath);

        final View imageWidget = LayoutInflater.from(_mActivity).inflate(R.layout.refund_image_widget, sglImageContainer, false);
        ImageView imageView = imageWidget.findViewById(R.id.refund_image);
        imageWidget.findViewById(R.id.btn_remove_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sglImageContainer.removeView(imageWidget);
                btnAddImage.setVisibility(View.VISIBLE);
            }
        });

        imageWidget.setTag(imageAbsolutePath);

        Glide.with(_mActivity).load(imageAbsolutePath).centerCrop().into(imageView);
        int childCount = sglImageContainer.getChildCount();
        if (childCount > 0) {
            if (childCount == 3) { // 最多3張圖片，如果原本已經有2張 加1個添加按鈕，則隱藏添加按鈕
                btnAddImage.setVisibility(View.GONE);
            }
            sglImageContainer.addView(imageWidget, childCount - 1);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_KEYBOARD_STATE_CHANGED) {
            int keyboardState = (int) message.data;
            SLog.info("keyboardState[%d]", keyboardState);
            if (keyboardState == Constant.KEYBOARD_SHOWN) {
                rlButtonContainer.setVisibility(View.GONE);
            } else {
                rlButtonContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlButtonContainer.setVisibility(View.VISIBLE);
                    }
                }, 150);
            }
        }
    }
}

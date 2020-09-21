package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ButtonClickInfo;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 建議表達Fragment
 * @author zwm
 */
public class CommitFeedbackFragment extends BaseFragment implements View.OnClickListener {
    SquareGridLayout sglImageContainer;
    ImageView btnAddImage;
    EditText etContent;
    TextView tvWordCount;

    Map<Integer, ButtonClickInfo> buttonClickInfoMap = new HashMap<>();

    public static CommitFeedbackFragment newInstance() {
        Bundle args = new Bundle();

        CommitFeedbackFragment fragment = new CommitFeedbackFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commit_feedback, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sglImageContainer = view.findViewById(R.id.sgl_image_container);
        etContent = view.findViewById(R.id.et_content);
        tvWordCount = view.findViewById(R.id.tv_word_count);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int wordCount = s.length();
                tvWordCount.setText(wordCount + "/200");
            }
        });
        btnAddImage = view.findViewById(R.id.btn_add_image);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_view, this);
        Util.setOnClickListener(view, R.id.btn_add_image, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);
        buttonClickInfoMap.put(R.id.btn_commit, new ButtonClickInfo());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_view) {
            start(MyFeedbackFragment.newInstance());
        } else if (id == R.id.btn_add_image) {
            openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (id == R.id.btn_commit) {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                ToastUtil.error(_mActivity,"檢查登錄狀態");
                return;
            }

            String content = etContent.getText().toString().trim();
            if (StringUtil.isEmpty(content)) {
                ToastUtil.error(_mActivity, "請填寫反饋內容");
                return;
            }

            ButtonClickInfo buttonClickInfo = buttonClickInfoMap.get(id);
            if (!buttonClickInfo.getCanClick()) {
                SLog.info("不能點擊太快");
                return;
            }

            buttonClickInfo.canClick = false;
            buttonClickInfo.lastClickTime = System.currentTimeMillis();

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
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ButtonClickInfo buttonClickInfo = buttonClickInfoMap.get(R.id.btn_commit);
                    buttonClickInfo.canClick = true; // 操作成功后，恢復可點擊

                    ToastUtil.success(_mActivity, "提交成功");
                    hideSoftInputPop();
                }
            };

            TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
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

        final View imageWidget = LayoutInflater.from(_mActivity).inflate(R.layout.feedback_image_widget, sglImageContainer, false);
        ImageView imageView = imageWidget.findViewById(R.id.feedback_image);
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


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}



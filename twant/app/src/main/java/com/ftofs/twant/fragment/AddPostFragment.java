package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ArticleCategory;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ArticleCategoryPopup;
import com.ftofs.twant.widget.BudgetPricePopup;
import com.ftofs.twant.widget.DateSelectPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 發表帖文Fragment
 * @author zwm
 */
public class AddPostFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    BaseQuickAdapter adapter;

    String coverImage;
    List<String> postImageList = new ArrayList<>();

    List<ArticleCategory> articleCategoryList = new ArrayList<>();
    int selectedCategoryId = -1;

    String title = "";
    String keyword = "";

    TextView tvTitle;
    TextView tvWordCount;
    EditText etContent;
    TextView tvKeyword;
    String deadline;
    TextView tvDeadline;
    String budgetPrice = "";
    TextView tvBudgetPrice;

    TextView tvArticleCategory;

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
        tvWordCount = view.findViewById(R.id.tv_word_count);
        etContent = view.findViewById(R.id.et_content);
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
                tvWordCount.setText(String.format("%d/500", wordCount));
            }
        });

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

        tvArticleCategory = view.findViewById(R.id.tv_article_category);


        Util.setOnClickListener(view, R.id.btn_commit, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_input_title, this);
        Util.setOnClickListener(view, R.id.btn_input_keyword, this);
        Util.setOnClickListener(view, R.id.btn_article_category, this);
        Util.setOnClickListener(view, R.id.btn_deadline, this);
        Util.setOnClickListener(view, R.id.btn_budget_price, this);

        Util.setOnClickListener(view, R.id.btn_remove_cover_image, this);
        Util.setOnClickListener(view, R.id.btn_add_post_content_image, this);

        loadArticleCategory();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_commit) {
            commitPost();
        } else if (id == R.id.btn_back) {
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
        } else if (id == R.id.btn_article_category) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ArticleCategoryPopup(_mActivity, selectedCategoryId, articleCategoryList, this))
                    .show();
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
            coverImage = absolutePath;

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
        } else if (type == Constant.POPUP_TYPE_ARTICLE_CATEGORY) {
            selectedCategoryId = id;
            tvArticleCategory.setText(getCategoryName(selectedCategoryId));
        }
    }

    private void loadArticleCategory() {
        Api.getUI(Api.PATH_POST_CATEGORY_LIST, null, new UICallback() {
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
                    EasyJSONArray categoryList = responseObj.getArray("datas.categoryList");
                    for (Object object : categoryList) {
                        EasyJSONObject category = (EasyJSONObject) object;
                        ArticleCategory articleCategory = (ArticleCategory) EasyJSONBase.jsonDecode(ArticleCategory.class, category.toString());
                        articleCategoryList.add(articleCategory);
                    }
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 提交帖文
     */
    private void commitPost() {
        final String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        if (StringUtil.isEmpty(title)) {
            ToastUtil.error(_mActivity, "請輸入標題");
            return;
        }

        final String content = etContent.getText().toString().trim();
        if (StringUtil.isEmpty(content)) {
            ToastUtil.error(_mActivity, "請輸入正文");
            return;
        }

        if (StringUtil.isEmpty(keyword)) {
            ToastUtil.error(_mActivity, "請輸入關鍵字");
            return;
        }

        String categoryName = getCategoryName(selectedCategoryId);
        if (StringUtil.isEmpty(categoryName)) {
            ToastUtil.error(_mActivity, "請選擇文章分類");
            return;
        }

        if (StringUtil.isEmpty(budgetPrice)) {
            ToastUtil.error(_mActivity, "請輸入預算價格");
            return;
        }

        if (StringUtil.isEmpty(coverImage)) {
            ToastUtil.error(_mActivity, "請選擇封面圖");
            return;
        }


        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                boolean success = (boolean) message;

                if (success) {
                    ToastUtil.success(_mActivity, "提交成功");
                    pop();
                } else {
                    ToastUtil.error(_mActivity, "提交失敗");
                }
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                try {
                    SLog.info("上傳圖片開始");
                    // 上傳封面圖
                    String url = Api.syncUploadFile(new File(coverImage));
                    if (StringUtil.isEmpty(url)) {
                        return null;
                    }
                    String coverImageUrl = url;

                    EasyJSONArray wantPostImages = EasyJSONArray.generate();
                    for (String absolutePath : postImageList) {
                        url = Api.syncUploadFile(new File(absolutePath));
                        if (StringUtil.isEmpty(url)) {
                            return null;
                        }
                        wantPostImages.append(url);
                    }
                    SLog.info("上傳圖片完成");


                    // token要附在url中
                    String path = Api.PATH_COMMIT_POST + Api.makeQueryString(EasyJSONObject.generate("token", token));
                    SLog.info("url[%s]", url);

                    EasyJSONObject params = EasyJSONObject.generate(
                            "title", title,
                            "content", content,
                            "postCategory", getCategoryName(selectedCategoryId),
                            "coverImage", coverImageUrl,
                            "postType", Constant.POST_TYPE_COMMON,
                            "keyWord", keyword,
                            "isPublish", 1,
                            "expiresDate", deadline,
                            "budgetPrice", budgetPrice,
                            "wantPostImages", wantPostImages);

                    // 提交數據
                    String json = params.toString();
                    SLog.info("json[%s]", json);

                    String responseStr = Api.syncPostJson(path, json);
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.isError(responseObj)) {
                        return false;
                    }

                    return true;
                } catch (Exception e) {
                    SLog.info("Error!%s", e);
                }
                return false;
            }
        });
    }

    private String getCategoryName(int categoryId) {
        String categoryName = "";
        for (ArticleCategory articleCategory : articleCategoryList) {
            if (categoryId == articleCategory.categoryId) {
                categoryName = articleCategory.categoryName;
                break;
            }
        }

        return categoryName;
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        hideSoftInput();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}

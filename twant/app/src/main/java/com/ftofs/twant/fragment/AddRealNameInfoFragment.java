package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.RealNameListItem;
import com.ftofs.twant.util.CameraUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.HwLoadingPopup;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.callback.CommonCallback;
import com.gzp.lib_common.utils.FileUtil;
import com.gzp.lib_common.utils.PermissionUtil;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.yanzhenjie.permission.runtime.Permission;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * 添加實名認證信息Fragment
 * @author zwm
 */
public class AddRealNameInfoFragment extends BaseFragment implements View.OnClickListener {
    int from; // 來自哪裏
    public static final int FROM_CROSS_BORDER = 1;  // 來自跨境購
    public static final int FROM_DISTRIBUTION = 2;  // 來自分銷系統


    /**
     * 拍照的圖片文件
     */
    File captureImageFile;

    HwLoadingPopup loadingPopup;

    /**
     * 動作： 添加還是編輯
     */
    int action;
    RealNameListItem realNameItem;

    View btnClearName;
    View btnClearId;

    EditText etName; // 姓名
    EditText etId; // 身份證號

    TextView tvFragmentTitle;

    boolean isFrontIdImage = true;   // 是否為身份證還是背面照

    // 遠程的文件Url
    String frontImageUrl;
    String backImageUrl;

    // 本地的文件路徑
    String frontImagePath;
    String backImagePath;

    ImageView imgFrontImage;
    ImageView imgBackImage;

    View iconImportFront;
    View iconImportBack;

    /**
     * 構造Ctor
     * @param action
     * @param realNameItem 編輯時才用到，添加時傳null即可
     * @return
     */
    public static AddRealNameInfoFragment newInstance(int from, int action, RealNameListItem realNameItem) {
        Bundle args = new Bundle();

        AddRealNameInfoFragment fragment = new AddRealNameInfoFragment();
        fragment.setArguments(args);
        fragment.setData(action, realNameItem);
        fragment.from = from;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_real_name_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        btnClearName = view.findViewById(R.id.btn_clear_name);
        btnClearName.setOnClickListener(this);
        btnClearId = view.findViewById(R.id.btn_clear_id);
        btnClearId.setOnClickListener(this);

        Util.setOnClickListener(view, R.id.btn_import_front_id_image, this);
        Util.setOnClickListener(view, R.id.btn_import_back_id_image, this);

        etName = view.findViewById(R.id.et_name);
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.findViewById(R.id.btn_clear_name).setVisibility(v.getText().length() > 0?View.VISIBLE:View.GONE);
                }
                return false;
            }
        });


        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (action == Constant.ACTION_EDIT) { // 如果是編輯，不需要顯示清除按鈕
                    return;
                }

                if (s.length() > 0) {
                    btnClearName.setVisibility(View.VISIBLE);
                } else {
                    btnClearName.setVisibility(View.GONE);
                }
            }
        });

        etId = view.findViewById(R.id.et_id);
        etId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    view.findViewById(R.id.btn_clear_id).setVisibility(v.getText().length() > 0?View.VISIBLE:View.GONE);
                    commitData();
                }

                return false;
            }
        });
        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (action == Constant.ACTION_EDIT) { // 如果是編輯，不需要顯示清除按鈕
                    return;
                }

                if (s.length() > 0) {
                    btnClearId.setVisibility(View.VISIBLE);
                } else {
                    btnClearId.setVisibility(View.GONE);
                }
            }
        });

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);

        imgFrontImage = view.findViewById(R.id.img_front_image);
        imgBackImage = view.findViewById(R.id.img_back_image);

        iconImportFront = view.findViewById(R.id.icon_import_front);
        iconImportBack = view.findViewById(R.id.icon_import_back);

        if (action == Constant.ACTION_ADD) {
            tvFragmentTitle.setText("信息添加");
        } else {
            tvFragmentTitle.setText("信息編輯");

            btnClearId.setVisibility(View.GONE);
            btnClearName.setVisibility(View.GONE);

            etName.setText(realNameItem.name);
            etId.setText(realNameItem.idNum);

            // 設置姓名和身份證為只讀狀態
            etName.setCursorVisible(false);      //设置输入框中的光标不可见
            etName.setFocusable(false);           //无焦点
            etName.setFocusableInTouchMode(false);     //触摸时也得不到焦点

            etId.setCursorVisible(false);      //设置输入框中的光标不可见
            etId.setFocusable(false);           //无焦点
            etId.setFocusableInTouchMode(false);     //触摸时也得不到焦点

            frontImageUrl = realNameItem.idCardFrontImage;
            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(frontImageUrl)).into(imgFrontImage);
            iconImportFront.setVisibility(View.GONE);

            backImageUrl = realNameItem.idCardBackImage;
            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(backImageUrl)).into(imgBackImage);
            iconImportBack.setVisibility(View.GONE);
        }
    }

    private void commitData() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String name = etName.getText().toString().trim();
            String idNum = etId.getText().toString().trim();

            if (StringUtil.isEmpty(name)) {
                ToastUtil.error(_mActivity, etName.getHint().toString());
                return;
            }

            if (StringUtil.isEmpty(idNum)) {
                ToastUtil.error(_mActivity, etId.getHint().toString());
                return;
            }

            if (action == Constant.ACTION_EDIT) { // 添加時，可以不上傳身份證，但編輯時，需要上傳
                if (StringUtil.isEmpty(frontImageUrl)) {
                    ToastUtil.error(_mActivity, "請上傳身份證人像面");
                    return;
                }

                if (StringUtil.isEmpty(backImageUrl)) {
                    ToastUtil.error(_mActivity, "請上傳身份證國徽面");
                    return;
                }
            }

            EasyJSONObject params = EasyJSONObject.generate();

            if (!StringUtil.isEmpty(frontImageUrl)) {
                if (from == FROM_CROSS_BORDER) {
                    params.set("idCardFrontImage", frontImageUrl);
                } else {
                    params.set("idCartFrontImage", frontImageUrl);
                }
            }
            if (!StringUtil.isEmpty(backImageUrl)) {
                if (from == FROM_CROSS_BORDER) {
                    params.set("idCardBackImage", backImageUrl);
                } else {
                    params.set("idCartBackImage", backImageUrl);
                }
            }

            String url;
            if (action == Constant.ACTION_ADD) {
                /*
                {
                    "authConsigneeName": "測試1 #真實姓名【必填】",
                    "idCardNumber": "360734199509121310 #身份證號【必填】",
                    "idCardFrontImage": "身份證正面照片(國徽)",
                    "idCardBackImage": "身份證背面照片(人像)"
                }
                 */
                if (from == FROM_CROSS_BORDER) {
                    url = Api.PATH_MEMBER_AUTH_ADD;
                    params.set("idCardNumber", idNum);
                    params.set("authConsigneeName", name);
                    SLog.info("url[%s], params[%s]", url, params);
                    Api.postJsonUi(url, params.toString(), new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtil.showNetworkError(_mActivity, e);
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            try {
                                SLog.info("responseStr[%s]", responseStr);

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                                if (ToastUtil.checkError(_mActivity, responseObj)) {
                                    return;
                                }
                                if (responseObj.exists("datas.isAuth")) {
                                    int isAuth = responseObj.getInt("datas.isAuth");
                                    if (isAuth == 1) {
                                        ToastUtil.success(_mActivity, "datas.message");
                                        return;
                                    }
                                }
                                ToastUtil.success(_mActivity, "保存成功");

                                Bundle bundle = new Bundle();
                                bundle.putBoolean("reloadData", true);
                                setFragmentResult(RESULT_OK, bundle);

                                hideSoftInputPop();
                            } catch (Exception e) {
                                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                            }
                        }
                    });
                } else {
                    url = Api.PATH_JOIN_REAL_NAME;
                    params.set("idCartNumber", idNum);
                    params.set("authRealName", name);
                    SLog.info("url[%s], params[%s]", url, params);
                    Api.postUI(url, params, new UICallback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            ToastUtil.showNetworkError(_mActivity, e);
                        }

                        @Override
                        public void onResponse(Call call, String responseStr) throws IOException {
                            try {
                                SLog.info("responseStr[%s]", responseStr);

                                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                                if (ToastUtil.checkError(_mActivity, responseObj)) {
                                    return;
                                }
                                if (responseObj.exists("datas.isAuth")) {
                                    int isAuth = responseObj.getInt("datas.isAuth");
                                    if (isAuth == 1) {
                                        ToastUtil.success(_mActivity, "datas.message");
                                        return;
                                    }
                                }
                                ToastUtil.success(_mActivity, "保存成功");

                                Bundle bundle = new Bundle();
                                bundle.putBoolean("reloadData", true);
                                setFragmentResult(RESULT_OK, bundle);

                                hideSoftInputPop();
                            } catch (Exception e) {
                                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                            }
                        }
                    });
                }
            } else { // 編輯操作
                /*
                {
                    "authId": 129 #認證ID【必填】,
                    "idCardFrontImage": "idCardFrontImage #身份證正面照(國徽)【必填】",
                    "idCardBackImage": "idCardBackImage #身份證反面照(人像)【必填】"
                }
                 */
                url = Api.PATH_MEMBER_AUTH_EDIT;
                params.set("authId", realNameItem.authId);

                SLog.info("url[%s], params[%s]", url, params);
                Api.putJsonUi(url, params.toString(), new UICallback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ToastUtil.showNetworkError(_mActivity, e);
                    }

                    @Override
                    public void onResponse(Call call, String responseStr) throws IOException {
                        try {
                            SLog.info("responseStr[%s]", responseStr);

                            EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                            if (ToastUtil.checkError(_mActivity, responseObj)) {
                                return;
                            }
                            if (responseObj.exists("datas.isAuth")) {
                                int isAuth = responseObj.getInt("datas.isAuth");
                                if (isAuth == 1) {
                                    ToastUtil.success(_mActivity, "datas.message");
                                    return;
                                }
                            }
                            ToastUtil.success(_mActivity, "保存成功");

                            Bundle bundle = new Bundle();
                            bundle.putBoolean("reloadData", true);
                            setFragmentResult(RESULT_OK, bundle);

                            hideSoftInputPop();
                        } catch (Exception e) {
                            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                        }
                    }
                });
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_ok) {
            commitData();
        } else if (id == R.id.btn_clear_name) {
            etName.setText("");
        } else if (id == R.id.btn_clear_id) {
            etId.setText("");
        } else if (id == R.id.btn_import_front_id_image) {
            isFrontIdImage = true;
            showImageSrc();
        } else if (id == R.id.btn_import_back_id_image) {
            isFrontIdImage = false;
            showImageSrc();
        }
    }

    private void showImageSrc() {
        new XPopup.Builder(getContext())
//                        .maxWidth(600)
                .asCenterList("请选择", new String[]{"從手機相冊選擇", "從相機拍攝"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                selectIdImage(position);
                            }
                        })
                .show();
    }

    private void selectIdImage(int position) {
        if (position == 0) {
            openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } if (position == 1) {
            takePhoto();
        }
    }

    public void setData(int action, RealNameListItem realNameItem) {
        this.action = action;
        this.realNameItem = realNameItem;
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("onActivityResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        // 上傳圖片到OSS
        if (requestCode == RequestCode.CAMERA_IMAGE.ordinal() || requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
            String absolutePath;  // 拍照得到的文件路徑

            if (requestCode == RequestCode.OPEN_ALBUM.ordinal()) {
                Uri uri = data.getData();
                absolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
                captureImageFile = new File(absolutePath);
            } else {
                absolutePath = captureImageFile.getAbsolutePath();  // 拍照得到的文件路徑
            }

            if (isFrontIdImage) {
                frontImagePath = absolutePath;
            } else {
                backImagePath = absolutePath;
            }
            SLog.info("absolutePath[%s]", absolutePath);

            showLoadingPopup("正在上載，請稍候...");

            Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    SLog.info("observable.threadId[%s]", Thread.currentThread().getId());

                    String path = Api.syncUploadFile(captureImageFile);
                    SLog.info("path[%s]", path);
                    if (isFrontIdImage) {
                        frontImageUrl = path;
                    } else {
                        backImageUrl = path;
                    }

                    if (StringUtil.isEmpty(path)) {
                        emitter.onError(new Exception("上傳圖片失敗"));
                    } else {
                        emitter.onComplete();
                    }
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            Observer<String> observer = new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {
                    SLog.info("onSubscribe, threadId[%s]", Thread.currentThread().getId());
                }
                @Override
                public void onNext(String s) {
                    SLog.info("onNext[%s], threadId[%s]", s, Thread.currentThread().getId());
                }
                @Override
                public void onError(Throwable e) {
                    SLog.info("onError[%s], threadId[%s]", e.getMessage(), Thread.currentThread().getId());
                    dismissLoadingPopup();
                }
                @Override
                public void onComplete() {
                    SLog.info("onComplete, threadId[%s]", Thread.currentThread().getId());
                    dismissLoadingPopup();

                    if (isFrontIdImage) {
                        Glide.with(_mActivity).load(frontImagePath).into(imgFrontImage);
                        iconImportFront.setVisibility(View.GONE);
                    } else {
                        Glide.with(_mActivity).load(backImagePath).into(imgBackImage);
                        iconImportBack.setVisibility(View.GONE);
                    }
                }
            };
            observable.subscribe(observer);
        }
    }


    private void takePhoto() {
        PermissionUtil.actionWithPermission(_mActivity, new String[] {
                Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE
        }, "拍攝照片/視頻需要授予", new CommonCallback() {
            @Override
            public String onSuccess(@Nullable String data) {
                captureImageFile = CameraUtil.openCamera(_mActivity, AddRealNameInfoFragment.this, Constant.CAMERA_ACTION_IMAGE);
                return null;
            }

            @Override
            public String onFailure(@Nullable String data) {
                ToastUtil.error(_mActivity, "您拒絕了授權");
                return null;
            }
        });
    }


    private void showLoadingPopup(String info) {
        if (loadingPopup == null) {
            loadingPopup = (HwLoadingPopup) new XPopup.Builder(_mActivity)
                    .dismissOnBackPressed(true) // 按返回键是否关闭弹窗，默认为true
                    .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new HwLoadingPopup(_mActivity, info));
        }

        loadingPopup.show();
    }

    private void dismissLoadingPopup() {
        if (loadingPopup != null) {
            loadingPopup.dismiss();
        }
    }
}

package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.widget.PositionSelectPopup;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class ExpectPositionFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    private Map<String, Integer> positionIdMap;
    private int postTypeIndex;
    String expectPosition;
    String resumeKeyword;
    String postTypeName;
    String salaryTypeName;
    String salaryRange;

    EditText etExpectPosition;
    TextView tvResumeKeyword;
    TextView tvPostTypeName;
    TextView tvSalaryRange;
    List<ListPopupItem> hrPostTypeList = new ArrayList<>();
    List<ListPopupItem> hrSalaryTypeList = new ArrayList<>();
    List<ListPopupItem> hrSalaryRangeList = new ArrayList<>();
    List<ListPopupItem> keywordList = new ArrayList<>();

    public static ExpectPositionFragment newInstance(String expectPosition,String resumeKeyword,String postTypeName,String salaryTypeName,String salaryRange,List<ListPopupItem> hrPostTypeList,List<ListPopupItem> hrSalaryTypeList,List<ListPopupItem> hrSalaryRangeList,List<ListPopupItem> keywordList){


        ExpectPositionFragment fragment =new ExpectPositionFragment();
        Bundle args = new Bundle();
        args.putString("expectPosition",expectPosition);
        args.putString("resumeKeyword",resumeKeyword);
        args.putString("postTypeName",postTypeName);
        args.putString("salaryTypeName",salaryTypeName);
        args.putString("salaryRange",salaryRange);
        fragment.setArguments(args);
        fragment.setList(hrPostTypeList,hrSalaryTypeList,hrSalaryRangeList,keywordList);
        return fragment;
    }

    private void setList(List<ListPopupItem> hrPostTypeList, List<ListPopupItem> hrSalaryTypeList, List<ListPopupItem> hrSalaryRangeList, List<ListPopupItem> keywordList) {
        this.hrPostTypeList = hrPostTypeList;
        this.hrSalaryTypeList = hrSalaryTypeList;
        this.hrSalaryRangeList = hrSalaryRangeList;
        this.keywordList = keywordList;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expect_position, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etExpectPosition = view.findViewById(R.id.et_receiver_expect_position);
        tvResumeKeyword = view.findViewById(R.id.tv_position_keyword_select);
        tvPostTypeName =view.findViewById(R.id.tv_position_category_select);
        tvSalaryRange = view.findViewById(R.id.tv_expect_salary_select);
        view.findViewById(R.id.rv_expect_position_title).setVisibility(View.GONE);
        Util.setOnClickListener(view, R.id.btn_back,this );
        Util.setOnClickListener(view, R.id.btn_ok,this );
        Util.setOnClickListener(view,R.id.et_receiver_expect_position,this);
        Util.setOnClickListener(view,R.id.rv_position_keyword,this);
        Util.setOnClickListener(view,R.id.rv_position_category,this);
        Util.setOnClickListener(view,R.id.rv_expect_salary,this);
        loadExpectPosition();
    }

    private void loadExpectPosition() {
        expectPosition = getArguments().getString("expectPosition");
        resumeKeyword = getArguments().getString("resumeKeyword");
        postTypeName = getArguments().getString("postTypeName");
        if (!StringUtil.isEmpty(postTypeName)) {
            for (int i = 0; i < hrPostTypeList.size(); i++) {
                if (postTypeName.equals(hrPostTypeList.get(i).title)) {
                    postTypeIndex=i;
                }
            }
        }
        salaryTypeName = getArguments().getString("salaryTypeName");
        salaryRange = getArguments().getString("salaryRange");
        etExpectPosition.setText(expectPosition);
        if (!StringUtil.isEmpty(resumeKeyword)) {
            tvResumeKeyword.setText(resumeKeyword);
        }
        tvPostTypeName.setText(postTypeName);
        tvSalaryRange.setText(String.format("%s:%s",salaryTypeName,salaryRange));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.rv_position_keyword:
                showKeywordSelectPopup();
                break;
            case R.id.rv_position_category:
                new XPopup.Builder(_mActivity)
                        .moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity,getString(R.string.text_position_category),
                                PopupType.POSITION_CATEGORY,hrPostTypeList, postTypeIndex,this))
                        .show();
                break;
            case R.id.rv_expect_salary:
                Util.showPickerView(_mActivity, getString(R.string.text_expect_salary), hrSalaryTypeList, hrSalaryRangeList, null, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int id1, int id2, int id3, View v) {
                        salaryTypeName = hrSalaryTypeList.get(id1).title;
                        salaryRange=hrSalaryRangeList.get(id2).title;
                        tvSalaryRange.setText(salaryTypeName.concat(":").concat(salaryRange));
                    }
                });
                break;
            case R.id.btn_ok:
                saveInfo();
                break;
                default:
                    break;

        }
    }

    private void saveInfo() {
        if (checkEmpty()) {
            return;
        }
        int postTypeId=1;
        int salaryTypeId=1;
        int salaryRangeId=1;
        for (ListPopupItem item : hrPostTypeList) {
            if (item.title.equals(expectPosition)) {
                postTypeId = item.id;
            }
        }
        for (ListPopupItem item : hrSalaryTypeList) {
            if (item.title.equals(salaryTypeName)) {
                salaryTypeId = item.id;
            }
        }
        for (ListPopupItem item : hrSalaryRangeList) {
            if (item.title.equals(salaryRange)) {
                salaryRangeId = item.id;
            }
        }
        if(salaryTypeId==5){
            salaryRangeId=1;
        }
        expectPosition = etExpectPosition.getText().toString();
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),
                "resumeWish",expectPosition,
                "resumeKeyword",resumeKeyword,
                "postType",postTypeId,
                "salaryType",salaryTypeId,
                "salaryRange",salaryRangeId);
        SLog.info("params[%s]",params);

        Api.postUI(Api.PATH_POSITION_EDIT, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity,e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]",responseStr);
                    ToastUtil.success(_mActivity,"保存成功");
                    Bundle bundle = new Bundle();
                    bundle.putString("expectPosition",expectPosition);
                    bundle.putString("resumeKeyword",resumeKeyword);
                    bundle.putString("postTypeName",postTypeName);
                    bundle.putString("salaryTypeName",salaryTypeName);
                    bundle.putString("salaryRange",salaryRange);
                    setFragmentResult(RESULT_OK,bundle);
                    hideSoftInputPop();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkEmpty() {
        if (StringUtil.isEmpty(etExpectPosition.getText().toString())) {
            ToastUtil.success(_mActivity,"不能為空");
            return true;
        }
        return false;
    }

    private void showKeywordSelectPopup() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new PositionSelectPopup(_mActivity, resumeKeyword,keywordList,this))
                .show();
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.POSITION_CATEGORY) {
            postTypeIndex=id;
            expectPosition=hrPostTypeList.get(postTypeIndex).title;
            tvPostTypeName.setText(expectPosition);
        }else if(type==PopupType.SELECT_POST_KEYWORD){
            resumeKeyword=extra.toString();
            tvResumeKeyword.setText(resumeKeyword);
        }
        else if (type == PopupType.SELECT_SALARY) {

        }
    }

}

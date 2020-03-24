package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

public class MemberDocumentFragment extends BaseFragment implements View.OnClickListener {
    public static MemberDocumentFragment newInstance(){
        MemberDocumentFragment item = new MemberDocumentFragment();
        Bundle args = new Bundle();

        item.setArguments(args);
        return item;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_document, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Util.setOnClickListener(view,R.id.btn_back,this);
        Util.setOnClickListener(view,R.id.btn_personal_info,this);
        Util.setOnClickListener(view, R.id.btn_size_assistant, this);
        Util.setOnClickListener(view, R.id.btn_education_experience, this);
        Util.setOnClickListener(view, R.id.btn_member_resume, this);
        loadTopInfo(view);

    }

    private void loadTopInfo(View v) {
        ImageView avatar=v.findViewById(R.id.img_avatar);
        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(User.getUserInfo(SPField.FIELD_AVATAR,null))).into(avatar);
        TextView memberName=v.findViewById(R.id.tv_member_name);
        TextView memberNumber=v.findViewById(R.id.tv_member_number);
//        memberName.setText(User.getUserInfo(SPField.FIELD_NICKNAME,null));
        memberNumber.setText(User.getUserInfo(SPField.FIELD_MOBILE,null));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_personal_info:
                start(PersonalInfoFragment.newInstance());
                break;
            case R.id.btn_size_assistant:
                start(SizeAssistantFragment.newInstance());
                break;
            case R.id.btn_education_experience:
                start(EducationExperienceFragment.newInstance());
                break;
            case R.id.btn_member_resume:
                start(MemberResumeFragment.newInstance());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

}

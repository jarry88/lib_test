package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ftofs.twant.R;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import cn.snailpad.easyjson.EasyJSONObject;

public class TestFragmentGif extends BaseFragment implements View.OnClickListener{
    ImageView imageView;
    public static TestFragmentGif newInstance(){
        Bundle args = new Bundle();
        TestFragmentGif testFragmentGif = new TestFragmentGif();
        testFragmentGif.setArguments(args);

        return testFragmentGif;
    }

    @Nullable
    //這裡為什麼知己ctrl+o 搜不到重載方法以及android.support.annotation.
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_layout, container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.image_view);
        Util.setOnClickListener(view,R.id.btn_test,this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_test){
            EasyJSONObject params = (EasyJSONObject) EasyJSONObject.generate("token","1dd31942cea34e408fb345d6ff9c6f77","payId",5157);
            //start(GifFragment.newInstance("https://gfile.oss-cn-hangzhou.aliyuncs.com/takewant/1.gif"));
        }

    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

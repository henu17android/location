package com.example.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.location.R;


public class SearchResultDialog extends DialogFragment {

    private View rootView;
    private String groupName;
    private int groupId;
    private static final String TAG = "SearchResultDialog";
    public boolean isCheck =false;
    private TextView groupText;


    public SearchResultDialog() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = getLayoutInflater().inflate(R.layout.fragment_search_result, container, false);

        Bundle bundle = getArguments();

        if (bundle!=null) {
            groupName = bundle.getString("group_name");
            groupId = bundle.getInt("group_id");
        }
        initView(rootView);
        Log.d(TAG, "onCreateView: ");
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CheckListener){
            checkListener = (CheckListener) context;//获取到宿主Activity，并给你赋值。
        }else{
            throw new IllegalArgumentException("activity must implements Fragment");
        }


    }

    private void initView(View rootView) {
        ImageView groupImage = rootView.findViewById(R.id.group_image);
        groupText = rootView.findViewById(R.id.group_chat_name);
        Button applyButton = rootView.findViewById(R.id.apply_join);
        groupText.setText(groupName);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkListener.isApply(true);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    //通过接口向activity 传递消息
    public interface CheckListener {
        void isApply(boolean isCheck);
    }

    public CheckListener checkListener;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        checkListener = null;  //避免内存泄漏
    }
}

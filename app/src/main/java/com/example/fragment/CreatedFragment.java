package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.location.R;
import com.example.location.SetSignInActivity;
import com.ornach.nobobutton.NoboButton;


/**
 * 进入创建的群聊界面
 */
public class CreatedFragment extends Fragment {

    private NoboButton startSignBtn;
    private int groupId;
    private String adminId;
    private startSignClickListener listener;

    public CreatedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof startSignClickListener){
            listener = (startSignClickListener) context;//获取到宿主Activity，并赋值。
        }else{
            throw new IllegalArgumentException("activity must implements Fragment");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle!=null) {
            String id = bundle.getString("group_id");
            Log.d("CreatedFragment", "onCreate: "+id);
            groupId = Integer.parseInt(id);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_created, container, false);
        startSignBtn = view.findViewById(R.id.start_sign);
        startSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.startSign(true);
            }
        });

        return view;
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);

    }

    public interface startSignClickListener {
        void startSign(Boolean isClick);
    }

}

package com.example.location;

import android.media.Image;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.zhouwei.library.CustomPopWindow;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ImageView imageAdd;
    private CustomPopWindow mCustomPopWindow;
    private View popMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popMenu = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_pop_menu,null);
        initView();

    }

    //初始化布局
    private void initView() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        imageAdd = (ImageView)findViewById(R.id.add_more);
        ImageView userImage = (ImageView)findViewById(R.id.user_image);
        userImage.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.add_more:
                mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(MainActivity.this)
                        .setView(popMenu)
                        .enableBackgroundDark(true)
                        .setBgDarkAlpha(0.8f)
                        .create()
                        .showAsDropDown(imageAdd);
                popWindowClick(popMenu);

                break;

            default:
        }
    }


    //弹出菜单点击事件
    private void popWindowClick(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow!=null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {

                }
            }
        };
    }
}

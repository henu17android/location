package com.example.location;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.adapter.GroupExpandListAdapter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.zhouwei.library.CustomPopWindow;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,SearchView.OnQueryTextListener {

    private DrawerLayout mDrawerLayout;
    private ImageView imageAdd;
    private CustomPopWindow mCustomPopWindow;
    private View popMenu;

    private ExpandableListView mGroupList;
    private GroupExpandListAdapter mAdapter;
    private SearchView mSearchView;
    private List<String> groups = asList("我创建的群", "我加入的群");  //asList()方法添加的是不可变的 List, 即不能添加、删除等操作
    private List<List<String>> childs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        popMenu = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_pop_menu,null);
        initData();
        initView();
        mGroupList = findViewById(R.id.expandable_listview);
        mAdapter = new GroupExpandListAdapter(this, groups, childs);
        mGroupList.setAdapter(mAdapter);
        mSearchView = findViewById(R.id.searchView);

        Button btnSignIn = findViewById(R.id.sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SetSignInActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * TODO 在输入时直接对本地数据库（创建的群和加入的群）进行检索
     * @param newText id或name
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText))
        { }
        else
        { }
        return true;
    }

    //同onQueryTextChange的else
    @Override
    public boolean onQueryTextSubmit(String query) {
        //实际应用中应该在该方法内执行实际查询
        return true;
    }

    //TODO 读取数据库数据（预计有两个，一个发起者，一个签到者）
    //关于图片，存入数据库时把图片转为字符，取出时再转换回来。
    private void initData(){

        //先创建一点，看一下
        childs = new ArrayList<>();
        List<String> childs1 = new ArrayList<>();
        childs1.add("child1-1");
        childs1.add("child1-2");
        childs1.add("child1-3");

        List<String> childs2 = new ArrayList<>();
        childs2.add("child2-1");
        childs2.add("child2-2");
        childs2.add("child2-3");
        childs.add(childs1);
        childs.add(childs2);
        popMenu = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_pop_menu,null);

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
                    case R.id.create_group :
                        Intent intent = new Intent(MainActivity.this,CreateGroupActivity.class);
                        startActivity(intent);
                }
            }
        };
    }
}

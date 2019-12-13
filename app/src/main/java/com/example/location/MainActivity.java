package com.example.location;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.LocationApp;
import com.example.Service.SocketService;
import com.example.adapter.GroupExpandListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bean.Group;
import com.example.bean.GroupSignInMessage;
import com.example.client.Client;
import com.example.client.ClientMessage;
import com.example.client.MessageType;
import com.example.fragment.SearchResultDialog;
import com.example.util.ActivityUtil;
import com.example.util.ExcelUtil;
import com.example.util.NotificationUtil;
import com.example.util.PermissionUtil;
import com.example.zhouwei.library.CustomPopWindow;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;


/**
 *
 */
public class MainActivity extends BaseActivity implements View.OnClickListener,SearchResultDialog.CheckListener
{

    private DrawerLayout mDrawerLayout;
    private ImageView imageAdd;
    private CustomPopWindow mCustomPopWindow;
    private View popMenu;
    private GroupExpandListAdapter mAdapter;
    private List<String> groupNames = asList("我创建的群", "我加入的群");  //asList()方法添加的是不可变的 List, 即不能添加、删除等操作
    private List<List<Group>> groups = new ArrayList<>();
    private SearchFragment searchFragment;
    private ExpandableListView mGroupList;
    private Group searchGroup;
    private String userNumber;
    private List<Group> createdGroup;
    private List<Group> joinedGroup;
    private static final String TAG = "MainActivity";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static int REQUEST_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LoginActivity.loginActivity.finish();
        popMenu = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_pop_menu,null);
        initView();
        initData();

        //判断用户通知栏权限
        if (!NotificationUtil.isNotificationEnabled(MainActivity.this)) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("权限请求")
                    .setMessage("您的通知权限没有开启，不能获取及时的通知，点击确定前去开启")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationUtil.openPush(MainActivity.this);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
        }
    }


    @Override
    protected void onResume() {

        Group group = (Group)getIntent().getSerializableExtra("create_group");
        if (group!=null) {
            createdGroup.add(group);
        }
        super.onResume();
    }

    //TODO 读取数据库数据（预计有两个，一个发起者，一个签到者）
    //关于图片，存入数据库时把图片转为字符，取出时再转换回来。
    private void initData(){

        createdGroup = new ArrayList<>();

        for (int i = 0;i<5;i++) {
            Group group = new Group();
            group.setGroupName("Group-----"+i);
            createdGroup.add(group);
        }

        joinedGroup = new ArrayList<>();
        for (int i =0;i<8;i++){
            Group group = new Group();
            group.setGroupName("Group-----"+i);
            joinedGroup.add(group);
        }

        groups.add(createdGroup);
        groups.add(joinedGroup);


    }



    //初始化布局
    private void initView() {
        mGroupList = findViewById(R.id.expandable_listview);
        mAdapter = new GroupExpandListAdapter(this, groupNames, groups);
        mGroupList.setAdapter(mAdapter);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        imageAdd = (ImageView)findViewById(R.id.add_more);
        ImageView userImage = (ImageView)findViewById(R.id.user_image);
        userImage.setOnClickListener(this);
        imageAdd.setOnClickListener(this);
        ImageView search = findViewById(R.id.search);
        popMenu = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_pop_menu,null);
        searchFragment  = SearchFragment.newInstance();  //搜索框

        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {

                //向服务器发送消息 搜索
                ClientMessage clientMessage = new ClientMessage();
                clientMessage.setMessageType(MessageType.SEARCH_GROUP);
                clientMessage.setGroupId(keyword);
                sendMessageBinder.sendMessage(JSON.toJSONString(clientMessage));

            }
        });

        //搜索框显示
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.showFragment(getSupportFragmentManager(),SearchFragment.TAG);
            }
        });

        //跳转群聊界面
        mGroupList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Group group = groups.get(groupPosition).get(childPosition);
                Intent intent = new Intent(MainActivity.this,GroupActivity.class);
                intent.putExtra("group_id",group.getGroupId());
                startActivity(intent);
                return true;
            }
        });

        Button btnSignIn = findViewById(R.id.sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,GetSignMessageActivity.class);
                startActivity(i);
            }
        });
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
                        break;

                    case R.id.add_friend_group:
                        NotificationUtil notificationUtil = new NotificationUtil(MainActivity.this);
                        notificationUtil.sendNotification("Location","小陈小陈");
                        break;

                    case R.id.share:
                        if(ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                        }else{
                            ToExcel();
                        }
                        break;
                }
            }
        };

        contentView.findViewById(R.id.create_group).setOnClickListener(listener);
        contentView.findViewById(R.id.add_friend_group).setOnClickListener(listener);
        contentView.findViewById(R.id.share).setOnClickListener(listener);
    }
    private void ToExcel(){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/record.xls";
        String[] title = {"id","签到人","签到时间","签到结果"};

        //TODO 从服务器获取recordList
        List<GroupSignInMessage> recordList = new ArrayList<>();
        GroupSignInMessage gm = new GroupSignInMessage();
        gm.setReceiverId("1762500418");
        gm.setDone(true);
        for(int i =0;i<100;i++){
            gm.setStartTime(System.currentTimeMillis());
            gm.setEndTime(System.currentTimeMillis()+1000);
            recordList.add(gm);
        }
        ExcelUtil.initExcel(filePath, title);
        boolean isSuccess = ExcelUtil.writeObjListToExcel(recordList,filePath);
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){}
        if(isSuccess){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            Uri uri;
            File file = new File(filePath);
            if(Build.VERSION.SDK_INT >= 24){
                uri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.LocationApp.fileprovider",file);
            }
            else{
                uri = Uri.fromFile(file);
            }
            shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent,"分享到"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            boolean isAllow = true;
            for (int i = 0; i < permissions.length; i++) {
                if(grantResults[i]==-1){
                    isAllow = false;
                    Toast.makeText(MainActivity.this,"导出签到记录需要文件权限",Toast.LENGTH_SHORT).show();
                }
            }
            if(isAllow){
                ToExcel();
            }
        }
    }

    @Override
    protected void initService() {
        Intent bindIntent = new Intent(MainActivity.this,SocketService.class);
        bindService(bindIntent,connection, BIND_AUTO_CREATE);
    }

    //接收消息并进行处理
    @Override
    public void getMessage(ClientMessage msg)  {
        switch (msg.getMessageType()) {
            case GET_GROUPS:
                createdGroup = msg.getCreateGroups();
                joinedGroup = msg.getJoinGroups();
                break;

            case SEARCH_GROUP:
                searchGroup = msg.getGroup();
                showSearchFragment(searchGroup.getGroupName());
                break;

            case APPLY_JOIN_GROUP:
                if (msg.isSuccess()) {
                    joinedGroup.add(msg.getGroup());
                    Toast.makeText(MainActivity.this,"成功加入群聊",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"管理员拒绝了您的请求",Toast.LENGTH_SHORT).show();
                }
                break;

            case RECEIVE_MEMBER:
                NotificationUtil notification = new NotificationUtil(MainActivity.this);
                notification.sendNotification("Location申请",msg.getUser().getUserName()+
                        "申请加入群"+msg.getGroup().getGroupName());
                break;

            default:
        }
    }


    //发送申请
    @Override
    public void isApply(boolean isCheck) {
        if (isCheck) {
            //申请加入群
            ClientMessage clientMessage = new ClientMessage();
            clientMessage.setMessageType(MessageType.APPLY_JOIN_GROUP);
            clientMessage.setGroup(searchGroup);
            sendMessageBinder.sendMessage(JSON.toJSONString(clientMessage));
            Toast.makeText(MainActivity.this,"消息已发送",Toast.LENGTH_SHORT).show();
            searchFragment.dismiss();
        }
    }

    //显示搜索出的群聊
    private void showSearchFragment(String name) {
        SearchResultDialog searchResultFrag = new SearchResultDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable("group_name",name);
        searchResultFrag.setArguments(bundle);
        searchResultFrag.show(getSupportFragmentManager(),"searchResult");

    }

    long touchTime = 0;
    long waitTime = 2000;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if((currentTime - touchTime) >= waitTime){
            Toast.makeText(this,"再按一次，退出程序",Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            LocationApp locationApp = (LocationApp)getApplication();
            locationApp.getActivityUtil().exit();
        }
    }
}

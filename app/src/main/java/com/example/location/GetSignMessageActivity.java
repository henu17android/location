package com.example.location;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.Service.SocketService;
import com.example.adapter.SimpleSignInRecordAdapter;
import com.example.bean.Group;
import com.example.bean.GroupSignInMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetSignMessageActivity extends BaseActivity implements SensorEventListener {

    private int PERMISSION_REQUEST = 127;
    private int GPS_CODE = 1315;
    MapView mMapView;
    TextView mTvEndTime ,mTvNumber;
    RecyclerView mSignRecordRecyclerView;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;
    BaiduMap mBaiduMap;

    int groupId;
    String adminId;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    GroupSignInMessage adminGroupSignInMessage;
    SimpleSignInRecordAdapter mSimpleSignInRecordAdapter;
    List<GroupSignInMessage> mGroupSignRecordList;
    //地球半径
    private static final double EARTH_RADIUS = 6378.137;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGPSAndPersimmions();
        setContentView(R.layout.activity_get_sign_message);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId",-1);
        adminId = intent.getStringExtra("adminId");
        findView();
        init();
    }

    void findView(){
        mMapView = findViewById(R.id.map_view);
        mTvEndTime = findViewById(R.id.tv_end_time);
        mTvNumber = findViewById(R.id.tv_number);
        mSignRecordRecyclerView = findViewById(R.id.rv_sign_record);
    }

    void init(){
        //地图初始化
        mBaiduMap = mMapView.getMap();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setIndoorEnable(true);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode,true,mCurrentMarker));
        MapStatus.Builder builder1 = new MapStatus.Builder();
        builder1.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
        //定位初始化
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType( "bd09ll" ); // 可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        option.setScanSpan(3000); // 可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedLocationDescribe(true); // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        option.setIsNeedLocationPoiList(true); // 可选，默认false，设置是否需要POI结果，可以在BDLocation
        option.setOpenGps(true); // 可选，默认false，设置是否开启Gps定位
        option.setIsNeedAltitude(false); // 可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        mLocClient.setLocOption(option);
        mLocClient.start();

        mGroupSignRecordList = new ArrayList<>();
        mSimpleSignInRecordAdapter = new SimpleSignInRecordAdapter(GetSignMessageActivity.this,mGroupSignRecordList);
        mSignRecordRecyclerView.setAdapter(mSimpleSignInRecordAdapter);
        Group group = new Group();
        group.setAdminId(adminId);
        group.setGroupId(groupId);
        //TODO 向服务器发送申请群成员签到情况表

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[0];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @TargetApi(23)
    private void getGPSAndPersimmions(){
        LocationManager lm = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                ArrayList<String> permissions = new ArrayList<>();
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if (permissions.size() > 0){
                    Toast.makeText(this, "开启后使用定位功能", Toast.LENGTH_SHORT).show();
                    requestPermissions(permissions.toArray(new String[permissions.size()]),PERMISSION_REQUEST);
                }
            }
        } else {
            Toast.makeText(this,"开启GPS后定位更精确",Toast.LENGTH_SHORT).show();
            Intent gpsIntent = new Intent();
            gpsIntent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(gpsIntent,GPS_CODE);
        }

    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void setMarker(double lat ,double lon){

        //设置坐标点
        LatLng point1 = new LatLng(lat, lon);

        View mView = LayoutInflater.from(GetSignMessageActivity.this).inflate(R.layout.layout_baidu_map_item, null);
        BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory
                .fromView(mView);

        OverlayOptions adminLocation = new MarkerOptions().position(point1)
                .icon(bitmapDescriptor1).zIndex(15).draggable(true);
        //在地图上添加
        mBaiduMap.addOverlay(adminLocation);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location){
            if(location == null || mMapView == null){
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }

    @Override
    public void initService() {
        Intent bindIntent = new Intent(GetSignMessageActivity.this, SocketService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }


    @Override
    public void getMessage(String msg){
        JSONObject jsonObject;
        String messageType;
        try {
            jsonObject = new JSONObject(msg);
            messageType = jsonObject.getString("messageType");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

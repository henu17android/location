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
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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
import com.example.bean.Group;
import com.example.bean.GroupSignInMessage;
import com.example.client.ClientMessage;
import com.example.client.MessagePostPool;
import com.example.client.MessageType;
import com.example.util.TimeTransform;
import com.example.client.ClientMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到页面
 */
public class ToSignInActivity extends BaseActivity implements View.OnClickListener,SensorEventListener {

    private int PERMISSION_REQUEST = 127;
    private int GPS_CODE = 1315;
    MapView mMapView;
    BaiduMap mBaiduMap;
    TextView mTvEndTime,mTvNumber,mTvCancel,mTvOk,mTvLat,mTvLon,mTvResult,mTvDistance;
    //定位相关
    LocationClient mLocClient;
    private MyLocationListener myListener = new MyLocationListener();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccray;
    int errorCode;

    String originatorId;    //发起人Id
    int messageId;
    int groupId;
    boolean mIsLegal;   //是否在指定签到范围
    boolean isFirstLoc = true;
    private MyLocationData locData;
    GroupSignInMessage adminGroupSignInMessage;
    private static final double EARTH_RADIUS = 6378.137;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getGPSAndPersimmions();
        setContentView(R.layout.activity_to_sign_in);

        Intent intent = getIntent();
        //messageId = intent.getIntExtra("messgaeId",-1);
        groupId = intent.getIntExtra("groupId",-1);
        findView();
        init();
        mMapView = findViewById(R.id.map_view);

    }
    void findView(){
        mMapView = findViewById(R.id.map_view);
        mTvNumber = findViewById(R.id.number);
        mTvEndTime = findViewById(R.id.end_time);
        mTvCancel = findViewById(R.id.tv_cancel);
        mTvOk = findViewById(R.id.tv_ok);
        mTvLat = findViewById(R.id.lat);
        mTvLon = findViewById(R.id.lon);
        mTvResult = findViewById(R.id.result);
        mTvDistance = findViewById(R.id.distance);

        mTvCancel.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
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
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        double x = sensorEvent.values[0];
        if(Math.abs(x - lastX) > 1.0){
            mCurrentDirection = (int)x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccray)
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                GroupSignInMessage groupSignInMessage = new GroupSignInMessage();
                groupSignInMessage.setGroupId(groupId);
                groupSignInMessage.setRecordId(messageId);
                groupSignInMessage.setType(2);
                groupSignInMessage.setOriginatorId(originatorId);
                long timeStamp = System.currentTimeMillis();
                groupSignInMessage.setStartTime(timeStamp);
                groupSignInMessage.setEndTime(adminGroupSignInMessage.getEndTime());
                groupSignInMessage.setLatitude(adminGroupSignInMessage.getLatitude());
                groupSignInMessage.setLongitude(adminGroupSignInMessage.getLongitude());
                groupSignInMessage.setRegion(adminGroupSignInMessage.getRegion());
                groupSignInMessage.setRlatitude(mCurrentLat);
                groupSignInMessage.setLongitude(mCurrentLon);
                if(mIsLegal){
                    groupSignInMessage.setResult(1);
                }else {
                    groupSignInMessage.setResult(0);
                }
                ClientMessage clientMessage = new ClientMessage();
                clientMessage.setMessageType(MessageType.USER_SIGN_IN);
                clientMessage.setGroupSignInMessage(groupSignInMessage);
                MessagePostPool.sendMessage(clientMessage);
                finish();
                break;
            default:
                    break;
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            if(location == null || mMapView == null){
                return;
            }
            errorCode = location.getLocType();
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mTvLat.setText(String.valueOf(mCurrentLat));
            mTvLon.setText(String.valueOf(mCurrentLon));

            mIsLegal = true;
            mIsLegal = GetDistance(adminGroupSignInMessage.getLatitude(),
                    adminGroupSignInMessage.getLongitude(),mCurrentLat,mCurrentLon,
                    adminGroupSignInMessage.getRegion());
            if(mIsLegal){
                mTvResult.setText("处在指定范围内");
            }else{
                mTvResult.setText("未处在指定范围内");
            }
            mCurrentAccray = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

        }
    }
    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onStop(){
        super.onStop();
        //取消注册传感监听器
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //退出时销毁定位
        mLocClient.stop();
        //关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
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

    /**
     * 根据经纬度查询距离
     */
    public static boolean GetDistance(double lat1, double lon1, double lat2, double lon2, int distance){
        double s = Math.acos(Math.sin(lat1) * Math.sin(lat2) +
        Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2-lon1)) * EARTH_RADIUS;
        if((double)distance >= s){
            return true;
        }else{
            return false;
        }
    }

    private static double rad(double d){
        return d * Math.PI/180.0;
    }

    void setMarker(double lat ,double lon){
        mBaiduMap.clear();
        //设置坐标点
        LatLng point1 = new LatLng(lat, lon);

        View mView = LayoutInflater.from(ToSignInActivity.this).inflate(R.layout.layout_baidu_map_item, null);
        BitmapDescriptor bitmapDescriptor1 = BitmapDescriptorFactory
                .fromView(mView);

        OverlayOptions adminLocation = new MarkerOptions().position(point1)
                .icon(bitmapDescriptor1).zIndex(15).draggable(true);
        //在地图上添加
        mBaiduMap.addOverlay(adminLocation);
    }


    @Override
    public void getMessage(ClientMessage msg) {
        if(msg != null){
            if(msg.getMessageType().equals(MessageType.GET_SINGLE_SIGNIN_RECORD)){
                List<GroupSignInMessage> recordList;
                recordList = msg.getSignInMessages();
                int number = 0;

                for(int i=0; i<recordList.size(); i++){
                    if(recordList.get(i).getType()==2){
                        number++;
                    }else if(recordList.get(i).getType()==1){
                        adminGroupSignInMessage = recordList.get(i);
                    }
                    String endTime = TimeTransform.stampToTime(adminGroupSignInMessage.getEndTime());
                    originatorId = adminGroupSignInMessage.getOriginatorId();
                    setMarker(adminGroupSignInMessage.getLatitude(),adminGroupSignInMessage.getLongitude());
                    mTvEndTime.setText(endTime);
                    mTvNumber.setText(String.valueOf(number));
                    mTvDistance.setText(adminGroupSignInMessage.getRegion() + "米");
                }
            }
        }
    }

}

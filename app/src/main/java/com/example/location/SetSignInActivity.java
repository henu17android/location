package com.example.location;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.bean.GroupMessage;
import com.example.bean.GroupSignInMessage;
import com.example.client.ClientMessage;
import com.example.client.MessageType;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class SetSignInActivity extends BaseActivity implements SensorEventListener, View.OnClickListener{

    private int PERMISSION_REQUEST = 127;
    private int GPS_CODE = 1315;
    MapView mMapView;
    TextView mTvDate ,mTvTime ,mTvOk ,mTvLat ,mTvLon;
    EditText mEtDistance;

    static int mYear;
    static int mMonth;
    static int mDay;
    static String mWeek;
    static int mHour;
    static int mMinute;

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
    private float mCurrentAccracy;
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    int groupId;
    String adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getGPSAndPersimmions();
        setContentView(R.layout.activity_set_sign_in);

        Intent intent = getIntent();
        groupId = intent.getIntExtra("groupId",-1);
        adminId = intent.getStringExtra("adminId");

        findView();
        init();
    }
    void findView(){
        mMapView = findViewById(R.id.map_view);
        mTvDate = findViewById(R.id.tv_date);
        mTvLat = findViewById(R.id.lat);
        mTvLon = findViewById(R.id.lon);
        mTvTime = findViewById(R.id.tv_time);
        mTvOk = findViewById(R.id.tv_ok);
        mEtDistance = findViewById(R.id.et_distance);

        mTvDate.setOnClickListener(this);
        mTvTime.setOnClickListener(this);
        mTvOk.setOnClickListener(this);
        mEtDistance.setOnClickListener(this);
    }

    void init(){
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONDAY);
        mDay = c.get(Calendar.DATE);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mWeek = getWeek(mYear, mMonth, mDay);
        mTvDate.setText(getDataString(mYear, mMonth, mDay, mWeek));
        mTvTime.setText(getTimeString(mHour, mMinute));

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
        if(Math.abs(x-lastX) > 1.0){
            mCurrentDirection = (int)x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mCurrentDirection)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_date:
                AlertDataPickerDialog();
                break;
            case R.id.tv_time:
                AlertTimePickerDialog();
                break;
            case R.id.tv_ok:
                GroupMessage groupMessage = new GroupMessage();
                groupMessage.setGroupId(groupId);
                groupMessage.setFromId(adminId);
                GroupSignInMessage groupSignInMessage = new GroupSignInMessage();
                groupSignInMessage.setOriginatorId(adminId);
                groupSignInMessage.setType(1);
                groupSignInMessage.setGroupId(groupId);
                long startTime = System.currentTimeMillis();
                groupSignInMessage.setStartTime(startTime);
                long endTime = toTimeStamp(mYear, mMonth, mDay, mHour, mMinute);
                groupSignInMessage.setEndTime(endTime);
                groupSignInMessage.setLatitude(mCurrentLat);
                groupSignInMessage.setLongitude(mCurrentLon);
                groupSignInMessage.setRegion(Integer.parseInt(mEtDistance.getText().toString()));
                ClientMessage clientMessage = new ClientMessage();
                clientMessage.setMessageType(MessageType.SIGN_IN);
                clientMessage.setGroupMessage(groupMessage);
                clientMessage.setGroupSignInMessage(groupSignInMessage);
                sendMessageBinder.sendMessage(JSON.toJSONString(clientMessage));
                finish();
                break;
            default:
                break;
        }
    }

    public static long toTimeStamp(int year, int month, int day, int hour, int minute){
        long timeStamp = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR ,year);
        calendar.add(Calendar.MONTH ,month);
        calendar.add(Calendar.DAY_OF_MONTH ,day);
        calendar.add(Calendar.HOUR_OF_DAY ,hour);
        calendar.add(Calendar.MINUTE ,minute);
        Date date = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = df.format(date);
        Timestamp ts = Timestamp.valueOf(time);
        timeStamp = ts.getTime();
        return timeStamp;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mTvLat.setText(String.valueOf(mCurrentLat));
            mTvLon.setText(String.valueOf(mCurrentLon));
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mCurrentDirection)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            mBaiduMap.setMyLocationData(locData);
            if(isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(mCurrentLat,mCurrentLon);
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            }
        }
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

    public void AlertDataPickerDialog(){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mYear = year;
                mMonth = month;
                mDay = day;
                mWeek = getWeek(mYear ,mMonth ,mDay);
                mTvDate.setText(getDataString(mYear,mMonth,mDay,mWeek));
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(SetSignInActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK,listener,mYear,mMonth,mDay);
        if(Build.VERSION.SDK_INT >= 11){
            dialog.getDatePicker().setMinDate(new Date().getTime()-1000);
        }
        dialog.show();
    }

    public void AlertTimePickerDialog(){
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                mHour = hour;
                mMinute = minute;
                mTvTime.setText(getTimeString(mHour, mMinute));
            }
        };
        TimePickerDialog dialog = new TimePickerDialog(SetSignInActivity.this,
                AlertDialog.THEME_DEVICE_DEFAULT_DARK, listener, mHour, mMinute, true);
        dialog.show();
    }

    /**
     * 得到日期对应的周几
     * @param year
     * @param month
     * @param day
     * @return 周几对应的字符串
     */
    private String getWeek(int year, int month, int day){
        Calendar setDate = Calendar.getInstance();
        setDate.set(year,month,day);
        int weekCode = setDate.get(Calendar.DAY_OF_WEEK);
        String weekString = null;
        switch (weekCode){
            case 1 :
                weekString = "星期天";
                break;
            case 2 :
                weekString = "星期一";
                break;
            case 3 :
                weekString = "星期二";
                break;
            case 4 :
                weekString = "星期三";
                break;
            case 5 :
                weekString = "星期四";
                break;
            case 6 :
                weekString = "星期五";
                break;
            case 7 :
                weekString = "星期六";
                break;
        }
        return weekString;
    }

    private String getDataString(int year, int month, int day, String week){
        String result;
        String monthString;
        String dayString;

        if((month+1)<10){
            monthString = "0" + String.valueOf(month);
        }else {
            monthString = String.valueOf(month);
        }
        if(day<10){
            dayString = "0" + String.valueOf(day);
        }else {
            dayString = String.valueOf(day);
        }
        result = String.valueOf(year) + "年" + monthString + "月" + dayString + "日" + week;
        return result;
    }

    private String getTimeString(int hour, int minute){
        String result;
        String hourString;
        String minuteString;
        if(hour<10){
            hourString = "0" + String.valueOf(hour);
        }else {
            hourString = String.valueOf(hour);
        }
        if(minute<10){
            minuteString = "0" + String.valueOf(minute);
        }else {
            minuteString = String.valueOf(minute);
        }
        result = hourString + ":" + minuteString;
        return result;
    }

    @Override
    public void initService() {
        Intent bindIntent = new Intent(SetSignInActivity.this, SocketService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void getMessage(ClientMessage msg) {

    }
}

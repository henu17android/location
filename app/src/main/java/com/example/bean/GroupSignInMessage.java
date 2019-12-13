package com.example.bean;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class GroupSignInMessage implements Serializable {

    @JSONField(name = "RecordId")
    private int recordId;//在群消息中id
    @JSONField(name = "Type")
    private int type;//1为发起签到 2为确认签到
    @JSONField(name = "GroupId")
    private String groupId;//群Id
    @JSONField(name = "OriginatorId")
    private String originatorId;//发起人Id 即手机号
    @JSONField(name = "StartTime")
    private long startTime; //发起时间
    @JSONField(name = "EndTime")
    private long endTime;//结束时间
    @JSONField(name = "Longtitude")
    private double longitude; //发起人经度
    @JSONField(name = "Latitude")
    private double latitude; //发起人纬度
    private int region; //签到地理范围
    @JSONField(name = "ReceiverId")
    private String receiverId; //签到人Id
    @JSONField(name = "Rlongtitude")
    private double rlongitude; //签到人经度
    @JSONField(name = "Rlatitude")
    private double rlatitude; //签到人纬度
    @JSONField(name = "State")
    private boolean state; //签到是否结束
    @JSONField(name = "Done")
    private boolean done; //签到人是否签到
    @JSONField(name = "Result")
    private int result; //签到结果

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

//    public int getGroupId() {
//        return groupId;
//    }
//
//    public void setGroupId(int groupId) {
//        this.groupId = groupId;
//    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOriginatorId() {
        return originatorId;
    }

    public void setOriginatorId(String originatorId) {
        this.originatorId = originatorId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public double getRlongitude() {
        return rlongitude;
    }

    public void setRlongitude(double rlongitude) {
        this.rlongitude = rlongitude;
    }

    public double getRlatitude() {
        return rlatitude;
    }

    public void setRlatitude(double rlatitude) {
        this.rlatitude = rlatitude;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}


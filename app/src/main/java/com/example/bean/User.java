package com.example.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.hgdendi.expandablerecycleradapter.BaseExpandableRecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息实体类
 */
public class User implements Serializable , BaseExpandableRecyclerViewAdapter.BaseGroupBean<SignRecord>{

    private int id =-1;
    @JSONField(name = "PhoneNumber")
    private String phoneNumber = null;
    @JSONField(name = "UserName")
    private String userName = null;
    @JSONField(name = "StudentNumber")
    private String studentNumber = null;
    @JSONField(name = "Password")
    private String password = null;

    //签到记录
    private List<SignRecord> signRecord;

    public User(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User () {

    }


    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<SignRecord> getSignRecord() {
        return signRecord;
    }

    public void setSignRecord(List<SignRecord> signRecord) {
        this.signRecord = signRecord;
    }

    @Override
    public int getChildCount() {
        return signRecord.size();
    }

    @Override
    public SignRecord getChildAt(int childIndex) {
        return childIndex < signRecord.size() ? signRecord.get(childIndex) : null;
    }

    @Override
    public boolean isExpandable() {
        return signRecord.size() > 0;
    }
}

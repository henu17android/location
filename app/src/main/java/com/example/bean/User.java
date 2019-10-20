package com.example.bean;

/**
 * 用户信息实体类
 */
public class User {

    private int id;
    private String phoneNumber;
    private String userName;
    private String createTime;
    private int studentNumber;

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public int getStudentNumber() {
        return studentNumber;
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

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }
}

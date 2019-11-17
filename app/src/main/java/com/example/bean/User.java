package com.example.bean;

/**
 * 用户信息实体类
 */
public class User {

    private int Id =-1;
    private String PhoneNumber = null;
    private String UserName = null;
    private String StudentNumber = null;
    private String Password = null;

//    public User(String phoneNumber, String password) {
//        this.phoneNumber = phoneNumber;
//        this.password = password;
//    }


    public User(String phoneNumber, String password) {
        this.PhoneNumber = phoneNumber;
        this.Password = password;
    }

    public User () {

    }

    public int getId() {
        return Id;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getUserName() {
        return UserName;
    }

    public String getStudentNumber() {
        return StudentNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setStudentNumber(String studentNumber) {
        StudentNumber = studentNumber;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

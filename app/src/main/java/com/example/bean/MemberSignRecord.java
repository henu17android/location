package com.example.bean;

import java.util.List;


/**
 * 群成员签到记录
 * @param <User>
 * @param <SignRecord>
 */
public class MemberSignRecord<User,SignRecord> {

    private User user;
    private List<SignRecord> signRecord;


    public MemberSignRecord(User user,List<SignRecord> signRecord) {
        this.user = user;
        this.signRecord =signRecord;
    }


    public User getUser() {
        return user;
    }

    public List<SignRecord> getSignRecord() {
        return signRecord;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public void setSignRecord(List<SignRecord> signRecord) {
        this.signRecord = signRecord;
    }
}

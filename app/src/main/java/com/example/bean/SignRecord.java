package com.example.bean;

/**
 *  签到记录
 */
public class SignRecord {

    private long signTime; //签到时间
    private boolean signResult;  //签到结果
    private int groupId;

    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public boolean isSignResult() {
        return signResult;
    }

    public void setSignResult(boolean signResult) {
        this.signResult = signResult;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}

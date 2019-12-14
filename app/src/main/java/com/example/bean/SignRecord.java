package com.example.bean;

/**
 *  个人签到记录
 */
public class SignRecord {

    String signTime; //签到时间
    String signResult;  //签到结果
    int groupId;

    public String getSignTime() {
        return signTime;
    }

    public String getSignResult() {
        return signResult;
    }

    public void setSignTime(String signTime) {
        this.signTime = signTime;
    }

    public void setSignResult(String signResult) {
        this.signResult = signResult;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}

package com.wjc.parttime.LitePalHelperDB;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by y_hui on 2018/1/4.
 */

public class UserHelperDB extends DataSupport {



    private String createDate;


    private String telePhone;


    private String passWord;


    private String token;


    private int userId;


    private int userType;


    private int studentId;

    public String getcreateDate() {
        return createDate;
    }

    public void setcreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}

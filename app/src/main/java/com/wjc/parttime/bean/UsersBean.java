package com.wjc.parttime.bean;

import java.util.Date;

/**
 * Created by y_hui on 2018/1/4.
 */

public class UsersBean {


    /**
     * success : true
     * stateCode : 0
     * stateMessage : 注册成功
     * result : {"user":{"Time":1515053814000,"userid":5,"telephone":"18250154819","password":"123456","usertype":1,"studentid":7},"token":"41cd7ec2-59e5-4e79-a209-1350a8d23807"}
     * errorCode : 
     */

    private boolean success;
    private int stateCode;
    private String stateMessage;
    private ResultBean result;
    private String errorCode;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateMessage() {
        return stateMessage;
    }

    public void setStateMessage(String stateMessage) {
        this.stateMessage = stateMessage;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        /**
         * user : {"Time":"2018-01-04 05:06:15","userid":5,"telephone":"18250154819","password":"123456","usertype":1,"studentid":7}
         * token : 41cd7ec2-59e5-4e79-a209-1350a8d23807
         */

        private UserBean user;
        private String token;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public static class UserBean {
            /**
             * Time : "2018-01-04 05:06:15"
             * userid : 5
             * telephone : 18250154819
             * password : 123456
             * usertype : 1
             * studentid : 7
             */

            private Date createTime;
            private int userid;
            private String telephone;
            private String password;
            private int usertype;
            private int studentid;

            public Date getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Date createTime) {
                this.createTime = createTime;
            }

            public int getUserid() {
                return userid;
            }

            public void setUserid(int userid) {
                this.userid = userid;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public int getUsertype() {
                return usertype;
            }

            public void setUsertype(int usertype) {
                this.usertype = usertype;
            }

            public int getStudentid() {
                return studentid;
            }

            public void setStudentid(int studentid) {
                this.studentid = studentid;
            }
        }
    }
}

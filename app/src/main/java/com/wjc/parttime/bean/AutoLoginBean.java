package com.wjc.parttime.bean;

/**
 * Created by y_hui on 2018-01-09.
 */

public class AutoLoginBean {


    /**
     * success : true
     * stateCode : 0
     * result : {"user":{"createDate":"2018-01-08 11:51:22","userid":23,"telephone":"18250154819","password":"9V8itFncM/cHmXw0Vm4+aA==","usertype":1,"studentid":23},"token":"ebb3f7af-611a-4864-99fe-d76122498ca1"}
     * errorCode :
     */

    private boolean success;
    private int stateCode;
    private ResultBean result;
    private String errorCode;

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
         * user : {"createDate":"2018-01-08 11:51:22","userid":23,"telephone":"18250154819","password":"9V8itFncM/cHmXw0Vm4+aA==","usertype":1,"studentid":23}
         * token : ebb3f7af-611a-4864-99fe-d76122498ca1
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
             * createDate : 2018-01-08 11:51:22
             * userid : 23
             * telephone : 18250154819
             * password : 9V8itFncM/cHmXw0Vm4+aA==
             * usertype : 1
             * studentid : 23
             */

            private String createDate;
            private int userid;
            private String telephone;
            private String password;
            private int usertype;
            private int studentid;

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
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

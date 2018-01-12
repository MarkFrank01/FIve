package com.wjc.parttime.bean;

import java.util.List;

/**
 * 开屏广告Bean
 * Created by y_hui on 2018/1/12.
 */

public class AdversitingBean {


    /**
     * success : true
     * stateCode : 0
     * result : [{"startTime":"2018-01-11 hh:ss:mm","endTime":"2018-01-11 hh:ss:mm","adType":"A","displayType":"P","adUrl":"http://XXXXXXXX","actionUrl":"http://XXXXXXX"},{"startTime":"2018-01-11 hh:ss:mm","endTime":"2018-01-11 hh:ss:mm","adType":"A","displayType":"P","adUrl":"http://XXXXXXXX","actionUrl":"http://XXXXXXX"}]
     * token : 18b34409-4551-4582-89c7-dda8369cc921
     * errorCode :
     */

    private boolean success;
    private int stateCode;
    private String token;
    private String errorCode;
    private List<ResultBean> result;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * advId:20180112144259001
         * startTime : 2018-01-11 hh:ss:mm
         * endTime : 2018-01-11 hh:ss:mm
         * adType : A
         * displayType : P
         * adUrl : http://XXXXXXXX
         * actionUrl : http://XXXXXXX
         */

        private String startTime;
        private String endTime;
        private String adType;
        private String displayType;
        private String adUrl;
        private String actionUrl;
        private int advManageID;

        public int getAdvManageID() {
            return advManageID;
        }

        public void setAdvManageID(int advManageID) {
            this.advManageID = advManageID;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getAdType() {
            return adType;
        }

        public void setAdType(String adType) {
            this.adType = adType;
        }

        public String getDisplayType() {
            return displayType;
        }

        public void setDisplayType(String displayType) {
            this.displayType = displayType;
        }

        public String getAdUrl() {
            return adUrl;
        }

        public void setAdUrl(String adUrl) {
            this.adUrl = adUrl;
        }

        public String getActionUrl() {
            return actionUrl;
        }

        public void setActionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
        }
    }
}

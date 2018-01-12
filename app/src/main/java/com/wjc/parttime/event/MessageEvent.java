package com.wjc.parttime.event;

import java.util.ArrayList;

/**
 * EventBus消息类型
 * Created by y_hui on 2018/1/12.
 */

public class MessageEvent {
    private String message;

    private ArrayList list;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(ArrayList list) {
        this.list = list;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

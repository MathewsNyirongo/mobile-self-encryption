package com.example.mobileselfencryption.helpers;

public class Message {
    private String id;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String contactName;

    public String getAddress(){
        return _address;
    }
    public String getMsg(){
        return _msg;
    }
    public String getReadState(){
        return _readState;
    }
    public String getTime(){
        return _time;
    }
    public String getContactName(){
        return contactName;
    }
    public String getId() {
        return id;
    }

    public void setAddress(String address){
        _address = address;
    }
    public void setMsg(String msg){
        _msg = msg;
    }
    public void setReadState(String readState){
        _readState = readState;
    }
    public void setTime(String time){ _time = time; }
    public void setContactName(String folderName){
        contactName = folderName;
    }
    public void setId(String id) { this.id = id; }

}

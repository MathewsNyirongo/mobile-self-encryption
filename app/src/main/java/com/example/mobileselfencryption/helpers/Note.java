package com.example.mobileselfencryption.helpers;

public class Note {
    private String id;
    private String title;
    private String text;
    private String time;

    public String getTitle(){
        return title;
    }
    public String getText(){
        return text;
    }
    public String getTime(){
        return time;
    }
    public String getId() {
        return id;
    }

    public void setTitle(String address){
        title = address;
    }
    public void setText(String msg){
        text = msg;
    }
    public void setTime(String time){ this.time = time; }
    public void setId(String id) { this.id = id; }
}

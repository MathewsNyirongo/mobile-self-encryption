package com.example.mobileselfencryption.helpers;

import android.graphics.Bitmap;

public class File {
    private String id;
    private Bitmap file;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setFile(Bitmap file) {
        this.file = file;
    }

    public Bitmap getFile() {
        return file;
    }
}
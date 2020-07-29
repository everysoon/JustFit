package com.example.justfit_minsun;

import android.graphics.drawable.Drawable;

public class Video {

    String teacherName;
    String title;
    String part;
    Drawable image;

    public Video(Drawable drawable, String part, String title, String teacherName){
        this.teacherName=teacherName;
        this.part=part;
        this.title=title;
        image = drawable;
        this.teacherName=teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}


package com.example.myapplication;

public class Model {


    private int userId;
    private String Title, PostDesc;

    public String getTopic() {
        return Title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPostDesc() {
        return PostDesc;
    }

    public void setPostDesc(String postDesc) {
        PostDesc = postDesc;
    }

    public Model(int userId, String title, String postDesc) {
        this.userId = userId;
        Title = title;
        PostDesc = postDesc;
    }
}

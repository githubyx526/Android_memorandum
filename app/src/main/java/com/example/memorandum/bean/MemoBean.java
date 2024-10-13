package com.example.memorandum.bean;

public class MemoBean {
    private String title;
    private String content;
    private String ImgPath;
    private String time;

    public MemoBean(String title,String content, String ImgPath,String time)
    {
        this.content = content;
        this.title = title;
        this.ImgPath = ImgPath;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

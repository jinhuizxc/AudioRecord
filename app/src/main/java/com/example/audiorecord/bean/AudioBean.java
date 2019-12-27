package com.example.audiorecord.bean;

public class AudioBean {

    private String filePath;
    private long time;

    public AudioBean(String filePath, long time) {
        this.filePath = filePath;
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

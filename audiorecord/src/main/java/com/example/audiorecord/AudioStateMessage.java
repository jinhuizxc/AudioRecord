package com.example.audiorecord;

public class AudioStateMessage {

    public int what;
    public Object obj;

    public AudioStateMessage() {

    }

    public static AudioStateMessage obtain() {
        return new AudioStateMessage();
    }
}

package com.example.s6proyect;

public class Message {
    private String message;

    public Message (String msg) {
        this.setMessage(msg);
    }

    public Message () {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

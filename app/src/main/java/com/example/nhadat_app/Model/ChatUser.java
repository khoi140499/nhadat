package com.example.nhadat_app.Model;

public class ChatUser {
    private String user_send;
    private String user_receiver;
    private String message;
    private String createAt;

    public ChatUser(String user_send, String user_receiver, String message, String createAt) {
        this.user_send = user_send;
        this.user_receiver = user_receiver;
        this.message = message;
        this.createAt = createAt;
    }

    public ChatUser(String user_send, String user_receiver, String message) {
        this.user_send = user_send;
        this.user_receiver = user_receiver;
        this.message = message;
    }

    public String getUser_send() {
        return user_send;
    }

    public String getUser_receiver() {
        return user_receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getCreateAt() {
        return createAt;
    }
}

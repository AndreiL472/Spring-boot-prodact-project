package sda.academy.restdemo.model;

import lombok.Data;

@Data
public class Message {
    private int id;
    private String contentOfMessage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentOfMessage() {
        return contentOfMessage;
    }

    public void setContentOfMessage(String contentOfMessage) {
        this.contentOfMessage = contentOfMessage;
    }
}

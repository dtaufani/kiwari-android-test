package com.taufani.kiwari.model;

import java.util.Date;

/**
 * Created by dtaufani@gmail.com on 28/10/19.
 */

public class ChatModel {
    String senderName;
    String senderEmail;
    Date createdDate;
    String message;

    public ChatModel() {}

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getMessage() {
        return message;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

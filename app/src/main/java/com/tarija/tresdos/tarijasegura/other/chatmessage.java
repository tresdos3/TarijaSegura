package com.tarija.tresdos.tarijasegura.other;

import java.util.Date;

/**
 * Created by Tresdos on 3/6/2018.
 */

public class chatmessage {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public chatmessage() {
    }

    public chatmessage(String messageText) {
        this.messageText = messageText;
        this.messageUser = "Tu tutor: ";
        this.messageTime = new Date().getTime();
    }

    public chatmessage(String messageText, String messageUser, long messageTime) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

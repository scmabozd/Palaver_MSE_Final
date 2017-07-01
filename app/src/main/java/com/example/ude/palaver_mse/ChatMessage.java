package com.example.ude.palaver_mse;

import android.support.v7.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatMessage extends AppCompatActivity {
    public boolean left;
    public String message;
    private String sender;
    private String recipient;
    private String date;
    private long timestamp;
    private String text;
    private String mimetype;

    public ChatMessage(boolean left , String message) {
        // TODO Auto-generated constructor stub
        super();
        this.left=left;
        this.message = message;

    }
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date date) {
        this.timestamp = date.getTime();
        DateFormat dfOutput = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
        this.date = dfOutput.format(date);
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
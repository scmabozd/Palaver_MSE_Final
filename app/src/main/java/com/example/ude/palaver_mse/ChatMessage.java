package com.example.ude.palaver_mse;

import android.support.v7.app.AppCompatActivity;

public class ChatMessage extends AppCompatActivity {
    public boolean left;
    public String message;

    public ChatMessage(boolean left , String message) {
        // TODO Auto-generated constructor stub
        super();
        this.left=left;
        this.message = message;
    }
}
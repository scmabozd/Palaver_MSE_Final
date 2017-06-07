package com.example.ude.palaver_mse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends AppCompatActivity {
    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private Button send;
    private boolean side = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        send = (Button) findViewById(R.id.btn);
        list = (ListView) findViewById(R.id.listview);
        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.activity_chat_window);
        list.setAdapter(adp);
        chatText = (EditText) findViewById(R.id.chat_text);

        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);
        adp.registerDataSetObserver(new DataSetObserver() {
            public void OnChanged(){
                super.onChanged();
                list.setSelection(adp.getCount() -1);
            }
        });

    }
    private boolean sendChatMessage() {
        adp.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }
}

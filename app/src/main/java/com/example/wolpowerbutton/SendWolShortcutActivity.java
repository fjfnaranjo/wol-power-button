package com.example.wolpowerbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SendWolShortcutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(SendWolReceiver.ACTION_SEND_WOL);
        intent.setClass(this, SendWolReceiver.class);
        sendBroadcast(intent);
        finish();
    }
}

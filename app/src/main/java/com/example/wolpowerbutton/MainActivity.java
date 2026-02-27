package com.example.wolpowerbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        Button mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(v -> {
            Intent intent = new Intent(SendWolReceiver.ACTION_SEND_WOL);
            intent.setClass(this, SendWolReceiver.class);
            sendBroadcast(intent);
        });
    }

}

package com.example.wolpowerbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;

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

        Button shortcutButton = findViewById(R.id.createShortcutButton);
        shortcutButton.setOnClickListener(v -> {
            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);
            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, "sendwol_pinned")
                    .setShortLabel(getString(R.string.turn_on))
                    .setLongLabel(getString(R.string.sendwol_shortcut_short_label))
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setIntent(new Intent(this, SendWolShortcutActivity.class)
                            .setAction(SendWolReceiver.ACTION_SEND_WOL))
                    .build();
            if (shortcutManager.isRequestPinShortcutSupported()) {
                shortcutManager.requestPinShortcut(shortcut, null);
            }
        });
    }

}

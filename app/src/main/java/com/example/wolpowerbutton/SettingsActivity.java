package com.example.wolpowerbutton;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Pattern;

public class SettingsActivity extends Activity {
    public static final String PREFS_NAME = "WolPowerButtonPrefs";
    public static final String MAC_ADDRESS_KEY = "mac_address";
    private static final Pattern MAC_PATTERN = Pattern.compile(
        "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText editText = findViewById(R.id.settingsText);
        Button saveButton = findViewById(R.id.saveButton);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedMac = prefs.getString(MAC_ADDRESS_KEY, "");
        editText.setText(savedMac);

        saveButton.setOnClickListener(v -> {
            String mac = editText.getText().toString().trim();
            if (!isValidMacAddress(mac)) {
                editText.setError("Invalid MAC address format");
                return;
            }
            prefs.edit().putString(MAC_ADDRESS_KEY, mac).apply();
            finish();
        });
    }

    private boolean isValidMacAddress(String mac) {
        return mac != null && MAC_PATTERN.matcher(mac).matches();
    }
}

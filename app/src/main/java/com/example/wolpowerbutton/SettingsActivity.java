package com.example.wolpowerbutton;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import java.util.regex.Pattern;

public class SettingsActivity extends Activity {
    public static final String PREFS_NAME = "WolPowerButtonPrefs";
    public static final String MAC_ADDRESS_KEY = "mac_address";
    public static final String BROADCAST_ADDRESS_KEY = "broadcast_address";
    private static final Pattern MAC_PATTERN = Pattern.compile(
        "^([0-9A-Fa-f]{2}:){5}([0-9A-Fa-f]{2})$"
    );
    private static final Pattern IP_PATTERN = Pattern.compile(
        "^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        String savedMac = prefs.getString(MAC_ADDRESS_KEY, "");
        EditText macText = findViewById(R.id.macAddressText);
        macText.setText(savedMac);

        String savedBroadcast = prefs.getString(BROADCAST_ADDRESS_KEY, "");
        EditText broadcastText = findViewById(R.id.broadcastAddressText);
        broadcastText.setText(savedBroadcast);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String mac = macText.getText().toString().trim();
            String broadcast = broadcastText.getText().toString().trim();

            boolean hasError = false;

            if (!isValidMacAddress(mac)) {
                macText.setError(getString(R.string.error_invalid_mac));
                hasError = true;
            }

            if (!isValidIpAddress(broadcast)) {
                broadcastText.setError(getString(R.string.error_invalid_ip));
                hasError = true;
            }

            if (hasError) {
                return;
            }

            prefs.edit()
                .putString(MAC_ADDRESS_KEY, mac)
                .putString(BROADCAST_ADDRESS_KEY, broadcast)
                .apply();
            finish();
        });
    }

    private boolean isValidMacAddress(String mac) {
        return mac != null && MAC_PATTERN.matcher(mac).matches();
    }

    private boolean isValidIpAddress(String ip) {
        return ip != null && IP_PATTERN.matcher(ip).matches();
    }
}

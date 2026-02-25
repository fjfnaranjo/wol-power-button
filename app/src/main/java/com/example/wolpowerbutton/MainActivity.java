package com.example.wolpowerbutton;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends Activity {
    private static final int WOL_PORT = 9;

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
            String mac = getSavedMac();
            String broadcast = getSavedBroadcast();
            if (mac.isEmpty() || broadcast.isEmpty()) {
                Toast.makeText(this, R.string.configure_settings, Toast.LENGTH_SHORT).show();
                return;
            }
            sendWolPacket(mac, broadcast);
        });
    }

    private String getSavedMac() {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(SettingsActivity.MAC_ADDRESS_KEY, "");
    }

    private String getSavedBroadcast() {
        SharedPreferences prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(SettingsActivity.BROADCAST_ADDRESS_KEY, "");
    }

    private void sendWolPacket(String mac, String broadcastIp) {
        new Thread(() -> {
            try {
                String[] hex = mac.split(":");
                byte[] bytes = new byte[102];
                byte[] macBytes = new byte[6];

                for (int i = 0; i < 6; i++) {
                    macBytes[i] = (byte) Integer.parseInt(hex[i], 16);
                    bytes[i] = (byte) 0xff;
                }

                for (int i = 6; i < bytes.length; i += macBytes.length) {
                    System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
                }

                InetAddress address = InetAddress.getByName(broadcastIp);
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, WOL_PORT);
                DatagramSocket socket = new DatagramSocket();
                socket.send(packet);
                socket.close();
            } catch (Throwable t) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + t.getClass().getSimpleName(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}

package com.example.wolpowerbutton;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SendWolReceiver extends BroadcastReceiver {
  public static final String ACTION_SEND_WOL = "com.example.wolpowerbutton.ACTION_SEND_WOL";

  private static final int WOL_PORT = 9;

  @Override
  public void onReceive(Context context, Intent intent) {
    android.util.Log.e("SendWolReceiver", "onReceive called, action=" + intent.getAction());
    if (ACTION_SEND_WOL.equals(intent.getAction())) {

      SharedPreferences prefs =
          context.getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);

      String mac = prefs.getString(SettingsActivity.MAC_ADDRESS_KEY, "");
      String broadcast = prefs.getString(SettingsActivity.BROADCAST_ADDRESS_KEY, "");

      if (mac.isEmpty() || broadcast.isEmpty()) {
        Intent i = new Intent(context, SettingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return;
      }

      sendWolPacket(mac, broadcast);
    }
  }

  private void sendWolPacket(String mac, String broadcastIp) {
    new Thread(
            () -> {
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
              } catch (Throwable e) {
                ;
              }
            })
        .start();
  }
}

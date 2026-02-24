package com.example.wolpowerbutton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EditText editText = findViewById(R.id.settingsText);
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String text = editText.getText().toString();
            // For now just toast
            Toast.makeText(this, "Saved: " + text, Toast.LENGTH_SHORT).show();
        });
    }
}

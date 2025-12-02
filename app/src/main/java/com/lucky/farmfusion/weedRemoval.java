package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class weedRemoval extends AppCompatActivity {
    TextView bluetoothTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weed_removal);
        bluetoothTxt=findViewById(R.id.bluetoothTxt);

        // Set initial status
        if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothTxt.setText("Connected");
        } else {
            bluetoothTxt.setText("Not Connected");
        }
    }
}
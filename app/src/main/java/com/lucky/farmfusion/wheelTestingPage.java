package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class wheelTestingPage extends AppCompatActivity {

    Button forwardBtn, backwardBtn, stopBtn;
    TextView bluetoothStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_testing_page);

        forwardBtn = findViewById(R.id.forwardBtn);
        backwardBtn = findViewById(R.id.backwardBtn);
        stopBtn = findViewById(R.id.stopBtn);
        bluetoothStatus = findViewById(R.id.bluetoothStatus);

        // Show connection status
        if (BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothStatus.setText("Connected");
        } else {
            bluetoothStatus.setText("Not Connected");
        }

        // Forward
        forwardBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("FORWARD");
                Toast.makeText(this, "Sent: FORWARD", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        // Backward
        backwardBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("BACKWARD");
                Toast.makeText(this, "Sent: BACKWARD", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        // Stop
        stopBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("STOP");
                Toast.makeText(this, "Sent: STOP", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
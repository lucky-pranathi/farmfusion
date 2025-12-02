package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class spraying extends AppCompatActivity {
    Button onHubBtn, offHubMotor;
    TextView bluetoothTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spraying);

        onHubBtn = findViewById(R.id.onHubBtn);
        offHubMotor = findViewById(R.id.offHubMotor);
        bluetoothTxt=findViewById(R.id.bluetoothTxt);

        // Set initial status
        if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothTxt.setText("Connected");
        } else {
            bluetoothTxt.setText("Not Connected");
        }

        onHubBtn.setOnClickListener(v -> {
            if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
                com.lucky.farmfusion.BluetoothConnectionManager.getInstance().sendData("ON");
                Toast.makeText(this, "Sent: ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        offHubMotor.setOnClickListener(v -> {
            if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
                com.lucky.farmfusion.BluetoothConnectionManager.getInstance().sendData("OFF");
                Toast.makeText(this, "Sent: OFF", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

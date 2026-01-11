package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.LinearLayout;

public class spraying extends AppCompatActivity {
    Button onHubBtn, offHubMotor,sprayOnBtn,sprayOffBtn;
    TextView bluetoothTxt;
    SeekBar spraySpeedSlider;
    TextView spraySpeedValue;
    LinearLayout spraySpeedLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spraying);

        onHubBtn = findViewById(R.id.onHubBtn);
        offHubMotor = findViewById(R.id.offHubMotor);
        bluetoothTxt=findViewById(R.id.bluetoothTxt);
        spraySpeedSlider = findViewById(R.id.spraySpeedSlider);
        spraySpeedValue = findViewById(R.id.spraySpeedValue);
        spraySpeedLayout = findViewById(R.id.spraySpeedLayout);
        spraySpeedSlider = findViewById(R.id.spraySpeedSlider);
        spraySpeedValue = findViewById(R.id.spraySpeedValue);
        sprayOnBtn=findViewById(R.id.sprayOnBtn);
        sprayOffBtn=findViewById(R.id.sprayOffBtn);


        // Set initial status
        if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothTxt.setText("Connected");
        } else {
            bluetoothTxt.setText("Not Connected");
        }

        sprayOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(spraying.this, "Spraying is on", Toast.LENGTH_SHORT).show();
                // SHOW ENTIRE LAYOUT
                spraySpeedLayout.setVisibility(View.VISIBLE);
                // Enable slider
                spraySpeedSlider.setEnabled(true);
            }
        });

        sprayOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(spraying.this, "Spraying is off", Toast.LENGTH_SHORT).show();
                // HIDE ENTIRE LAYOUT
                spraySpeedLayout.setVisibility(View.GONE);

                // Disable slider
                spraySpeedSlider.setEnabled(false);
            }
        });

        onHubBtn.setOnClickListener(v -> {
            if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
                com.lucky.farmfusion.BluetoothConnectionManager.getInstance().sendData("FORWARD");
                Toast.makeText(this, "Sent: ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        offHubMotor.setOnClickListener(v -> {
            if (com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
                com.lucky.farmfusion.BluetoothConnectionManager.getInstance().sendData("STOP");
                Toast.makeText(this, "Sent: OFF", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });
        spraySpeedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int value, boolean fromUser) {
                spraySpeedValue.setText("Speed: " + value + "%");

                if (fromUser && com.lucky.farmfusion.BluetoothConnectionManager.getInstance().isConnected()) {
                    // Send speed value as string (example: "SPD:50")
                    com.lucky.farmfusion.BluetoothConnectionManager.getInstance().sendData("SPD:" + value);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

    }
}

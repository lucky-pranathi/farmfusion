package com.lucky.farmfusion;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.lucky.farmfusion.BluetoothConnectionManager;



import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ImageButton weedRemoval, spraying, fertilizers, routing, planting, harvesting, seeding, rotateWheel,refreshBtn;

    TextView bluetoothTxt;
    Button testingBtn;

    // Bluetooth
//    private static final String ESP32_MAC_ADDRESS = "00:4B:12:EF:2C:56"; // <-- Replace with your ESP32's MAC
    private static final String TARGET_DEVICE_NAME = "ESP32_MOTOR";
    private static final UUID ESP32_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // Standard SPP UUID

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothTxt=findViewById(R.id.bluetoothTxt);
        refreshBtn=findViewById(R.id.refreshBtn);
        weedRemoval = findViewById(R.id.weedRemoval);
        spraying = findViewById(R.id.spraying);
        fertilizers = findViewById(R.id.fertilizers);
        routing = findViewById(R.id.routing);
        planting = findViewById(R.id.planting);
        harvesting = findViewById(R.id.harvesting);
        seeding = findViewById(R.id.seeding);
        rotateWheel = findViewById(R.id.rotateWheel);
        testingBtn=findViewById(R.id.testingBtn);

        // Set initial status
        if (BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothTxt.setText("Connected");
        } else {
            bluetoothTxt.setText("Not Connected");
        }

        // Initialize Bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else {
            connectToESP32();
        }

        weedRemoval.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.weedRemoval.class);
            startActivity(intent);
        });

        spraying.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, spraying.class);
            startActivity(intent);
        });

        fertilizers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.fertilizers.class);
            startActivity(intent);
        });

        routing.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.routing.class);
            startActivity(intent);
        });

        planting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.planting.class);
            startActivity(intent);
        });

        harvesting.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.harvesting.class);
            startActivity(intent);
        });

        seeding.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.seeding.class);
            startActivity(intent);
        });

        rotateWheel.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, com.lucky.farmfusion.rotateWheels.class);
            startActivity(intent);
        });
        testingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), wheelTestingPage.class));
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // ------------------------------
    // Find paired ESP32 by NAME
    // ------------------------------
    private BluetoothDevice findDeviceByName(String name) {
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            if (device.getName() != null && device.getName().equals(name)) {
                return device;
            }
        }
        return null;
    }

    // ------------------------------
    // Connect using name-based lookup
    // ------------------------------
    private void connectToESP32() {
        new Thread(() -> {
            try {
                BluetoothDevice device = findDeviceByName(TARGET_DEVICE_NAME);

                if (device == null) {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "ESP_MOTOR not paired!", Toast.LENGTH_LONG).show()
                    );
                    return;
                }

                bluetoothSocket = device.createRfcommSocketToServiceRecord(ESP32_UUID);
                bluetoothSocket.connect();

                // IMPORTANT: Now this matches your BluetoothConnectionManager EXACTLY
                BluetoothConnectionManager.getInstance().setSocket(bluetoothSocket);

                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Connected to ESP_MOTOR", Toast.LENGTH_SHORT).show()
                );

            } catch (IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_LONG).show()
                );
                e.printStackTrace();
            }
        }).start();
    }
}

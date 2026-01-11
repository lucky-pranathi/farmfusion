package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class harvesting extends AppCompatActivity {

    ImageButton hubOnImg, hubRevImg;
    Button harvestOnBtn, harvestOffBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvesting);

        hubOnImg = findViewById(R.id.hubOnImg);
        hubRevImg = findViewById(R.id.hubRevImg);
        harvestOnBtn = findViewById(R.id.harvestOnBtn);
        harvestOffBtn = findViewById(R.id.harvestOffBtn);

        // ------------------------------------------------------------
        //  CLICK & HOLD — HUB FORWARD  (WITH ANIMATION)
        // ------------------------------------------------------------
        hubOnImg.setOnTouchListener((v, event) -> {
            if (!BluetoothConnectionManager.getInstance().isConnected()) {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
                return true;
            }

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    animatePress(hubOnImg, true);   // ANIMATION ADDED
                    BluetoothConnectionManager.getInstance().sendData("FORWARD");
                    Toast.makeText(this, "Hub Motor: Forward", Toast.LENGTH_SHORT).show();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    animatePress(hubOnImg, false);  // ANIMATION ADDED
                    BluetoothConnectionManager.getInstance().sendData("STOP");
                    Toast.makeText(this, "Hub Motor Stopped", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

        // ------------------------------------------------------------
        //  CLICK & HOLD — HUB REVERSE (WITH ANIMATION)
        // ------------------------------------------------------------
        hubRevImg.setOnTouchListener((v, event) -> {
            if (!BluetoothConnectionManager.getInstance().isConnected()) {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
                return true;
            }

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    animatePress(hubRevImg, true);   // ANIMATION ADDED
                    BluetoothConnectionManager.getInstance().sendData("REVERSE");
                    Toast.makeText(this, "Hub Motor: Reverse", Toast.LENGTH_SHORT).show();
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    animatePress(hubRevImg, false);  // ANIMATION ADDED
                    BluetoothConnectionManager.getInstance().sendData("STOP");
                    Toast.makeText(this, "Hub Motor Stopped", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

        // ------------------------------------------------------------
        //  ROTATE 90°
        // ------------------------------------------------------------
        harvestOnBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("HARVEST_ON");
                Toast.makeText(this, "Sent: 90° Rotate", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        // ------------------------------------------------------------
        //  ROTATE 0°
        // ------------------------------------------------------------
        harvestOffBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("HARVEST_OFF");
                Toast.makeText(this, "Sent: 0° Rotate", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ------------------------------------------------------------
    //  ANIMATION FUNCTION (ONLY THIS ADDED)
    // ------------------------------------------------------------
    private void animatePress(ImageButton btn, boolean press) {
        float scale = press ? 0.85f : 1f;  // shrink on press, restore on release

        btn.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(120)
                .start();
    }
}
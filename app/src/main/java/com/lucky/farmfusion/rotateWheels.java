package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class rotateWheels extends AppCompatActivity {

    ImageButton hubOnImg, hubRevImg;
    Button rotate90Btn, rotate0Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_wheels);

        hubOnImg = findViewById(R.id.hubOnImg);
        hubRevImg = findViewById(R.id.hubRevImg);
        rotate90Btn = findViewById(R.id.rotate90Btn);
        rotate0Btn = findViewById(R.id.rotate0Btn);

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
        rotate90Btn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("ROTATE_90");
                Toast.makeText(this, "Sent: 90° Rotate", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        // ------------------------------------------------------------
        //  ROTATE 0°
        // ------------------------------------------------------------
        rotate0Btn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("ROTATE_0");
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

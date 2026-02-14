package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class weedRemoval extends AppCompatActivity {
    ImageButton rotateLeftImg,rotateRightImg,hydroUpImg, hydroDownImg,refreshBtn;
    Button weedOnBtn, weedOffBtn, forwardHubBtn, backHubMotor;
    SeekBar hubSpeedSlider;
    TextView hubSpeedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weed_removal);

        // UI Elements
        hydroUpImg = findViewById(R.id.hydroUpImg);
        hubSpeedValue=findViewById(R.id.hubSpeedValue);
        hubSpeedSlider=findViewById(R.id.hubSpeedSlider);
        hydroDownImg = findViewById(R.id.hydroDownImg);
        weedOnBtn = findViewById(R.id.weedOnBtn);
        weedOffBtn = findViewById(R.id.weedOffBtn);
        forwardHubBtn = findViewById(R.id.forwardHubBtn);
        backHubMotor = findViewById(R.id.backHubMotor);
        rotateRightImg=findViewById(R.id.rotateRightImg);
        rotateLeftImg=findViewById(R.id.rotateLeftImg);
        refreshBtn = findViewById(R.id.refreshBtn);

        // Add press effect to all clickable items
        addPressEffect(hydroUpImg);
        addPressEffect(hydroDownImg);
        addPressEffect(weedOnBtn);
        addPressEffect(weedOffBtn);
        addPressEffect(forwardHubBtn);
        addPressEffect(backHubMotor);

        // ---------------------------------------------------------
        // HYDRAULIC UP
        // ---------------------------------------------------------
        hydroUpImg.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("SMALL_HYDRO_UP");
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    sendData("SMALL_HYDRO_STOP");
                    break;
            }
            return true;
        });

        // ---------------------------------------------------------
        // HYDRAULIC DOWN
        // ---------------------------------------------------------
        hydroDownImg.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("SMALL_HYDRO_DOWN");
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    sendData("SMALL_HYDRO_STOP");
                    break;
            }
            return true;
        });

        // ---------------------------------------------------------
        // FERTILIZER ON (Rotate 90°)
        // ---------------------------------------------------------
        weedOnBtn.setOnClickListener(v -> {
            sendData("WEED_ON");
            Toast.makeText(this, "Weed On", Toast.LENGTH_SHORT).show();
        });

        // ---------------------------------------------------------
        // FERTILIZER OFF (Rotate 0°)
        // ---------------------------------------------------------
        weedOffBtn.setOnClickListener(v -> {
            sendData("WEED_OFF");
            Toast.makeText(this, "Weed off", Toast.LENGTH_SHORT).show();
        });

        // ---------------------------------------------------------
        // BIG HUB MOTOR – FORWARD
        // ---------------------------------------------------------
        forwardHubBtn.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("FORWARD");
                    break;

                case MotionEvent.ACTION_UP:
                    sendData("STOP");
                    break;
            }
            return true;
        });

        // ---------------------------------------------------------
        // BIG HUB MOTOR – BACKWARD
        // ---------------------------------------------------------
        backHubMotor.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("BACKWARD");
                    break;

                case MotionEvent.ACTION_UP:
                    sendData("STOP");
                    break;
            }
            return true;
        });

        rotateLeftImg.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("ROTATE_90");
                    break;
                case MotionEvent.ACTION_UP:
                    sendData("stop_rotate");
                    break;
            }
            return true;
        });

        rotateRightImg.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("ROTATE_0");
                    break;
                case MotionEvent.ACTION_UP:
                    sendData("stop_rotate");
                    break;
            }
            return true;
        });

        hubSpeedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int step = 10;
                    int newValue = (progress / step) * step;

                    seekBar.setProgress(newValue);
                    hubSpeedValue.setText("Hub Speed: " + newValue + "%");
                    sendData("HSPD:" + newValue);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // ---------------------------------------------------------
        // REFRESH BUTTON
        // ---------------------------------------------------------
        refreshBtn.setOnClickListener(v -> {
            Intent i = new Intent(weedRemoval.this, MainActivity.class);
            startActivity(i);
            finish();
        });

    } // END onCreate




    // ---------------------------------------------------------
    // UNIVERSAL PRESS ANIMATION FOR ANY BUTTON / IMAGEBUTTON
    // ---------------------------------------------------------
    private void addPressEffect(View btn) {
        btn.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            return false; // allow normal click
        });
    }

    private void handlePressAnimation(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(120).start();
        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
        }
    }

    // ---------------------------------------------------------
    // SAFE BLUETOOTH SEND
    // ---------------------------------------------------------
    private void sendData(String msg) {
        if (BluetoothConnectionManager.getInstance().isConnected()) {
            BluetoothConnectionManager.getInstance().sendData(msg);
        } else {
            Toast.makeText(this, "Not connected to ESP32", Toast.LENGTH_SHORT).show();
        }
    }
}
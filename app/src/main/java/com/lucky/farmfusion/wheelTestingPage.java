package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MotionEvent;

public class wheelTestingPage extends AppCompatActivity {

    Button forwardHubBtn,backHubMotor;
    TextView bluetoothStatus;
    SeekBar hubSpeedSlider;
    TextView hubSpeedValue;
    ImageButton rotateLeftImg,rotateRightImg,mainHydroUpImg,mainHydroDownImg,refreshBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_testing_page);

        forwardHubBtn = findViewById(R.id.forwardHubBtn);
        hubSpeedSlider=findViewById(R.id.hubSpeedSlider);
        hubSpeedValue=findViewById(R.id.hubSpeedValue);
        backHubMotor = findViewById(R.id.backHubMotor);
        mainHydroDownImg=findViewById(R.id.mainHydroDownImg);
        rotateRightImg=findViewById(R.id.rotateRightImg);
        rotateLeftImg=findViewById(R.id.rotateLeftImg);
        mainHydroUpImg=findViewById(R.id.mainHydroUpImg);
        refreshBtn=findViewById(R.id.refreshBtn);
        bluetoothStatus=findViewById(R.id.bluetoothStatus);

        // Add press effect to all clickable items
        addPressEffect(mainHydroUpImg);
        addPressEffect(mainHydroDownImg);
        addPressEffect(rotateLeftImg);
        addPressEffect(rotateRightImg);
        addPressEffect(forwardHubBtn);
        addPressEffect(backHubMotor);


        // Show connection status
        if (BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothStatus.setText("Connected");
        } else {
            bluetoothStatus.setText("Not Connected");
        }

        mainHydroUpImg.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("MOVE_UP_HYDRO");
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    sendData("STOP_HYDRO");
                    break;
            }
            return true;
        });

        // ---------------------------------------------------------
        // HYDRAULIC DOWN
        // ---------------------------------------------------------
        mainHydroDownImg.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("MOVE_DOWN_HYDRO");
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    sendData("STOP_HYDRO");
                    break;
            }
            return true;
        });

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
            Intent i = new Intent(wheelTestingPage.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

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
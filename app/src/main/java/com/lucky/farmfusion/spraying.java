package com.lucky.farmfusion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.widget.LinearLayout;
import android.view.MotionEvent;

public class spraying extends AppCompatActivity {

    Button forwardHubBtn, backHubMotor, sprayOnBtn, sprayOffBtn, activateSpray, deactivateSpray;
    ImageButton rotateLeftImg, rotateRightImg,refreshBtn;
    TextView bluetoothTxt, spraySpeedValue, hubSpeedValue;
    SeekBar spraySpeedSlider, hubSpeedSlider;
    LinearLayout spraySpeedLayout, hubSpeedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spraying);

        activateSpray = findViewById(R.id.activateSpray);
        refreshBtn=findViewById(R.id.refreshBtn);
        deactivateSpray = findViewById(R.id.deactivateSpray);
        forwardHubBtn = findViewById(R.id.forwardHubBtn);
        backHubMotor = findViewById(R.id.backHubMotor);
        sprayOnBtn = findViewById(R.id.sprayOnBtn);
        sprayOffBtn = findViewById(R.id.sprayOffBtn);

        rotateLeftImg = findViewById(R.id.rotateLeftImg);
        rotateRightImg = findViewById(R.id.rotateRightImg);

        bluetoothTxt = findViewById(R.id.bluetoothTxt);

        spraySpeedSlider = findViewById(R.id.spraySpeedSlider);
        spraySpeedValue = findViewById(R.id.spraySpeedValue);
        spraySpeedLayout = findViewById(R.id.spraySpeedLayout);

        hubSpeedSlider = findViewById(R.id.hubSpeedSlider);
        hubSpeedValue = findViewById(R.id.hubSpeedValue);
        hubSpeedLayout = findViewById(R.id.hubSpeedLayout);

        // Apply press animation effect
        addPressEffect(forwardHubBtn);
        addPressEffect(backHubMotor);
        addPressEffect(sprayOnBtn);
        addPressEffect(sprayOffBtn);
        addPressEffect(activateSpray);
        addPressEffect(deactivateSpray);
        addPressEffect(rotateLeftImg);
        addPressEffect(rotateRightImg);

        // Check BT status
        if (BluetoothConnectionManager.getInstance().isConnected()) {
            bluetoothTxt.setText("Connected");
        } else {
            bluetoothTxt.setText("Not Connected");
        }

        sprayOnBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("spray_on");
                Toast.makeText(spraying.this, "Spraying is on", Toast.LENGTH_SHORT).show();
                spraySpeedLayout.setVisibility(View.VISIBLE);
                spraySpeedSlider.setEnabled(true);
            } else {
                Toast.makeText(getApplicationContext(), "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        sprayOffBtn.setOnClickListener(v -> {
            if (BluetoothConnectionManager.getInstance().isConnected()) {
                BluetoothConnectionManager.getInstance().sendData("spray_off");
                Toast.makeText(spraying.this, "Spraying is off", Toast.LENGTH_SHORT).show();
                spraySpeedLayout.setVisibility(View.GONE);
                spraySpeedSlider.setEnabled(false);
            } else {
                Toast.makeText(getApplicationContext(), "Not connected to ESP32", Toast.LENGTH_SHORT).show();
            }
        });

        activateSpray.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("activate_spray");
                    break;
                case MotionEvent.ACTION_UP:
                    sendData("stop_actuator");
                    break;
            }
            return true;
        });

        deactivateSpray.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("deactivate_spray");
                    break;
                case MotionEvent.ACTION_UP:
                    sendData("stop_actuator");
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

        forwardHubBtn.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("FORWARD");
                    Toast.makeText(this, "Forward sent", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
                    sendData("STOP");
                    Toast.makeText(this, "Stop hub", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });

        backHubMotor.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    sendData("BACKWARD");
                    Toast.makeText(this, "Reverse sent", Toast.LENGTH_SHORT).show();
                    break;
                case MotionEvent.ACTION_UP:
                    sendData("STOP");
                    Toast.makeText(this, "Stop hub", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        });
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(spraying.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spraySpeedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int step = 10;
                    int newValue = (progress / step) * step;

                    seekBar.setProgress(newValue);
                    spraySpeedValue.setText("Speed: " + newValue + "%");
                    sendData("SPD:" + newValue);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
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
    }

    // Universal press effect for Buttons & ImageButtons
    private void addPressEffect(View btn) {
        btn.setOnTouchListener((v, event) -> {
            handlePressAnimation(v, event);
            return false; // allow normal click to continue
        });
    }

    // Reusable animation
    private void handlePressAnimation(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.animate().scaleX(0.85f).scaleY(0.85f).setDuration(120).start();
        } else if (event.getAction() == MotionEvent.ACTION_UP ||
                event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.animate().scaleX(1f).scaleY(1f).setDuration(120).start();
        }
    }

    // Safe sendData wrapper
    private void sendData(String msg) {
        if (BluetoothConnectionManager.getInstance().isConnected()) {
            BluetoothConnectionManager.getInstance().sendData(msg);
        }
    }
}
